package com.a401.spicoandroid.data.main.repository

import com.a401.spicoandroid.data.main.api.MainApi
import com.a401.spicoandroid.domain.main.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi
): MainRepository {

}