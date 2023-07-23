package com.hyparz.ui.base

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.hyparz.BuildConfig
import com.hyparz.R
import com.hyparz.data.local.AppPreference
import com.hyparz.data.local.PreferenceKeys
import com.hyparz.ui.notification.NotificationActivity
import com.hyparz.utils.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable

/**
 * The type Base activity.
 *
 * @param <T> the type parameter
 * @param <V> the type parameter
</V></T> */
/*Base Class for all activities*/
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseFragment.Callback, CommonNavigator {
// Imran First Commit
// Imran Second Commit
    /**
     * Get view data binding.
     *
     * @return the view data binding
     */
    val REQUEST_CODE_FOR_PERMISSION_SETTING = 1111
    var compositeDisposable = CompositeDisposable()
    var viewDataBinding: T? = null
    private var mViewModel: V? = null
    private var permission: Array<String>? = null
    var context: Context? = null
    var appreference: AppPreference? = null


    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * Gets layout id.
     *
     * @return layout resource id
     */
    abstract//    @LayoutRes
    val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    companion object {
        var mIsInForegroundMode = false
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission = null
        context = this@BaseActivity
        getToken()
        performDataBinding()

        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(
                ForegroundBackgroundListener()
                    .also {

                    })
    }

    /*** method for show progress dialog ***/
    private var pDialog: CustomProgressDialog? = null

    fun showProgressDialog(context: Context, loadingText: String) {
        try {
            pDialog = CustomProgressDialog.show(context, false, loadingText)
            if (pDialog != null) {
                pDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /*** method for hide progress dialog ***/
    fun hideProgressDialog() {
        try {
            if (pDialog != null) {
                pDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun finishActivity() {
        finish()
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
    }

    /**
     * Hide keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    //performing data binding
    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }


    //rx method to check permission
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(context: Context, vararg permissions: String) {
        compositeDisposable.add(
            RxPermissions(this)
                .request(*permissions)
                .subscribe { granted ->
                    if (granted!!) {
                        rxPermissionGranted()
                    } else {
                        rxPermissionDenied()
                    }
                })
    }

    /*invoked when permission granted*/
    protected open fun rxPermissionGranted() {

    }

    /*invoked when permission denied*/
    protected open fun rxPermissionDenied() {

    }

    fun moveToApplicationSetting() {

    }
    override fun showValidationError(message: String) {

    }

    override fun showProgressBar() {
        showProgressDialog(this, resources.getString(R.string.LOADING))
    }

    override fun hideProgressBar() {
        hideProgressDialog()
    }

    fun checkIfInternetOn(): Boolean {
        if (CommonUtils.isInternetOn(this)) {
            return true
        } else {

       return false
        }
    }

    private fun getToken() {
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
//            AppPreference.getInstance(this@BaseActivity)
//                .addValue(PreferenceKeys.DEVICE_ID, instanceIdResult.token)
//        }
    }

    override fun onResume() {
        super.onResume()
        if (mIsInForegroundMode && !AppPreference.getInstance(this).getBoolean(PreferenceKeys.APP_LOCK))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val km =
                    getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (km.isKeyguardSecure) {
                    val authIntent = km.createConfirmDeviceCredentialIntent(
                        getString(R.string.app_name),""
                    )
                    startActivityForResult(authIntent, 2101)
                }
            }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2101) {
            if (resultCode == Activity.RESULT_OK) {
                mIsInForegroundMode = false
            } else {
                mIsInForegroundMode = false
                val intent = Intent()
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_HOME)
                startActivity(intent)
            }
        }
    }

    class ForegroundBackgroundListener : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun startSomething() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stopSomething() {
            mIsInForegroundMode = true
        }
    }
}

