package com.example.lab10.repo

import com.example.lab10.data.Character
import com.example.lab10.entity.CharacterDao
import com.example.lab10.entity.toDomain
import com.example.lab10.network.RickAndMortyApi
import com.example.lab10.entity.toEntity

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>
    suspend fun getCharacterById(id: Int): Character
}

class CharacterRepositoryRoom(
    private val dao: CharacterDao,
    private val api: RickAndMortyApi
) : CharacterRepository {

    override suspend fun getCharacters(): List<Character> {
        // Offline First: Primero intentamos obtener de Room
        val localData = dao.getAll()

        return if (localData.isEmpty()) {
            // Si no hay data local, llamamos al API
            try {
                val response = api.getCharacters()
                val entities = response.results.map { it.toEntity() }
                // Guardamos en Room
                dao.insertAll(entities)
                // Retornamos como domain models
                entities.map { it.toDomain() }
            } catch (e: Exception) {
                // Si falla el API, retornamos lista vacía
                emptyList()
            }
        } else {
            // Si hay data local, la retornamos
            localData.map { it.toDomain() }
        }
    }

    override suspend fun getCharacterById(id: Int): Character =
        requireNotNull(dao.getById(id)).toDomain()
}