package com.example.definehub.domain.repo

import com.example.definehub.domain.model.WordItem
import com.example.definehub.data.dto.WordItemDto
import com.example.definehub.utill.Result
import kotlinx.coroutines.flow.Flow

interface DictionaryRepo {
    suspend fun getWordInfo(word: String): Flow<Result<WordItem>>
}