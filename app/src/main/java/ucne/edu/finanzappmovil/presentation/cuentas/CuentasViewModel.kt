package ucne.edu.finanzappmovil.presentation.cuentas

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
import ucne.edu.finanzappmovil.data.local.entities.CuentaEntity
import ucne.edu.finanzappmovil.data.repository.CuentaRepository
import javax.inject.Inject

@HiltViewModel
class CuentaViewModel @Inject constructor(
    private val cuentaRepository: CuentaRepository
) : ViewModel() {

    // Estado de la UI
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        getCuentas()
    }

    // Función para actualizar el saldo de la cuenta
    fun actualizarSaldo(cuenta: CuentaEntity, nuevoSaldo: Double) {
        viewModelScope.launch {
            val cuentaActualizada = cuenta.copy(saldo = nuevoSaldo)
            cuentaRepository.actualizar(cuentaActualizada) // Llamada al repositorio para guardar el saldo actualizado
        }
    }



    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            when {
                state.nombre.isBlank() -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessages = "El nombre de la cuenta no puede estar vacío",
                            successMessage = null
                        )
                    }
                }

                state.saldo < 0 -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessages = "El saldo no puede ser negativo",
                            successMessage = null
                        )
                    }
                }

                state.CuentaId == 0 -> {  // Verifica que la cuenta sea nueva
                    try {
                        val exists = cuentaRepository.getCuentaByNombre(state.nombre)
                        if (exists != null) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessages = "Ya existe una cuenta con este nombre",
                                    successMessage = null
                                )
                            }
                        } else {
                            cuentaRepository.save(state.toEntity())
                            _uiState.update { currentState ->
                                currentState.copy(
                                    successMessage = "Cuenta guardada exitosamente",
                                    errorMessages = null
                                )
                            }
                            getCuentas() // Recargar lista
                            nuevo() // Restablecer formulario
                            delay(3000)
                            _uiState.update { currentState ->
                                currentState.copy(successMessage = null, errorMessages = null)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CuentaViewModel", "Error al guardar la cuenta", e)
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessages = "Error al guardar la cuenta",
                                successMessage = null
                            )
                        }
                        delay(3000)
                        _uiState.update { currentState ->
                            currentState.copy(successMessage = null, errorMessages = null)
                        }
                    }
                }

                else -> {  // Si no es nueva, entonces es edición
                    try {
                        val exists = cuentaRepository.getCuentaByNombre(state.nombre)
                        if (exists != null && exists.CuentaId != state.CuentaId) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessages = "Ya existe una cuenta con este nombre",
                                    successMessage = null
                                )
                            }
                        } else {
                            cuentaRepository.save(state.toEntity())
                            _uiState.update { currentState ->
                                currentState.copy(
                                    successMessage = "Cuenta editada exitosamente",
                                    errorMessages = null
                                )
                            }
                            getCuentas() // Recargar lista
                            nuevo() // Restablecer formulario
                            delay(3000)
                            _uiState.update { currentState ->
                                currentState.copy(successMessage = null, errorMessages = null)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CuentaViewModel", "Error al guardar la cuenta", e)
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessages = "Error al guardar la cuenta",
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

    fun selectedCuenta(CuentaId: Int) {
        viewModelScope.launch {
            if (CuentaId > 0) {
                cuentaRepository.getCuentaById(CuentaId)?.let { cuenta ->
                    _uiState.update {
                        it.copy(
                            CuentaId = cuenta.CuentaId,
                            nombre = cuenta.nombre,
                            tipo = cuenta.tipo,
                            saldo = cuenta.saldo,
                            isNew = false
                        )
                    }
                }
            }
        }
    }

    fun onCategoriaSeleccionada(categoria: CategoriaEntity) {
        _uiState.update { currentState ->
            currentState.copy(categoriaSeleccionada = categoria)
        }
    }

    fun delete() {
        viewModelScope.launch {
            try {
                cuentaRepository.delete(_uiState.value.toEntity())
                setSuccessMessage("Cuenta eliminada exitosamente")
                nuevo()
            } catch (e: Exception) {
                Log.e("CuentaViewModel", "Error al eliminar la cuenta", e)
                setErrorMessage("Error al eliminar la cuenta")
            }
        }
    }

    fun getCuentas() {
        viewModelScope.launch {
            cuentaRepository.getAll().collect { cuentas ->  // Aquí se recoge la lista de cuentas
                _uiState.update { it.copy(cuentas = cuentas) }
            }
        }
    }


    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun onTipoChange(tipo: String) {
        _uiState.update { it.copy(tipo = tipo) }
    }

    fun onSaldoChange(saldo: Double) {
        _uiState.update { it.copy(saldo = saldo) }
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
        val CuentaId: Int? = null,
        val nombre: String = "",
        val tipo: String = "",
        val saldo: Double = 0.0,
        val errorMessages: String? = null,
        val categoriaSeleccionada: CategoriaEntity? = null,
        val successMessage: String? = null,
        val cuentas: List<CuentaEntity> = emptyList(),
        val isNew: Boolean = true
    )

    fun UiState.toEntity() = CuentaEntity(
        CuentaId = CuentaId,
        usuarioId = 0,
        nombre = nombre,
        tipo = tipo,
        saldo = saldo
    )
}
