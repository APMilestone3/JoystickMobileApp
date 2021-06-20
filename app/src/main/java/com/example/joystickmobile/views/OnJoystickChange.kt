package com.example.joystickmobile.views

import androidx.lifecycle.MutableLiveData

fun interface OnJoystickChange {
    fun change(aileron: Float, elevator: Float)
}