/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orieange.stock.data

import android.text.SpannableStringBuilder
import com.orieange.stock.utilities.UTRxHttp
import com.orieange.stock.utilities.runOnIoThread
import com.orieange.stock.utilities.simpleText
import com.orieange.stock.utilities.totalText
import java.util.concurrent.TimeUnit

/**
 * Repository module for handling data operations.
 */
class StockRepository private constructor(private val stockDao: StockDao) {

    fun getLvStocks() = stockDao.getLvStocks()

    fun getFaStocks() = stockDao.getFaStocks().throttleLast(1000, TimeUnit.MILLISECONDS)

    fun getFaStocksPrice() = stockDao.getFaStocks()
            .throttleLast(1000, TimeUnit.MILLISECONDS)
            .map { it -> Pair(it.simpleText(), it.totalText()) }

    fun getStocks() = stockDao.getStocks()

    fun addStock(sotck: Stock) {
        runOnIoThread { stockDao.insertOne(sotck) }
    }

    /**
     * 刷新股票数据
     */
    fun refreshStockData() {
        getStocks().forEach {
            UTRxHttp.get()!!.getStock(it.code!!)
                    .subscribe({ stock ->
                        addStock(stock)
                    })
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: StockRepository? = null

        fun getInstance(stockDao: StockDao) =
                instance ?: synchronized(this) {
                    instance ?: StockRepository(stockDao).also { instance = it }
                }
    }
}
