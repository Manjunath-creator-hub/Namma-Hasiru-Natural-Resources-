package com.example.nammahasiru

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var db: PlantDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = PlantDatabase.getDatabase(this)

        val speciesInput = findViewById<EditText>(R.id.speciesInput)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val listView = findViewById<TextView>(R.id.listView)

        refreshPlants(listView)

        saveBtn.setOnClickListener {
            val species = speciesInput.text.toString()

            if (species.isNotEmpty()) {
                Thread {
                    db.plantDao().insertPlant(
                        PlantEntity(
                            species = species,
                            latitude = "12.9716",
                            longitude = "77.5946",
                            status = "Planted"
                        )
                    )

                    val request = OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInitialDelay(90, TimeUnit.DAYS)
                        .build()

                    WorkManager.getInstance(this).enqueue(request)

                    runOnUiThread {
                        speciesInput.text.clear()
                        Toast.makeText(this, "Plant Saved", Toast.LENGTH_SHORT).show()
                        refreshPlants(listView)
                    }
                }.start()
            }
        }
    }

    private fun refreshPlants(view: TextView) {
        Thread {
            val plants = db.plantDao().getAllPlants()
            val text = plants.joinToString("\n") {
                "🌱 ${it.species} - ${it.status}"
            }

            runOnUiThread {
                view.text = text.ifEmpty { "No Plants Added" }
            }
        }.start()
    }
}