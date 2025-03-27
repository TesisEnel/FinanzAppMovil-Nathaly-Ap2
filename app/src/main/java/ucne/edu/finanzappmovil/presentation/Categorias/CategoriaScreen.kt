@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.finanzappmovil.presentation.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.finanzappmovil.presentation.categorias.CategoriaViewModel

@Composable
fun CategoriaScreen(
    viewModel: CategoriaViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.nuevo() // Siempre iniciar con una nueva categoría
    }

    CategoriaBodyScreen(
        uiState = uiState.value,
        onNombreChange = viewModel::onNombreChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onSaveClick = {
            viewModel.save()
            goBack()
        },
        goBack = goBack
    )
}

@Composable
fun CategoriaBodyScreen(
    uiState: CategoriaViewModel.UiState,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    goBack: () -> Unit
) {
    var mostrarErrores by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Categorías") },
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
                label = { Text(text = "Nombre de la Categoría") },
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
                label = { Text(text = "Descripción de la Categoría") },
                value = uiState.descripcion,
                onValueChange = {
                    onDescripcionChange(it)
                    mostrarErrores = false
                },
                isError = mostrarErrores && uiState.descripcion.isBlank()
            )
            if (mostrarErrores && uiState.descripcion.isBlank()) {
                Text(text = "Este campo es obligatorio", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (uiState.nombre.isBlank() || uiState.descripcion.isBlank()) {
                        mostrarErrores = true
                    } else {
                        onSaveClick()
                    }
                }
            ) {
                Text(text = "Guardar Categoría")
            }
        }
    }
}
