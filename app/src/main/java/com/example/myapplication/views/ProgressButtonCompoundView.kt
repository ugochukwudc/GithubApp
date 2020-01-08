package com.example.myapplication.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ViewLoadingButtonBinding
import com.example.myapplication.models.LoadingViewModel

class ProgressButtonCompoundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {


    private val mBinding = DataBindingUtil.inflate<ViewLoadingButtonBinding>(
        LayoutInflater.from(context),
        R.layout.view_loading_button, this, true
    )

    fun setText(text: CharSequence) {
        mBinding.button.text = text
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mBinding.button.isEnabled = enabled
    }

    fun setViewModel(viewModel: LoadingViewModel) {
        mBinding.viewModel = viewModel

        viewModel.isLoading.observe(this.context as LifecycleOwner, Observer {loading ->
            if (loading){
                mBinding.button.visibility = View.GONE
                mBinding.progressBar.visibility = View.VISIBLE
                mBinding.progressBar.show()
            } else {
                mBinding.button.visibility = View.VISIBLE
                mBinding.progressBar.hide()
                mBinding.progressBar.visibility = View.GONE
            }
        })
    }

    fun setOnClick(onClick :() -> Unit){
        mBinding.button.setOnClickListener {
            onClick()
        }
    }
}