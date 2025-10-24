package com.example.lab10.repo

import com.example.lab10.data.Location
import com.example.lab10.entity.LocationDao
import com.example.lab10.entity.toDomain
import com.example.lab10.network.RickAndMortyApi
import com.example.lab10.entity.toEntity

interface LocationRepository {
    suspend fun getLocations(): List<Location>
    suspend fun getLocationById(id: Int): Location
}

class LocationRepositoryRoom(
    private val dao: LocationDao,
    private val api: RickAndMortyApi
) : LocationRepository {

    override suspend fun getLocations(): List<Location> {
        // Offline First: Primero intentamos obtener de Room
        val localData = dao.getAll()

        return if (localData.isEmpty()) {
            // Si no hay data local, llamamos al API
            try {
                val response = api.getLocations()
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

    override suspend fun getLocationById(id: Int): Location =
        requireNotNull(dao.getById(id)).toDomain()
}