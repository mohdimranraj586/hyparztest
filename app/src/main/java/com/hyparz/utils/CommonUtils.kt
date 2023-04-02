package com.hyparz.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hyparz.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.ceil

object CommonUtils {

    private var pDialog: CustomProgressDialog? = null
    private lateinit var pattern: Pattern
    private var matcher: Matcher? = null

    val READ_WRITE_EXTERNAL_STORAGE_AND_CAMERA =
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

    val CONTACTS_READS_AND_WRITE_PERMISSION =
        arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )

    val FINGER_PRINT_PERMISSION =
        arrayOf(
            Manifest.permission.USE_FINGERPRINT
        )

    fun isValidUrl(urlString: String): Boolean {
        try {
            var url = URL(urlString);
            return URLUtil.isValidUrl(url.toString()) && Patterns.WEB_URL.matcher(url.toString()).matches();
        } catch (e: MalformedURLException) {

        }
        return false;
    }

    fun roundOffValue(value: Float): String {
        val string_form: String = value.toString().substring(0, value.toString().indexOf('.'))
        var formated_decimal: String? = null

        if (string_form.length == 1) {
            formated_decimal = String.format("%.6f", value)
        } else if (string_form.length == 2) {
            formated_decimal = String.format("%.5f", value)
        } else if (string_form.length == 3) {
            formated_decimal = String.format("%.4f", value)
        } else if (string_form.length == 4) {
            formated_decimal = String.format("%.3f", value)
        } else if (string_form.length >= 5) {
            formated_decimal = String.format("%.2f", value)
        }
        /**************/
        val string_formated: String =
            formated_decimal.toString().substring(0, formated_decimal.toString().indexOf('.'))
        if (string_formated.length >= 5) {
            formated_decimal = String.format("%.2f", value)
        }
        /*************/
        return formated_decimal!!
    }

    fun isEmailValid(strEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }


    private val PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#\$%^&*()_+={[}]|:;\"'<,>.?/-])(?=\\S+$).{6,15}$"
    private val MOBILE_PATTERN = "[0-9]{6,15}"

    private val EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    fun PasswordValidator(): Pattern {
        pattern = Pattern.compile(PASSWORD_PATTERN)
        return pattern
    }

    fun MobileValidator(): Pattern {
        pattern = Pattern.compile(MOBILE_PATTERN)
        return pattern
    }

    fun isMobileValidate(strMobile: String): Boolean {
        matcher = MobileValidator().matcher(strMobile)
        return matcher!!.matches()
    }

    fun emailValidator(): Pattern {
        pattern = Pattern.compile(EMAIL_PATTERN)
        return pattern
    }

    fun checkEmailValidation(email: String): Boolean {
        matcher = emailValidator().matcher(email)
        return matcher!!.matches()
    }

    fun validatePassword(password: String): Boolean {
        matcher = PasswordValidator().matcher(password)
        return matcher!!.matches()
    }

    fun getReportText(title: String, description: String): String {
        return "Title:" + title + "<br>" + "Description:" + description
    }


    private const val USERNAME_PATTERN = "[a-zA-z]+([ '-][a-zA-Z]+)*\$"

    fun userNameValidator(): Pattern {
        pattern = Pattern.compile(USERNAME_PATTERN)
        return pattern
    }

    fun userNameValidate(strName: String): Boolean {
        matcher = userNameValidator().matcher(strName)
        return matcher!!.matches()
    }

    fun calculatePageLimit(totalCount: Int, limit: Int): Double {
        try {

            val page = totalCount.toDouble() / limit
            return ceil(page)
        } catch (w: Exception) {
            w.printStackTrace()
        }

        return 0.0
    }

    fun validateName(firstName: String): Boolean {
        return firstName.matches("^[a-zA-Z]+\$".toRegex())
    }

    /*** method for check network connection ***/

    fun isInternetOn(context: Context?): Boolean {
        if (context != null) {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (true) {
                val networkInfo = connectivity.activeNetworkInfo
                return (!(networkInfo == null || !networkInfo.isAvailable || !networkInfo.isConnected))
            }
        }
        return false
    }


    fun isStringDataNullOrBlank(str: String?): String {
        var valueToReturn: String = ""
        if (str == null || str == "null") {
            return valueToReturn;
        } else {
            return str
        }
    }

    /**
     * if string contains any value except null then it'll return true
     */
    fun checkString(string: String?): Boolean {
        return if (string != null && !string.trim { it <= ' ' }.equals(
                "null",
                ignoreCase = true
            ) && string.trim { it <= ' ' } != ""
        ) {
            true
        } else false
    }

    fun convertTimeAMPM(time: String?): String {
        var localTime = ""
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            println(dateObj)
            println(SimpleDateFormat("K:mm a").format(dateObj))
            localTime = SimpleDateFormat("K:mm a").format(dateObj)
            //  localTime = localTime.replace("a.m.", "AM").replace("p.m.", "PM");

            val splited = localTime!!.split(":")

            if (splited[0].equals("0")) {
                localTime = "12:" + splited[1]
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return localTime
    }


    fun convertTimeAMPMSec(time: String?): String {
        var localTime = ""
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            localTime = SimpleDateFormat("HH:mm:ss").format(dateObj)
            localTime = localTime.replace("a.m.", "AM").replace("p.m.", "PM");

            val splited = localTime!!.split(":")
            if (splited[0].equals("0")) {
                localTime = "12:" + splited[1]
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return localTime
    }

    /*** method for string validation ***/
    fun isStringNullOrBlank(str: String?): Boolean {

        try {
            if (str == null) {
                return true
            } else if (str == "null" || str == "" || str.isEmpty() || str.equals(
                    "null",
                    ignoreCase = true
                )
            ) {
                return true
            }
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_CAT, e.message!!)
        }

        return false
    }


    /*** method for show progress dailog ***/

    fun showProgressDialog(context: Context, loadingText: String) {
        try {
            CommonUtils.pDialog = CustomProgressDialog.show(context, false, loadingText)
            if (CommonUtils.pDialog != null) {
                if (!CommonUtils.pDialog!!.isShowing)
                    CommonUtils.pDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /*** method for show hide dailog ***/

    fun hideProgressDialog() {
        try {
            if (CommonUtils.pDialog != null) {
                if (CommonUtils.pDialog!!.isShowing) {
                    CommonUtils.pDialog!!.hide()
                }

                if (CommonUtils.pDialog!!.isShowing) {
                    CommonUtils.pDialog!!.dismiss()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getDateInFormat(dateInput: String, dateOutput: String, dateString: String): String {
        var result = ""
        if (!isStringNullOrBlank(dateString)) {
            @SuppressLint("SimpleDateFormat") val formatComingFromServer =
                SimpleDateFormat(dateInput)
            @SuppressLint("SimpleDateFormat") val formatRequired = SimpleDateFormat(dateOutput)
            try {
                Log.v(AppConstants.LOG_CAT, "COMING DATE : $dateString")
                val dateN = formatComingFromServer.parse(dateString)
                result = formatRequired.format(dateN)

                if (result.contains("a.m.")) {
                    result = result.replace("a.m.", "AM")
                } else if (result.contains("p.m.")) {
                    result = result.replace("p.m.", "PM")
                } else if (result.contains("am")) {
                    result = result.replace("am", "AM")
                } else if (result.contains("pm")) {
                    result = result.replace("pm", "PM")
                }

                Log.v(AppConstants.LOG_CAT, "! RETURNING PARSED DATE : $result")
            } catch (e: Exception) {
                Log.v(
                    AppConstants.LOG_CAT,
                    "Some Exception while parsing the date : " + e.toString()
                )
            }
        }
        return result
    }

    fun getDateInFormatUTC(dateInput: String, dateOutput: String, _date: String): String {
        return try {
            @SuppressLint("SimpleDateFormat") val inputFormat = SimpleDateFormat(dateInput)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            @SuppressLint("SimpleDateFormat") val outputFormat = SimpleDateFormat(dateOutput)
            outputFormat.timeZone = TimeZone.getDefault()
            val date = inputFormat.parse(_date)

            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }


    fun localToGMT(): String {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:m:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }


    fun localToUTC(dateFormat: String, datesToConvert: String): String {


        val sourceFormat = SimpleDateFormat(dateFormat)
        sourceFormat.timeZone = TimeZone.getDefault()
        val parsed = sourceFormat.parse(datesToConvert)

        val sdf = SimpleDateFormat(dateFormat)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(parsed)

    }

    fun localToUTCTimeOnly(
        sourceFormat: String,
        targetFormat: String,
        datesToConvert: String
    ): String {


        val sourceFormat = SimpleDateFormat(sourceFormat)
        sourceFormat.timeZone = TimeZone.getDefault()
        val parsed = sourceFormat.parse(datesToConvert)

        val sdf = SimpleDateFormat(targetFormat)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(parsed)

    }


    fun convertUTCToLocalTimeOnly(
        sourceFormat: String,
        targetFormat: String,
        datesToConvert: String
    ): String {

        val sourceFormat = SimpleDateFormat(sourceFormat)
        sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
        val parsed = sourceFormat.parse(datesToConvert)

        val sdf = SimpleDateFormat(targetFormat)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(parsed)

    }


    /**
     * Convert utc to local string.
     *
     * @param resposneDate the resposne date
     * @return the string
     */
    fun convertUTCToLocal(resposneDate: String): String? {
        try {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(resposneDate)
            df.timeZone = TimeZone.getDefault()
            return df.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }


    fun convertUTCToLocalAMPM(resposneDate: String): String? {
        try {

            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(resposneDate)
            df.timeZone = TimeZone.getDefault()
            return df.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    fun getDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

    fun getOnlyCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

    fun getDiffYears(first: String, last: String): Int {

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date1 = simpleDateFormat.parse(first)
        val date2 = simpleDateFormat.parse(last)
        val a = getCalendar(date1)
        val b = getCalendar(date2)
        var diff = b.get(YEAR) - a.get(YEAR)
        if (a.get(MONTH) > b.get(MONTH) || a.get(MONTH) === b.get(MONTH) && a.get(DATE) > b.get(DATE)) {
            diff--
        }
        return diff
    }

    fun getCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }


    lateinit var strModified: String
    fun firstLetterCapital(str: String): String {

        if (!isStringNullOrBlank(str)) {
            strModified = str.capitalize()
        } else {
            strModified = ""
        }


        return strModified

    }


    /**
     * This method is used to calculate time ago format
     */
    fun timeAgo(createTimeStr: String, context: Context): String {
        var createTimeStr = createTimeStr
        try {
            val f = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val tz = TimeZone.getDefault()
            simpleDateFormat.timeZone = tz
            val d = simpleDateFormat.parse(createTimeStr)
            val currentDateandTime = f.format(Date())
            val d1 = f.parse(currentDateandTime)
            val milliseconds = d.time
            val millisecondsCurrent = d1.time
            val diff_Milli = millisecondsCurrent - milliseconds
            var minutes = Math.abs((millisecondsCurrent - milliseconds) / 60000)
            val seconds = Math.abs(diff_Milli / 1000)
            var hours = Math.abs(minutes / 60)
            val days = Math.abs(hours / 24)

            val calculateday = TimeUnit.SECONDS.toDays(seconds);
            val calculatehours = TimeUnit.SECONDS.toHours(seconds) - (calculateday * 24);
            val calculateminute1 =
                TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
            if (diff_Milli > 0) {
                createTimeStr =
                    calculateday.toString() + "T" + calculatehours.toString() + "T" + calculateminute1.toString()
            } else {
                createTimeStr =
                    "0" + "T" + "0" + "T" + "0"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            createTimeStr = ""
        }
        return createTimeStr
    }

    /*** method for use handle exception in web api calling ***/
    fun errorResponseHandler(
        output: String,
        response: Response<ResponseBody>,
        mNetworkResponseCallback: NetworkResponseCallback<*>
    ) {
        var output = output
        if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401 || response.code() == 422 || response.code() == 423) {
            output = ServerResponseHandler.getResponseBody(response)!!
            var outobject: JSONObject? = null
            try {
                outobject = JSONObject(output)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val jsonArray = outobject!!.optJSONArray(AppConstants.ERROR)
            if (jsonArray == null) {
                if (response.code() == 401) {
                    val s = ServerResponseHandler.checkJsonErrorBody(outobject)
                    mNetworkResponseCallback.onSessionExpire(s)
                } else {
                    mNetworkResponseCallback.onServerError(
                        ServerResponseHandler.checkJsonErrorBody(
                            outobject
                        )
                    )
                }

            } else {
                mNetworkResponseCallback.onServerError(
                    ServerResponseHandler.checkJsonErrorBody(
                        jsonArray.optJSONObject(
                            0
                        )
                    )
                )
            }
        } else {
            val responseStr = ServerResponseHandler.getResponseBody(response)
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(responseStr)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mNetworkResponseCallback.onServerError(
                ServerResponseHandler.checkJsonErrorBody(
                    jsonObject!!
                )
            )
        }

    }

    /*** method for use getting real path of media file ***/
    fun getRealPathFromURI(activity: Activity, contentURI: Uri): String? {
        var result: String? = null

        val cursor = activity.contentResolver.query(contentURI, null, null, null, null)

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
            }
            cursor.close()
        }
        return result
    }


    /*** method for use clear all previous activity ***/

    fun clearAllActivity(activity: Activity, tClass: Class<*>) {
        val intent = Intent(activity, tClass)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun showProfile(context: Context, path: String?, mview: ImageView) {

        try {
            if (!isStringNullOrBlank(path)) {
                Glide.with(context).load(path)
                    .thumbnail(0.5f)
                    .apply(RequestOptions().placeholder(R.mipmap.ic_user_icon).dontAnimate())
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .apply(RequestOptions.circleCropTransform())
                    .into(mview)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun addImages(context: Context, path: String?, mview: ImageView) {

        try {
            if (!isStringNullOrBlank(path)) {
                Glide.with(context).load(path)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(mview)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*** method for set or change application language ***/

    private var myLocale: Locale? = null


    fun getCurrentDate(givenDate: String): String? {
        try {
            val c = Calendar.getInstance().time
            println("Current time => $c")
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDate = df.format(c)
            return differenceTwoDate(currentDate, givenDate)


        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        return null
    }



    fun getResponseBody(response: Response<ResponseBody>): String? {

        var reader: BufferedReader? = null
        var output: String? = null
        try {

            if (response.body() != null) {
                reader = BufferedReader(InputStreamReader(response.body()!!.byteStream()))
            } else if (response.errorBody() != null) {
                reader = BufferedReader(InputStreamReader(response.errorBody()!!.byteStream()))
            }
            output = reader!!.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return output
    }




    private fun differenceTwoDate(firstDate: String, secondDate: String): String {
        var finalString = ""
        try {

            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val startDate = df.parse(firstDate)
            val endDate = df.parse(secondDate)

            var different = startDate.time - endDate.time

            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")

            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = different / daysInMilli
            different = different % daysInMilli

            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli

            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli

            val elapsedSeconds = different / secondsInMilli

            System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
            )


            if (elapsedDays >= 1) {
                finalString = elapsedDays.toString() + ""

                if (finalString.equals("1")) {
                    finalString = "$finalString day ago"
                } else {
                    finalString = "$finalString days ago"
                }

            } else if (elapsedHours >= 1) {
                finalString = elapsedHours.toString() + ""
                finalString = "$finalString hour ago"
            } else if (elapsedMinutes >= 1) {
                finalString = elapsedMinutes.toString() + ""
                finalString = "$finalString min ago"
            } else {
                finalString = " Just now"
            }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return finalString
    }

    public fun removeFile() {
        val mPath =
            Environment.getExternalStorageDirectory().toString() + "/soacail_sharing" + ".jpg"
        val pngFile = File(mPath)
        if (pngFile.exists()) {
            pngFile.delete()
        }
    }


    public fun applyBlurMaskFilter(tv: TextView, style: Blur?) {
        val radius = tv.textSize / 2
        val filter = BlurMaskFilter(radius, style)
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        tv.paint.maskFilter = filter
    }

    public fun changeFormat(stringDate: String): String {
        var finalFormatedDateStr: String? = null

        if (!CommonUtils.isStringNullOrBlank(stringDate)) {
            val format1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val format = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val c = getInstance()
            c.time = format1.parse(stringDate)

            var formatedDate = format.format(c.time)
            val splitedBySpace: List<String> = formatedDate.split(" ")
            val splitedBySlash: List<String> = splitedBySpace[0].split("/")

            finalFormatedDateStr = splitedBySlash.get(1) + "/" + splitedBySlash.get(2)

        } else {
            finalFormatedDateStr = ""
        }

        return finalFormatedDateStr
    }

    public fun prependZero(number: Int): String {
        if (number < 10 && number > 0)
            return "0" + number;
        else
            return number.toString();
    }

    /**
     * Format price without omr string.
     *
     * @param priceComing the price coming
     * @return the string
     */
    fun _formatPrice(priceComing: Float): Float? {
        var outPut = ""
        try {
            val price = priceComing.toDouble()
            //DecimalFormat df = new DecimalFormat("00.000");
            val df = DecimalFormat("0.000000")
            df.maximumFractionDigits = 5
            outPut = df.format(price)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return outPut.toFloat()
    }

    fun getTimezone(): String? {
        val tz = TimeZone.getDefault()
        return tz.id + ""
    }

    fun getDeviceId(activity: Context): String? {
        val androidId: String =
            Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
        return androidId
    }

    fun getDeviceModel(): String? {
        return Build.MODEL
    }

    fun getJsonObject(data: String?): JSONObject? {
        return try {
            JSONObject(data)
        } catch (e: JSONException) {
            //
            null
        }
    }

    fun getJsonValueStr(key: String?, response: JSONObject?): String? {
        try {
            if (response != null) {
                val value_str = response.getString(key)
                if (value_str != null && value_str != "null" && value_str != "") {
                    return value_str
                }
            }
        } catch (e: JSONException) {
//
            return ""
        }
        return ""
    }

    fun getJsonValueInt(key: String?, response: JSONObject?): Int? {
        try {
            if (response != null) {
                val value_str = response.getInt(key)
                if (value_str != null && value_str != 0) {
                    return value_str
                }
            }
        } catch (e: JSONException) {

            return 0
        }
        return 0
    }


    /**
     * Check valid numeric string
     *
     * @param str the str
     * @return boolean boolean
     */
    fun isValidNumeric(str: String): Boolean {
        var str = str
        str = str.trim { it <= ' ' } // trims the white spaces.
        if (str.length == 0) return false

        // if string is of length 1 and the only
        // character is not a digit
        if (str.length == 1 &&
            !Character.isDigit(str[0])
        ) return false

        // If the 1st char is not '+', '-', '.' or digit
        if (str[0] != '+' && str[0] != '-' &&
            !Character.isDigit(str[0]) && str[0] != '.'
        ) return false

        if (str.toDouble().toInt() == 0) {
            return false
        }

        // To check if a '.' or 'e' is found in given
        // string. We use this flag to make sure that
        // either of them appear only once.
        var flagDotOrE = false
        for (i in 1 until str.length) {
            // If any of the char does not belong to
            // {digit, +, -, ., e}
            if (!Character.isDigit(str[i]) && str[i] != 'e' && str[i] != '.' && str[i] != '+' && str[i] != '-'
            ) return false
            if (str[i] == '.') {
                // checks if the char 'e' has already
                // occurred before '.' If yes, return 0.
                if (flagDotOrE == true) return false

                // If '.' is the last character.
                if (i + 1 >= str.length) return false

                // if '.' is not followed by a digit.
                if (!Character.isDigit(str[i + 1])) return false
            } else if (str[i] == 'e') {
                // set flagDotOrE = 1 when e is encountered.
                flagDotOrE = true

                // if there is no digit before 'e'.
                if (!Character.isDigit(str[i - 1])) return false

                // If 'e' is the last Character
                if (i + 1 >= str.length) return false

                // if e is not followed either by
                // '+', '-' or a digit
                if (!Character.isDigit(str[i + 1]) && str[i + 1] != '+' && str[i + 1] != '-'
                ) return false
            }
        }
        /* If the string skips all above cases, then
           it is numeric*/return true
    }
}