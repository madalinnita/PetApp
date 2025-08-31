package com.nitaioanmadalin.petapp.ui.petlist.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    LifecycleHandler(
        onCreate = { viewModel.getData() }
    )

    Scaffold(
        topBar = {
            PetListTopBar()
        }
    ) { paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            when (val state = viewState) {
                is PetListScreenState.Error -> ErrorScreen(
                    onRetry = { viewModel.getData() },
                    isInternetConnectionAvailable = state.isInternetAvailable
                )

                is PetListScreenState.Loading -> LoadingScreen()
                is PetListScreenState.Success -> SuccessScreen(
                    data = state.pets,
                    onPetClicked = onPetClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Find your new friend")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}