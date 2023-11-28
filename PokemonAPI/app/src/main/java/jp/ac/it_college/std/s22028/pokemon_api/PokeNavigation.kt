package jp.ac.it_college.std.s22028.pokemon_api

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import jp.ac.it_college.std.s22028.pokemon_api.database.PokeRoomDatabase
import jp.ac.it_college.std.s22028.pokemon_api.generation.SelectGenerationScene
import jp.ac.it_college.std.s22028.pokemon_api.model.PokeQuiz
import jp.ac.it_college.std.s22028.pokemon_api.quiz.QuizScene
import jp.ac.it_college.std.s22028.pokemon_api.result.ResultScene
import jp.ac.it_college.std.s22028.pokemon_api.title.TitleScene
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pokeAPI")
object Destinations {
    const val TITLE = "title"
    const val GENERATION = "generation_select"
    const val QUIZ = "quiz/{order}"
    const val RESULT = "result"
}

@OptIn(ExperimentalMaterial3Api::class)     // 比較的新しい実験的な機能を使うときに指定するらしい。
@Composable
fun PokeNavigation(
    navController: NavHostController = rememberNavController(),     // ナビゲーションの指示出すやつ
) {
    // AppBar の文言を保持するやつ
    var titleText by remember { mutableStateOf("") }
    // クイズのデータ
    var quizData by remember { mutableStateOf(listOf<PokeQuiz>()) }
    // スコア(正解数)
    var score by remember { mutableIntStateOf(0) }
    // context
    val context = LocalContext.current
    // コルーチンスコープ
    val scope = rememberCoroutineScope()

    // Scaffold を使うと、NavHost 以外の部分の構築を手っ取り早くできる。
    Scaffold(
        // 上部のバー
        topBar = {
            TopAppBar(title = {
                Text(text = titleText)
            })
        }
    ) {
        // メインコンテンツ部分
        // startDestination は初期表示デスティネーション
        // modifier で余白設定をする。Scaffold からもらえる PaddingValues[it] を使う
        NavHost(
            navController = navController,
            startDestination = Destinations.TITLE,
            modifier = Modifier.padding(it)
        ) {
            // composable関数でデスティネーションを登録していく。
            composable(Destinations.TITLE) {
                // タイトル画面
                titleText = ""
                TitleScene(
                    onTitleClick = {
                        isReadyData(context)
                        navController.navigate(Destinations.GENERATION)
                    }
                )
            }

            composable(Destinations.GENERATION) {
                // 世代選択画面
                // AppBar のタイトル設定
                titleText = stringResource(id = R.string.please_select_generation)
                // score のリセット
                score = 0

                SelectGenerationScene(onGenerationSelected = { gen ->
                    scope.launch {
                        quizData = generateQuizData(context, gen)
                        navController.navigate("quiz/0") {
                            popUpTo(Destinations.GENERATION)
                        }
                    }
                })
            }

            composable(
                Destinations.QUIZ,
                // [arguments] パラメータに前のデスティネーションから受け取るデータを定義する
                arguments = listOf(navArgument("order") { type = NavType.IntType })
            ) {
                // クイズ画面
                // AppBar のテキスト設定
                titleText = stringResource(id = R.string.who)

                // composable#arguments の定義に従って取得できるようになる。非タイプセーフ
                val order = it.arguments?.getInt("order") ?: 0
                QuizScene(quizData[order]) {
                    // 正解数のカウント
                    score += if (it) 1 else 0

                    // 次の問題番号
                    val next = order + 1
                    if (quizData.size > next) {
                        // まだ次の問題がある
                        navController.navigate("quiz/$next") {
                            popUpTo(Destinations.GENERATION)
                        }
                    } else {
                        // 次の問題がないので結果画面へ
                        navController.navigate(Destinations.RESULT) {
                            popUpTo(Destinations.GENERATION)
                        }
                    }
                }

            }

            composable(Destinations.RESULT) {
                // 結果画面
                // AppBar のてきすｔ
                titleText = ""
                ResultScene(
                    result = score,
                    onClickGenerationButton = {
                        // 遷移(navigate)ではなくて、履歴を戻る(popBackStack)
                        navController.popBackStack()
                    },
                    onCLickTitleButton = {
                        // 遷移(navigate)ではなくて、履歴をタイトルまで戻る(popBackStack)
                        navController.popBackStack(Destinations.TITLE, false)
                    }
                )
            }
        }
    }
}

fun isReadyData(context: Context) {
    val LAST_UPDATED_AT = intPreferencesKey("last_updated_at")
    val lastUpdatedAtStringFlow: Flow<Any> = context.dataStore.data
        .map {
            it[LAST_UPDATED_AT] ?: LocalDateTime.MIN.toString()
        }
    val lastUpdatedAt = runBlocking { LocalDateTime.parse(lastUpdatedAtStringFlow.first()) }
    return ChronoUnit.MONTHS.between(lastUpdatedAt, LocalDateTime.now()) == 0L
}

suspend fun generateQuizData(context: Context, generation: Int): List<PokeQuiz> =
    withContext(Dispatchers.IO) {
        // PokeDao の取得
        val dao = PokeRoomDatabase.getDatabase(context).pokeDao()

        // [generation] に応じたデータを取ってくる
        val pokeData = dao.findByGeneration(generation)

        // シャッフルして10件取り出す
        val currentList = pokeData.shuffled().subList(0, 10)

        return@withContext currentList.map { target ->
            // 選択肢用のリストを作る(初期値は正解が1つのみ入っている)
            val choices = mutableListOf(target.name)
            // 誤答の選択肢を3つ追加する
            choices.addAll(
                currentList.filter { it.id != target.id }.shuffled().subList(0, 3).map { it.name }
            )
            PokeQuiz(
                target.mainTextureUrl,
                choices.shuffled(),     // ここで選択肢をシャッフルしている
                target.name
            )
        }
    }