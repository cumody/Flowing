package com.mahmoudshaaban.flowing.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.mahmoudshaaban.flowing.adapters.LoadStateAdapter
import com.mahmoudshaaban.flowing.adapters.PhotosAdapter
import com.mahmoudshaaban.flowing.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val adapter = PhotosAdapter()
    private val viewModel: PhotosViewModel by viewModels()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    requireContext(),
                    "Sorry Check your Network",
                    Toast.LENGTH_LONG
                ).show()
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

    private fun observeObservers() {
        lifecycleScope.launch {
            viewModel.getPhotos().collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}