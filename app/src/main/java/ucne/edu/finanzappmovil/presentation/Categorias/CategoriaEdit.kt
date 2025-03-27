@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.finanzappmovil.presentation.categorias

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.finanzappmovil.presentation.categorias.CategoriaViewModel

@Composable
fun CategoriaEditScreen(
    categoriaId: Int,
    viewModel: CategoriaViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(categoriaId) {
        viewModel.selectedCategoria(categoriaId)
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
