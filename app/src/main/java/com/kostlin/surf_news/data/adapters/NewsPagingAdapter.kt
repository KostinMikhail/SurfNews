package com.kostlin.surf_news.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kostlin.surf_news.R
import com.kostlin.surf_news.data.enteties.NewsEntity
import com.kostlin.surf_news.data.utils.DateFormatter
import com.kostlin.surf_news.databinding.ItemNewsLayoutBinding

class NewsPagingAdapter(private val onBookmarkClicked: (NewsEntity) -> Unit): PagingDataAdapter<NewsEntity, NewsPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class MyViewHolder(val binding: ItemNewsLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsEntity) {
            val image: Any? = if (data.urlToImage.isNullOrEmpty()){
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_no)
            }else{
                data.urlToImage
            }
            val author = if (data.author.isNullOrEmpty()) "Anonymous" else data.author

            with(binding){
                Glide.with(itemView.context)
                    .load(image)
                    .centerCrop()
                    .into(ivItemNewsImage)
                tvItemNewsAuthor.text = author
                tvItemNewsTitle.text = data.title
                tvItemNewsDate.text = DateFormatter.formatDate(data.publishedAt)
                when(data.isBookmarked){
                    true -> ivItemNewsBookmark.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_bookmarked))
                    false -> ivItemNewsBookmark.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_bookmark))
                }
            }
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        val ivBookmark = holder.binding.ivItemNewsBookmark
        if (data != null) {
            holder.bind(data)
            ivBookmark.setImageDrawable(
                ContextCompat.getDrawable(ivBookmark.context,
                if (data.isBookmarked) {
                    R.drawable.ic_baseline_bookmarked
                } else {
                    R.drawable.ic_baseline_bookmark
                }
            ))
            ivBookmark.setOnClickListener { onBookmarkClicked(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data:NewsEntity)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>(){
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}