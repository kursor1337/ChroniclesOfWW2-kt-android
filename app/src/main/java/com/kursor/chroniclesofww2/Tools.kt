package com.kursor.chroniclesofww2

import android.content.res.Resources

object Tools {

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

}