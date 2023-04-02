package com.hyparz.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.hyparz.BuildConfig
import com.hyparz.R
import com.hyparz.ui.notification.NotificationActivity
import com.hyparz.utils.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable

/**
 * The type Base fragment.
 *
 * @param <T> the type parameter
 * @param <V> the type parameter
</V></T> */
/*Base Class for all fragments*/
abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(),
    CommonNavigator {

    val REQUEST_CODE_FOR_PERMISSION_SETTING = 1111
    var compositeDisposable = CompositeDisposable()

    /**
     * Get base activity.
     *
     * @return the base activity
     */
    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null

    /**
     * Get view data binding.
     *
     * @return the view data binding
     */
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    //    private ProgressHUD pDialog;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * Get layout id.
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

    override fun onAttach(context: Context) {
        if (context != null) {
            super.onAttach(context)
        }
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
            activity!!.onFragmentAttached()
        }
    }

    /**
     * This message is used to create a object of view model
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        setHasOptionsMenu(false)
    }

    /**
     * This message is used to bind layout
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding!!.root
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    /**
     * Hide keyboard.
     */
    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
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

    //rx method to check permission
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    /**
     * The interface Callback.
     */
    interface Callback {

        /**
         * On fragment attached.
         */
        fun onFragmentAttached()

        /**
         * On fragment detached.
         *
         * @param tag the tag
         */
        fun onFragmentDetached(tag: String)
    }

    fun openRewardsCashback(mContext: Activity) {
    }

    fun openSupport(mContext: Activity) {
    }

    override fun showProgressBar() {
        showProgressDialog(requireContext(), resources.getString(R.string.LOADING))
    }

    override fun hideProgressBar() {
        hideProgressDialog()
    }

    fun checkIfInternetOn(): Boolean {
        if (CommonUtils.isInternetOn(requireContext())) {
            return true
        } else {
            return false
        }
    }
}
