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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.albumreader.presentation.databinding.FragmentHomeBinding
import fr.leboncoin.albumreader.presentation.model.HomeUiState
import fr.leboncoin.albumreader.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val adapter = PictureAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    homeViewModel.state.collect(::onStateChanged)
                }
                launch {
                    homeViewModel.albumSelectionState.collect(::onAlbumSelection)
                }
            }
        }

        viewBinding.homeAlbums.setUpRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun onStateChanged(state: HomeUiState) {
        when (state) {
            is HomeUiState.Loading -> viewBinding.apply {
                homeAlbums.isVisible = false
                homeLoader.isVisible = true
            }
            is HomeUiState.Error -> {
                viewBinding.homeLoader.isVisible = false
                Toast.makeText(context, state.errorMessageResId, Toast.LENGTH_SHORT).show()
            }
            is HomeUiState.Success -> {
                adapter.submitList(state.albums)
                viewBinding.apply {
                    homeAlbums.isVisible = true
                    homeLoader.isVisible = false
                }
            }
        }
    }
    
    private fun onAlbumSelection(albumId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToAlbumFragment(albumId = albumId)
        findNavController().navigate(action)
    }

    private fun RecyclerView.setUpRecyclerView() {
        val recyclerViewLayoutManager = GridLayoutManager(context, 4)
        layoutManager = recyclerViewLayoutManager
        adapter = this@HomeFragment.adapter
    }
}