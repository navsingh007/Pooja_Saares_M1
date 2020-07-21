package com.seasia.poojasarees.views.categories

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.adapters.category.CategoryAdapter
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityCategoryListBinding
import com.seasia.poojasarees.model.response.category.CategoryListOut
import com.seasia.poojasarees.viewmodel.category.CategoryVM

class CategoryListActivity : BaseActivity() {
    private lateinit var binding: ActivityCategoryListBinding
    private var categoryList = ArrayList<CategoryListOut.ChildrenData>()
    private var categoryAdapter: CategoryAdapter? = null
    private lateinit var categoryVM: CategoryVM

    override fun getLayoutId(): Int {
        return R.layout.activity_category_list
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityCategoryListBinding
        categoryVM = ViewModelProvider(this).get(CategoryVM::class.java)

        setCategoryAdapter()
        getCategoriesListObserver()
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()
    }

    private fun setCategoryAdapter() {
        categoryAdapter = CategoryAdapter(this, categoryList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.rvCategories.layoutManager = linearLayoutManager
        binding.rvCategories.adapter = categoryAdapter
    }

    private fun getCategoriesListObserver() {
        categoryVM.getCategories().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
                if (it.children_data != null) {

                    if (it.children_data.size > 0) {
                        categoryList.clear()
                        categoryList.addAll(it.children_data)
                        categoryAdapter?.notifyDataSetChanged()

                        binding.tvNoRecord.visibility = View.GONE
                        binding.rvCategories.visibility = View.VISIBLE
                    } else {
                        binding.tvNoRecord.visibility = View.VISIBLE
                        binding.rvCategories.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun loadingObserver() {
        categoryVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        categoryVM.showApiMsg().observe(this, Observer { msg ->
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
        categoryVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                // TODO: 17-07-2020
            }
        })
    }
}