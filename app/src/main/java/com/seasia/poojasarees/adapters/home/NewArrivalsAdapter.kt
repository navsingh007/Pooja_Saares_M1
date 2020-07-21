package com.seasia.poojasarees.adapters.home

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.databinding.RowGridNewArrivalsBinding

import com.seasia.poojasarees.model.response.home.HomeOut
import com.seasia.poojasarees.utils.PreferenceKeys


class NewArrivalsAdapter(
    val context: Context,
    val newArrivalsList: ArrayList<HomeOut.NewProduct>
//    val currency: String
) : RecyclerView.Adapter<NewArrivalsAdapter.BrandsVH>() {
    private val TAG = "NewArrivalsAdapter"
    private val NUMBERS_OF_ITEM_TO_DISPLAY = 3.6

    override fun getItemCount(): Int {
//        return 5
        return newArrivalsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsVH {
        val binding = DataBindingUtil.inflate<RowGridNewArrivalsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_grid_new_arrivals,
            parent,
            false
        )
        binding.root.getLayoutParams().width = (getScreenWidth() / NUMBERS_OF_ITEM_TO_DISPLAY).toInt() /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return BrandsVH(binding)
    }

    override fun onBindViewHolder(holder: BrandsVH, position: Int) {
        val response = newArrivalsList[position]

        holder.binding.tvProductName.text = response.name ?: ""
        val price = response.price ?: ""
        val specialPrice = response.special_price ?: ""
        val brandCode = response.brand ?: ""

        var formattedPrice = ""
        var formattedSpecialPrice = ""
        try {
            formattedPrice = "${UtilsFunctions.priceStringToInt(price)}"
            formattedSpecialPrice = "${UtilsFunctions.priceStringToInt(specialPrice)}"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val radius = 20f
        Glide.with(context)
            .load("${AppConstants.IMG_BASE_URL}${response.image}")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .transform(RoundedCorners(20))
            .placeholder(R.drawable.placeholder_logo_image)
            .into(holder.binding.ivProduct)


        // Strike through off price
        UtilsFunctions.setPriceAndSplPrice(
            formattedPrice,
            formattedSpecialPrice,
            holder.binding.tvProductPrice,
            holder.binding.tvSpecialPrice
        )

        var brandName = ""
        if (!brandCode.isEmpty()) {
            val brands = MyApplication.sharedPref.getString(PreferenceKeys.BRANDS, "")
            val myType = object : TypeToken<ArrayList<HomeOut.Brand>>() {}.type
            val allBrands = Gson().fromJson<ArrayList<HomeOut.Brand>>(brands, myType)

            for (i in allBrands.indices) {
                if (allBrands[i].id.equals(brandCode)) {
                    brandName = allBrands[i].name ?: ""
                }
            }
            Log.d("Brands ===========> ", brands ?: "")
        }
        holder.binding.tvProductBrand.text = brandName

        holder.binding.root.setOnClickListener {

        }
    }

    fun onNewProductClick(position: Int) {

    }

    inner class BrandsVH(val binding: RowGridNewArrivalsBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Three items
    fun getScreenWidth(): Int {
        val display = ((context as Activity).getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }
}