package com.example.nammahasiru

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val species: String,
    val latitude: String,
    val longitude: String,
    val status: String
)