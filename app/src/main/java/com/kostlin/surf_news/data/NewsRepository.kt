package com.kostlin.surf_news.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.*
import com.kostlin.surf_news.data.enteties.BookmarkEntity
import com.kostlin.surf_news.data.enteties.NewsEntity
import com.kostlin.surf_news.data.inerfaces.NewsDatabase
import com.kostlin.surf_news.data.remote.ApiService
import com.kostlin.surf_news.data.utils.AppExecutors
import com.kostlin.surf_news.data.utils.Result

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val database: NewsDatabase,
    private val appExecutors: AppExecutors
){

    @OptIn(ExperimentalPagingApi::class)
    fun getNews(language: String, sortBy: String): LiveData<Result<PagingData<NewsEntity>>> {
        val result = MediatorLiveData<Result<PagingData<NewsEntity>>>()
        result.value = Result.Loading
        try {
            val data = Pager(
                config = PagingConfig(pageSize = 8),
                remoteMediator = NewsRemoteMediator(language,sortBy, database, apiService),
                pagingSourceFactory = {database.newsDao().news()}
            ).liveData
            result.addSource(data){ newData ->
                result.value = Result.Success(newData)
            }
        }catch (t:Throwable){
            result.value = Result.Error(t.message.toString())
        }
        return result
    }

    fun setBookmark(news: NewsEntity, bookmarkState: Boolean){
        appExecutors.diskIO.execute {
            news.isBookmarked = bookmarkState
            if (bookmarkState){
                database.bookmarkDao().insertBookmark(BookmarkEntity(news.title))
            }else{
                database.bookmarkDao().deleteBookmark(news.title)
            }
            database.newsDao().update(news)
        }
    }

    fun getBookmark(): LiveData<List<NewsEntity>> = database.newsDao().bookmarked()

    companion object{
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(
            apiService: ApiService,
            database: NewsDatabase,
            appExecutors: AppExecutors
        ): NewsRepository = instance?: synchronized(this){
            instance?: NewsRepository(apiService, database, appExecutors)
        }.also { instance = it }
    }
}