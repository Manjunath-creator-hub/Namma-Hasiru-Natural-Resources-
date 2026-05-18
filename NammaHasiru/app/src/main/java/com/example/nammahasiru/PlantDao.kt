package com.example.nammahasiru

import androidx.room.*

@Dao
interface PlantDao {

    @Insert
    fun insertPlant(plant: PlantEntity)

    @Query("SELECT * FROM plants")
    fun getAllPlants(): List<PlantEntity>
}