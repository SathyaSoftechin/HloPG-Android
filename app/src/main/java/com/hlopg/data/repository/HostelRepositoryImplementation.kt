package com.hlopg.data.repository

import com.hlopg.data.api.RetrofitInstance
import com.hlopg.data.model.Hostel
import com.hlopg.domain.mapper.HostelMapper.toDomain
import com.hlopg.domain.repository.HostelRepository
import com.hlopg.domain.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HostelRepositoryImpl @Inject constructor(
    // ideally inject HostelApi instead of using RetrofitInstance directly
    // private val api: HostelApi
) : HostelRepository {

    override suspend fun getPopularHostels(): Resource<List<Hostel>> {
        return withContext(Dispatchers.IO) {
            try {
                // Returns List<HostelDto>
                val dtoList = RetrofitInstance.hostelApi.getPopularHostels()
                // Map to List<Hostel>
                val hostels = dtoList.map { it.toDomain() }
                Resource.Success(hostels)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An error occurred while fetching popular hostels")
            }
        }
    }

    override suspend fun getHyderabadHostels(): Resource<List<Hostel>> {
        return withContext(Dispatchers.IO) {
            try {
                val dtoList = RetrofitInstance.hostelApi.getHyderabadHostels()
                val hostels = dtoList.map { it.toDomain() }
                Resource.Success(hostels)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An error occurred while fetching Hyderabad hostels")
            }
        }
    }

    override suspend fun getChennaiHostels(): Resource<List<Hostel>> {
        return withContext(Dispatchers.IO) {
            try {
                val dtoList = RetrofitInstance.hostelApi.getChennaiHostels()
                val hostels = dtoList.map { it.toDomain() }
                Resource.Success(hostels)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An error occurred while fetching Chennai hostels")
            }
        }
    }

    override suspend fun getBangaloreHostels(): Resource<List<Hostel>> {
        return withContext(Dispatchers.IO) {
            try {
                val dtoList = RetrofitInstance.hostelApi.getBangaloreHostels()
                val hostels = dtoList.map { it.toDomain() }
                Resource.Success(hostels)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An error occurred while fetching Bangalore hostels")
            }
        }
    }

    override suspend fun getHostelById(hostelId: Int): Resource<Hostel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.hostelApi.getHostelById(hostelId)

                if (response.isSuccessful) {
                    val body = response.body()

                    val hostel = body?.data
                    if (body?.success == true && hostel != null) {
                        Resource.Success(hostel)
                    } else {
                        Resource.Error("Failed to fetch hostel details")
                    }
                } else {
                    Resource.Error("Failed to fetch hostel details (code: ${response.code()})")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An error occurred while fetching hostel details")
            }
        }
    }
}
