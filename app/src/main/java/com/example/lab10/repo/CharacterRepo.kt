package com.example.lab10.repo

import com.example.lab10.data.Character
import com.example.lab10.entity.CharacterDao
import com.example.lab10.entity.toDomain

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>
    suspend fun getCharacterById(id: Int): Character
}

class CharacterRepositoryRoom(
    private val dao: CharacterDao
) : CharacterRepository {
    override suspend fun getCharacters(): List<Character> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getCharacterById(id: Int): Character =
        requireNotNull(dao.getById(id)).toDomain()
}
