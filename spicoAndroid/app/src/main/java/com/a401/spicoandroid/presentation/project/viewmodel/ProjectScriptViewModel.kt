package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ProjectScriptViewModel @Inject constructor() : ViewModel() {

    private val _scriptState = MutableStateFlow(ProjectScriptState())
    val scriptState: StateFlow<ProjectScriptState> = _scriptState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _scriptState.value = ProjectScriptState(
            scriptId = 1L,
            title = "자기소개 예시 대본",
            paragraphs = listOf(
                ParagraphItem(text = "안녕하세요. 저는 소프트웨어 개발자를 꿈꾸는 김민규입니다."),
                ParagraphItem(text = "항상 사용자 경험을 먼저 생각하며, 문제 해결에 집중하는 개발자가 되기 위해 노력하고 있습니다."),
                ParagraphItem(text = "감사합니다.")
            )
        )
    }

    fun loadScript(id: Long, title: String, body: String) {
        _scriptState.value = ProjectScriptState(
            scriptId = id,
            title = title,
            paragraphs = body.split("\n\n").map { ParagraphItem(text = it) }
        )
    }

    fun updateText(id: Long, newText: String) {
        _scriptState.update {
            it.copy(paragraphs = it.paragraphs.map { p ->
                if (p.id == id) p.copy(text = newText) else p
            })
        }
    }

    fun addParagraph() {
        _scriptState.update {
            it.copy(paragraphs = it.paragraphs + ParagraphItem(text = ""))
        }
    }

    fun deleteParagraph(id: Long) {
        _scriptState.update {
            it.copy(paragraphs = it.paragraphs.filterNot { it.id == id })
        }
    }

    fun moveParagraph(from: Int, to: Int) {
        _scriptState.update {
            val list = it.paragraphs.toMutableList()
            val item = list.removeAt(from)
            list.add(to, item)
            it.copy(paragraphs = list)
        }
    }

    fun setEditing(editing: Boolean) {
        _scriptState.update { it.copy(isEditing = editing) }
    }

    fun getMergedScript(): String {
        return _scriptState.value.paragraphs.joinToString("\n\n") { it.text.trim() }
    }
}