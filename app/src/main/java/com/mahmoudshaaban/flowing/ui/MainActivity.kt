package com.mahmoudshaaban.flowing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.mahmoudshaaban.flowing.R
import com.mahmoudshaaban.flowing.adapters.LoadStateAdapter
import com.mahmoudshaaban.flowing.adapters.PhotosAdapter
import com.mahmoudshaaban.flowing.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = PhotosAdapter()
    private val viewModel: PhotosViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initAdapter()
        observeObservers()

        binding.retryButton.setOnClickListener { adapter.retry() }

    }

    private fun initAdapter() {
        binding.recyclerPopularPhotos.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter {adapter.retry()} ,
            footer = LoadStateAdapter {adapter.retry()}

        )

        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.recyclerPopularPhotos.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            binding.networkConnection.isVisible = loadState.source.refresh is LoadState.Error
            binding.networkText.isVisible = loadState.source.refresh is LoadState.Error


            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(
                    this,
                    "Sorry Check your Network",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun observeObservers() {
       lifecycleScope.launch {
            viewModel.searchRepo().collect {
                adapter.submitData(it)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.recyclerPopularPhotos.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.recyclerPopularPhotos.visibility = View.VISIBLE
        }
    }

}