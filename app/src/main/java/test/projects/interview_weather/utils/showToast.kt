package test.projects.interview_weather.utils

import android.content.Context
import android.widget.Toast


class showToast {

    //show success toast
    fun showSuccess(context: Context?, message: String) {
        if (context != null) {
           /* Toasty.success(
                context, message,
                Toast.LENGTH_SHORT,
                true
            ).show()*/
        }
    }


    //show warning failure toast
    fun showFailure(context: Context?, message: String) {
        if (context != null) {
           /* Toasty.error(
                context, message,
                Toast.LENGTH_SHORT, true
            ).show()*/
        }
    }
}