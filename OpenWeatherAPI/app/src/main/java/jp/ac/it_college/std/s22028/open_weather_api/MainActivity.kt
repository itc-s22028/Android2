package jp.ac.it_college.std.s22028.open_weather_api


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s22028.open_weather_api.databinding.ActivityMainBinding
import org.json.JSONObject

import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    companion object {
        private const val DEBUG_TAG = "OpenWeatherAPI"
        private const val WEATHER_INFO_URL = "https://api.openweathermap.org/data/2.5/forecast"
        private const val APP_ID = BuildConfig.API_KEY
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCityList.apply {
            adapter = CityAdapter {
                receiveWeatherInfo(it.cityId)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }
    private fun receiveWeatherInfo(cityId: Int) {
        val url = "$WEATHER_INFO_URL?id=$cityId&appid=$APP_ID"
        val executorService = Executors.newSingleThreadExecutor()
        val backgroundReceiver = WeatherInfoBackgroundReceiver(url)
        val future = executorService.submit(backgroundReceiver)
        val result = future.get()
        showWeatherInfo(result)
    }

    /*
weather": [ <- これはArray型
    {              <- []の中に入っているので数字で取り出す
      "id": 802,
      "main": "Clouds",
      "description": "雲",
      "icon": "03d"
    }
],
 */
    @SuppressLint("StringFormatMatches", "StringFormatInvalid")
    @UiThread
    private fun showWeatherInfo(result: String) {
        val root = JSONObject(result) //ここで一個一個取り出せるようになる

        val cityObject = root.getJSONObject("city")
        val cityName = cityObject.getString("name")

//        val coord = root.getJSONObject("cord")
        val mainData = root.getJSONObject("main")
//        val latitude = coord.getDouble("lat")
//        val longitude = coord.getDouble("lon")

        val weatherArray = root.getJSONArray("weather")
        val current = weatherArray.getJSONObject(0)


//        val weather = current.getString("description")

        val weatherMain = current.getString("main")
        val weatherTemp = mainData.getString("temp")

        // 以下、表示処理
        binding.tvWeatherTelop.text = getString(R.string.tv_telop, cityName)
//        binding.tvWeatherDesc.text = getString(
//            R.string.tv_desc, weather, latitude, longitude
//        )
        binding.tvWeatherDesc.text = getString(
            R.string.tv_weather_main, weatherMain,
            R.string.tv_temp, weatherTemp
        )
    }

    private class WeatherInfoBackgroundReceiver(val urlString: String) : Callable<String> {
        @WorkerThread
        override fun call(): String {
            val url = URL(urlString)
            val con = url.openConnection() as HttpURLConnection
            con.apply {
                connectTimeout = 1000
                readTimeout = 1000
                requestMethod = "GET"
            }
            return try {
                con.connect()
                val result = con.inputStream.reader().readText()
                /*
                InputStream.reader() で、自動的に UTF-8 であるとして Byte を String へ
                変換して読み取ってくれる、 InputStreamReader を作ってくれる。
                さらに、InputStreamReader.readText() を呼び出せば
                BufferedReader を使って1行づつ読み取って全て1つの文字列に組み立てる
                面倒くさい工程をすべて肩代わりしてくれる。
                */
                con.disconnect()
                return result
            } catch (ex: SocketTimeoutException) {
                Log.w(DEBUG_TAG,"通信タイムアウト", ex)
                ""
            }
        }
    }
}