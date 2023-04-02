package com.hyparz.ui.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyparz.BR
import com.hyparz.R
import com.hyparz.data.local.AppPreference
import com.hyparz.data.local.PreferenceKeys
import com.hyparz.data.model.bean.Results
import com.hyparz.data.model.response.NotificationListResponse
import com.hyparz.databinding.ActivityNotificationBinding
import com.hyparz.ui.base.BaseActivity
import com.hyparz.ui.notification.adapter.NotificationAdapter
import com.hyparz.utils.ClickListener
import com.hyparz.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_notification.*

/**
 * Author : Hyparz test
 * Date   : 3-Sep-19.
 * Description : This class is used to showing result list
 */

class NotificationActivity : BaseActivity<ActivityNotificationBinding, NotificationViewModel>(),
    NotificationNavigator {

    private lateinit var list: ArrayList<Results>
    private var page = 1
    private var notificationAdapter: NotificationAdapter? = null
    private var isLoading: Boolean = false
    var mCustomViewModel: NotificationViewModel = NotificationViewModel()
    override val viewModel: NotificationViewModel get() = mCustomViewModel
    override val bindingVariable: Int get() = BR.notificationVM

    /**
     * This variable in which pass layout for showing.
     */
    override val layoutId: Int get() = R.layout.activity_notification

    /**
     * This method is main method of class
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        super.onCreate(savedInstanceState)
        mCustomViewModel.navigator = this
        mCustomViewModel.initView()

    }

    /**
     * This method is called when click on back button
     */
    override fun backToPreviousActivity() {
        finish()
    }

    /**
     * This method is used to show progress bar
     */
    override fun showBottomProgress() {
        progress.visibility = View.VISIBLE
    }

    /**
     * This method is used to hide progress bar
     */
    override fun hideBottomProgress() {
        progress.visibility = View.GONE
    }

    override fun showPageLoader() {
        viewDataBinding!!.progress.visibility = View.VISIBLE
    }

    override fun showHideLoader() {
        viewDataBinding!!.progress.visibility = View.GONE
    }

    override fun hideProgressBar() {
        hideProgressDialog()
    }

    override fun showProgressBar() {
        showProgressDialog(this@NotificationActivity, resources.getString(R.string.LOADING))
    }

    /**
     * This method is used to show network error alert
     */
    override fun showNetworkError(error: String?) {
    }

    /**
     * This method is used to show update version alert
     */
    override fun onUpdateAppVersion(error: String?) {
//        DialogUtils.dialogForceUpdate(
//            this@NotificationActivity,
//            resources.getString(R.string.oops),
//            error!!,
//            ItemEventListener()
//        )
    }

    /**
     * This method is used to show session expire alert
     */
    override fun showSessionExpireAlert() {
//        DialogUtils.sessionExpireDialog(this@NotificationActivity)
    }

    override fun getStringResource(id: Int): String {
        return resources.getString(id)
    }

    /**
     * initialize and call api to get notificaitonn
     *
     */
    override fun init() {
        list = ArrayList()

        initializeAdapter()

        if (checkIfInternetOn()) {
            mCustomViewModel.notificationAPI(false, AppPreference.getInstance(this), "100")
        } else {
            tryAgain()
        }
    }

    inner class ItemEventListener : ClickListener() {
        override fun ondeleteAccount() {
        }

        override fun onForceUpdate() {
            super.onForceUpdate()
            val appPackageName = packageName // getPackageName() from Context or Activity object
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }
    }

    /**
     * retry api calling  on click of try again
     *
     */
    override fun tryAgain() {
        if (CommonUtils.isInternetOn(this@NotificationActivity)) {
            initializeAdapter()
            viewDataBinding!!.mainLayout.visibility = View.VISIBLE
            viewDataBinding!!.noInternetConnectionLayout.visibility = View.GONE
            mCustomViewModel.notificationAPI(false, AppPreference.getInstance(this), "100")
        } else {
            viewDataBinding!!.mainLayout.visibility = View.GONE
            viewDataBinding!!.noInternetConnectionLayout.visibility = View.VISIBLE
            viewDataBinding!!.noInternet.text ="No internet"// getString(R.string.no_internet)
            return
        }
    }

    /**
     * This method is called when click on back button
     */
    override fun onBackPressed() {
        finishActivity()
    }

    /**
     * This method is used to initialize adapter
     */
    private fun initializeAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        viewDataBinding!!.notificationRecycle.layoutManager = linearLayoutManager
        notificationAdapter = NotificationAdapter(this, list)
        viewDataBinding!!.notificationRecycle.adapter = notificationAdapter

        notificationAdapter!!.setOnItemClickListener(object :
            NotificationAdapter.OnItemClickListener {
            override fun onItemClicked(notificationRow: Results, position: Int) {

            }
        })


        viewDataBinding!!.notificationRecycle.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = linearLayoutManager!!.childCount
                val totalItemCount = linearLayoutManager!!.itemCount
                val firstVisibleItemPosition = linearLayoutManager!!.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems()
                    }
                }
            }
        })
    }

    fun loadMoreItems() {
        if (checkIfInternetOn()) {
            isLoading = true
            page += 1

            /*mCustomViewModel.notificationAPI(
                true,
                AppPreference.getInstance(this@NotificationActivity),
                list.size.toString()
            )*/
        }
    }

    /**
     * This method is called when getting response after calling API.
     */
    override fun getNotificationListResponse(notificationListResponse: NotificationListResponse) {
        if (notificationListResponse.data!!.results.isNotEmpty()) {
            AppPreference.getInstance(this)
                .addInt(
                    PreferenceKeys.BADGE_COUNT, 0)

            viewDataBinding!!.txtNoArticalFound.visibility = View.GONE
            viewDataBinding!!.notificationRecycle.visibility = View.VISIBLE
            list!!.addAll(notificationListResponse!!.data!!.results!!)
            notificationAdapter!!.notifyDataSetChanged()
        } else {
            viewDataBinding!!.txtNoArticalFound.visibility = View.VISIBLE
            viewDataBinding!!.notificationRecycle.visibility = View.GONE
        }
        isLoading = false
        if (page.toDouble() == CommonUtils.calculatePageLimit(
                notificationListResponse.data!!.results.size,
                10
            )
        ) {
            isLoading = true
        }
    }
}

