package com.seasia.poojasarees.views.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.poojasarees.R
import com.seasia.poojasarees.adapters.home.*
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.callbacks.ChoiceCallBack
import com.seasia.poojasarees.common.NetworkStateReceiver
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityHomeBinding
import com.seasia.poojasarees.databinding.AppBarMainBinding
import com.seasia.poojasarees.databinding.ContentMainBinding
import com.seasia.poojasarees.databinding.NavHeaderMainBinding
import com.seasia.poojasarees.model.response.home.HomeOut
import com.seasia.poojasarees.utils.*
import com.seasia.poojasarees.viewmodel.home.HomeVM
import com.seasia.poojasarees.viewmodel.home.HomeVM.Companion.profileImage
import com.seasia.poojasarees.views.auth.LoginActivity
import com.seasia.poojasarees.views.categories.CategoryListActivity
import com.uniongoods.adapters.PromoBannerAdapter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//        bannerUrls.add("https://www.circleone.in/images/products_gallery_images/PVC-Banner.jpg")
//        bannerUrls.add("https://image.freepik.com/free-psd/online-shopping-with-discount_23-2148536749.jpg")

class HomeActivity : BaseActivity(), ChoiceCallBack, DialogssInterface,
    NetworkStateReceiver.NetworkStateReceiverListener {
    public lateinit var binding: ActivityHomeBinding
    private lateinit var contentBinding: ContentMainBinding
    private lateinit var appBarBinding: AppBarMainBinding
    private lateinit var navBinding: NavHeaderMainBinding
    private lateinit var homeVM: HomeVM
    private var confirmLogoutDialog: Dialog? = null
    private var confirmExitDialog: Dialog? = null
    val handler = Handler()

    private var networkStateReceiver: NetworkStateReceiver? = null

    // Camera image
    private val RESULT_LOAD_IMAGE = 100
    private val CAMERA_REQUEST = 1888

    // All lists
    private var bannersList = ArrayList<HomeOut.SliderBanner>()
    private var categoriesList = ArrayList<HomeOut.Category>()
    private var newArrivalsList = ArrayList<HomeOut.NewProduct>()
    private var motivationList = ArrayList<HomeOut.InfoBanner>()
    private var brandsList = ArrayList<HomeOut.Brand>()

    // All adapters
    private var bannerAdapter: PromoBannerAdapter? = null
    private var categoryAdapter: CategoriesGridAdapter? = null
    private var newArrivalsAdapter: NewArrivalsAdapter? = null
    private var msgAdapter: MotivationalMsgAdapter? = null
    private var brandsAdapter: BrandsAdapter? = null

    private var timerApplied = false
    private var update: Runnable? = null
    private var timer: Timer? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityHomeBinding
        homeVM = ViewModelProvider(this).get(HomeVM::class.java)

        // Status bar color change
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }

        initNavDrawer()
        setToolbar()
        homeResponseObserver()
        loadingObserver()
        profilePicResponseObserver()
        profilePicListener()
        setDefaultAddress()
        serverErrorObserver()
        onSearchClicked()
        refreshHome()
        setAllAdapters()
        getHomeData()
        showApiMsgObserver()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        // Network state change listener
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver?.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        viewAllCategories()
    }

    private fun viewAllCategories() {
        contentBinding.btnAllCategories.setOnClickListener {
            startActivity(Intent(this, CategoryListActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val userName = MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, "" ?: "")
        val lastName = MyApplication.sharedPref.getString(PreferenceKeys.LAST_NAME, "" ?: "")
        navBinding.tvNavUserName.text = "$userName $lastName"
    }

    private fun setAllAdapters() {
        setBannerAdapter()
        setMotivationalMsgAdapter()
        setCategoriesGrid()
        setBrandsAdapter()
        setNewArrivalsGrid()
    }

    private fun refreshHome() {
        contentBinding.swipeRefresh.setOnRefreshListener {
            getHomeData()
            contentBinding.swipeRefresh.isRefreshing = false
        }
    }

    private fun getHomeData() {
        // Show products acc. to Locale => Language id - 2 (HINDI), 1 (ENGLISH)
        when (LocaleManager.getLanguagePref(this)) {
            LocaleManager.HINDI -> {
                homeVM.getHomeDataAccToLang("2")
            }
            LocaleManager.ENGLISH -> {
                homeVM.getHomeDataAccToLang("1")
            }
        }
    }

    private fun onSearchClicked() {
        contentBinding.etSearch.setOnClickListener {
            UtilsFunctions.comingSoonDialog(this)
        }

        contentBinding.rlSearchClick.setOnClickListener {
            UtilsFunctions.comingSoonDialog(this)
        }
    }

    private fun setDefaultAddress() {
        contentBinding.tvAddress.setText("Deliver to : #234 Sector 12B, VIkas nagar.(Ambala)Pincode 1340678")

//        val defultAddress =
//            MyApplication.sharedPref.getString(PreferenceKeys.USER_DEFAULT_ADDRESS, "")
//        if (defultAddress != null && !defultAddress.isEmpty()) {
//            contentBinding.tvAddress.text = defultAddress
//
//            contentBinding.rlDefaultAddress.visibility = View.VISIBLE
//        } else {
//            contentBinding.rlDefaultAddress.visibility = View.GONE
//        }
    }

    private fun initNavDrawer() {
        navBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.nav_header_main,
            binding.navView,
            false
        )

        navBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        contentBinding = binding.incAppBar.incContentMain
        appBarBinding = binding.incAppBar

        // Set navigation adapter
        binding.rvNavDrawer.adapter = NavOptionsAdapter(this)
        binding.rvNavDrawer.layoutManager = LinearLayoutManager(this)
        binding.rvNavDrawer.addItemDecoration(SimpleDividerItemDecoration(this))

        // Set Navigation toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.incAppBar.ivDrawerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // User NAME and Image
        val userName =
            "${MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME)} ${MyApplication.sharedPref.getString(
                PreferenceKeys.LAST_NAME
            )}"
