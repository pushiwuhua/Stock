package com.orieange.stock.data

import android.content.Context

/**
 * Created by Administrator on 2017/7/18.
 */

class ObservableOneItem {
    var item1: TextItem? = null
    var item2: TextItem? = null
    var item3: TextItem? = null

    override fun toString(): String {
        return "OneItem{" +
                "item1=" + item1 +
                ", item2=" + item2 +
                ", item3=" + item3 +
                '}'.toString()
    }

    companion object {
        fun buildWith(context: Context, t1: String, t2: String, t3: String): ObservableOneItem {
            val oneItem = ObservableOneItem()
            oneItem.item1 = TextItem(context, t1)
            oneItem.item2 = TextItem(context, t2)
            oneItem.item3 = TextItem(context, t3)
            return oneItem
        }
    }
}
