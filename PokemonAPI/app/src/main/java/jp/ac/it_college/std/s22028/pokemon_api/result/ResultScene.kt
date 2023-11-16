package jp.ac.it_college.std.s22028.pokemon_api.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.it_college.std.s22028.pokemon_api.R
import jp.ac.it_college.std.s22028.pokemon_api.ui.theme.PokemonAPITheme

@Composable
fun ResultScene(
    result: Int,
    modifier: Modifier = Modifier,
) {
    Surface(modifier) {
        Column {
            // 見出し用のラベル
            Text(text = stringResource(id = R.string.score))
            // 実際の点数
            Text(text = stringResource(id = R.string.point, result))
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