package com.a401.spicoandroid.presentation.project.viewmodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    val title: String,
    val date: String
) : Parcelable