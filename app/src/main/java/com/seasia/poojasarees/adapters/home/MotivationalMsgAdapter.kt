package com.seasia.poojasarees.adapters.home

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.seasia.poojasarees.R
import com.seasia.poojasarees.databinding.RowBrandsBinding
import com.seasia.poojasarees.databinding.RowMotivationalMsgBinding
import com.seasia.poojasarees.model.response.HomeOut


class MotivationalMsgAdapter(
    val context: Context,
    val motivationalMsgList: ArrayList<HomeOut.InfoBanner>
//    val currency: String
) : RecyclerView.Adapter<MotivationalMsgAdapter.MotivationalMsgVH>() {
    private val TAG = "MotivationalMsgAdapter"
    private val TYPE_FIRST = 1
    private val TYPE_SECOND = 2

    override fun getItemCount(): Int {
//        return 2
        return motivationalMsgList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {
            return TYPE_FIRST
        } else {
            return TYPE_SECOND
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotivationalMsgVH {
        val binding = DataBindingUtil.inflate<RowMotivationalMsgBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_motivational_msg,
            parent,
            false
        )

        if (viewType == TYPE_FIRST) {
            binding.root.getLayoutParams().width = (parent.width * 0.4).toInt()
        } else {
            binding.root.getLayoutParams().width = (parent.width * 0.58).toInt()
        }
        return MotivationalMsgVH(binding)
    }

    override fun onBindViewHolder(holder: MotivationalMsgVH, position: Int) {
        val result = motivationalMsgList[position]

        Glide.with(context)
            .load(result.image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.placeholder_logo_image)
            .into(holder.binding.ivMotivation)

        holder.binding.root.setOnClickListener {

        }
    }

    fun onMotivationalMsgClick(position: Int) {

    }

    inner class MotivationalMsgVH(val binding: RowMotivationalMsgBinding) :
        RecyclerView.ViewHolder(binding.root)
}