package com.example.charactervault.network

import com.example.charactervault.model.Character

data class CharacterResponse(
    val results: List<Character>
)
