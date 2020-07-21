package com.seasia.poojasarees.adapters.home

import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.databinding.RowNavItemsBinding
import com.seasia.poojasarees.utils.NavOptionsClick
import com.seasia.poojasarees.views.home.HomeActivity


class NavOptionsAdapter(
    val context: HomeActivity
) : RecyclerView.Adapter<NavOptionsAdapter.SubServicesVH>() {
    private var optionsList = ArrayList<String>()
    private var optionsIcon = ArrayList<Int>()

    init {
        optionsList.add(context.resources.getString(R.string.nav_home))
        optionsList.add(context.resources.getString(R.string.nav_profile))
        optionsList.add(context.resources.getString(R.string.nav_my_orders))
        optionsList.add(context.resources.getString(R.string.nav_my_fav))
        optionsList.add(context.resources.getString(R.string.nav_saved_address))
        optionsList.add(context.resources.getString(R.string.nav_offers))
        optionsList.add(context.resources.getString(R.string.nav_notification))
        optionsList.add(context.resources.getString(R.string.nav_about_us))
        optionsList.add(context.resources.getString(R.string.nav_terms_n_conditions))
        optionsList.add(context.resources.getString(R.string.nav_privacy_policy))
        optionsList.add(context.resources.getString(R.string.nav_settings))
        optionsList.add(context.resources.getString(R.string.nav_call_us))
        optionsList.add(context.resources.getString(R.string.nav_logout))

        optionsIcon.add(R.drawable.nav_home2)
        optionsIcon.add(R.drawable.nav_profile2)
        optionsIcon.add(R.drawable.nav_orders2)
        optionsIcon.add(R.drawable.nav_favourites2)
        optionsIcon.add(R.drawable.nav_address2)
        optionsIcon.add(R.drawable.nav_offers2)
        optionsIcon.add(R.drawable.nav_notification2)
        optionsIcon.add(R.drawable.nav_about_us2)
        optionsIcon.add(R.drawable.nav_terms_conditions2)
        optionsIcon.add(R.drawable.nav_about_us2)
        optionsIcon.add(R.drawable.nav_settings2)
        optionsIcon.add(R.drawable.nav_call_us2)
        optionsIcon.add(R.drawable.nav_logout2)
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubServicesVH {
        val binding = DataBindingUtil.inflate<RowNavItemsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_nav_items,
            parent,
            false
        )
        return SubServicesVH(binding)
    }

    override fun onBindViewHolder(holder: SubServicesVH, position: Int) {
        holder.binding.tvOptionName.text = optionsList.get(position)
        holder.binding.imgIcon.setImageResource(optionsIcon.get(position))

        holder.binding.root.setOnClickListener {
            context.binding.drawerLayout.closeDrawers()
            NavOptionsClick(context).onOptionClick(optionsList[position], holder.binding.tvOptionName)
        }

        val tv = holder.binding.tvOptionName
        val icon = holder.binding.imgIcon
        when(holder.binding.tvOptionName.text.toString()) {
            context.resources.getString(R.string.nav_my_orders) -> {
                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
            context.resources.getString(R.string.nav_my_fav) -> {
//                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
//                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
            context.resources.getString(R.string.nav_saved_address) -> {
//                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
//                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
            context.getString(R.string.nav_offers) -> {
                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
            context.resources.getString(R.string.nav_notification) -> {
                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
            context.resources.getString(R.string.nav_about_us) -> {
//                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
            }
            context.resources.getString(R.string.nav_settings) -> {
                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
            context.resources.getString(R.string.nav_call_us) -> {
                tv.setTextColor(context.resources.getColor(R.color.colorGrey1))
                icon.setColorFilter(context.resources.getColor(R.color.colorGrey1), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    inner class SubServicesVH(val binding: RowNavItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}