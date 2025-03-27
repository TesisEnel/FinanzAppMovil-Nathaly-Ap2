package edu.ucne.finanzappmovil.presentation.cuentas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.finanzappmovil.presentation.cuentas.CuentaViewModel

@Composable
fun DeleteCuentaScreen(
    viewModel: CuentaViewModel = hiltViewModel(),
    cuentaId: Int,
    goBack: () -> Unit
) {
    LaunchedEffect(cuentaId) {
        viewModel.selectedCuenta(cuentaId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DeleteCuentaBodyScreen(
        uiState = uiState,
        onDeleteCuenta = {
            viewModel.delete()
            goBack()
        },
        goBack = goBack
    )
}

@Composable
fun DeleteCuentaBodyScreen(
    uiState: CuentaViewModel.UiState,
    onDeleteCuenta: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Text(
                text = "¿Está seguro de Eliminar esta Cuenta?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(80.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Nombre de la Cuenta: ${uiState.nombre}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Tipo: ${uiState.tipo}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Saldo: ${uiState.saldo}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onDeleteCuenta()
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(text = "Eliminar", color = Color.White)
            }

            Button(
                onClick = {
                    goBack()
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text(text = "Cancelar", color = Color.White)
            }
        }
    }
}
