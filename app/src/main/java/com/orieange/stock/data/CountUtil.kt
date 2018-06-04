package com.orieange.stock.data

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Created by Administrator on 2017/7/18.
 */

object CountUtil {
    /**
     * 解析股票价格
     *
     * @param source 原始文本
     * @param code   股票代码
     * @return 股票模型
     */
        fun decodeStock(source: String, code: String): Stock? {
            val matcher = Pattern.compile("$code=\"\\S+\"", Pattern.CASE_INSENSITIVE).matcher(source)
            while (matcher.find()) {
                val content = matcher.group(0)
                val subContent = content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\""))
                if (!TextUtils.isEmpty(subContent)) {
                    val stock = Stock()
                    if (source.contains("s_sh")) {
                        stock.market = 0
                    } else {
                        stock.market = 1
                    }
                    val arr = subContent.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    stock.code = code
                    stock.name = arr[0]
                    stock.price = java.lang.Double.valueOf(arr[1])!!
                    stock.moneyChange = java.lang.Double.valueOf(arr[2])!!
                    stock.percentChange = java.lang.Double.valueOf(arr[3])!!
                    stock.exchangeCount = java.lang.Double.valueOf(arr[4])!!
                    stock.exchangeMoney = java.lang.Double.valueOf(arr[5])!!
                    return stock
                }
            }
            return null
        }
}
