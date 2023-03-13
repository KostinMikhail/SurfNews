package com.kostlin.surf_news.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kostlin.surf_news.data.adapters.NewsPagingAdapter
import com.kostlin.surf_news.data.enteties.NewsEntity
import com.kostlin.surf_news.databinding.FragmentNewsBinding
import com.kostlin.surf_news.ui.viewmodel.MainViewModel
import com.kostlin.surf_news.ui.viewmodel.MainViewModelFactory
import com.kostlin.surf_news.data.utils.Result
import com.kostlin.surf_news.ui.WebActivity

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding
    private lateinit var viewFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewFactory }
    private lateinit var newsPagingAdapter: NewsPagingAdapter
    private var url = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFactory = MainViewModelFactory.getInstance(view.context)
        newsPagingAdapter = NewsPagingAdapter { news ->
            if (news.isBookmarked) {
                viewModel.deleteNews(news)
            } else {
                viewModel.saveNews(news)
            }
        }

        binding?.rvNews?.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = newsPagingAdapter
        }

        updateListNews(view.context)
        newsPagingAdapter.setOnItemClickCallback(object : NewsPagingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: NewsEntity) {
                selectedItem(view.context, data)
            }
        })
    }

    private fun selectedItem(context: Context, url: NewsEntity) {
//        val intent = Intent(context, WebActivity::class.java)
//        intent.putExtra(WebActivity.EXTRA_URL, url.url)
//        startActivity(intent)
// TODO transition to web activity
    }


    private fun onCategoriesChange(result: Result<List<NewsEntity>>) {
//TODO categories change listener -> update query in ApiService
    }

    private fun updateListNews(context: Context) {
        try {
            viewModel.news(language[0], sortBy[2]).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        newsPagingAdapter.submitData(lifecycle, result.data)
                        showLoading(false)
                    }
                    is Result.Error -> {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    companion object {
        private val language = arrayListOf("id", "us", "en", "es")
        private val sortBy = arrayListOf("relevancy", "popularity", "publishedAt")
    }
}