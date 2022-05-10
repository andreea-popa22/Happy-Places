package com.example.happyplacesapp.data

import HappyPlaceItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast

import com.example.happyplacesapp.MainActivity

import com.google.firebase.database.DatabaseError

import androidx.annotation.NonNull

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import android.util.JsonReader
import org.json.JSONObject


class Datasource {
    private var places : MutableList<HappyPlaceItem> = mutableListOf<HappyPlaceItem>()
    private lateinit var database: DatabaseReference

    fun loadPlaces(): MutableList<HappyPlaceItem> {
        var value: String = "{}"
        val strUrl = "https://happy-places-57ca4-default-rtdb.europe-west1.firebasedatabase.app/data/"
        database = FirebaseDatabase.getInstance(strUrl).getReference("places")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value = snapshot.getValue(String::class.java).toString()
            }
            override fun onCancelled(error: DatabaseError) {
                    println("error getting places data")
                }
            });
        val placesText = JSONObject(value)
        for (i in 0 until placesText.length()) {
            val item : JSONObject = placesText.getString(i.toString()) as JSONObject
            val name : String = item.getString("name")
            val location : String = item.getString("location")
            val image : String = item.getString("image")
            val id = i+1
            val place : HappyPlaceItem = HappyPlaceItem(id.toString(), name, image, location)
            places.add(place)
        }
        return places
    }
}
