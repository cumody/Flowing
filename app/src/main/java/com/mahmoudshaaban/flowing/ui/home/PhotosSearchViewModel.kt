package com.mahmoudshaaban.flowing.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mahmoudshaaban.flowing.data.UnsplashRepository
import com.mahmoudshaaban.flowing.model.PhotoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotosSearchViewModel
@Inject constructor(private val repository: UnsplashRepository) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<PhotoModel>>? = null

    // if the same data are retrieved return the same flow else return the dif data
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
