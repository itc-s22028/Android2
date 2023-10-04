package jp.ac.it_college.std.s22028.async_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s22028.async_sample.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    companion object {
        private const val DEBUG_TAG = "AsyncSample"
        private const val WEATHER_INFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID = BuildConfig.APP_ID
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCityList.apply {
            adapter = CityAdapter {
                receiveWeatherInfo(it.q)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }
    private fun receiveWeatherInfo(q: String) {
        val url = "$WEATHER_INFO_URL&q=$q&appid=$APP_ID"
        val executorService = Executors.newSingleThreadExecutor()
        val backgroundReceiver = WeatherInfoBackgroundReceiver(url)
        val future = executorService.submit(backgroundReceiver)
        val result = future.get()
        binding.tvWeatherDesc.text = result
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

