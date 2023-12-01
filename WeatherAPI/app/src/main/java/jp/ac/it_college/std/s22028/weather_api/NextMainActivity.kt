package jp.ac.it_college.std.s22028.weather_api

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.s22028.weather_api.databinding.ActivityNextMainBinding
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class NextMainActivity : AppCompatActivity() {
    companion object {
        private const val DEBUG_TAG = "AsyncSample"
        private const val WEATHER_INFO_URL =
            "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID = BuildConfig.API_KEY
    }

    private lateinit var binding: ActivityNextMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intentから都市データを受け取る
        val selectedCity: City? = intent.getSerializableExtra("SELECTED_CITY") as? City
        Log.d("NextMainActivity", "Selected City: $selectedCity")

        // ここでselectedCityを使用して表示や処理を行う
        selectedCity?.let {
            // 例: TextViewに都市名とIDを表示する
            val cityNameTextView = findViewById<TextView>(R.id.tvWeatherTelop)

            cityNameTextView.text = it.name

            // 以下、APIから天気情報を取得する処理を追加
            val weatherTask = WeatherInfoAsyncTask(this)
            weatherTask.execute(it.cityId.toString())
        }
    }

    private fun showWeatherInfo(result: String) {
        try {
            val root = JSONObject(result)

            val weatherArray = root.getJSONArray("weather")
            val current = weatherArray.getJSONObject(0)
            val weatherDescription = current.getString("description")

            val mainObject = root.getJSONObject("main")
            val temperature = mainObject.getDouble("temp")
            val feelslike = mainObject.getDouble("feels_like")
            val weatherTemp = (temperature - 273.15).toInt()
            val weatherFeelsLike = (feelslike - 273.15).toInt()

            val windObject = root.getJSONObject("wind")
            val windSpeed = windObject.getDouble("speed")


            val cityName = root.getString("name")

            binding.tvWeatherTelop.text = "都市名 : $cityName"
            binding.weatherMain.text = "天気 : $weatherDescription"
            binding.mainTemp.text = "気温 : $weatherTemp ℃ | 体感温度 : $weatherFeelsLike ℃"
            binding.windSpeed.text = "風速 : $windSpeed hpa"


//            val listArray = root.getJSONArray("list")
//            val current2 = listArray.getJSONObject(1)
//            val listDescription = current2.getString("description")






        } catch (e: Exception) {
            Log.e(DEBUG_TAG, "Error parsing weather information", e)
        }
    }

    private class WeatherInfoAsyncTask(activity: NextMainActivity) :
        AsyncTask<String, Void, String>() {
        private val activityReference: WeakReference<NextMainActivity> = WeakReference(activity)

        override fun doInBackground(vararg params: String): String {
            val cityId = params[0]
            val url = "$WEATHER_INFO_URL&id=$cityId&appid=$APP_ID"

            return try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 30000
                urlConnection.readTimeout = 30000

                val result = urlConnection.inputStream.bufferedReader().use { it.readText() }
                urlConnection.disconnect()

                result
            } catch (e: Exception) {
                Log.e(DEBUG_TAG, "Error fetching weather information", e)
                ""
            }
        }

        override fun onPostExecute(result: String) {
            val activity = activityReference.get()
            if (activity != null && !activity.isFinishing) {
                activity.showWeatherInfo(result)
            }
        }
    }
}