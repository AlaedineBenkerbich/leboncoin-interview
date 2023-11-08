package fr.leboncoin.albumreader.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.leboncoin.albumreader.presentation.databinding.FragmentAlbumBinding
import fr.leboncoin.albumreader.presentation.model.ItemUiModel

internal class AlbumFragment : Fragment() {

    private val args: AlbumFragmentArgs by navArgs()

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

        viewBinding.albumPictures.setUpRecyclerView()

        adapter.submitList(getPictures(args.albumId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun getPictures(albumId: Int): List<ItemUiModel> {
        val onPictureSelection: (Int) -> Unit = { pictureId ->
            val action = AlbumFragmentDirections.actionAlbumFragmentToPictureFragment(pictureId = pictureId)
            findNavController().navigate(action)
        }
        return listOf(
            ItemUiModel(
                id = 1,
                thumbnailUrl = "https://via.placeholder.com/150/92c952",
                onSelection = onPictureSelection
            ),
            ItemUiModel(
                id = 2,
                thumbnailUrl = "https://via.placeholder.com/150/771796",
                onSelection = onPictureSelection
            ),
            ItemUiModel(
                id = 3,
                thumbnailUrl = "https://via.placeholder.com/150/24f355",
                onSelection = onPictureSelection
            ),
            ItemUiModel(
                id = 4,
                thumbnailUrl = "https://via.placeholder.com/150/d32776",
                onSelection = onPictureSelection
            ),
            ItemUiModel(
                id = 5,
                thumbnailUrl = "https://via.placeholder.com/150/f66b97",
                onSelection = onPictureSelection
            ),
            ItemUiModel(
                id = 6,
                thumbnailUrl = "https://via.placeholder.com/150/f66b97",
                onSelection = onPictureSelection
            )
        )
    }

    private fun RecyclerView.setUpRecyclerView() {
        val recyclerViewLayoutManager = GridLayoutManager(context, 4)
        layoutManager = recyclerViewLayoutManager
        adapter = this@AlbumFragment.adapter
    }
}