package com.example.charactervault.model

data class CharactersResponse(
    val results: List<Character>
)

data class Character(
    val id: Int,
    val name: String,
    val image: Image,
    val deck: String?
)

data class Image(
    val icon_url: String,
    val medium_url: String
)

data class CharacterDetailResponse(
    val results: CharacterDetail
)

data class CharacterDetail(
    val name: String,
    val real_name: String?,
    val aliases: String?,
    val gender: String?,
    val birthday: String?,
    val deck: String?,
    val description: String?,
    val first_appeared_in_game: Game?,
    val site_detail_url: String?,
    val image: Image?
)

data class Game(
    val name: String
)
