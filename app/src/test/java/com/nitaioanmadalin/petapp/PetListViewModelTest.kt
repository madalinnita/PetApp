package com.nitaioanmadalin.petapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider
import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.usecase.getpets.GetPetsUseCase
import com.nitaioanmadalin.petapp.presentation.petlist.PetListScreenState
import com.nitaioanmadalin.petapp.presentation.petlist.PetListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PetListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: PetListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @MockK(relaxed = true)
    private lateinit var connectivityUtils: ConnectivityUtils

    @MockK(relaxed = true)
    private lateinit var getPetsUseCase: GetPetsUseCase

    @MockK(relaxed = true)
    private lateinit var logProvider: LogProvider

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = PetListViewModel(getPetsUseCase, connectivityUtils, logProvider)
    }

    @Test
    fun `getData emits success state when use case returns success`() = runTest {
        every { connectivityUtils.isConnectionAvailable() } returns true
        val pets = listOf(Pet(name = "Dog"))
        every { getPetsUseCase.getPetList(any(), any()) } returns flowOf(AppResult.Success(pets))

        val states = mutableListOf<PetListScreenState>()
        val job = launch { viewModel.state.toList(states) }

        viewModel.getData()

        advanceUntilIdle()

        Assert.assertTrue(states.last() is PetListScreenState.Success)

        job.cancel()
    }
}
