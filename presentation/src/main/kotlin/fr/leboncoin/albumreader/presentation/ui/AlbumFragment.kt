package fr.leboncoin.albumreader.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.albumreader.presentation.databinding.FragmentAlbumBinding
import fr.leboncoin.albumreader.presentation.model.AlbumUiState
import fr.leboncoin.albumreader.presentation.viewmodel.AlbumViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class AlbumFragment : Fragment() {

    private val args: AlbumFragmentArgs by navArgs()

    private val albumViewModel: AlbumViewModel by activityViewModels()

    private var _viewBinding: FragmentAlbumBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val adapter = PictureAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentAlbumBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    albumViewModel.state.collect(::onStateChanged)
                }
                launch {
                    albumViewModel.pictureSelectionState.collect(::onPictureSelection)
                }
            }
        }

        viewBinding.albumPictures.setUpRecyclerView()

        albumViewModel.getAlbumPictures(albumId = args.albumId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun onStateChanged(state: AlbumUiState) {
        when (state) {
            is AlbumUiState.Loading -> viewBinding.apply {
                albumPictures.isVisible = false
                albumLoader.isVisible = true
            }
            is AlbumUiState.Error -> {
                viewBinding.albumLoader.isVisible = false
                Toast.makeText(context, state.errorMessageResId, Toast.LENGTH_SHORT).show()
            }
            is AlbumUiState.Success -> {
                adapter.submitList(state.pictures)
                viewBinding.apply {
                    albumPictures.isVisible = true
                    albumLoader.isVisible = false
                }
            }
        }
    }

    private fun onPictureSelection(pictureId: Int) {
        val action = AlbumFragmentDirections.actionAlbumFragmentToPictureFragment(pictureId = pictureId)
        findNavController().navigate(action)
    }

    private fun RecyclerView.setUpRecyclerView() {
        val recyclerViewLayoutManager = GridLayoutManager(context, 4)
        layoutManager = recyclerViewLayoutManager
        adapter = this@AlbumFragment.adapter
    }
}