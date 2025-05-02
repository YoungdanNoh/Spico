package com.ssafy.spico.domain.practice.exception

enum class SearchError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    EMPTY_KEYWORD("400", "2001", "검색어가 비어 있습니다."),
    NO_RESULT_FOUND("404", "2002", "더 이상 검색되는 결과가 없습니다."),
    ELASTICSEARCH_ERROR("500", "2003", "Elasticsearch 검색 중 오류가 발생했습니다.");
}
