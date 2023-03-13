package com.kostlin.surf_news.data.remote

data class Articles(
    var status: String,
    var totalResults: Int,
    var articles: List<News>
)