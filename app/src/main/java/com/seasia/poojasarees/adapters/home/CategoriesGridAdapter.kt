package com.seasia.poojasarees.adapters.home

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.seasia.poojasarees.R
import com.seasia.poojasarees.model.response.HomeOut

class CategoriesGridAdapter(
    val context: FragmentActivity,
    val categoriesList: ArrayList<HomeOut.Category>,
    var activity: Context
//    val currency: String
) : ArrayAdapter<CategoriesGridAdapter.ItemHolder>(activity, R.layout.row_grid_category) {

    private var TAG = "CategoriesGridAdapter"


    override fun getCount(): Int {
//         return 4
        return categoriesList.size
//        return if (this.categoriesList != null) this.categoriesList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.row_grid_category, null)
            holder = ItemHolder()
            holder.icon = convertView.findViewById(R.id.ivProduct)
            holder.name = convertView!!.findViewById(R.id.tvProductName)
//            holder.rating = convertView!!.findViewById(R.id.rbProductRating)
//            holder.price = convertView!!.findViewById(R.id.tvProductPrice)
//            holder.offPrice = convertView!!.findViewById(R.id.tvProductOff)
//            holder.offPercentage = convertView!!.findViewById(R.id.tvProductPercentageOff)
            holder.topLayout = convertView.findViewById(R.id.topLayout)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemHolder
        }

//        holder.topLayout!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#" + categoriesList[position].colorCode.trim()))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

        val response = categoriesList[position]
        holder.name?.text = response.name

        holder.topLayout?.setOnClickListener { onProductClick() }

        Log.d(TAG, "======> ${response.image}")
        Glide.with(context)
            .load(response.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.placeholder_logo_image)
            .into(holder.icon!!)

        return convertView
    }

    private fun onProductClick() {

    }

    class ItemHolder {
        var name: TextView? = null
        var icon: ImageView? = null
        var topLayout: LinearLayout? = null

//        var price: TextView? = null
//        var rating: RatingBar? = null
//        var offPrice: TextView? = null
//        var offPercentage: TextView? = null
    }
}