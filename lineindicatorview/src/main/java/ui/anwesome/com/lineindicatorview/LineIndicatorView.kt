package ui.anwesome.com.lineindicatorview

import android.content.Context

/**
 * Created by anweshmishra on 16/12/17.
 */
import android.content.*
import android.view.*
import android.graphics.*
class LineIndicatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var scale = 0f
    override fun onDraw(canvas:Canvas) {
        paint.strokeCap = Paint.Cap.ROUND
        val w = canvas.width.toFloat()
        val h = canvas.height.toFloat()
        paint.strokeWidth = canvas.height.toFloat()/2
        canvas.drawLine(w/10,h/2,w/10+(4*w/5)*scale,h/2,paint)
    }
    fun update(scale:Float) {
        this.scale = scale
        invalidate()
    }
}