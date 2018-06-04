package com.orieange.stock.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.text.TextUtils

/**
 * Stock 股票模型
 * wzz created at 2017/7/21 17:31
 */
@Entity(tableName = "stock")
data class Stock(
        var market: Int = 0, //0:沪市 1:深市
        @PrimaryKey
        var code: String = "",
        var name: String = "",
        var price: Double = 0.toDouble(),//价格
        var moneyChange: Double = 0.toDouble(),
        var percentChange: Double = 0.toDouble(),
        var exchangeCount: Double = 0.toDouble(),//成交数, 单位:手
        var exchangeMoney: Double = 0.toDouble()//成交额, 单位:万
) {

    val isValid: Boolean
        get() = !TextUtils.isEmpty(name) && !TextUtils.isEmpty(code) && code!!.length == 6

    override fun toString(): String {
        return "Stock{" +
                "market=" + market +
                ", code=" + code +
                ", name='" + name + '\''.toString() +
                ", price=" + price +
                ", moneyChange=" + moneyChange +
                ", percentChange=" + percentChange +
                ", exchangeCount=" + exchangeCount +
                ", exchangeMoney=" + exchangeMoney +
                '}'.toString()
    }
}
