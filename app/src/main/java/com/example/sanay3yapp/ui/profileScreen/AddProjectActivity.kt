package com.example.sanay3yapp.ui.profileScreen

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sanay3yapp.databinding.ActivityAddProjectBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import dataBase.fireStore.DAO
import dataBase.models.GallaryProject

class AddProjectActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProjectBinding
    private val imageUris = mutableListOf<Uri>()
    private var newProject = GallaryProject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addPhoto.setOnClickListener {
            if (hasStoragePermission()) {
                pickImages()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }

        binding.addProject.setOnClickListener {
            newProject.workerId = SessionUser.worker.id
            newProject.nameProject = binding.etProjectName.text.toString()
            newProject.detailsProject = binding.etDetailsProject.text.toString()
            newProject.photoArray = convertUriListToStringList(imageUris)


            DAO.addNewProject(
                SessionUser.worker.id,
                newProject,
            ) { projectId ->
                DAO.uploadImages(projectId.toString(), imageUris) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "project done", Toast.LENGTH_LONG).show()
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {

                    }

                }

            }


        }


    }


    private fun hasStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pickImages()
            } else {
                // Handle permission denial
            }
        }

    private fun pickImages() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        pickImagesLauncher.launch(intent)
    }

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.let { data ->
                    val clipData = data.clipData
                    if (clipData != null && clipData.itemCount in 1..3) {
                        imageUris.clear() // Clear the existing URIs if any
                        for (i in 0 until clipData.itemCount) {
                            imageUris.add(clipData.getItemAt(i).uri) // Add new URIs from the result
                        }
                        displayImages() // Update the UI with the new images
                    } else if (data.data != null) { // Single image selected
                        imageUris.clear()
                        imageUris.add(data.data!!)
                        displayImages()
                    } else {
                        // Handle the case where no images are selected
                        Toast.makeText(this, "No images selected.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private fun displayImages() {
        when (imageUris.size) {
            1 -> {
                binding.photo1.setImageURI(imageUris[0])
                binding.photo2.setImageDrawable(null) // Clear the ImageView if no image is selected
                binding.photo3.setImageDrawable(null)
            }

            2 -> {
                binding.photo1.setImageURI(imageUris[0])
                binding.photo2.setImageURI(imageUris[1])
                binding.photo3.setImageDrawable(null)
            }

            3 -> {
                binding.photo1.setImageURI(imageUris[0])
                binding.photo2.setImageURI(imageUris[1])
                binding.photo3.setImageURI(imageUris[2])
            }
        }
    }

    private fun convertUriListToStringList(uriList: MutableList<Uri>): MutableList<String> {
        return uriList.map { it.toString() }.toMutableList()
    }


}