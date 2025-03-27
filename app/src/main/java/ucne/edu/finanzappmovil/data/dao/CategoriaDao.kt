package ucne.edu.finanzappmovil.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.finanzappmovil.data.entities.CategoriaEntity

@Dao
interface CategoriaDao {

    @Upsert()
    suspend fun save(categoria: CategoriaEntity)

    @Query(
        """
            SELECT *
            FROM Categorias 
            WHERE CategoriaId == :id limit 1
        """
    )
    suspend fun find(id: Int): CategoriaEntity?

    @Delete
    suspend fun delete(Categoria: CategoriaEntity)

    @Query("SELECT * FROM Categorias")
    fun getAll(): Flow<List<CategoriaEntity>>

    @Update
    suspend fun update(categoria: CategoriaEntity)

    @Query("SELECT * FROM Categorias WHERE nombre = :nombre LIMIT 1")
    suspend fun findBynombredelaCategoria(nombre: String): CategoriaEntity?
}