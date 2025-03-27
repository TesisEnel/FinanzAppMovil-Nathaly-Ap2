package ucne.edu.finanzappmovil.presentation.login

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class AuthUser(
    val name: String,
    val email: String,
    val registrationDate: String
)

@Composable
fun ProfileScreen(
    navController: NavController,
    user: AuthUser
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Icono de usuario",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = androidx.compose.ui.Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Nombre: ${user.name}", style = MaterialTheme.typography.h6)
            Text(text = "Correo: ${user.email}", style = MaterialTheme.typography.body1)
            Text(text = "Miembro desde: ${user.registrationDate}", style = MaterialTheme.typography.body2)
        }
    }
}