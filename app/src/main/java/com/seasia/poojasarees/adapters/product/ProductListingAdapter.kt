package com.seasia.poojasarees.adapters.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.databinding.RowGridProductListingBinding
import com.seasia.poojasarees.model.response.products.ProductsByCategoryIdOut

class ProductListingAdapter(
    val context: Context,
    val productsList: ArrayList<ProductsByCategoryIdOut.Item>
) : RecyclerView.Adapter<ProductListingAdapter.ViewHolder>() {
    private var totalItemsInApi = 0

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_grid_product_listing,
            parent,
            false
        ) as RowGridProductListingBinding
        return ViewHolder(binding.root, viewType, binding, context, productsList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {

        // Show Loading progress
    }

    override fun getItemCount(): Int {
        return productsList.count()
    }

    fun setTotalItemsInApi(totalItemsInApi: Int) {
        this.totalItemsInApi = totalItemsInApi
    }

    inner class ViewHolder
        (
        v: View, val viewType: Int,
        val binding: RowGridProductListingBinding,
        context: Context,
        productsList: ArrayList<ProductsByCategoryIdOut.Item>
    ) : RecyclerView.ViewHolder(v)

}
