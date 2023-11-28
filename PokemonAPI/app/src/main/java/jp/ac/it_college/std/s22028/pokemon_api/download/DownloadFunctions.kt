package jp.ac.it_college.std.s22028.pokemon_api.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun pokeDataDownload(scope: CoroutineScope) {
    scope.launch {
        withContext(Dispatchers.IO) {
            val data = pokeApi()
        }
    }
}