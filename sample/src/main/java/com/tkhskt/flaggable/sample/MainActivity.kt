package com.tkhskt.flaggable.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tkhskt.flaggable.Flaggable
import com.tkhskt.flaggable.sample.ui.theme.FlaggableTheme

private const val FLAG_KEY_RELEASE = "release"
private const val FLAG_KEY_DEBUG = "debug"
private const val COMPOSABLE_NAME_TEXT = "FlagText"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlaggableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SampleScreen()
                }
            }
        }
    }
}

@Composable
fun SampleScreen() {
    Column {
        FlagText(key = FLAG_KEY_RELEASE, text = "release")
        FlagText(key = FLAG_KEY_DEBUG, text = "debug")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlaggableTheme {
        SampleScreen()
    }
}

@Flaggable(FLAG_KEY_RELEASE, COMPOSABLE_NAME_TEXT)
@Composable
fun ReleaseText(text: String) {
    Text(
        modifier = Modifier.background(Color.Red),
        text = text
    )
}

@Flaggable(FLAG_KEY_DEBUG, COMPOSABLE_NAME_TEXT)
@Composable
fun DebugText(text: String) {
    Text(
        modifier = Modifier.background(Color.Green),
        text = text
    )
}
