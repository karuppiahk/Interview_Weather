package test.projects.interview_weather.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import test.projects.interview_weather.R
import test.projects.interview_weather.ui.main.MainActivity
import test.projects.interview_weather.ui.main.SplashScreen

class SignInDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.close_dialog_bg)

        val view = inflater.inflate(R.layout.sign_in_dialog, container, false)

        val sing_in_btn = view.findViewById<View>(R.id.create_account)
        sing_in_btn.setOnClickListener {
            //dismiss
            dismiss()

            //open signing in page
            (activity as MainActivity?)?.showFragment(SplashScreen(false), false)


        }

        val undoBtn = view.findViewById<View>(R.id.cancel)
        undoBtn.setOnClickListener {
            dismiss()
        }

        return view;
    }
}