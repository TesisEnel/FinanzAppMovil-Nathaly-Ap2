package ucne.edu.finanzappmovil.presentation.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ucne.edu.finanzappmovil.data.entities.CategoriaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaListScreen(
    viewModel: CategoriaViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToDelete: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredCategorias = uiState.categorias.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) ||
                it.descripcion.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Categorías") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF2196F3))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Categoría", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Campo de búsqueda
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar Categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredCategorias.isEmpty()) {
                Text(
                    text = "No hay categorías registradas",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(filteredCategorias) { categoria ->
                        CategoriaItem(
                            categoria = categoria,
                            onEditClick = { categoria.CategoriaId?.let { onNavigateToEdit(it) } },
                            onDeleteClick = { categoria.CategoriaId?.let { onNavigateToDelete(it) } }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CategoriaItem(
    categoria: CategoriaEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = categoria.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Descripción: ${categoria.descripcion}", style = MaterialTheme.typography.bodySmall)
            }

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}
