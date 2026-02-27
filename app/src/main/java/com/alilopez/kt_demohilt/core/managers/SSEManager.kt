package com.alilopez.kt_demohilt.core.managers

import android.util.Log
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SSEManager @Inject constructor(
    private val client: OkHttpClient
) {

    private val _orderUpdates = MutableSharedFlow<Order>()
    val orderUpdates = _orderUpdates.asSharedFlow()

    private val _orderDeletes = MutableSharedFlow<Int>()
    val orderDeletes = _orderDeletes.asSharedFlow()

    private var currentEventSource: EventSource? = null
    private var currentUserId: Int? = null
    private var isConnected = false

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun connectToUser(userId: Int) {
        if (currentUserId == userId && isConnected) {
            return
        }

        disconnect()
        currentUserId = userId

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/sse?userId=$userId") // 10.0.2.2 para emulador
            .build()

        val listener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                isConnected = true
                Log.d("SSEManager", "SSE Connected for user $userId")
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                try {
                    val json = JSONObject(data)
                    val event = json.getString("event")
                    val orderData = json.getJSONObject("data")

                    when (event) {
                        "order_update" -> {
                            val order = parseOrder(orderData)
                            scope.launch { _orderUpdates.emit(order) }
                        }
                        "order_deleted" -> {
                            val orderId = orderData.getInt("id")
                            scope.launch { _orderDeletes.emit(orderId) }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SSEManager", "Error parsing SSE event", e)
                }
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: okhttp3.Response?
            ) {
                isConnected = false
                Log.e("SSEManager", "SSE Connection failed", t)
                // Intentar reconectar después de un delay
                scope.launch {
                    delay(5000)
                    currentUserId?.let { connectToUser(it) }
                }
            }

            override fun onClosed(eventSource: EventSource) {
                isConnected = false
                Log.d("SSEManager", "SSE Connection closed")
            }
        }

        currentEventSource = EventSources.createFactory(client)
            .newEventSource(request, listener)
    }

    private fun parseOrder(json: JSONObject): Order {
        return Order(
            id = json.getInt("id"),
            title = json.getString("title"),
            description = json.getString("description"),
            status = json.getString("status"),
            establishmentName = json.getString("establishmentName"),
            establishmentAddress = json.getString("establishmentAddress"),
            price = json.getDouble("price"),
            userId = json.getInt("userId"),
            deliveryId = if (json.has("deliveryId") && !json.isNull("deliveryId"))
                json.getInt("deliveryId") else null,
            createdAt = json.getString("createdAt"),
            updatedAt = json.getString("updatedAt")
        )
    }

    fun disconnect() {
        currentEventSource?.cancel()
        currentEventSource = null
        currentUserId = null
        isConnected = false
    }

    fun connect(
        onOrderUpdate: (Order) -> Unit,
        onOrderDeleted: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        scope.launch {
            launch {
                _orderUpdates.collect { order ->
                    onOrderUpdate(order)
                }
            }
            launch {
                _orderDeletes.collect { orderId ->
                    onOrderDeleted(orderId)
                }
            }
        }
    }
}
