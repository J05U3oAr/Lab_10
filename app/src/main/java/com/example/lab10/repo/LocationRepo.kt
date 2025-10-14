package com.example.lab10.repo

import com.example.lab10.data.Location
import com.example.lab10.entity.LocationDao
import com.example.lab10.entity.toDomain

interface LocationRepository {
    suspend fun getLocations(): List<Location>
    suspend fun getLocationById(id: Int): Location
}

class LocationRepositoryRoom(
    private val dao: LocationDao
) : LocationRepository {
    override suspend fun getLocations(): List<Location> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getLocationById(id: Int): Location =
        requireNotNull(dao.getById(id)).toDomain()
}
