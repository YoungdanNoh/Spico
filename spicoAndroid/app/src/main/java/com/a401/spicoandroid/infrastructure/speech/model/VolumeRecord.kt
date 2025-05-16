package com.a401.spicoandroid.infrastructure.speech.model

data class VolumeRecord(
    val startTime: String,
    var endTime: String,
    val volumeLevel: VolumeLevel
)