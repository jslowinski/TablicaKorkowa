package com.example.tablicakorkowa.helpers

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth


fun View.show(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

// Crossinline helps avoiding non-local control flow.

inline fun <T> LiveData<T>.subscribe(lifecycle: LifecycleOwner, crossinline onChanged: (T) -> Unit) {
    observe(lifecycle, Observer { it?.run(onChanged) })
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun getUid(): String {
    return FirebaseAuth.getInstance().uid.toString()
}


@SuppressLint("ClickableViewAccessibility")
fun buttonEffect(button: View) {
    button.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                v.invalidate()
            }
            MotionEvent.ACTION_UP -> {
                v.background.clearColorFilter()
                v.invalidate()
            }
        }
        false
    }
}

fun showProgress(swipeRefreshLayout: SwipeRefreshLayout){
    swipeRefreshLayout.isRefreshing = true
}

fun hideProgress(swipeRefreshLayout: SwipeRefreshLayout){
    swipeRefreshLayout.isRefreshing = false
}