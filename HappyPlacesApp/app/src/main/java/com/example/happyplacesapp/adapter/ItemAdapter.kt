package com.example.happyplacesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.R
import android.graphics.drawable.Drawable
import HappyPlaceItem
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.happyplacesapp.data.Datasource

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
    private lateinit var dataset: List<HappyPlaceItem>

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.place_name)
        val location: TextView = view.findViewById(R.id.place_location)
        val image: ImageView = view.findViewById(R.id.place_img)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.happy_place_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        dataset = Datasource().loadPlaces()
        return dataset.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.name.text =  item.name
        holder.location.text =  item.location
        holder.image.setImageDrawable(holder.image.context.getDrawable(holder.image.context.resources.getIdentifier(item.image, "drawable", holder.image.context.packageName)))
        //val myDrawable: Drawable? = context.getDrawable(context.resources.getIdentifier(item.image, "drawable", context.packageName))
        //holder.image.setImageDrawable(myDrawable)
    }
}