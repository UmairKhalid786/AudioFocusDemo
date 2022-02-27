package com.techlad.audiofocusdemo.focus_request


import android.media.AudioManager
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat

/**
 * Created by umair.khalid on 27,February,2022
 **/

class PlaybackFocusManager(private val audioManager: AudioManager) {

    fun requestAudioFocus(onFocusChange: ((gainedFocus: Boolean) -> Unit)?) {
        AudioManagerCompat.requestAudioFocus(audioManager,
            createFocusRequest(buildAttributes(), onFocusChange))
    }

    private fun createFocusRequest(
        attributes: AudioAttributesCompat,
        onFocusChange: ((gainedFocus: Boolean) -> Unit)?
    ): AudioFocusRequestCompat {
        return AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN_TRANSIENT)
            .setAudioAttributes(attributes)
            .setOnAudioFocusChangeListener {
                onFocusChange?.invoke(it == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            }
            .build()
    }

    private fun buildAttributes(): AudioAttributesCompat {
        return AudioAttributesCompat.Builder()
            .setContentType(AudioAttributesCompat.CONTENT_TYPE_MOVIE)
            .setUsage(AudioAttributesCompat.USAGE_MEDIA)
            .build()
    }
}