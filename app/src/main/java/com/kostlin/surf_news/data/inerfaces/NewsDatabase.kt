package com.kostlin.surf_news.data.inerfaces

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kostlin.surf_news.data.enteties.BookmarkEntity
import com.kostlin.surf_news.data.enteties.NewsEntity
import com.kostlin.surf_news.data.enteties.RemoteKeys
import com.kostlin.surf_news.data.room.BookmarkDao
import com.kostlin.surf_news.data.room.NewsDao
import com.kostlin.surf_news.data.room.RemoteKeysDao

@Database(entities = [NewsEntity::class, RemoteKeys::class, BookmarkEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun remoteKeyDao(): RemoteKeysDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object{
        @Volatile
        private var INSTANCE:NewsDatabase? = null
        @JvmStatic
        fun getInstance(context: Context): NewsDatabase{
            if (INSTANCE == null){
                synchronized(NewsDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NewsDatabase::class.java, "news_db")
                        .build()
                }
            }
            return INSTANCE as NewsDatabase
        }
    }
}
