package ucne.edu.finanzappmovil.presentation.cuentas

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
import ucne.edu.finanzappmovil.data.local.entities.CuentaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaListScreen(
    viewModel: CuentaViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,  // Navegar a la pantalla de edición con el ID
    onNavigateToDelete: (Int) -> Unit // Manejar eliminación
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }  // Estado para la consulta de búsqueda

    // Filtrar las cuentas basado en la consulta de búsqueda
    val filteredCuentas = uiState.cuentas.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) || it.tipo.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cuentas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF2196F3))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Cuenta", tint = Color.White)
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
                label = { Text("Buscar Cuentas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredCuentas.isEmpty()) {
                Text(
                    text = "No hay cuentas registradas",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(filteredCuentas) { cuenta ->
                        CuentaItem(
                            cuenta = cuenta,
                            onEditClick = { cuenta.CuentaId?.let { onNavigateToEdit(it) } },
                            onDeleteClick = { cuenta.CuentaId?.let { onNavigateToDelete(it) } }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun CuentaItem(
    cuenta: CuentaEntity,
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
                Text(text = cuenta.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Tipo: ${cuenta.tipo}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Saldo de Apertura: $${cuenta.saldo}", style = MaterialTheme.typography.bodySmall)
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
