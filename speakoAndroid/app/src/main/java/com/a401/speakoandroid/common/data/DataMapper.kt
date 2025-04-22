package com.a401.speakoandroid.common.data

internal interface DataMapper<DomainModel> {
    fun toDomain(): DomainModel
}

internal fun <DataModel, DomainModel> DataModel.toDomain(): DomainModel {
    @Suppress("UNCHECKED_CAST")
    return when (this) {
        is DataMapper<*> -> toDomain()

        is List<*> -> map {
            val domainModel: DomainModel = it.toDomain()
            domainModel
        }

        is Unit, is Boolean, is Int, is String, is Long, is Char -> this

        else -> throw IllegalArgumentException("DataModel Type Error: ${this!!::class.simpleName}")
    } as DomainModel
}

internal fun <DataModel : DataMapper<DomainModel>, DomainModel> List<DataModel>.toDomain(): List<DomainModel> {
    return map(DataMapper<DomainModel>::toDomain)
}