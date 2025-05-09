package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class ProjectScriptViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectScriptState())
    val uiState: StateFlow<ProjectScriptState> = _uiState.asStateFlow()

    fun loadScript(id: Long, title: String, body: String) {
        _uiState.value = ProjectScriptState(
            scriptId = id,
            title = title,
            paragraphs = body.split("\n\n").map { ParagraphItem(text = it) }
        )
    }

    fun updateText(id: Long, newText: String) {
        _uiState.update {
            it.copy(paragraphs = it.paragraphs.map { p ->
                if (p.id == id) p.copy(text = newText) else p
            })
        }
    }

    fun addParagraph() {
        _uiState.update {
            it.copy(paragraphs = it.paragraphs + ParagraphItem(text = ""))
        }
    }

    fun deleteParagraph(id: Long) {
        _uiState.update {
            it.copy(paragraphs = it.paragraphs.filterNot { it.id == id })
        }
    }

    fun moveParagraph(from: Int, to: Int) {
        _uiState.update {
            val list = it.paragraphs.toMutableList()
            val item = list.removeAt(from)
            list.add(to, item)
            it.copy(paragraphs = list)
        }
    }

    fun setEditing(editing: Boolean) {
        _uiState.update { it.copy(isEditing = editing) }
    }

    fun getMergedScript(): String {
        return _uiState.value.paragraphs.joinToString("\n\n") { it.text.trim() }
    }
}