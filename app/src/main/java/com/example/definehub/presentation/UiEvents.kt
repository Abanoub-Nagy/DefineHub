package com.example.definehub.presentation

sealed class UiEvents {
    data class OnSearchWordChanged(val text: String) : UiEvents()

    object OnSearchClicked : UiEvents()

}