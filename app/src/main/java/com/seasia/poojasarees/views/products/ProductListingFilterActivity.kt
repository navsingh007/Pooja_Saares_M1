package com.seasia.poojasarees.views.products

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.adapters.product.ProductListingAdapter
import com.seasia.poojasarees.common.NetworkStateReceiver
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityProductListingFilterBinding
import com.seasia.poojasarees.model.response.products.ProductsByCategoryIdOut
import com.seasia.poojasarees.paging.endless.EndlessRecyclerViewScrollListener
import com.seasia.poojasarees.viewmodel.products.ProductListingVM
import kotlinx.android.synthetic.main.activity_product_listing_filter.*

class ProductListingFilterActivity : BaseActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    private val TAG = "ProductLstFilterAct"
    private lateinit var binding: ActivityProductListingFilterBinding
    private lateinit var productListingVM: ProductListingVM
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private val productsList = ArrayList<ProductsByCategoryIdOut.Item>()
    private var adapter: ProductListingAdapter? = null
    private var totalItemsInApi = 0
    private var currentPageCounter = 1

    private var networkStateReceiver: NetworkStateReceiver? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_product_listing_filter
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityProductListingFilterBinding
        productListingVM = ViewModelProvider(this).get(ProductListingVM::class.java)

        /**
         * HARDCODED VALUE
         */
        productListingVM.filterProducts("1", "4")
        productListingVM.initFilterAttributes("4")

        initProductListAdapter()
        initNetworkStateListner()
        filteredProductsObserver()
        filterAttributesObserver()
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()
    }

    private fun initNetworkStateListner() {
        // Network state change listener
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver?.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun initProductListAdapter() {
        adapter = ProductListingAdapter(this, productsList)
        rvProductListing.adapter = adapter

        val gridLayout = GridLayoutManager(this, 2)
        rvProductListing.layoutManager = gridLayout

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayout) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                if (totalItemsCount < totalItemsInApi) {
                    loadNextDataFromApi(page + 1)

                    currentPageCounter = page + 1

                    llLoading.visibility = View.VISIBLE
                }
            }
        }
        // Adds the scroll listener to RecyclerView
        rvProductListing.addOnScrollListener(scrollListener)
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    private fun loadNextDataFromApi(page: Int) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        productListingVM.filterProducts(page.toString(), "4")

        Log.d(TAG, "=-==========> PAGE COUNT - $page")
//        UtilsFunctions.showToastSuccess("Loaded from next page - $page")
    }

    private fun resetEndlessScrollState() {
        // 1. First, clear the array of data
        productsList.clear()
        // 2. Notify the adapter of the update
        adapter?.notifyDataSetChanged() // or notifyItemRangeRemoved
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState()
    }


    private fun filteredProductsObserver() {
        productListingVM.getFilteredProducts().observe(this, Observer { filteredProducts ->
            stopProgressDialog()

            llLoading.visibility = View.GONE
            if (filteredProducts == null) {
                UtilsFunctions.showToastError("NULL because TIME-OUT")
            }


            if (filteredProducts != null) {
                if (filteredProducts.items != null) {
//                    productsList.clear()

                    totalItemsInApi = filteredProducts.total_count ?: 0
                    // Set total items in Adapter also to notify Loading
                    adapter?.setTotalItemsInApi(totalItemsInApi)

                    val curSize = adapter?.itemCount ?: 0
                    productsList.addAll(filteredProducts.items)
                    adapter?.notifyItemRangeChanged(curSize, productsList.size - 1)
//                    adapter?.notifyDataSetChanged()
                }
//                UtilsFunctions.showToastSuccess("Toral items - ${filteredProducts.total_count}")
            }
        })
    }

    private fun filterAttributesObserver() {
        productListingVM.getFilterAttributes().observe(this, Observer { filterAttributes ->
            stopProgressDialog()

            if (filterAttributes != null) {

            }
        })
    }

    private fun loadingObserver() {
        productListingVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        productListingVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

            if (msg != null) {
                if (msg.equals("*****************************")) {
                    // TODO: 17-07-2020
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }

    private fun showUserWarningObserver() {
        productListingVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                // TODO: 17-07-2020
            }
        })
    }

    /**
     *  Network update
     */
    override fun onDestroy() {
        super.onDestroy()
        networkStateReceiver?.removeListener(this)
        this.unregisterReceiver(networkStateReceiver)
    }

    override fun networkAvailable() {
        if (productsList.size < totalItemsInApi) {
            llLoading.visibility = View.VISIBLE

            loadNextDataFromApi(currentPageCounter)
        }
    }

    override fun networkUnavailable() {
        llLoading.visibility = View.GONE
        UtilsFunctions.showToastWarning(resources.getString(R.string.internet_connection))
    }
}