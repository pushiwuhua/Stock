package com.orieange.stock

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.TextView
import com.orieange.stock.data.Stock
import com.orieange.stock.utilities.*
import com.orieange.stock.view.SmallWindowView
import com.orieange.stock.view.WindowView


class OverlayWindowManager {
    companion object {
        private var instance: OverlayWindowManager? = null
        fun getInstance(): OverlayWindowManager? {
            if (instance == null) {
                instance = OverlayWindowManager()
            }
            return instance
        }
    }

    private var mWindowManager: WindowManager? = null
    private var mWindowView: WindowView? = null
    private var windowParams: LayoutParams? = null
    private var windowType: Int = 0//窗口类型
    private var tvShow: TextView? = null//显示控件

    val isWindowShowing: Boolean
        get() = mWindowView != null

    fun createWindow(context: Context) {
        createWindow(context, SMALL_WINDOW_TYPE)
    }

    private fun createWindow(context: Context, type: Int) {
        val windowManager = getWindowManager(context)
        if (windowParams == null) {
            windowParams = getWindowParams(context)
        }
        windowType = type
        if (mWindowView == null) {
            mWindowView = SmallWindowView(context)
            val background = ContextCompat.getDrawable(context, R.drawable.float_bg)
            ViewCompat.setBackground(mWindowView, background)//设置背景
            windowManager?.addView(mWindowView, windowParams)
        }
        tvShow = mWindowView!!.findViewById(R.id.tvSum)

        if (windowType == SMALL_WINDOW_TYPE) {
            setOnGeatureListener(windowManager!!, context, mWindowView!!, BIG_WINDOW_TYPE)
        } else {
            setOnGeatureListener(windowManager!!, context, mWindowView!!, SMALL_WINDOW_TYPE)
        }
        updateViewData()
    }

    private fun getWindowParams(context: Context): LayoutParams {
        val windowManager = getWindowManager(context)
        val sizePoint = Point()
        windowManager!!.defaultDisplay.getSize(sizePoint)
        val screenWidth = sizePoint.x
        val screenHeight = sizePoint.y
        val windowParams = WindowManager.LayoutParams()
        windowParams.type = LayoutParams.TYPE_SYSTEM_ALERT//wzz实测,官方sdk参数https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_APPLICATION_OVERLAY
        windowParams.format = PixelFormat.RGBA_8888
        windowParams.flags = LayoutParams.FLAG_LAYOUT_IN_SCREEN or LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL
        windowParams.gravity = Gravity.START or Gravity.TOP
        windowParams.width = LayoutParams.WRAP_CONTENT
        windowParams.height = LayoutParams.WRAP_CONTENT
        var x = PreferenceUtil.getSingleton(context)!!.getInt(SP_X, -1)
        var y = PreferenceUtil.getSingleton(context)!!.getInt(SP_Y, -1)
        if (x == -1 || y == -1) {
            x = screenWidth
            y = screenHeight / 2
        }
        windowParams.x = x
        windowParams.y = y
        return windowParams
    }

    private fun setOnGeatureListener(windowManager: WindowManager, context: Context, windowView: WindowView, type: Int) {
        val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.OnGestureListener {
            internal var paramX: Int = 0
            internal var paramY: Int = 0

            override fun onDown(motionEvent: MotionEvent): Boolean {
                paramX = windowParams!!.x
                paramY = windowParams!!.y
                return false
            }

            override fun onShowPress(motionEvent: MotionEvent) {

            }

            override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
                createWindow(context, type)
                return false
            }

            override fun onScroll(motionEvent: MotionEvent, motionEvent1: MotionEvent, dx: Float, dy: Float): Boolean {
                windowParams!!.x = (paramX + (motionEvent1.rawX - motionEvent.rawX)).toInt()
                windowParams!!.y = (paramY + (motionEvent1.rawY - motionEvent.rawY)).toInt()
                // 更新悬浮窗位置
                windowManager.updateViewLayout(windowView, windowParams)
                return false
            }

            override fun onLongPress(motionEvent: MotionEvent) {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            override fun onFling(motionEvent: MotionEvent, motionEvent1: MotionEvent, v: Float, v1: Float): Boolean {
                return false
            }
        })
        windowView.setOnTouchListener(OnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            false
        })
    }

    private fun removeWindow(context: Context, windowView: WindowView?) {
        if (windowView != null) {
            val windowManager = getWindowManager(context)
            windowManager?.removeView(windowView)
        }
    }

    fun removeAllWindow(context: Context) {
        removeWindow(context, mWindowView)
        mWindowView = null
    }

    private var dataOfSimle: SpannableStringBuilder? = null
    private var dataOfTotal: SpannableStringBuilder? = null
    fun updateViewData(dataSimple: SpannableStringBuilder, dataTotal: SpannableStringBuilder) {
        if (mWindowView != null) {
            this.dataOfSimle = dataSimple
            this.dataOfTotal = dataTotal
            Log.i("wzz", "OverlayWindowManager updateViewData $dataSimple")
            if (windowType == BIG_WINDOW_TYPE) {
                tvShow?.text = dataOfTotal
            } else {
                tvShow?.text = dataOfSimle
            }
        }
    }

    fun updateViewData() {
        if (mWindowView != null) {
            if (windowType == BIG_WINDOW_TYPE) {
                tvShow?.text = dataOfTotal
            } else {
                tvShow?.text = dataOfSimle
            }
        }
    }

    private fun getWindowManager(context: Context): WindowManager? {
        if (mWindowManager == null) {
            mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return mWindowManager
    }
}
