package com.mahmoudshaaban.flowing.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mahmoudshaaban.flowing.api.UnsplashApiService
import com.mahmoudshaaban.flowing.model.PhotoModel
import com.mahmoudshaaban.flowing.util.Constants
import com.mahmoudshaaban.flowing.util.Constants.Companion.UNSPLASH_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UnsplashSearchDataSource
@Inject
constructor(val service: UnsplashApiService, val query: String) : PagingSource<Int, PhotoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val apiQuery = query
        return try {
            val response = service.searchPhotos(apiQuery , position , params.loadSize)
            val photos = response.photosList
            val nextKey = if (photos.isEmpty()) {
                null
            } else {
                val NETWORK_PAGE_SIZE = 10
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}