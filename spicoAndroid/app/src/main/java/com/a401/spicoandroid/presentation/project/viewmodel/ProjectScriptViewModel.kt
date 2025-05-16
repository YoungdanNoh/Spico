package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
open class ProjectScriptViewModel @Inject constructor() : ViewModel() {

    private val _scriptState = MutableStateFlow(ProjectScriptState())
    val scriptState: StateFlow<ProjectScriptState> = _scriptState.asStateFlow()

    fun initializeScript(projectId: Long, title: String, rawScript: String) {
        val current = _scriptState.value
        val incoming = rawScript.split("\n\n")

        val isSame = current.title == title && current.paragraphs.map { it.text } == incoming

        if (isSame) return

        _scriptState.value = ProjectScriptState(
            scriptId = projectId,
            title = title,
            paragraphs = incoming.map { ParagraphItem(text = it) }
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