package com.kostlin.surf_news.data.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var author: String?,
    var title: String,
    var description: String?,
    var url: String,
    var urlToImage: String?,
    var publishedAt: String
): Parcelable
