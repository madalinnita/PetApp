package com.nitaioanmadalin.petapp.presentation.petlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitaioanmadalin.petapp.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider
import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.petapp.core.utils.rx.SchedulerProvider
import com.nitaioanmadalin.petapp.domain.usecase.getdevices.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
    private val getPetsUseCase: GetPetsUseCase,
    private val connectivityUtils: ConnectivityUtils,
    private val dispatchers: CoroutineDispatchersProvider,
    private val scheduler: SchedulerProvider,
    private val logProvider: LogProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _state: MutableStateFlow<PetListScreenState> = MutableStateFlow(
        PetListScreenState.Loading()
    )
    val state: StateFlow<PetListScreenState> = _state

    private val isUsingRx = true

    fun getData(context: Context) {
        if (isUsingRx) {
            getRxData()
        } else {
            getCoroutinesData(context)
        }
    }

    fun getRxData() {
        val disposable = getPetsUseCase
            .getRxPetList()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.main()).subscribe({
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
            }, {
                _state.value = PetListScreenState.Error(it)
            })

        disposables.add(disposable)
    }

    private fun getCoroutinesData(context: Context) {
        if (connectivityUtils.isConnectionAvailable(context)) {
            viewModelScope.launch(dispatchers.io()) {
                // Delay applied in order to see properly the Loading screen for Assessment purposes
                delay(2000)
                getPetsUseCase.getPetList().onEach {
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
                    Throwable("Internet connection is not available"), isInternetAvailable = false
                )
            }
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    companion object {
        private const val TAG = "PetsViewModel"
    }
}