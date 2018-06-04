package com.orieange.stock.data

import android.content.Context
import android.support.v4.content.ContextCompat

/**
 * Created by Administrator on 2017/7/18.
 */

class TextItem {
    var text: String = ""
    var textColor: Int = 0

    constructor(text: String, textColor: Int) {
        this.text = text
        this.textColor = textColor
    }

    constructor(context: Context, text: String) : this(text, ContextCompat.getColor(context, android.R.color.black))


    override fun toString(): String {
        return "TextItem{" +
                "text='" + text + '\''.toString() +
                ", textColor=" + textColor +
                '}'.toString()
    }
}
