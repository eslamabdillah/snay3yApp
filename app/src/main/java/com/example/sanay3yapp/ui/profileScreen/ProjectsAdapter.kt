package com.example.sanay3yapp.ui.profileScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import dataBase.models.GallaryProject

class ProjectsAdapter(var projects: MutableList<GallaryProject>? = null) :
    RecyclerView.Adapter<ProjectsAdapter.projectVH>() {
    class projectVH(var view: View, var context: Context) : ViewHolder(view) {
        val projectName: TextView = view.findViewById(R.id.name_project)
        val projectPhoto: ImageView = view.findViewById(R.id.image_project)

        fun bindData(project: GallaryProject) {
            // Set the project name into the projectName TextView
            projectName.text = project.nameProject

            // Check if the photoArray is not empty to prevent IndexOutOfBoundsException
            if (project.photoArray.isNotEmpty()) {
                // Using Glide to load the first image from photoArray into projectPhoto ImageView
                Glide.with(context)
                    .load(project.photoArray[0].toUri())  // Ensure the string is properly converted to a Uri
                    .placeholder(R.drawable.logo)         // Shows a placeholder image while loading
                    .error(R.drawable.logo)               // Shows this image in case of an error
                    .into(projectPhoto)
            } else {
                // If no images are available, set a default image
                projectPhoto.setImageResource(R.drawable.logo)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): projectVH {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallary, parent, false)
        return projectVH(view, parent.context)
    }

    override fun getItemCount(): Int {
        return projects?.size ?: 0
    }

    override fun onBindViewHolder(holder: projectVH, position: Int) {
        var currentProject = projects!![position]
        holder.bindData(currentProject)
    }

    fun changeAdapterList(newList: MutableList<GallaryProject>) {
        projects = newList
        notifyDataSetChanged()

    }
}