package com.orieange.stock.utilities

import com.orieange.stock.data.CountUtil
import com.orieange.stock.data.Stock

import java.io.IOException
import java.util.Locale

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody


/**
 * 响应式编程的Http通信管理类
 * 1. 处理网络不通
 * 2. 处理网络超时
 *
 * @author wzz created at 2016/10/27 17:01
 */
class UTRxHttp private constructor() {
    var URL_STOCK_SH = "http://hq.sinajs.cn/list=s_sh%s"//沪市
    var URL_STOCK_SZ = "http://hq.sinajs.cn/list=s_sz%s"//深市

    internal var okHttpClient: OkHttpClient

    init {
        okHttpClient = OkHttpClient()
    }

    fun getStock(code: String): PublishSubject<Stock> {
        val subject = PublishSubject.create<Stock>()
        getString(String.format(Locale.getDefault(), URL_STOCK_SH, code))
                .subscribe { s ->
                    val stock = CountUtil.decodeStock(s, code)
                    if (stock != null) {
                        subject.onNext(stock)
                        subject.onComplete()
                    }
                }

        getString(String.format(Locale.getDefault(), URL_STOCK_SZ, code))
                .subscribe { s ->
                    val stock = CountUtil.decodeStock(s, code)
                    if (stock != null) {
                        subject.onNext(stock)
                        subject.onComplete()
                    }
                }
        return subject
    }

    fun getString(url: String): Observable<String> {

        return Observable.create { emitter ->
            // step 2： 创建一个请求，不指定请求方法时默认是GET。
            val requestBuilder = Request.Builder().url(url)
            //可以省略，默认是GET请求
            requestBuilder.method("GET", null)
            // step 3：创建 Call 对象
            val call = okHttpClient.newCall(requestBuilder.build())

            //step 4: 开始异步请求
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    emitter.onComplete()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    // TODO: 17-1-4 请求成功
                    //获得返回体
                    val body = response.body()
                    val content = body!!.string()
                    emitter.onNext(content)
                    emitter.onComplete()
                }
            })
        }
    }

    companion object {
        @Volatile
        private var defaultInstance: UTRxHttp? = null

        fun get(): UTRxHttp? {
            if (defaultInstance == null) {
                synchronized(UTRxHttp::class.java) {
                    if (defaultInstance == null) {
                        defaultInstance = UTRxHttp()
                    }
                }
            }
            return defaultInstance
        }
    }
}
