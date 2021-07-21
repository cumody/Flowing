package com.mahmoudshaaban.flowing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.mahmoudshaaban.flowing.R
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

    }

    private fun initAdapter() {
        binding.recyclerPopularPhotos.adapter = adapter
    }

    private fun observeObservers() {
       lifecycleScope.launch {
            viewModel.searchRepo().collect {
                adapter.submitData(it)
            }
        }
    }

}