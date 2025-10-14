package com.example.lab10.entity

import com.example.lab10.data.Character
import com.example.lab10.data.Location

fun CharacterEntity.toDomain() = Character(id, name, species, status, gender, image)
fun Character.toEntity() = CharacterEntity(id, name, species, status, gender, image)

fun LocationEntity.toDomain() = Location(id, name, type, dimension)
fun Location.toEntity() = LocationEntity(id, name, type, dimension)
