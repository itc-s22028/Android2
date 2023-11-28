package jp.ac.it_college.std.s22028.pokemon_api.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.it_college.std.s22028.pokemon_api.R
import jp.ac.it_college.std.s22028.pokemon_api.ui.theme.PokemonAPITheme


@Composable
fun ResultScene(
    result: Int,
    modifier: Modifier = Modifier,
    onClickGenerationButton: () -> Unit = {},
    onCLickTitleButton: () -> Unit = {},
) {
    Surface(modifier) {
        Column {
            // 見出し用のラベル
            Text(
                text = stringResource(id = R.string.score),
                style = MaterialTheme.typography.headlineLarge
            )
            // 実際の点数
            Text(
                text = stringResource(id = R.string.point, result),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            // ちょっと空間開ける
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            // 世代選択ボタン
            Button(
                onClick = onClickGenerationButton,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = stringResource(id = R.string.to_generation))
            }
            // タイトルへボタン
            Button(
                onClick = onCLickTitleButton,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = stringResource(id = R.string.to_title))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ResultScenePreview() {
    PokemonAPITheme {
        ResultScene(
            result = 0,
            modifier = Modifier.fillMaxSize()
        )
    }
}