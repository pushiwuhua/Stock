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

package com.orieange.stock.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.orieange.stock.data.Stock
import com.orieange.stock.data.StockRepository
import com.orieange.stock.utilities.UTRxHttp

class StockViewModel : ViewModel() {
    lateinit var stocks: LiveData<List<Stock>>
    lateinit var stockRepository: StockRepository

    fun setRepository(resp: StockRepository) {
        this.stockRepository = resp;
        this.stocks = stockRepository.getLvStocks()
    }

    init {

    }

    /**
     * get请求
     *
     * @param code 股票代码
     */
    fun addStockCode(code: String) {
        UTRxHttp.get()!!.getStock(code)
                .subscribe({ stock ->
                    stockRepository.addStock(stock)
                })
    }

}
