package fr.leboncoin.albumreader.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.albumreader.presentation.databinding.FragmentPictureBinding
import fr.leboncoin.albumreader.presentation.model.PictureUiState
import fr.leboncoin.albumreader.presentation.viewmodel.PictureViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class PictureFragment : Fragment() {

    private val args: PictureFragmentArgs by navArgs()

    private val pictureViewModel: PictureViewModel by viewModels()

    private var _viewBinding: FragmentPictureBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentPictureBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                pictureViewModel.state.collect(::onStateChanged)
            }
        }

        pictureViewModel.getPictureDetails(pictureId = args.pictureId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun onStateChanged(state: PictureUiState) {
        when (state) {
            is PictureUiState.Loading -> viewBinding.pictureLoader.isVisible = true
            is PictureUiState.Error -> {
                viewBinding.pictureLoader.isVisible = false
                Toast.makeText(context, state.errorMessageResId, Toast.LENGTH_SHORT).show()
            }
            is PictureUiState.Success -> {
                viewBinding.apply {
                    pictureTitle.text = state.title
                    pictureImg.load(data = state.pictureUrl.toUri())
                    pictureLoader.isVisible = false
                }
            }
        }
    }
}