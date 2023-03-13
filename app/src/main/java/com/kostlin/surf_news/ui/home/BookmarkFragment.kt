package com.kostlin.surf_news.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kostlin.surf_news.data.adapters.BookmarkAdapter
import com.kostlin.surf_news.data.enteties.NewsEntity
import com.kostlin.surf_news.databinding.FragmentBookmarkBinding
import com.kostlin.surf_news.ui.viewmodel.MainViewModel
import com.kostlin.surf_news.ui.viewmodel.MainViewModelFactory

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding
    private lateinit var viewFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels{viewFactory}
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFactory = MainViewModelFactory.getInstance(view.context)
        bookmarkAdapter = BookmarkAdapter{ news ->
            if (news.isBookmarked){
                viewModel.deleteNews(news)
            }else{
                viewModel.saveNews(news)
            }
        }

        binding?.rvBookmarks?.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = bookmarkAdapter
        }

        updateBookmarkNews()
        bookmarkAdapter.setOnItemClickCallback(object : BookmarkAdapter.OnItemClickCallback{
            override fun onItemClicked(data: NewsEntity) {
                selectedItem(view.context, data)
            }
        })
    }

    private fun selectedItem(context: Context, data: NewsEntity) {
//TODO transition to web activity
    }

    private fun updateBookmarkNews() {
        try {
            showLoading(true)
            viewModel.bookmark().observe(viewLifecycleOwner){ bookmarkNews ->
                bookmarkAdapter.submitList(bookmarkNews)
            }
            showLoading(false)
        }catch (e: Exception){
            showLoading(false)
        }
    }

    private fun showLoading(status: Boolean) {
        if (status){
            binding?.progressBar?.visibility = View.VISIBLE
        }else{
            binding?.progressBar?.visibility = View.GONE
        }
    }
}