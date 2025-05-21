package com.a401.spicoandroid.presentation.project.viewmodel

data class ProjectScriptState(
    val scriptId: Long = 0L,
    val title: String = "",
    val paragraphs: List<ParagraphItem> = emptyList(),
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ParagraphItem(
    val id: Long = System.nanoTime(),
    val text: String
)