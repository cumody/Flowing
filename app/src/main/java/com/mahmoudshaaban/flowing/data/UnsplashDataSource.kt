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

class UnsplashDataSource
@Inject
constructor(val service : UnsplashApiService) : PagingSource<Int , PhotoModel>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = service.loadPhotos(position,params.loadSize)
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                val NETWORK_PAGE_SIZE = 10
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = response,
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
        TODO("Not yet implemented")
    }
}
