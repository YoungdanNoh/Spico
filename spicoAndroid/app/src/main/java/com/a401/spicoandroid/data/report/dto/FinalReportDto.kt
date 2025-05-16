package com.a401.spicoandroid.data.report.dto

import com.a401.spicoandroid.domain.report.model.FinalReport
import com.a401.spicoandroid.domain.report.model.PauseRecord
import com.a401.spicoandroid.domain.report.model.QaRecord
import com.a401.spicoandroid.domain.report.model.SpeedRecord
import com.a401.spicoandroid.domain.report.model.VolumeRecord

data class FinalReportDto(
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
    val volumeRecords: List<VolumeRecordDto>,
    val speedRecords: List<SpeedRecordDto>,
    val pauseRecords: List<PauseRecordDto>,
    val qaRecord: List<QaRecordDto>
)

data class VolumeRecordDto(val startTime: String, val endTime: String, val volumeLevel: String)
data class SpeedRecordDto(val startTime: String, val endTime: String, val speedLevel: String)
data class PauseRecordDto(val startTime: String, val endTime: String)
data class QaRecordDto(val question: String, val answer: String)

fun FinalReportDto.toDomain(): FinalReport = FinalReport(
    projectName = projectName,
    practiceName = practiceName,
    date = date,
    videoUrl = videoUrl,
    totalScore = totalScore,
    volumeScore = volumeScore,
    speedScore = speedScore,
    pauseScore = pauseScore,
    pronunciationScore = pronunciationScore,
    scriptMatchRate = scriptMatchRate,
    volumeStatus = volumeStatus,
    speedStatus = speedStatus,
    pauseCount = pauseCount,
    volumeRecords = volumeRecords.map { VolumeRecord(it.startTime, it.endTime, it.volumeLevel) },
    speedRecords = speedRecords.map { SpeedRecord(it.startTime, it.endTime, it.speedLevel) },
    pauseRecords = pauseRecords.map { PauseRecord(it.startTime, it.endTime) },
    qaRecord = qaRecord.map { QaRecord(it.question, it.answer) }
)
