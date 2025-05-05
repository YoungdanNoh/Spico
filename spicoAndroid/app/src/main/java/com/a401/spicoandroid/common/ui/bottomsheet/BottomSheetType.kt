package com.a401.spicoandroid.common.ui.bottomsheet

sealed class BottomSheetType {
    // 프로젝트 생성 / 연습하기 바텀시트
    object ProjectAction : BottomSheetType()
    // 리포트 삭제 바텀시트
    object Delete : BottomSheetType()
    // 대본 보기 및 수정 바텀시트
    data class Script(val content: List<String>) : BottomSheetType()
}
