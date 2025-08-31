package com.nitaioanmadalin.petapp.presentation.petlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider
import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.petapp.domain.usecase.getpets.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PetListViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val connectivityUtils: ConnectivityUtils,
    private val logProvider: LogProvider
) : ViewModel() {

    private val _state: MutableStateFlow<PetListScreenState> = MutableStateFlow(
        PetListScreenState.Loading()
    )
    val state: StateFlow<PetListScreenState> = _state

    fun getData(type: String = "dog", page: Int = 1) {
        if (connectivityUtils.isConnectionAvailable()) {
            getPetsUseCase.getPetList(type, page)
                .onEach {
                    _state.value = when (it) {
                        is AppResult.Error -> {
                            logProvider.logError(TAG, "Error - ${it.message}")
                            PetListScreenState.Error(it.exception)
                        }

                        is AppResult.Loading -> {
                            logProvider.logDebug(TAG, "Loading - ${it.daoData}")
                            PetListScreenState.Loading(it.daoData)
                        }

                        is AppResult.Success -> {
                            logProvider.logDebug(TAG, "Success - ${it.successData}")
                            PetListScreenState.Success(it.successData)
                        }
                    }
                }
                .launchIn(viewModelScope)
        } else {
            _state.value = PetListScreenState.Error(
                Throwable("Internet connection is not available"),
                isInternetAvailable = false
            )
        }
    }

    companion object {
        private const val TAG = "PetsViewModel"
    }
}