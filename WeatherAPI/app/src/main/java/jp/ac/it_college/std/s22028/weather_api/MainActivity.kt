package jp.ac.it_college.std.s22028.weather_api

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val citySpinner: Spinner = findViewById(R.id.citySpinner)
        val cityList = getCityNames()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val selectedCityName = citySpinner.selectedItem.toString()
            val selectedCityId = getCityIdByName(selectedCityName)

            val selectedCity = City(selectedCityName, selectedCityId)

            val intent = Intent(this, NextMainActivity::class.java)
            intent.putExtra("SELECTED_CITY", selectedCity)
            startActivity(intent)
            Log.d("MainActivity", "Selected City: $selectedCity")
        }
    }

    private fun getCityNames(): List<String> {
        return cityList.map { it.name }
    }
    private fun getCityIdByName(cityName: String): Int {
        val demoCityList = getDemoCityList()
        val matchingCity = demoCityList.find { it.name == cityName }
        return matchingCity?.cityId ?: 0
    }

    private fun getDemoCityList(): List<City> {
        return cityList
    }
}