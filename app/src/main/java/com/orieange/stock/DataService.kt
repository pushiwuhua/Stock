package com.orieange.stock

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleService
import android.content.Intent
import android.os.IBinder
import com.orieange.stock.utilities.InjectorUtils
import com.orieange.stock.utilities.UTRxHttp
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DataService : LifecycleService() {
    private var mDisposableLooper: Disposable? = null

    @SuppressLint("MissingSuperCall")
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (mDisposableLooper == null) {
            mDisposableLooper = Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.io())
                    .subscribe {
                        InjectorUtils.getPlantRepository(applicationContext).refreshStockData()
                    }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Service被终止的同时也停止定时器继续运行
        mDisposableLooper!!.dispose()
        mDisposableLooper = null
    }
}
