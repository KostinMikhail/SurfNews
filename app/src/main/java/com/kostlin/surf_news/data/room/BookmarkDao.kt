package com.kostlin.surf_news.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kostlin.surf_news.data.enteties.BookmarkEntity

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(news: BookmarkEntity)

    @Query("DELETE FROM bookmark WHERE title = :title ")
    fun deleteBookmark(title: String)

    @Query("SELECT EXISTS(SELECT * FROM bookmark WHERE title = :title)")
    fun isBookmarked(title: String): Boolean
}