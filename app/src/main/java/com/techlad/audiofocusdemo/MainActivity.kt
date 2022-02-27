package com.techlad.audiofocusdemo

import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ui.PlayerView
import com.techlad.audiofocusdemo.focus_request.PlaybackFocusManager
import com.techlad.audiofocusdemo.ui.theme.AudioFocusDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PlaybackFocusManager(applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager).requestAudioFocus {
            log(if (it) "Audio is gained" else "Audio focus is lost")
        }

        setContent {
            AudioFocusDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ExoPlayerComposeView()
                }
            }
        }
    }
}

@Composable
private fun ExoPlayerComposeView() {
    val context = LocalContext.current

    // create our player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(provideMediaItem())
            prepare()
            playWhenReady = true
        }
    }

    // player view
    DisposableEffect(
        AndroidView(
            factory = {
                // exo player view for our video player
                PlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    ) {
        onDispose {
            // release player when no longer needed
            exoPlayer.release()
        }
    }
}

fun provideMediaItem() = MediaItem.Builder()
    .setUri("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4")
    .setMediaId("1")
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setDisplayTitle("Sample Video")
            .build()
    )
    .build()

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AudioFocusDemoTheme {
        ExoPlayerComposeView()
    }
}


fun MainActivity.log(text: String) {
    Log.d("focus_update", text)
}