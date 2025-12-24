package com.hlopg.data.repository

import com.hlopg.data.api.HostelApi
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.Hostel
import com.hlopg.domain.mapper.HostelMapper.toDomain
import com.hlopg.domain.repository.HostelRepository
import com.hlopg.domain.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
class HostelRepositoryImpl @Inject constructor(
    private val api: HostelApi
) : HostelRepository {

    override suspend fun getPopularHostels(): Resource<List<Hostel>> =
        withContext(Dispatchers.IO) {
            try {
                val dtoList = api.getPopularHostels() // List<HostelDto>
                Resource.Success(dtoList.map { it.toDomain() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to fetch popular hostels")
            }
        }

    override suspend fun getHyderabadHostels(): Resource<List<Hostel>> =
        withContext(Dispatchers.IO) {
            try {
                val dtoList = api.getHyderabadHostels()
                Resource.Success(dtoList.map { it.toDomain() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to fetch Hyderabad hostels")
            }
        }

    override suspend fun getChennaiHostels(): Resource<List<Hostel>> =
        withContext(Dispatchers.IO) {
            try {
                val dtoList = api.getChennaiHostels()
                Resource.Success(dtoList.map { it.toDomain() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to fetch Chennai hostels")
            }
        }

    override suspend fun getBangaloreHostels(): Resource<List<Hostel>> =
        withContext(Dispatchers.IO) {
            try {
                val dtoList = api.getBangaloreHostels()
                Resource.Success(dtoList.map { it.toDomain() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to fetch Bangalore hostels")
            }
        }


    override suspend fun getHostelById(hostelId: String): Resource<Hostel> =
        safeApiCall(
            apiCall = { api.getHostelById(hostelId) },
            mapper = { it.toDomain() }
        )

    private suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>,
        mapper: (T) -> R
    ): Resource<R> = withContext(Dispatchers.IO) {
        try {
            handleApiResponse(apiCall(), mapper)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    private fun <T, R> handleApiResponse(
        response: Response<ApiResponse<T>>,
        mapper: (T) -> R
    ): Resource<R> {

        if (!response.isSuccessful) {
            return Resource.Error("Error ${response.code()}")
        }

        val body = response.body()
        val payload = body?.data ?: body?.user

        return if (payload != null) {
            Resource.Success(mapper(payload))
        } else {
            Resource.Error(body?.message ?: "Unknown error")
        }
    }
}
