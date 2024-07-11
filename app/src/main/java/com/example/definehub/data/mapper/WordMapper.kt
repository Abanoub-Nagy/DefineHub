package com.example.definehub.data.mapper

import com.example.definehub.data.dto.DefinitionDto
import com.example.definehub.data.dto.MeaningDto
import com.example.definehub.data.dto.WordItemDto
import com.example.definehub.domain.model.Definition
import com.example.definehub.domain.model.Meaning
import com.example.definehub.domain.model.WordItem


fun WordItemDto.toWordItem(): WordItem {
    return WordItem(
        word = word ?: "",
        phonetic = phonetic ?: "",
        meanings = meanings?.map { it.toMeaning() } ?: emptyList()
    )
}

fun MeaningDto.toMeaning(): Meaning {
    return Meaning(
        partOfSpeech = partOfSpeech ?: "",
        definition = definitionDtoToDefinition(definitions?.get(0))
    )
}


fun definitionDtoToDefinition(
    definitionDto: DefinitionDto?
): Definition {
    return Definition(
        definition = definitionDto?.definition ?: "",
        example = definitionDto?.example ?: ""
    )
}