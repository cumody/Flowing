package com.mahmoudshaaban.flowing.ui.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.mahmoudshaaban.flowing.adapters.LoadStateAdapter
import com.mahmoudshaaban.flowing.adapters.PhotosAdapter
import com.mahmoudshaaban.flowing.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val adapter = PhotosAdapter()
    private val viewModel by viewModels<PhotosViewModel>()
    private val binding get() = _binding!!
    private var searchJob: Job? = null
    lateinit var query: String

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
        searchListener(view.context)


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
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                observeObservers()
            }

            }


        }

    private fun searchListener(context: Context) {
        binding.txtSearchPhotos.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH &&  binding.txtSearchPhotos.text!!.isNotEmpty()) {
                search(binding.txtSearchPhotos.text.toString())
                closeSearch(context)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun closeSearch(context: Context) {
        binding.txtSearchPhotos.text!!.clear()
        binding.txtSearchPhotos.clearFocus()
        val input = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun search(query : String ){
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collect {
                adapter.submitData(it)
            }
        }

    }

   /* private fun updateRepoListFromInput() {
        binding.txtSearchPhotos.text?.trim().let {
            if (it!!.isNotEmpty()) {
                search(it.toString())
            }
        }
    }*/

    /*private fun initSearch(query: String) {
        binding.txtSearchPhotos.setText(query)

        binding.txtSearchPhotos.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.txtSearchPhotos.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recyclerPopularPhotos.scrollToPosition(0) }
        }
    }*/



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}