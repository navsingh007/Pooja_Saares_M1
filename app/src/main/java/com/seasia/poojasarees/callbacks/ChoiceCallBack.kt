package com.seasia.poojasarees.callbacks

interface ChoiceCallBack {
    fun photoFromCamera(mKey:String)
    fun photoFromGallery(mKey:String)
}

interface OnAddressDelete {
    fun onDeleteAddress(addressId: String)
    fun onDefaultAddress(addressId: String)
}
