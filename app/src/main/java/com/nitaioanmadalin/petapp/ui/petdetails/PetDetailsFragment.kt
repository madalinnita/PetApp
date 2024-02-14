package com.nitaioanmadalin.petapp.ui.petdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nitaioanmadalin.petapp.ui.base.BaseComposeWrapperFragment
import com.nitaioanmadalin.petapp.ui.petdetails.screen.DeviceDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetDetailsFragment: BaseComposeWrapperFragment() {

    private val args: PetDetailsFragmentArgs by navArgs()

    @Composable
    override fun FragmentContent(modifier: Modifier) {
        DeviceDetailsScreen(
            pet = args.pet,
            navigator = findNavController()
        )
    }
}