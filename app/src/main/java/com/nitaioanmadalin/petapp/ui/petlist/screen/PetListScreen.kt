package com.nitaioanmadalin.petapp.ui.petlist.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nitaioanmadalin.petapp.core.utils.lifecycle.LifecycleHandler
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.presentation.petlist.PetListScreenState
import com.nitaioanmadalin.petapp.presentation.petlist.PetListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    viewModel: PetListViewModel,
    onPetClicked: (Pet) -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleHandler(
        onCreate = { viewModel.getData(context = context) }
    )

    Scaffold(
        topBar = {
            PetListTopBar()
        }
    ) { paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            when (val state = viewState) {
                is PetListScreenState.Error -> ErrorScreen(
                    onRetry = { viewModel.getData(context) },
                    isInternetConnectionAvailable = state.isInternetAvailable
                )

                is PetListScreenState.Loading -> LoadingScreen()
                is PetListScreenState.Success -> SuccessScreen(
                    data = state.repositories,
                    onDeviceClicked = onPetClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListTopBar() {
    TopAppBar(
        title = { Text("List of pets", color = Color.Black) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.LightGray,
            titleContentColor = Color.White
        )
    )
}