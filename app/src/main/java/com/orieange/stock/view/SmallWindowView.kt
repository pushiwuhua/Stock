package com.orieange.stock.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.orieange.stock.R
import com.orieange.stock.utilities.PreferenceUtil
import com.orieange.stock.utilities.SP_STATUSBAR_HEIGHT


class SmallWindowView(context: Context) : WindowView(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this)
        val view: View = findViewById(R.id.linLayoutSmall)
        val statusBarHeight: Int = PreferenceUtil.getSingleton(context)!!.getInt(SP_STATUSBAR_HEIGHT, 0)
        if (statusBarHeight != 0) {
            val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
            layoutParams.height = statusBarHeight
            view.layoutParams = layoutParams
        }
        viewWidth = view.layoutParams.width
        viewHeight = view.layoutParams.height
    }
}
