package com.kostlin.surf_news.data.remote

import androidx.lifecycle.LiveData
import com.kostlin.surf_news.BuildConfig
import com.kostlin.surf_news.data.inerfaces.NewsApiConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://newsapi.org/v2/"
    private const val apiKey = BuildConfig.API_KEY
    private val newsApiConfig: NewsApiConfig by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(NewsApiConfig::class.java)
    }

    suspend fun topHeadlines(country: String, pageSize: Int): Call<LiveData<Articles>> {
        return newsApiConfig.getTopHeadlines(apiKey,country, pageSize)
    }
    suspend fun news(language: String, sortBy: String, page:Int, pageSize: Int): Articles{
        return newsApiConfig.getNews("android", apiKey, "ru", sortBy, page, pageSize)
    }
    suspend fun searchNews(query: String, page: Int, pageSize: Int): Articles{
        return newsApiConfig.getSearch(query, apiKey, page, pageSize)
    }
}