package fr.leboncoin.albumreader.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.databinding.FragmentPictureBinding

internal class PictureFragment : Fragment() {

    private val args: PictureFragmentArgs by navArgs()

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

        viewBinding.apply {
            pictureTitle.text = "accusamus beatae ad facilis cum similique qui sunt"
            viewBinding.pictureImg.load(data = "https://via.placeholder.com/600/92c952".toUri()) {
                crossfade(true)
                placeholder(R.drawable.ic_picture)
                fallback(R.drawable.ic_picture)
                error(R.drawable.ic_picture)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}