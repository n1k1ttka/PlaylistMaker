package com.example.playlistmaker.UI.media.fragment

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Presentation.utils.dpToPx
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.media.view_model.NewPlayListViewModel
import com.example.playlistmaker.databinding.PlaylistCreateFragmentBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlayListFragment: Fragment() {

    private var binding: PlaylistCreateFragmentBinding? = null
    private val viewModel: NewPlayListViewModel by viewModel()

    private var isExitConfirmed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaylistCreateFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBackPress()
        binding?.playlistAvatar?.setImageURI(viewModel.isImageSelected.value)

        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            binding?.playlistAvatar?.setImageDrawable(null)
            Glide.with(this)
                .load(uri)
                .transform(RoundedCorners(8.dpToPx(requireContext())))
                .into(binding?.playlistAvatar!!)
            binding?.playlistAvatar?.setImageURI(uri)
            viewModel.setImageSelected(uri)
        }

        binding?.nameEditText?.addTextChangedListener {
            viewModel.updateName(it?.toString().orEmpty())
            binding?.createButton?.isEnabled = !it?.trim().isNullOrEmpty()
        }

        binding?.descriptionEditText?.addTextChangedListener {
            viewModel.updateDescription(it?.toString().orEmpty())
        }

        binding?.back?.setOnClickListener {
            if (viewModel.hasUnsavedChanges()) {
                showConfirmExitDialog()
            } else {
                findNavController().popBackStack()
            }
        }

        binding?.playlistAvatar?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding?.createButton?.setOnClickListener {
            isExitConfirmed = true
            val uri = viewModel.isImageSelected.value
            val path = if (uri != null) {
                saveImageToPrivateStorage(uri)
            } else ""
            viewModel.createPlaylist(path)

            Snackbar.make(
                requireView(),
                getString(R.string.playlist_created, viewModel.name.value),
                Snackbar.LENGTH_SHORT
            )
                .setBackgroundTint(resources.getColor(R.color.black_white))
                .setTextColor(resources.getColor(R.color.white_black))
                .show()
            findNavController().popBackStack()
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!isExitConfirmed && viewModel.hasUnsavedChanges()) {
                showConfirmExitDialog()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun showConfirmExitDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.finish_to_create_playlist))
            .setMessage(R.string.all_unsaved_data_will_be_lost)
            .setPositiveButton(R.string.finish) { _, _ ->
                isExitConfirmed = true
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.setOnShowListener {
            val positiveColor = ContextCompat.getColor(requireContext(), R.color.blue)
            val negativeColor = ContextCompat.getColor(requireContext(), R.color.blue)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(positiveColor)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(negativeColor)
        }

        dialog.show()
    }

    private fun saveImageToPrivateStorage(uri: Uri): String {

        val directory = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistAvatars")
        if (!directory.exists()){
            directory.mkdirs()
        }

        val file = File(directory, "playlist_${System.currentTimeMillis()}.png")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.absolutePath
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}