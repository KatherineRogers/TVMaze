package com.example.tvmazesample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvmazesample.databinding.OnNowItemBinding

class OnNowAdapter(private val listener: OnNowClickListener): RecyclerView.Adapter<ShowViewHolder>() {

    private var shows = mutableListOf<ShowMain>()

    fun setOnNow(shows: ArrayList<ShowMain>){
        this.shows = shows.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OnNowItemBinding.inflate(inflater, parent, false)
        binding.item.clipToOutline = true
        return ShowViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val show = shows[position]
        holder.binding.name.text = show.show?.name
        holder.itemView.setOnClickListener{
            listener.onClick(show, position)
        }
        Glide.with(holder.itemView.context).load(show.show?.image?.original).placeholder(R.color.white).into(holder.binding.image)
    }
}

class ShowViewHolder(val binding: OnNowItemBinding) : RecyclerView.ViewHolder(binding.root)