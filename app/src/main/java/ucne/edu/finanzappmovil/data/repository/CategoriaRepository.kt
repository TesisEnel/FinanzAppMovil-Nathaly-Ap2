package ucne.edu.finanzappmovil.data.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.finanzappmovil.data.dao.CategoriaDao
import ucne.edu.finanzappmovil.data.entities.CategoriaEntity
import javax.inject.Inject

class CategoriaRepository  @Inject constructor(
    private val categoriaDao: CategoriaDao
){
    // Obtener todas las cuentas
    fun getAll(): Flow<List<CategoriaEntity>> = categoriaDao.getAll()

    // Guardar una cuenta
    suspend fun save(cuenta: CategoriaEntity) = categoriaDao.save(cuenta)

    // Actualizar cuenta
    suspend fun actualizar(cuenta: CategoriaEntity) = categoriaDao.update(cuenta)

    // Obtener cuenta por ID
    suspend fun getCuentaById(CategoriaID: Int): CategoriaEntity? = categoriaDao.find(CategoriaID)

    // Eliminar cuenta
    suspend fun delete(CategoriaID: CategoriaEntity) = categoriaDao.delete(CategoriaID)

    // Obtener cuenta por nombre
    suspend fun getCategoriaByNombre(nombre: String): CategoriaEntity? = categoriaDao.findBynombredelaCategoria(nombre)


}