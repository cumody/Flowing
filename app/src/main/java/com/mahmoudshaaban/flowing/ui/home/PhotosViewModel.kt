package com.mahmoudshaaban.flowing.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mahmoudshaaban.flowing.api.UnsplashApiService
import com.mahmoudshaaban.flowing.data.UnsplashRepository
import com.mahmoudshaaban.flowing.model.PhotoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class PhotosViewModel
@Inject constructor(private val repository: UnsplashRepository) : ViewModel()  {


    private var currentSearchResult: Flow<PagingData<PhotoModel>>? = null
    private var currentQueryValue: String? = null


    private var lastQuery = "popular"

    // if the same data are retrieved return the same flow else return the dif data
    fun getPhotos(): Flow<PagingData<PhotoModel>> {
        val lastResult = currentSearchResult
        if ( lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<PhotoModel>> = repository.loadPhotos()
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun searchRepo(queryString: String): Flow<PagingData<PhotoModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<PhotoModel>> = repository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }






}



