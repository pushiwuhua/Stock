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

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface StockDao {
    @Query("SELECT * FROM stock ORDER BY code")
    fun getLvStocks(): LiveData<List<Stock>>

    @Query("SELECT * FROM stock ORDER BY code")
    fun getFaStocks(): Flowable<List<Stock>>

    @Query("SELECT * FROM stock ORDER BY code")
    fun getStocks(): List<Stock>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stocks: List<Stock>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(stock: Stock)
}
