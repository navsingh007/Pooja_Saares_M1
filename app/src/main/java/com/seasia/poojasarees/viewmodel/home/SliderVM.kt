package com.seasia.poojasarees.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class SliderVM : ViewModel() {
    private val mIndex = MutableLiveData<Int>()
    val text =
        Transformations.map(
            mIndex
        ) { input -> input - 1 }

    fun setIndex(index: Int) {
        mIndex.value = index
    }
}