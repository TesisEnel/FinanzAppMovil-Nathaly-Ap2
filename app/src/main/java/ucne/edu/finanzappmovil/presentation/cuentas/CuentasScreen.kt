@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.finanzappmovil.presentation.cuentas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.finanzappmovil.presentation.cuentas.CuentaViewModel

@Composable
fun CuentaScreen(
    viewModel: CuentaViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.nuevo() // Siempre iniciar con una nueva cuenta
    }

    CuentaBodyScreen(
        uiState = uiState.value,
        onNombreChange = viewModel::onNombreChange,
        onTipoChange = viewModel::onTipoChange,
        onSaldoChange = viewModel::onSaldoChange,
        onSaveClick = {
            viewModel.save()
            goBack()
        },
        goBack = goBack
    )
}

@Composable
fun CuentaBodyScreen(
    uiState: CuentaViewModel.UiState,
    onNombreChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onSaldoChange: (Double) -> Unit,
    onSaveClick: () -> Unit,
    goBack: () -> Unit
) {
    var mostrarErrores by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Cuentas") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            uiState.errorMessages?.let { errorMessage ->
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            }
            uiState.successMessage?.let { successMessage ->
                Text(text = successMessage, color = Color.Green, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nombre de la Cuenta") },
                value = uiState.nombre,
                onValueChange = {
                    onNombreChange(it)
                    mostrarErrores = false
                },
                isError = mostrarErrores && uiState.nombre.isBlank()
            )
            if (mostrarErrores && uiState.nombre.isBlank()) {
                Text(text = "Este campo es obligatorio", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Tipo de Cuenta") },
                value = uiState.tipo,
                onValueChange = {
                    onTipoChange(it)
                    mostrarErrores = false
                },
                isError = mostrarErrores && uiState.tipo.isBlank()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Saldo de Apertura") },
                value = uiState.saldo.toString(),
                onValueChange = { value ->
                    onSaldoChange(value.toDoubleOrNull() ?: 0.0)
                    mostrarErrores = false
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = mostrarErrores && uiState.saldo < 0
            )
            if (mostrarErrores && uiState.saldo < 0) {
                Text(text = "El saldo no puede ser negativo", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (uiState.nombre.isBlank() || uiState.tipo.isBlank() || uiState.saldo < 0) {
                        mostrarErrores = true
                    } else {
                        onSaveClick()
                    }
                }
            ) {
                Text(text = "Guardar Cuenta")
            }
        }
    }
}
