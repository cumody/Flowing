package com.mahmoudshaaban.flowing.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mahmoudshaaban.flowing.api.UnsplashApiService
import com.mahmoudshaaban.flowing.model.PhotoModel
import com.mahmoudshaaban.flowing.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnsplashRepository
@Inject
constructor(private val service: UnsplashApiService) {

        fun loadPhotos(): Flow<PagingData<PhotoModel>> {
            return Pager(
                config = PagingConfig(
                    pageSize = Constants.NETWORK_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { UnsplashDataSource(service) }
            ).flow
        }

    fun getSearchResultStream(query: String): Flow<PagingData<PhotoModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashSearchDataSource(service, query) }
        ).flow
    }


}