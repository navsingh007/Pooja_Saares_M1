package com.seasia.poojasarees.views.profile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.seasia.poojasarees.R
import com.seasia.poojasarees.model.helper.Profile

private val TAB_TITLES = arrayOf(
    R.string.profile_contact,
    R.string.profile_personal
)

class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    val profile: Profile?
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        val userProfile = profile
//        val personal = profile

        val contactFrag = ProfileContactFragment.newInstance(userProfile)!!
        val personalFrag =  ProfilePersonalFragment.newInstance(userProfile)!!
        return if (position == 0) contactFrag else personalFrag
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}