package jp.ac.it_college.std.s22028.pokemon_api

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import jp.ac.it_college.std.s22028.pokemon_api.api.GamesGroup
import jp.ac.it_college.std.s22028.pokemon_api.api.PokemonGroup
import jp.ac.it_college.std.s22028.pokemon_api.database.PokeRoomDatabase
import jp.ac.it_college.std.s22028.pokemon_api.database.entity.Poke
import jp.ac.it_college.std.s22028.pokemon_api.ui.theme.PokemonAPITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonAPITheme {
                PokeNavigation()
            }
        }
    }
}