package ucne.edu.finanzappmovil.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categorias")
data class CategoriaEntity(
    @PrimaryKey
    val CategoriaId:Int? = null,
    val usuarioId: Int,
    val nombre: String,
    val descripcion: String,
)
