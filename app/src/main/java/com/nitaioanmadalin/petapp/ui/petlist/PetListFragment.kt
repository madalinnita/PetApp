package com.nitaioanmadalin.petapp.ui.petlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nitaioanmadalin.petapp.presentation.petlist.PetListViewModel
import com.nitaioanmadalin.petapp.ui.base.BaseComposeWrapperFragment
import com.nitaioanmadalin.petapp.ui.petlist.screen.PetListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetListFragment: BaseComposeWrapperFragment() {

    private val viewModel by activityViewModels<PetListViewModel>()

    @Composable
    override fun FragmentContent(modifier: Modifier) {
        PetListScreen(
            viewModel = viewModel,
            onPetClicked = {
                val action = PetListFragmentDirections.petlistToDetails(it)
                findNavController().navigate(action)
            }
        )
    }
}