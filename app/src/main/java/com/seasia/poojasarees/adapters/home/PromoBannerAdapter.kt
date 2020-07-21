package com.uniongoods.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.seasia.poojasarees.R
import com.seasia.poojasarees.model.response.home.HomeOut
import com.seasia.poojasarees.views.home.HomeActivity
import kotlinx.android.synthetic.main.promo_banner_item.view.*
import kotlin.collections.ArrayList

class PromoBannerAdapter(
    val context: HomeActivity,
    val bannersList: ArrayList<HomeOut.SliderBanner>,
    var activity: Context
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return bannersList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.promo_banner_item, null)

        // view.imageView_slide.setImageResource(images[position])
//        view.tv_service_name!!.text = offersList[position].name
//        view.tv_service_name!!.visibility = View.GONE

        val result = bannersList[position]
        Glide.with(context)
            .load(result.image)
            .placeholder(R.drawable.placeholder_logo_image)
            .transform(RoundedCorners(10))
            .transition(DrawableTransitionOptions.withCrossFade())
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(view.imgService!!)
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}