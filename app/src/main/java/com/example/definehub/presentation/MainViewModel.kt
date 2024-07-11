package com.example.definehub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.definehub.domain.repo.DictionaryRepo
import com.example.definehub.utill.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepo
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        _state.update {
            it.copy(searchText = "Word")
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            loadWord()
        }
    }

    init {
        _state.update {
            it.copy(searchText = "Word")
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            loadWord()
        }
    }

    fun onEvent(event: UiEvents) {
        when (event) {
            is UiEvents.OnSearchWordChanged -> {
                _state.update { it.copy(searchText = event.text.lowercase()) }

            }

            is UiEvents.OnSearchClicked -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    loadWord()
                }
            }
        }
    }

    private fun loadWord() {
        viewModelScope.launch {
            dictionaryRepo.getWordInfo(_state.value.searchText).collect { result ->
                when (result) {
                    is Result.Empty -> TODO()
                    is Result.Error -> Unit
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = result.isLoading) }
                    }

                    is Result.Success -> {
                        result.data?.let { wordItem ->
                            _state.update { it.copy(word = wordItem) }
                        }

                    }
                }
            }
        }
    }

}