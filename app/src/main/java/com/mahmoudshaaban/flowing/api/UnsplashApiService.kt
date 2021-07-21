package com.mahmoudshaaban.flowing.api

import com.mahmoudshaaban.flowing.model.PhotoModel
import com.mahmoudshaaban.flowing.model.responses.SearchPhotosResponse
import com.mahmoudshaaban.flowing.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.nio.ByteOrder

interface UnsplashApiService {



    companion object {
        const val BASE_API_URL = "https://api.unsplash.com/"
        const val API_KEY = "wxl8gDYQvKHvTcfTzfFJ2Fy0GoSuKoJovMopdieYBvk"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $API_KEY")
    @GET("photos")
    suspend fun loadPhotos(
        @Query("page") page: Int,
        @Query("per_page") numOfPhotos: Int ,
        @Query("order_by") order: String = "popular"
    ): List<PhotoModel>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") numOfPhotos: Int = 10,
    ): SearchPhotosResponse


}