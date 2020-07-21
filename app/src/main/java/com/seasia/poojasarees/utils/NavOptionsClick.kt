package com.seasia.poojasarees.utils

import android.content.Intent
import android.widget.TextView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.views.address.AddressListActivity
import com.seasia.poojasarees.views.cart.MyCartActivity
import com.seasia.poojasarees.views.cms.AboutUsActivity
import com.seasia.poojasarees.views.cms.PrivacyPolicyActivity
import com.seasia.poojasarees.views.cms.TermsAndConditionsActivity
import com.seasia.poojasarees.views.home.HomeActivity
import com.seasia.poojasarees.views.products.ProductDetailsActivity
import com.seasia.poojasarees.views.products.ProductListingFilterActivity
import com.seasia.poojasarees.views.profile.ProfileActivity
import com.seasia.poojasarees.views.wishlist.WishListActivity

class NavOptionsClick(private val context: BaseActivity) {

    fun onOptionClick(option: String, tv: TextView) {
        when (option) {
            context.resources.getString(R.string.nav_home) -> {
//                context.startActivity(Intent(context, ServicesListActivity::class.java))
            }
            context.resources.getString(R.string.nav_profile) -> {
                context.startActivity(Intent(context, ProfileActivity::class.java))
            }
            context.resources.getString(R.string.nav_my_orders) -> {
//                UtilsFunctions.comingSoonDialog(context)
                context.startActivity(Intent(context, MyCartActivity::class.java))
            }
            context.resources.getString(R.string.nav_my_fav) -> {
//                UtilsFunctions.comingSoonDialog(context)
//                context.startActivity(Intent(context, ProductDetailsActivity::class.java))
                context.startActivity(Intent(context, ProductListingFilterActivity::class.java))
//                context.startActivity(Intent(context, WishListActivity::class.java))
            }
            context.resources.getString(R.string.nav_saved_address) -> {
//                UtilsFunctions.comingSoonDialog(context)
                context.startActivity(Intent(context, AddressListActivity::class.java))
            }
            context.resources.getString(R.string.nav_offers) -> {
                UtilsFunctions.comingSoonDialog(context)
            }
            context.resources.getString(R.string.nav_notification) -> {
                UtilsFunctions.comingSoonDialog(context)
            }
            context.resources.getString(R.string.nav_about_us) -> {
//                UtilsFunctions.comingSoonDialog(context)
                context.startActivity(Intent(context, AboutUsActivity::class.java))
            }
            context.resources.getString(R.string.nav_terms_n_conditions) -> {
                context.startActivity(Intent(context, TermsAndConditionsActivity::class.java))
            }
            context.resources.getString(R.string.nav_privacy_policy) -> {
                context.startActivity(Intent(context, PrivacyPolicyActivity::class.java))
            }
            context.resources.getString(R.string.nav_settings) -> {
                UtilsFunctions.comingSoonDialog(context)
            }
            context.resources.getString(R.string.nav_call_us) -> {
                UtilsFunctions.comingSoonDialog(context)
            }
            context.resources.getString(R.string.nav_logout) -> {
                (context as HomeActivity).showLogoutAlert()
            }
        }
    }
}