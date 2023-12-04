package jp.ac.it_college.std.s22028.weather_api

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.s22028.weather_api.databinding.ActivityNextMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class NextMainActivity : AppCompatActivity() {
    private var stopLoop = false
    private var A = 0
    private lateinit var binding: ActivityNextMainBinding

    companion object {
        private const val DEBUG_TAG = "WeatherAPI"
        private const val WEATHER_INFO_URL =
            "https://api.openweathermap.org/data/2.5/forecast?lang=ja"
        private const val APP_ID = BuildConfig.API_KEY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedCity: City? = intent.getSerializableExtra("SELECTED_CITY") as? City
        Log.d("NextMainActivity", "Selected City: $selectedCity")

        selectedCity?.let {
            val cityNameTextView = findViewById<TextView>(R.id.tvWeatherTelop)
            cityNameTextView.text = it.name

            val weatherTask = WeatherInfoAsyncTask(this)
            weatherTask.execute(it.cityId.toString())
        }
    }

    @SuppressLint("StringFormatMatches", "StringFormatInvalid")
    private fun showWeatherInfo(result: String) {
        try {
            val root = JSONObject(result)
            val city = root.getJSONObject("city")
            val cityName = city.getString("name")

            val listArray = root.getJSONArray("list")
            if (listArray.length() > 0) {
                val nextButton = findViewById<Button>(R.id.nextButton)
                val loopendbutton = findViewById<Button>(R.id.loop_end)
                val backButton = findViewById<Button>(R.id.backButton)

                nextButton.setOnClickListener {
                    A += 1
                }

                loopendbutton.setOnClickListener {
                    val goBack = Intent(this, MainActivity::class.java)
                    startActivity(goBack)
                }

                backButton.setOnClickListener {
                    A -= 1
                }

                GlobalScope.launch(Dispatchers.Main) {
                    while (A < listArray.length()) {
                        val firstWeatherObject = listArray.getJSONObject(A)
                        val dtObject = firstWeatherObject.getString("dt_txt")

                        // dtObjectと現在時刻を比較
                        val currentDateTime = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                        val formattedDateTime = currentDateTime.format(formatter)
                        val dtObjectDateTime = LocalDateTime.parse(dtObject, formatter)

                        if (dtObjectDateTime.isAfter(currentDateTime)) {
                            updateUI(firstWeatherObject, result)
                            delay(1000)
                        } else {
                            A += 1
                        }
                    }
                }
            }
        } catch (e: JSONException) {
            Log.e(DEBUG_TAG, "天気情報の解析エラー", e)
        }
    }


    @SuppressLint("StringFormatMatches")
    private suspend fun updateUI(firstWeatherObject: JSONObject, result: String) {
        val root = JSONObject(result)
        val city = root.getJSONObject("city")
        val cityName = city.getString("name")
        val pop = firstWeatherObject.getString("pop")

        val mainObject = firstWeatherObject.getJSONObject("main")
        val dtObject = firstWeatherObject.getString("dt_txt")
        val dateOnly = dtObject.split(" ")[0]
        val formattedDate = runCatching {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
            val date = inputFormat.parse(dateOnly)
            outputFormat.format(date!!)
        }.getOrElse { dateOnly }
        val dayOnly = dtObject.split(" ")[1].substring(0, 2).toInt()

        val mainTemp = mainObject.optDouble("temp", Double.NaN)
        val mainDesc = (mainTemp - 273.15).toInt()
        val mainfeelsLike = mainObject.optDouble("feels_like", Double.NaN)
        val mainFeels = (mainfeelsLike - 273.15).toInt()
        val mainhumidity = mainObject.optInt("humidity", Int.MIN_VALUE)
        val maingrndlevel = mainObject.optInt("grnd_level", Int.MIN_VALUE)

        val weatherArray = firstWeatherObject.getJSONArray("weather")
        if (weatherArray.length() > 0) {
            val current = weatherArray.getJSONObject(0)
            val weatherDescription = current.optString("description", "N/A")

            // wind
            val windObject = firstWeatherObject.getJSONObject("wind")
            val windSpeed = windObject.optDouble("speed", Double.NaN)
            val windDeg = windObject.optDouble("deg", Double.NaN)
            val windGust = windObject.optDouble("gust", Double.NaN)

            // 1日目の表示するところ
            binding.tvWeatherTelop.text = "${if (dayOnly != Int.MIN_VALUE) dayOnly else "N/A"}"
            binding.tvTitle2.text = "${if (formattedDate.isNotEmpty()) formattedDate else "N/A"}"
            binding.WeatherTitle.text = getString(R.string.tv_title, dayOnly)
            binding.tvWeatherTelop.text = getString(R.string.tv_telop, cityName)
            binding.weatherMain.text = "天気 : ${if (weatherDescription.isNotEmpty()) weatherDescription else "N/A"}"
            binding.mainTemp.text = "気温 : ${if (mainDesc != Int.MIN_VALUE) mainDesc else "N/A"} ℃ | 体感気温 : ${if (mainFeels != Int.MIN_VALUE) mainFeels else "N/A"} ℃"
            binding.mainHumidity.text = "湿度 : ${if (mainhumidity != Int.MIN_VALUE) mainhumidity else "N/A"} % | 気圧 : ${if (maingrndlevel != Int.MIN_VALUE) maingrndlevel else "N/A"} PA"
            binding.windSpeed.text = "風速 : ${if (!windSpeed.isNaN()) windSpeed else "N/A"} m/s | 瞬間風速 : ${if (!windGust.isNaN()) windGust else "N/A"} m/s"
            binding.windDeg.text = "風向 : ${if (!windDeg.isNaN()) windDeg else "N/A"} kt"
            binding.POP.text = "降水確率 : ${if (mainDesc != Int.MIN_VALUE) pop else "N/A"} %"

            binding.tvD.text = getString(R.string.tv_d, dtObject)
        }
    }

    private class WeatherInfoAsyncTask(activity: NextMainActivity) :
        AsyncTask<String, Void, String>() {
        private val activityReference: WeakReference<NextMainActivity> = WeakReference(activity)

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String): String {
            val cityId = params[0]
            val url = "$WEATHER_INFO_URL&id=$cityId&appid=$APP_ID"

            return try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 30000
                urlConnection.readTimeout = 30000

                val result = urlConnection.inputStream.bufferedReader().use { it.readText() }
                Log.d(DEBUG_TAG, "API Response: $result")  // レスポンスをログに出力

                urlConnection.disconnect()

                result
            } catch (e: Exception) {
                Log.e(DEBUG_TAG, "Error fetching weather information", e)
                ""
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String) {
            val activity = activityReference.get()
            if (activity != null && !activity.isFinishing) {
                activity.showWeatherInfo(result)
            }
        }
    }
}
