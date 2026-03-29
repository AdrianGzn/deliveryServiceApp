package com.alilopez.kt_demohilt.core.managers

import android.util.Log
import com.alilopez.kt_demohilt.BuildConfig
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) {
    private val _orderUpdates = MutableSharedFlow<Order>()
    val orderUpdates = _orderUpdates.asSharedFlow()

    private var webSocket: WebSocket? = null
    private var currentUserId: Int? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isConnected = false

    fun connect(userId: Int) {
        if (currentUserId == userId && isConnected) return

        disconnect()
        currentUserId = userId

        val baseUrl = BuildConfig.BASE_URL.replace("http://", "ws://").replace("https://", "wss://")
        val request = Request.Builder()
            .url("${baseUrl}ws/$userId")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                Log.d("WebSocketManager", "Connected to WS for user $userId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val event = json.optString("event")
                    val data = json.optJSONObject("data")

                    when (event) {
                        "order_update" -> {
                            data?.let {
                                val order = gson.fromJson(it.toString(), Order::class.java)
                                scope.launch { _orderUpdates.emit(order) }
                            }
                        }
                        "connected" -> {
                            Log.d("WebSocketManager", "Server confirmed connection: ${data?.optString("message")}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocketManager", "Error parsing WS message", e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                Log.e("WebSocketManager", "WS Connection failed", t)
                scope.launch {
                    delay(5000)
                    currentUserId?.let { connect(it) }
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                isConnected = false
                Log.d("WebSocketManager", "WS Closing: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                isConnected = false
                Log.d("WebSocketManager", "WS Closed: $reason")
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        currentUserId = null
        isConnected = false
    }
}