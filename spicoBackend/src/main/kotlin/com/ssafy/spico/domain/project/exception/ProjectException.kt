package com.ssafy.spico.domain.project.exception

class ProjectException(val error: ProjectError) : RuntimeException(error.errorMsg)