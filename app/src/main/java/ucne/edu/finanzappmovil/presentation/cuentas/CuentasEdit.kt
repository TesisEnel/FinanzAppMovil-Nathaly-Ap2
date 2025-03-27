@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.finanzappmovil.presentation.cuentas

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.finanzappmovil.presentation.cuentas.CuentaViewModel

@Composable
fun CuentaEditScreen(
    cuentaId: Int,
    viewModel: CuentaViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    // Cargar la cuenta para edición
    LaunchedEffect(cuentaId) {
        viewModel.selectedCuenta(cuentaId)
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
