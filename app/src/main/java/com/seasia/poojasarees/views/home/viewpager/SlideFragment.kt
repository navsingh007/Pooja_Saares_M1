package com.seasia.poojasarees.views.home.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.viewmodel.home.SliderVM


class SlideFragment(/*val bannerUrls: ArrayList<String>*/) : Fragment() {
    private var sliderViewModel: SliderVM? = null
    private lateinit var bannerUrls: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sliderViewModel = ViewModelProvider(this).get(SliderVM::class.java)
        var index = 1
        if (arguments != null) {
            index = requireArguments().getInt(ARG_SECTION_NUMBER)
            bannerUrls = requireArguments().getStringArrayList(BANNER_URL)!!
        }
        sliderViewModel?.setIndex(index)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_slide, container, false)
        val textView = root.findViewById<TextView>(R.id.section_label)
        val imageView = root.findViewById<ImageView>(R.id.imageView)

        sliderViewModel?.text?.observe(viewLifecycleOwner, Observer<Int?> { index ->
            val requestOptions = RequestOptions().transforms(CenterCrop(), RoundedCorners(16))
            UtilsFunctions.loadImage(
                requireContext(),
                bannerUrls[index!!],
                requestOptions,
                R.drawable.no_image,
                imageView
            )
        })
        return root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val BANNER_URL = "bannerUrl"

        fun newInstance(index: Int, bannerUrl: ArrayList<String>): SlideFragment {
            val fragment = SlideFragment()
//            bannerUrls.clear()
//            bannerUrls.addAll(bannerUrl)

            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            bundle.putStringArrayList(BANNER_URL, bannerUrl)
            fragment.arguments = bundle
            return fragment
        }
    }
}