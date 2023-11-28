package jp.ac.it_college.std.s22028.pokemon_api.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.ac.it_college.std.s22028.pokemon_api.R
import jp.ac.it_college.std.s22028.pokemon_api.model.PokeQuiz
import jp.ac.it_college.std.s22028.pokemon_api.ui.theme.PokemonAPITheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * クイズのメインとなるやつ
 */
@Composable
fun QuizScene(
    quiz: PokeQuiz,
    modifier: Modifier = Modifier,
    onFinished: (Boolean) -> Unit = {},
) {
    var state by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Surface(modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            PokeImage(quiz.imageUrl, state)
            PokeNameList(quiz.choices, state == 0) {
                // 正誤チェック
                state = if (it == quiz.correct) 1 else -1
                // 待機と終了
                scope.launch {
                    delay(3000)
                    onFinished(state > 0)
                }
            }
        }
    }
}

/**
 * [imageUrl] にポケモンの画像がある URL を指定
 * [isSilhouette] が ture だとシルエット表示
 */
@Composable
fun PokeImage(imageUrl: String, state: Int = 0) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(vertical = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(20))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "PokeImage",
                // カラーフィルターでシルエット表示みたいなことができる。
                // 画像加工の詳細は各自で勉強してください
                colorFilter = if (state == 0) ColorFilter.tint(
                    Color.Black,
                    BlendMode.SrcIn
                ) else null,
                modifier = Modifier.fillMaxSize()
            )
        }

        // stateがプラスならマルを出す
        if (state > 0) {
            Image(
                painter = painterResource(id = R.drawable.maru),
                contentDescription = "",
                alpha = 0.25f,
                modifier = Modifier.fillMaxSize()
            )
        }
        // stateがマイナスならバツを出す
        if (state < 0) {
            Image(
                painter = painterResource(id = R.drawable.batu),
                contentDescription = "",
                alpha = 0.25f,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun PokeName(name: String, enabled: Boolean, onClick: (String) -> Unit = {}) {
    // 背景色・文字色を全体的に設定するために使ってる
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            enabled = enabled,
            onClick = { onClick(name) },
            modifier = Modifier
                .padding(8.dp)

        ) {
            // なまえ
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun PokeNameList(
    items: List<String>,
    enabled: Boolean = true,
    onSelected: (String) -> Unit = {},
) {
    LazyColumn() {
        items(items) {
            PokeName(
                name = it,
                enabled = enabled,
                onClick = onSelected
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun QuizScenePreview() {
    PokemonAPITheme {
        QuizScene(
            PokeQuiz(
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/906.png;",
                choices = listOf("ニャオハ", "ホゲータ", "クワッス", "グルトン"),
                correct = "ニャオハ"
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}