//        +"${MyApplication.sharedPref.getString(PreferenceKeys.LAST_NAME)}"
        navBinding.tvNavUserName.text = userName

        val userImage = MyApplication.sharedPref.getString(PreferenceKeys.PROFILE_PIC)
        Glide.with(this)
            .load(userImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.user_profile)
            .into(navBinding.ivNavImage)
    }

    private fun setToolbar() {
        appBarBinding.toolbarTitle.text = resources.getString(R.string.home_title)
    }

    // ******************** Banners **************************
    fun setBannerAdapter() {
//        if (banners.size > 0) {
        bannerAdapter = PromoBannerAdapter(this, bannersList, this)
        contentBinding.vpBanner.adapter = bannerAdapter
        contentBinding.tabs.setupWithViewPager(contentBinding.vpBanner)

        timeViewPagerManually(bannersList.size, contentBinding.vpBanner)

//        contentBinding.rlViewPager.visibility = View.VISIBLE

        // Layout height in dp to pixels
//        val heightInDp = 170
//        val scale: Float = getResources().getDisplayMetrics().density
//        val pixels = (heightInDp * scale + 0.5f).toInt()
//        contentBinding.llSearch.layoutParams.height = pixels
//        } else {
//            contentBinding.llSearch.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
//            contentBinding.rlViewPager.visibility = View.GONE
//        }
    }

    fun timeViewPagerManually(totalPages: Int, vpBanner: ViewPager) {
        // init timer and runnable
        update = null
        if (timer != null) {
            timer?.cancel()
            timer = null
        }

        val AUTO_SCROLL_THRESHOLD_IN_MILLI: Long = 5000

        var currentPage = vpBanner.currentItem

        update = Runnable {
            if (currentPage == totalPages) {
                currentPage = 0
            }
            vpBanner?.let { it.setCurrentItem(currentPage++, true) }
        }


//        if (!timerApplied) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 100, AUTO_SCROLL_THRESHOLD_IN_MILLI)
//        }
//        timerApplied = true
    }

    // ******************** Categories Grid **************************
    private fun setCategoriesGrid() {
//        if (!categoriesList.isEmpty()) {
        categoryAdapter = CategoriesGridAdapter(
            this,
            categoriesList,
            this
        )
        contentBinding.gvCategories.adapter = categoryAdapter
        //       }
    }

    // ******************** New Arrivals Grid **************************
    private fun setNewArrivalsGrid() {
//        if (!newArrivals.isEmpty()) {

        newArrivalsAdapter = NewArrivalsAdapter(this, newArrivalsList)
        contentBinding.rvNewArrivals.adapter = newArrivalsAdapter

        contentBinding.rvNewArrivals.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
//        }
    }

