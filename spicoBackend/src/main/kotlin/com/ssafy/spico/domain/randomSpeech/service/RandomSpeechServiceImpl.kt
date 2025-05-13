package com.ssafy.spico.domain.randomSpeech.service

import com.ssafy.spico.domain.randomSpeech.model.Content
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech
import com.ssafy.spico.domain.randomSpeech.model.toModel
import com.ssafy.spico.domain.randomSpeech.repository.RandomSpeechRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RandomSpeechServiceImpl(
    private val randomSpeechRepository: RandomSpeechRepository
): RandomSpeechService {

    @Transactional
    override fun startRandomSpeech(randomSpeech: RandomSpeech): Content {
        // 1. 일단 randomSpeech 엔티티 저장
        // 2. 뉴스 가져와서 content 객체에 저장, 엔티티에 업데이트
        // 3. gpt 갔다와서 content 객체에 질문 저장, 엔티티에 업데이트
        // 4. 내려주기
        val content = Content(
            id = 1,
            newsTitle = "",
            newsUrl = "",
            newsSummary = "",
            question = ""
        )
        return content
    }

    override fun endRandomSpeech() {
        TODO("Not yet implemented")
    }

    override fun getRandomSpeechList(userId: Int): List<RandomSpeech> {
        val entities = randomSpeechRepository.findRandomSpeechesByUserId(userId)
        return entities.map { it.toModel() }
    }

    override fun getRandomSpeechDetail() {
        TODO("Not yet implemented")
    }

    override fun deleteRandomSpeech() {
        TODO("Not yet implemented")
    }
}