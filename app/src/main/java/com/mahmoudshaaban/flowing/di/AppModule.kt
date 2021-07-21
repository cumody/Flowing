package com.mahmoudshaaban.flowing.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mahmoudshaaban.flowing.api.UnsplashApiService
import com.mahmoudshaaban.flowing.util.Constants
import com.mahmoudshaaban.flowing.util.Constants.Companion.BASE_API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                var newRequest = request.newBuilder().header("Authorization", Constants.API_KEY)
                chain.proceed(newRequest.build())
            }
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson , okHttpClient : OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))

    }

    @Singleton
    @Provides
    fun provideGithubService(retrofit: Retrofit.Builder): UnsplashApiService {
        return retrofit
            .build()
            .create(UnsplashApiService::class.java)

    }





}