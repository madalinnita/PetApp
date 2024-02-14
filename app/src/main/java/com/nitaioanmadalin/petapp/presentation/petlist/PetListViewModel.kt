package com.nitaioanmadalin.petapp.presentation.petlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitaioanmadalin.petapp.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider
import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.cosmodeviceexplorer.domain.usecase.getdevices.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PetListViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetPetsUseCase,
    private val connectivityUtils: ConnectivityUtils,
    private val dispatchers: CoroutineDispatchersProvider,
    private val logProvider: LogProvider
): ViewModel() {

    private val _state: MutableStateFlow<PetListScreenState> = MutableStateFlow(
        PetListScreenState.Loading()
    )
    val state: StateFlow<PetListScreenState> = _state

    fun getData(context: Context) {
        if (connectivityUtils.isConnectionAvailable(context)) {
            viewModelScope.launch(dispatchers.io()) {
                // Delay applied in order to see properly the Loading screen for Assessment purposes
                delay(2000)
                getRepositoriesUseCase.getCosmoDevices().onEach {
                    withContext(dispatchers.main()) {
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
                }.launchIn(this)
            }
        } else {
            viewModelScope.launch {
                _state.value = PetListScreenState.Error(
                    Throwable("Internet connection is not available"),
                    isInternetAvailable = false
                )
            }
        }
    }

    companion object{
        private const val TAG = "PetsViewModel"
    }
}