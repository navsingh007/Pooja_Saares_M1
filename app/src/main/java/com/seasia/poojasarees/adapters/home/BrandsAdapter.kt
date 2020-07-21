package com.seasia.poojasarees.adapters.home

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.poojasarees.R
import com.seasia.poojasarees.databinding.RowBrandsBinding
import com.seasia.poojasarees.model.response.home.HomeOut


class BrandsAdapter(
    val context: Context,
    val brandsList: ArrayList<HomeOut.Brand>
//    val currency: String
) : RecyclerView.Adapter<BrandsAdapter.BrandsVH>() {
    private val TAG = "BrandsAdapter"
    private val NUMBERS_OF_ITEM_TO_DISPLAY = 3.6

    override fun getItemCount(): Int {
//        return 5
        return brandsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsVH {
        val binding = DataBindingUtil.inflate<RowBrandsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_brands,
            parent,
            false
        )
        binding.root.getLayoutParams().width = (getScreenWidth() / NUMBERS_OF_ITEM_TO_DISPLAY).toInt() /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return BrandsVH(binding)
    }

    override fun onBindViewHolder(holder: BrandsVH, position: Int) {
        val response = brandsList[position]

        holder.binding.tvBrandName.text = response.name

        Glide.with(context)
            .load(response.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.placeholder_logo_image)
            .into(holder.binding.ivBrand)

        holder.binding.root.setOnClickListener {

        }
    }

    fun onBrandClick(position: Int) {

    }

    inner class BrandsVH(val binding: RowBrandsBinding) :
        RecyclerView.ViewHolder(binding.root)


    // Three items
    fun getScreenWidth(): Int {
        val display = ((context as Activity).getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }
}