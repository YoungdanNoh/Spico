package com.a401.spicoandroid.presentation.report.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.a401.spicoandroid.domain.report.repository.RamdomReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoReplayViewModel @Inject constructor(
    private val app: Application,
    private val ramdomReportRepository: RamdomReportRepository
) : ViewModel() {

    val player: ExoPlayer = ExoPlayer.Builder(app).build()



    fun setMedia(uri: Uri) {
        viewModelScope.launch {
            val mediaItem = MediaItem.fromUri(uri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
