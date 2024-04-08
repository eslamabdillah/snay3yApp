package com.example.sanay3yapp.ui.authentication_forms.signup


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityUploadPhotoBinding
import com.example.sanay3yapp.ui.Constants
import com.example.sanay3yapp.ui.authentication_forms.login.LoginActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dataBase.fireStore.DAO

class UploadPhotoActivity : AppCompatActivity() {
    lateinit var binding: ActivityUploadPhotoBinding
    lateinit var imageView: ImageView
    private val storage = Firebase.storage
    var storageRef = storage.reference
    var imagesRef = storageRef.child("images")


    companion object {
        private const val REQUEST_GALLERY_PHOTO = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.imageView.setOnClickListener {
            selectImageFromGallery()

        }

        binding.skip.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.next.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun selectImageFromGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY_PHOTO)
    }

    // This method will be called when the user selects an image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_PHOTO) {
            data?.data?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.logo) // Optional placeholder
                    .error(R.drawable.logo)
                    .into(binding.imageView)
                uploadImageToFirebase(uri)

            }
        }
    }

    // Step 4: Upload the photo to Firebase Storage
    fun uploadImageToFirebase(fileUri: Uri) {
        val storageRef = Firebase.storage.reference
        val photoRef = storageRef.child("images/${fileUri.lastPathSegment}")
        val uploadTask = photoRef.putFile(fileUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Get the download URL
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                // Here you get the download URL of the uploaded photo
                DAO.addPhotoUrl(downloadUri.toString(), Constants.idForSignUp) {

                    if (it.isSuccessful) {
                        Toast.makeText(this, "photo upload Done", Toast.LENGTH_LONG).show()
                    }
                }

            }

            Toast.makeText(this, "upload photo done", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            // Handle unsuccessful uploads
        }
    }

    fun updateUserProfile(photoUri: Uri) {
        // Here you can use the photoUri to display the image or store it in the user's profile
        // For example, using Glide to load the image into an ImageView:
        /*Glide.with(this)
            .load(photoUri)
            .into(profileImageView) // Assuming you have an ImageView with this ID*/


    }


}