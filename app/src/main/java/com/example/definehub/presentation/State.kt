package com.example.definehub.presentation

import com.example.definehub.domain.model.WordItem

data class State(
    val isLoading: Boolean = false,
    val word: WordItem? = null,
    val error: String? = null,
    val searchText: String = ""
)
