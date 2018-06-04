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

package com.orieange.stock

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.orieange.stock.adapters.StockAdapter
import com.orieange.stock.utilities.InjectorUtils
import com.orieange.stock.utilities.OVERLAY_PERMISSION_REQ_CODE
import com.orieange.stock.viewmodels.StockViewModel

//import com.google.samples.apps.sunflower.adapters.GardenPlantingAdapter
//import com.google.samples.apps.sunflower.utilities.InjectorUtils
//import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel

class StockFragment : Fragment() {
    private lateinit var viewModel: StockViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stock, container, false)
        val adapter = StockAdapter(view.context)
        view.findViewById<RecyclerView>(R.id.stock_list).adapter = adapter
        subscribeUi(adapter)
        view.findViewById<Button>(R.id.button_add).setOnClickListener(
                {
                    Log.i("wzz", "click")
                    viewModel.addStockCode(view.findViewById<EditText>(R.id.editText_code).text.toString());
                })

        view.findViewById<Switch>(R.id.switch_open).setOnCheckedChangeListener { btn, isCheck ->
            if (isCheck) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity?.applicationContext)) {
                    Log.i("wzz", "StockFragment onCheckedChanged 没有权限")
                    Toast.makeText(context, resources.getString(R.string.permission_alert), Toast.LENGTH_LONG).show()
                    val permissionIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity?.packageName))
                    startActivityForResult(permissionIntent, OVERLAY_PERMISSION_REQ_CODE)
                } else {
                    val intent = Intent(context, FloatWindowService::class.java)
                    context?.startService(intent)
                }
            } else {
                val intent = Intent(context, FloatWindowService::class.java)
                context?.stopService(intent)
            }
        }
        val intent = Intent(context, DataService::class.java)
        context?.startService(intent)
        return view
    }

    private fun subscribeUi(adapter: StockAdapter) {
        viewModel = ViewModelProviders.of(this).get(StockViewModel::class.java)
        viewModel.setRepository(InjectorUtils.getPlantRepository(context!!))
        viewModel.stocks.observe(this, Observer { stocks ->
            if (stocks != null && stocks.isNotEmpty()) {
                adapter.values = stocks
            }
        })
    }
}
