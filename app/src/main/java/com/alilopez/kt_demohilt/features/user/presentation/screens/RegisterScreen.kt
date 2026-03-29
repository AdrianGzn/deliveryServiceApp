package com.alilopez.kt_demohilt.features.user.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alilopez.kt_demohilt.core.components.Input
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.presentation.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: (User) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isDarkTheme = isSystemInDarkTheme()

    val name by viewModel.name.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val address by viewModel.address.collectAsStateWithLifecycle()
    val establishmentName by viewModel.establishmentName.collectAsStateWithLifecycle()
    val establishmentAddress by viewModel.establishmentAddress.collectAsStateWithLifecycle()
    val selectedRole by viewModel.selectedRole.collectAsStateWithLifecycle()

    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFF5F5F5)
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color(0xFF212121)
    val secondaryTextColor = if (isDarkTheme) Color(0xFFB0B0B0) else Color(0xFF757575)


    LaunchedEffect(key1 = uiState.isRegistered) {
        if (uiState.isRegistered && uiState.user != null) {
            onRegisterSuccess(uiState.user!!)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAdd,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Text(
                text = "Completa tus datos para registrarte",
                fontSize = 14.sp,
                color = secondaryTextColor,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Input(
                        value = name,
                        onValueChange = { viewModel.onNameChange(it) },
                        placeholder = "Nombre de usuario",
                        leadingIcon = Icons.Outlined.Person,
                        keyboardType = KeyboardType.Text
                    )

                    Input(
                        value = password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        placeholder = "Contraseña",
                        leadingIcon = Icons.Outlined.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )

                    Text(
                        text = "Tipo de cuenta",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Selección de Roles (3 opciones ahora)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedRole == "customer",
                            onClick = { viewModel.onRoleChange("customer") },
                            label = { Text("Cliente", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = selectedRole == "delivery",
                            onClick = { viewModel.onRoleChange("delivery") },
                            label = { Text("Repartidor", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = selectedRole == "seller",
                            onClick = { viewModel.onRoleChange("seller") },
                            label = { Text("Vendedor", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Campo de dirección general
                    Input(
                        value = address,
                        onValueChange = { viewModel.onAddressChange(it) },
                        placeholder = "Dirección",
                        leadingIcon = Icons.Outlined.Home,
                        keyboardType = KeyboardType.Text
                    )

                    // Campos específicos para SELLER
                    if (selectedRole == "seller") {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Información del establecimiento",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Input(
                            value = establishmentName,
                            onValueChange = { viewModel.onEstablishmentNameChange(it) },
                            placeholder = "Nombre del establecimiento",
                            leadingIcon = Icons.Outlined.Store,
                            keyboardType = KeyboardType.Text
                        )

                        Input(
                            value = establishmentAddress,
                            onValueChange = { viewModel.onEstablishmentAddressChange(it) },
                            placeholder = "Dirección del establecimiento",
                            leadingIcon = Icons.Outlined.LocationOn,
                            keyboardType = KeyboardType.Text
                        )
                    }

                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.onRegisterClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !uiState.isLoading &&
                                name.isNotBlank() &&
                                password.isNotBlank() &&
                                (selectedRole != "seller" || (establishmentName.isNotBlank() && establishmentAddress.isNotBlank())),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Registrarse",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = onNavigateToLogin,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Inicia sesión",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}