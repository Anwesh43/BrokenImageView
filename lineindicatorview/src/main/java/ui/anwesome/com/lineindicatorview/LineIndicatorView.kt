package ui.anwesome.com.lineindicatorview

import android.app.Activity
import android.content.Context

/**
 * Created by anweshmishra on 16/12/17.
 */
import android.content.*
import android.view.*
import android.graphics.*
import android.hardware.display.DisplayManager

class LineIndicatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var scale = 0f
    override fun onDraw(canvas:Canvas) {
        paint.strokeCap = Paint.Cap.ROUND
        val w = canvas.width.toFloat()
        val h = canvas.height.toFloat()
        paint.strokeWidth = canvas.height.toFloat()/2
        paint.color = Color.parseColor("#4A148C")
        if(scale > 0) {
            canvas.drawLine(w / 10, h / 2, w / 10 + (4 * w / 5) * scale, h / 2, paint)
        }
    }
    fun update(scale:Float) {
        this.scale = scale
        invalidate()
    }
    companion object {
        fun create(activity:Activity):LineIndicatorView {
            val view = LineIndicatorView(activity)
            val displayManager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = displayManager.getDisplay(0)
            val size = Point()
            display?.getSize(size)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.y/20))
            view.y = (9*size.y/10-size.y/20).toFloat()
            return view
        }
    }
}