package fr.leboncoin.albumreader.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.leboncoin.albumreader.presentation.databinding.FragmentHomeBinding
import fr.leboncoin.albumreader.presentation.model.ItemUiModel

internal class HomeFragment : Fragment() {

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

        viewBinding.homeAlbums.setUpRecyclerView()

        val onAlbumSelection: (Int) -> Unit = { albumId ->
            val action = HomeFragmentDirections.actionHomeFragmentToAlbumFragment(albumId = albumId)
            findNavController().navigate(action)
        }
        adapter.submitList(
            listOf(
                ItemUiModel(
                    id = 1,
                    thumbnailUrl = "https://via.placeholder.com/150/92c952",
                    onSelection = onAlbumSelection
                ),
                ItemUiModel(
                    id = 2,
                    thumbnailUrl = "https://via.placeholder.com/150/771796",
                    onSelection = onAlbumSelection
                ),
                ItemUiModel(
                    id = 3,
                    thumbnailUrl = "https://via.placeholder.com/150/24f355",
                    onSelection = onAlbumSelection
                ),
                ItemUiModel(
                    id = 4,
                    thumbnailUrl = "https://via.placeholder.com/150/d32776",
                    onSelection = onAlbumSelection
                ),
                ItemUiModel(
                    id = 5,
                    thumbnailUrl = "https://via.placeholder.com/150/f66b97",
                    onSelection = onAlbumSelection
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun RecyclerView.setUpRecyclerView() {
        val recyclerViewLayoutManager = GridLayoutManager(context, 4)
        layoutManager = recyclerViewLayoutManager
        adapter = this@HomeFragment.adapter
    }
}