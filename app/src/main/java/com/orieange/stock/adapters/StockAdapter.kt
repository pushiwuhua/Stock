package com.orieange.stock.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.orieange.stock.R
import com.orieange.stock.data.Stock
import com.orieange.stock.utilities.timeHHmmss
import com.orieange.stock.utilities.toColor

/**
 * Created by Sasha Grey on 5/24/2016.
 */
class StockAdapter(private val mContext: Context) : RecyclerView.Adapter<StockAdapter.ViewHolderItem>() {

    var values: List<Stock> = ArrayList(0)
        set(items) {
            field = items
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sheet_stock, parent, false)
        return ViewHolderItem(view)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
//        this.market.set(String.valueOf(stock.market));
//        this.code.set(stock.code);
//        this.name.set(stock.name);
//        this.price.set(String.valueOf(stock.price));
//        this.moneyChange.set(String.valueOf(stock.moneyChange));
//        if (stock.moneyChange > 0) {
//            this.moneyChangeColor.set(ContextCompat.getColor(context, android.R.color.holo_red_dark));
//        } else if (stock.moneyChange == 0) {
//            this.moneyChangeColor.set(ContextCompat.getColor(context, android.R.color.darker_gray));
//        } else if (stock.moneyChange < 0) {
//            this.moneyChangeColor.set(ContextCompat.getColor(context, android.R.color.holo_green_dark));
//        }
//        this.percentChange.set(String.valueOf(stock.percentChange));
//        this.exchangeCount.set(String.valueOf(stock.exchangeCount));
//        this.exchangeMoney.set(String.valueOf(stock.exchangeMoney));
//        this.updateTime.set("" + System.currentTimeMillis());

        val stock: Stock = values[position]
        Log.i("wzz", "haha:" + stock)
        holder.tvCode.text = stock.code
        holder.tvName.text = stock.name
        holder.tvPrice.text = stock.price.toString()
        holder.tvMonneyChange.text = stock.moneyChange.toString()
        holder.tvPercentChange.setTextColor(stock.moneyChange.toColor(mContext))
        holder.tvPercentChange.text = stock.percentChange.toString()
        holder.tvPercentChange.setTextColor(stock.moneyChange.toColor(mContext))
        holder.tvUpdateTime.text = System.currentTimeMillis().timeHHmmss()
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolderItem(view: View) : RecyclerView.ViewHolder(view) {
        val tvCode: TextView = view.findViewById(R.id.tvStockCode)
        val tvName: TextView = view.findViewById(R.id.tvStockName)
        val tvPrice: TextView = view.findViewById(R.id.tvStockPrice)
        val tvMonneyChange: TextView = view.findViewById(R.id.tvMoneyChange)
        val tvPercentChange: TextView = view.findViewById(R.id.tvPercentChange)
        val tvUpdateTime: TextView = view.findViewById(R.id.tvUpdateTime)
    }
}
