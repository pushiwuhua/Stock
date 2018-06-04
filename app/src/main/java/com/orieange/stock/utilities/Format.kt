package com.orieange.stock.utilities

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.orieange.stock.data.Stock
import java.text.SimpleDateFormat
import java.util.*

fun String.timeHHmmss(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(this)
}

fun Long.timeHHmmss(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(this)
}

fun Double.toColor(context: Context): Int {
    if (this > 0) {
        return ContextCompat.getColor(context, android.R.color.holo_red_dark)
    } else if (this == 0.0) {
        return ContextCompat.getColor(context, android.R.color.darker_gray)
    } else {
        return ContextCompat.getColor(context, android.R.color.holo_green_dark)
    }
}

fun List<Stock>.totalText(): SpannableStringBuilder {
    val spannableString = SpannableStringBuilder()
    if (this != null) {
        for (i in indices) {
            val stock = get(i)
            if (stock != null && !TextUtils.isEmpty(stock!!.price.toString())) {
                val start = spannableString.length
                spannableString.append(stock.name + " " + stock.price + " " + stock.percentChange)
                if (i < size - 1) {
                    spannableString.append("\n")
                }
                val colorSpan: ForegroundColorSpan
                if (stock.moneyChange > 0) {
                    colorSpan = ForegroundColorSpan(Color.parseColor("#FF0000"))
                } else if (stock.moneyChange == 0.0) {
                    colorSpan = ForegroundColorSpan(Color.parseColor("#FFFFFF"))
                } else {
                    colorSpan = ForegroundColorSpan(Color.parseColor("#00FF00"))
                }
                spannableString.setSpan(colorSpan, start, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
    return spannableString
}

fun List<Stock>.simpleText(): SpannableStringBuilder {
    val spannableString = SpannableStringBuilder()
    if (this != null) {
        for (i in indices) {
            val stock = get(i)
            if (stock != null && !TextUtils.isEmpty(stock.price.toString())) {
                val start = spannableString.length
                spannableString.append(stock.price.toString())
                if (i < size - 1) {
                    spannableString.append("\n")
                }
                val colorSpan: ForegroundColorSpan
                if (stock.moneyChange > 0) {
                    colorSpan = ForegroundColorSpan(Color.parseColor("#FF0000"))
                } else if (stock.moneyChange == 0.0) {
                    colorSpan = ForegroundColorSpan(Color.parseColor("#FFFFFF"))
                } else {
                    colorSpan = ForegroundColorSpan(Color.parseColor("#00FF00"))
                }
                spannableString.setSpan(colorSpan, start, spannableString.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            }
        }
    }
    return spannableString
}