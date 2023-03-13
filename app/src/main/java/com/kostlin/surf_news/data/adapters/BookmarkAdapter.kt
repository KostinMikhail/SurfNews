package com.kostlin.surf_news.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kostlin.surf_news.R
import com.kostlin.surf_news.data.enteties.NewsEntity
import com.kostlin.surf_news.data.utils.DateFormatter
import com.kostlin.surf_news.databinding.ItemNewsLayoutBinding

class BookmarkAdapter(private val onBookmarkClicked: (NewsEntity) -> Unit): ListAdapter<NewsEntity, BookmarkAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class MyViewHolder(val binding: ItemNewsLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: NewsEntity) {
            val image: Any? = if (data.urlToImage.isNullOrEmpty()) {
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_no)
            } else {
                data.urlToImage
            }
            val author = if (data.author.isNullOrEmpty()) "Anonymous" else data.author

            with(binding) {
                Glide.with(itemView.context)
                    .load(image)
                    .centerCrop()
                    .into(ivItemNewsImage)
                tvItemNewsAuthor.text = author
                tvItemNewsTitle.text = data.title
                tvItemNewsDate.text = DateFormatter.formatDate(data.publishedAt)
                when (data.isBookmarked) {
                    true -> ivItemNewsBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_baseline_bookmarked
                        )
                    )
                    false -> ivItemNewsBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_baseline_bookmark
                        )
                    )
                }
            }
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        val ivBookmark = holder.binding.ivItemNewsBookmark
        if (data != null) {
            holder.bind(data)
        }
        ivBookmark.setOnClickListener { onBookmarkClicked(data) }
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