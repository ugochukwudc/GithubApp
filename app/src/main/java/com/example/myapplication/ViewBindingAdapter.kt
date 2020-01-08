package com.example.myapplication

import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * Toggles visibility of view with given boolean to VISIBLE or GONE
 */
@BindingAdapter("visibleOrGone", "animIn", "animOut", requireAll = false)
fun View.setVisibleOrGone(isVisible: Boolean?, animIn: Animation?, animOut: Animation?) {
    if (isVisible != null && isVisible) {
        visibility = View.VISIBLE
        animIn?.let { startAnimation(it) } // Apply entrance animation if provided
    } else {
        if (animOut != null) {
            // Apply exit animation if provided, then set visibility to View.GONE
            animOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    /* no-op */
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    /* no-op */
                }

                override fun onAnimationEnd(p0: Animation?) {
                    visibility = View.GONE
                }
            })
            startAnimation(animOut)
        } else {
            // No exit animation provided, simply set visibility to View.GONE
            visibility = View.GONE
        }
    }
}

@BindingAdapter("app:errorText")
fun TextInputLayout.setErrorText(errorMessage: String?){
    Log.d("THIS", "Setting Error for text input layout to $errorMessage")
    isErrorEnabled = true
    error = errorMessage ?: ""
}