/*    private fun setNewArrivalsGrid(newArrivals: ArrayList<HomeOut.NewProduct>) {
        if (!newArrivals.isEmpty()) {
            val adapter = NewArrivalsGridAdapter(
                this,
                newArrivals,
                this
            )
            contentBinding.gvNewArrivals.adapter = adapter
        }
    }*/

    // ******************** Brands **************************
    private fun setBrandsAdapter() {
//        if (!brandsList.isEmpty()) {
        brandsAdapter = BrandsAdapter(this, brandsList)
        contentBinding.rvBrands.adapter = brandsAdapter

        contentBinding.rvBrands.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
//        }
    }

    // ******************** Motivational Messages **************************
    private fun setMotivationalMsgAdapter() {
//        if (motivationList.size > 0) {
        msgAdapter = MotivationalMsgAdapter(this, motivationList)
        contentBinding.rvMotivationalMsg.adapter = msgAdapter

        contentBinding.rvMotivationalMsg.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

/*        contentBinding.rvMotivationalMsg.setLayoutManager(object : LinearLayoutManager(this) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = width / 2
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return true
            }
        })*/


//        contentBinding.rvMotivationalMsg.visibility = View.VISIBLE
//        } else {
//            contentBinding.rvMotivationalMsg.visibility = View.GONE
//        }
    }

    private fun homeResponseObserver() {
        homeVM.homeResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
//                it.categories
//                it.newProducts

                // Save brands list locally to use throughout App
                MyApplication.sharedPref.save(PreferenceKeys.BRANDS, it.brands ?: "")

                // Banners
                if (it.banners != null) {
                    // Sales banners
                    if (it.banners.slider_banner != null) {
                        bannersList.clear()
                        bannersList.addAll(it.banners.slider_banner)
                        bannerAdapter?.notifyDataSetChanged()

                        timeViewPagerManually(bannersList.size, contentBinding.vpBanner)

                        if (it.banners.slider_banner.size > 0) {
//                            setBannerAdapter(it.banners.slider_banner)

                            // For size > 0 => Layout height in dp to pixels
                            val heightInDp = 170
                            val scale: Float = getResources().getDisplayMetrics().density
                            val pixels = (heightInDp * scale + 0.5f).toInt()
                            contentBinding.llSearch.layoutParams.height = pixels

                            contentBinding.rlViewPager.visibility = View.VISIBLE
                        } else {
                            // For size = 0
                            contentBinding.llSearch.layoutParams.height =
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            contentBinding.rlViewPager.visibility = View.GONE
                        }
                    }

                    // Motivational banners
                    if (it.banners.info_banner != null) {
//                        setMotivationalMsgAdapter(it.banners.info_banner)

                        motivationList.clear()
                        motivationList.addAll(it.banners.info_banner)
                        msgAdapter?.notifyDataSetChanged()

                        if (it.banners.info_banner.size > 0) {
                            contentBinding.rvMotivationalMsg.visibility = View.VISIBLE
                        } else {
                            contentBinding.rvMotivationalMsg.visibility = View.GONE
                        }
                    }
                }

                // Categories
                if (it.categories != null) {
//                    setCategoriesGrid(it.categories)

                    categoriesList.clear()
                    categoriesList.addAll(it.categories)
                    categoryAdapter?.notifyDataSetChanged()
                }

                // New Arrivals
                if (it.newProducts != null) {
//                    setNewArrivalsGrid(it.newProducts)

                    newArrivalsList.clear()
                    newArrivalsList.addAll(it.newProducts)
                    newArrivalsAdapter?.notifyDataSetChanged()
                }

                // Brands
                if (it.brands != null) {
//                    setBrandsAdapter(it.brands)

                    brandsList.clear()
                    brandsList.addAll(it.brands)
                    brandsAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun loadingObserver() {
        homeVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    /**
     *  *****************  User profile Image *************************
     */
    override fun photoFromCamera(mKey: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(this, packageName + ".fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //currentPhotoPath = File(baseActivity?.cacheDir, fileName)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents

            profileImage = absolutePath
        }
    }

    override fun photoFromGallery(mKey: String) {
        val i = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }

    private fun profilePicResponseObserver() {
        homeVM.getProfilePicResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
                MyApplication.sharedPref.saveString(PreferenceKeys.PROFILE_PIC, it.imagePath ?: "")
                UtilsFunctions.showToastSuccess(resources.getString(R.string.profile_pic_success))
//                UtilsFunctions.showToastSuccess("${it.message}")

//                val imageLink = it.message
//                if (imageLink != null) {
//                    imageLink.replace("\\", "")
//                }
//                setImage(imageLink ?: "")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            profileImage = picturePath
            setImage(picturePath)
            cursor.close()
            homeVM.updateProfilePic()
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK /*&& null != data*/) {
            setImage(profileImage)
            homeVM.updateProfilePic()
        }
    }

    private fun setImage(path: String) {
        Glide.with(this)
            .load(path)
            .placeholder(R.drawable.user_profile)
            .into(navBinding.ivNavImage)
    }

    private fun profilePicListener() {
        navBinding.ivNavImage.setOnClickListener {
            if (checkAndRequestPermissions()) {
                DialogClass().setConfirmationDialog(this, this, "gallery")
            }
        }
    }

    /**
     *  User Logout
     */

    override fun onBackPressed() {
//        showLogoutAlert()
        exitAppAlert()
    }

    private fun exitAppAlert() {
        confirmExitDialog = DialogClass().setDefaultDialog(
            this,
            this,
            getString(R.string.home_exit_app),
            getString(R.string.home_exit_app_title)
        )
        confirmExitDialog?.show()
    }

    public fun showLogoutAlert() {

        confirmLogoutDialog = DialogClass().setDefaultDialog(
            this,
            this,
            getString(R.string.home_logout),
            getString(R.string.home_logout_confirm)
        )
        confirmLogoutDialog?.show()
    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            getString(R.string.home_logout) -> {
                // Empty local strings on logout
                MyApplication.sharedPref.saveString(PreferenceKeys.CUSTOMER_ID, "")
                MyApplication.sharedPref.saveString(PreferenceKeys.CUSTOMER_TOKEN, "")
                MyApplication.sharedPref.saveBoolean(PreferenceKeys.IS_LOGIN, false)

                confirmLogoutDialog?.dismiss()

                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            getString(R.string.home_exit_app) -> {
                confirmExitDialog?.dismiss()
//                UtilsFunctions.finishAppProcess()
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {
            getString(R.string.home_logout) -> confirmLogoutDialog?.dismiss()
            getString(R.string.home_exit_app) -> confirmExitDialog?.dismiss()
        }
    }

    private fun serverErrorObserver() {
        homeVM.isServerError().observe(this, Observer {
            if (it) {
                UtilsFunctions.serverErrorDialog(this)
            }
        })
    }

    private fun showApiMsgObserver() {
        homeVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

            if (msg != null) {
                UtilsFunctions.showToastError(msg)
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
        getHomeData()
    }

    override fun networkUnavailable() {
        UtilsFunctions.showToastWarning(resources.getString(R.string.internet_connection))
    }
}