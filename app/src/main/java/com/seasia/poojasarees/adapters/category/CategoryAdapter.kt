package com.seasia.poojasarees.adapters.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.databinding.RowAddressBinding
import com.seasia.poojasarees.model.response.category.CategoryListOut

class CategoryAdapter(
    val context: Context,
    val categoryList: ArrayList<CategoryListOut.ChildrenData>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_address,
            parent,
            false
        ) as RowAddressBinding
        return ViewHolder(binding.root, viewType, binding, context, categoryList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {

        holder.binding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return categoryList.count()
//        return 5
    }

    inner class ViewHolder
        (
        v: View, val viewType: Int,
        val binding: RowAddressBinding,
        context: Context,
        categoryList: ArrayList<CategoryListOut.ChildrenData>
    ) : RecyclerView.ViewHolder(v)

}
