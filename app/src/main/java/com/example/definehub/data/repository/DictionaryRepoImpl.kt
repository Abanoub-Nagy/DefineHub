package com.example.definehub.data.repository

import android.app.Application
import com.example.definehub.R
import com.example.definehub.data.api.DictionaryApi
import com.example.definehub.data.mapper.toWordItem
import com.example.definehub.domain.model.WordItem
import com.example.definehub.domain.repo.DictionaryRepo
import com.example.definehub.utill.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class DictionaryRepoImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val application: Application
) : DictionaryRepo {
    override suspend fun getWordInfo(word: String): Flow<Result<WordItem>> {
        return flow {
            emit(Result.Loading(true))
            val remoteWord = try {
                dictionaryApi.getWordInfo(word)
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
                emit(Result.Loading(false))
                return@flow
            } catch (e: java.io.IOException) {
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
                emit(Result.Loading(false))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
                emit(Result.Loading(false))
                return@flow
            }

            remoteWord?.let { wordResult ->
                wordResult[0].let { wordItem ->
                    emit(Result.Success(wordItem?.toWordItem()))
                    emit(Result.Loading(false))
                    return@flow
                }
            }

            emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
            emit(Result.Loading(false))

        }

    }
}