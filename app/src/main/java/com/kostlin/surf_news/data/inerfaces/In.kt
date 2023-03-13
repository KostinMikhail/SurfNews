package com.kostlin.surf_news.data.inerfaces

import android.content.Context
import com.kostlin.surf_news.data.NewsRepository
import com.kostlin.surf_news.data.remote.ApiService
import com.kostlin.surf_news.data.utils.AppExecutors

object In {
    fun provideNewsRepository(context: Context): NewsRepository{
        val apiService = ApiService
        val database = NewsDatabase.getInstance(context)
        val appExecutors = AppExecutors()
        return NewsRepository.getInstance(apiService, database, appExecutors)
    }
}