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
import android.util.Log
import com.google.firebase.database.ktx.getValue
import org.json.JSONObject


class Datasource {
    private var places : MutableList<HappyPlaceItem> = mutableListOf<HappyPlaceItem>()
    private lateinit var database: DatabaseReference
    private var placesBackup: String = "{ \"places\": {\"1\": {\"name\": \"Happy Place\", \"image\": \"happy_places1.png\", \"location\": \"Herriman, Utah\"}, \"2\": {\"name\": \"Happy Place\", \"image\": \"happy_places2.png\", \"location\": \"Disneyland, Paris\"}, \"3\": {\"name\": \"Happy Place\", \"image\": \"happy_places3.png\", \"location\": \"Wien, Austria\"}, \"4\" : {\"name\": \"Happy Place\", \"image\": \"happy_places4.png\", \"location\": \"Herriman, Utah\"}}}"

    public fun loadPlaces(): MutableList<HappyPlaceItem> {
        var value: String = "{}"
        val strUrl = "https://happy-places-57ca4-default-rtdb.europe-west1.firebasedatabase.app/"
        database = FirebaseDatabase.getInstance(strUrl).getReference("data")

        database.get().addOnSuccessListener {
            if (it.exists()){
                value = it.child("places").toString()

            } else {
                Log.w("Datasource","No places")
            }

        }.addOnFailureListener{
            Log.w("Datasource","Failed")
        }

        //        val postListener = object : ValueEventListener {
        //            override fun onDataChange(snapshot: DataSnapshot) {
        //                value = snapshot.getValue<String>().toString()
        //            }
        //            override fun onCancelled(error: DatabaseError) {
        //                    Log.w("Load Places", "error getting places data")
        //                }
        //            }
        //        database.addValueEventListener(postListener)
        // val placesText = JSONObject(value)
        val placesText = JSONObject(placesBackup)
        for (i in 1 until placesText.length()) {
            val item : JSONObject = JSONObject(placesText.getString(i.toString()))
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
