package com.example.tablicakorkowa.helpers

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import fr.ganfra.materialspinner.MaterialSpinner


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

fun validateTextField(field: TextInputEditText, layout: TextInputLayout) : Boolean{
    return if (field.text.toString().isEmpty()){
        layout.error = "Pole nie może być puste"
        true
    } else {
        layout.error = null
        false
    }
}

fun validateTextField2(field: MaterialAutoCompleteTextView, layout: TextInputLayout) : Boolean{
    return if (field.text.toString().isEmpty()){
        layout.error = "Pole nie może być puste"
        true
    } else {
        layout.error = null
        false
    }
}

fun validateSpinnerField(field: MaterialSpinner): Boolean{
    return if (field.selectedItemId.toInt() == 0){
        field.error = "Pole nie może być uste"
        true
    } else {
        field.error = null
        false
    }
}