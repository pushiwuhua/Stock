package com.orieange.stock

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleService
import android.content.Intent
import android.os.IBinder
import android.text.SpannableStringBuilder
import android.util.Log
import com.orieange.stock.utilities.InjectorUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class FloatWindowService : LifecycleService() {
    private val disposable = CompositeDisposable()

    @SuppressLint("MissingSuperCall")
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("wzz", "FloatWindowService onStartCommand ")
        disposable.add(InjectorUtils.getPlantRepository(this).getFaStocksPrice()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> updateOverlayWindow(data.first, data.second) }))
//        InjectorUtils.getPlantRepository(this).getLvStocks().observe(this, Observer { list ->
//            run {
//                if (list != null) {
//                    updateOverlayWindow(list)
//                }
//            }
//        })


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Service被终止的同时也停止定时器继续运行
        OverlayWindowManager.getInstance()?.removeAllWindow(applicationContext)
        disposable.clear()
    }

    private fun updateOverlayWindow(dataSimple: SpannableStringBuilder, dataTotal: SpannableStringBuilder) {
        // 当前没有悬浮窗显示，则创建悬浮窗。
        if (!OverlayWindowManager.getInstance()!!.isWindowShowing) {
            OverlayWindowManager.getInstance()?.createWindow(applicationContext)
            OverlayWindowManager.getInstance()?.updateViewData(dataSimple, dataTotal)
        } else {
            OverlayWindowManager.getInstance()?.updateViewData(dataSimple, dataTotal)
        }// 当前有悬浮窗显示，则更新内存数据。
    }
}
