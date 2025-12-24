package com.hlopg.domain.repository

import com.hlopg.data.model.Hostel


interface HostelRepository {
    suspend fun getPopularHostels(): Resource<List<Hostel>>
    suspend fun getHyderabadHostels(): Resource<List<Hostel>>
    suspend fun getChennaiHostels(): Resource<List<Hostel>>
    suspend fun getBangaloreHostels(): Resource<List<Hostel>>
    suspend fun getHostelById(hostelId: String): Resource<Hostel>
}