package com.a401.spicoandroid.domain.report.model

data class FinalReport(
    val projectName: String,
    val practiceName: String,
    val date: String,
    val videoUrl: String,
    val totalScore: Int,
    val volumeScore: Int,
    val speedScore: Int,
    val pauseScore: Int,
    val pronunciationScore: Int,
    val scriptMatchRate: Int,
    val volumeStatus: String,
    val speedStatus: String,
    val pauseCount: Int,
    val volumeRecords: List<VolumeRecord>,
    val speedRecords: List<SpeedRecord>,
    val pauseRecords: List<PauseRecord>,
    val qaRecord: List<QaRecord>,
    val voiceScript: String
)

data class VolumeRecord(val startTime: String, val endTime: String, val volumeLevel: String)
data class SpeedRecord(val startTime: String, val endTime: String, val speedLevel: String)
data class PauseRecord(val startTime: String, val endTime: String)
data class QaRecord(val question: String, val answer: String)
