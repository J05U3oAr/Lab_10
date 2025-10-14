package com.example.lab10.data

//Atributos de los personajes.
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String?
)