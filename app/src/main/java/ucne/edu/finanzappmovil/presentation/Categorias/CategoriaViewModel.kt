package ucne.edu.finanzappmovil.presentation.categorias

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.finanzappmovil.data.entities.CategoriaEntity
import ucne.edu.finanzappmovil.data.repository.CategoriaRepository
import javax.inject.Inject

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    // Estado de la UI
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        getCategorias()
    }

    // Función para guardar o editar una categoría
    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            when {
                state.nombre.isBlank() -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessages = "El nombre de la categoría no puede estar vacío",
                            successMessage = null
                        )
                    }
                }

                state.descripcion.isBlank() -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessages = "La descripción no puede estar vacía",
                            successMessage = null
                        )
                    }
                }

                state.CategoriaId == 0 -> { // Si la categoría es nueva
                    try {
                        val exists = categoriaRepository.getCategoriaByNombre(state.nombre)
                        if (exists != null) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessages = "Ya existe una categoría con este nombre",
                                    successMessage = null
                                )
                            }
                        } else {
                            categoriaRepository.save(state.toEntity())
                            _uiState.update { currentState ->
                                currentState.copy(
                                    successMessage = "Categoría guardada exitosamente",
                                    errorMessages = null
                                )
                            }
                            getCategorias() // Recargar lista
                            nuevo() // Restablecer formulario
                            delay(3000)
                            _uiState.update { currentState ->
                                currentState.copy(successMessage = null, errorMessages = null)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CategoriaViewModel", "Error al guardar la categoría", e)
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessages = "Error al guardar la categoría",
                                successMessage = null
                            )
                        }
                        delay(3000)
                        _uiState.update { currentState ->
                            currentState.copy(successMessage = null, errorMessages = null)
                        }
                    }
                }

                else -> { // Si no es nueva, entonces es edición
                    try {
                        val exists = categoriaRepository.getCategoriaByNombre(state.nombre)
                        if (exists != null && exists.CategoriaId != state.CategoriaId) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessages = "Ya existe una categoría con este nombre",
                                    successMessage = null
                                )
                            }
                        } else {
                            categoriaRepository.save(state.toEntity())
                            _uiState.update { currentState ->
                                currentState.copy(
                                    successMessage = "Categoría editada exitosamente",
                                    errorMessages = null
                                )
                            }
                            getCategorias() // Recargar lista
                            nuevo() // Restablecer formulario
                            delay(3000)
                            _uiState.update { currentState ->
                                currentState.copy(successMessage = null, errorMessages = null)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CategoriaViewModel", "Error al guardar la categoría", e)
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessages = "Error al guardar la categoría",
                                successMessage = null
                            )
                        }
                        delay(3000)
                        _uiState.update { currentState ->
                            currentState.copy(successMessage = null, errorMessages = null)
                        }
                    }
                }
            }
        }
    }

    fun nuevo() {
        _uiState.update { UiState() }
    }

    fun selectedCategoria(CategoriaId: Int) {
        viewModelScope.launch {
            if (CategoriaId > 0) {
                categoriaRepository.getCuentaById(CategoriaId)?.let { categoria ->
                    _uiState.update {
                        it.copy(
                            CategoriaId = categoria.CategoriaId,
                            nombre = categoria.nombre,
                            descripcion = categoria.descripcion,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            try {
                categoriaRepository.delete(_uiState.value.toEntity())
                setSuccessMessage("Categoría eliminada exitosamente")
                nuevo()
            } catch (e: Exception) {
                Log.e("CategoriaViewModel", "Error al eliminar la categoría", e)
                setErrorMessage("Error al eliminar la categoría")
            }
        }
    }

    fun getCategorias() {
        viewModelScope.launch {
            categoriaRepository.getAll().collect { categorias ->
                _uiState.update { it.copy(categorias = categorias) }
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    private fun setErrorMessage(message: String) {
        _uiState.update { it.copy(errorMessages = message, successMessage = null) }
        clearMessagesAfterDelay()
    }

    private fun setSuccessMessage(message: String) {
        _uiState.update { it.copy(successMessage = message, errorMessages = null) }
        clearMessagesAfterDelay()
    }

    private fun clearMessagesAfterDelay() {
        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(successMessage = null, errorMessages = null) }
        }
    }

    data class UiState(
        val CategoriaId: Int? = null,
        val nombre: String = "",
        val descripcion: String = "",
        val errorMessages: String? = null,
        val successMessage: String? = null,
        val categorias: List<CategoriaEntity> = emptyList(),
        val isNew: Boolean = true
    )

    fun UiState.toEntity() = CategoriaEntity(
        CategoriaId = CategoriaId,
        usuarioId = 0,  // Aquí puedes cambiarlo según cómo manejarás el usuarioId
        nombre = nombre,
        descripcion = descripcion
    )
}
