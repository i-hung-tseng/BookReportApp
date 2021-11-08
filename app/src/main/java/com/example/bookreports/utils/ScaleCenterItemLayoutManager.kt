package com.example.bookreports.utils

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class ScaleCenterItemLayoutManager: LinearLayoutManager {

    constructor(context: Context?,orientation:Int,reverselayout:Boolean):super(context,orientation,reverselayout)


    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        lp?.width = width/5
        return true
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        Log.d("Testing","enter onLayoutCompleted")
        scaleMiddleItem()
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        Log.d("Testing","enter scrollHorizontallBy dx:$dx,recyclerview:$recycler,state:$state")
        return if (orientation == HORIZONTAL){
            val scrolled =  super.scrollHorizontallyBy(dx, recycler, state)
            scaleMiddleItem()
            scrolled
            Log.d("Testing","scroll$scrolled")
        }else{
            0
        }

    }

    private fun scaleMiddleItem() {
        val mid = width/2
        val d1 = 0.9f * mid
        for(i in 0 until childCount){
            val child = getChildAt(i)
            val childMid = child?.let {  (getDecoratedRight(it) + getDecoratedLeft(it))/2f}
            val d = childMid?.let {  d1.coerceAtMost(abs(mid - it))}
            val scale = d?.let {1 - 0.15f * it/d1  }
            if (scale != null) {
                child.scaleX = scale
                child.scaleY = scale
            }
        }
    }



}