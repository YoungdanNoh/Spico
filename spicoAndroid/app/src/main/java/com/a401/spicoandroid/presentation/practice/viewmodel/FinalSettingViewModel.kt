package com.a401.spicoandroid.presentation.practice.viewmodel
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//// UI 상태 데이터 클래스
//data class FinalSettingState(
//    val hasAudience: Boolean = true,
//    val hasQnA: Boolean = true,
//    val questionCount: Int = 1,
//    val answerTimeLimitSec: Int = 90 // 기본 1분 30초
//)
//
//// API 요청 DTO (서버에 보낼 형태)
//data class FinalSettingRequest(
//    val hasAudience: Boolean,
//    val hasQnA: Boolean,
//    val questionCount: Int,
//    val answerTimeLimit: Int // 초 단위
//)
//
//@HiltViewModel
//class FinalSettingViewModel @Inject constructor() : ViewModel() {
//
//    var uiState by mutableStateOf(FinalSettingState())
//        private set
//
//    // 청중 여부 설정
//    fun setHasAudience(value: Boolean) {
//        uiState = uiState.copy(hasAudience = value)
//    }
//
//    // 질의응답 여부 설정
//    fun setHasQnA(value: Boolean) {
//        uiState = uiState.copy(hasQnA = value)
//    }
//
//    // 질문 개수 (1~3 제한)
//    fun setQuestionCount(count: Int) {
//        uiState = uiState.copy(questionCount = count.coerceIn(1, 3))
//    }
//
//    // 답변 시간 (30~180초 제한)
//    fun setAnswerTimeSeconds(seconds: Int) {
//        uiState = uiState.copy(answerTimeLimitSec = seconds.coerceIn(30, 180))
//    }
//
//    // API 요청을 위한 DTO 변환
//    fun toRequestBody(): FinalSettingRequest {
//        return FinalSettingRequest(
//            hasAudience = uiState.hasAudience,
//            hasQnA = uiState.hasQnA,
//            questionCount = uiState.questionCount,
//            answerTimeLimit = uiState.answerTimeLimitSec
//        )
//    }
//}