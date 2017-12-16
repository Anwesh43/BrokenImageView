package ui.anwesome.com.brokenimageview

/**
 * Created by anweshmishra on 16/12/17.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.util.Log
import android.view.*
import android.widget.ImageView
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet

class BrokenImageView(ctx:Context):ImageView(ctx) {
    var bitmap:Bitmap?=null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = BrokenImageRenderer(this)
    var listener:BrokenImageSetListener?=null
    override fun setImageBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }
    fun addBrokenImageListener(setListener: () -> Unit,resetListener: () -> Unit) {
        listener = BrokenImageSetListener(setListener, resetListener)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.startUpdating()
            }
        }
        return true
    }
    data class BrokenImageState(var dir:Float = 0f,var scale:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1-2*scale
            startcb()
        }
    }
    data class BrokenImage(var i:Int,var j:Int,var di:Int,var dj:Int):Comparable<BrokenImage> {
        fun draw(bitmap:Bitmap,canvas: Canvas,paint:Paint,size:Float,scale:Float) {
            val sx = i*size.toInt()
            val sy = j*size.toInt()
            val dx = di*size
            val dy = dj*size
            val x = dx+(sx-dx)*scale
            val y = dy+(sy-dy)*scale
            canvas.drawBitmap(bitmap,Rect(sx,sy,sx+size.toInt(),sy+size.toInt()),RectF(x,y,x+size,y+size),paint)
        }
        override fun compareTo(other:BrokenImage):Int = i+j - (other.i+other.j)
        override fun equals(other: Any?): Boolean {
            val brokenImage = other as BrokenImage
            return other.di == di && dj == other.dj
        }
        override fun hashCode():Int = (di+dj)*(di-dj)
    }
    data class BrokenImageContainer(var bitmap:Bitmap) {
        val brokenImages:ConcurrentLinkedQueue<BrokenImage> = ConcurrentLinkedQueue()

        var w = bitmap.width
        var h = bitmap.height
        var size = Math.min(w,h)/2
        val state = BrokenImageState()
        init {
            var brokenIndexMap:HashSet<String> = HashSet()
            while(size >= 1) {
                if(w%size == 0 && h%size == 0) {
                    break
                }
                size--
            }
            val maxI = w/size
            val maxJ = h/size

            for(j in 0..maxJ-1) {
                for(i in 0..maxI -1) {
                    val random = Random()
                    var di = random.nextInt(maxI)
                    var dj = random.nextInt(maxJ)
                    var brokenImage = BrokenImage(i,j,di,dj)
                    while(brokenIndexMap.contains(String.format("%d,%d",di,dj))) {
                        di = random.nextInt(maxI)
                        dj = random.nextInt(maxJ)
                        brokenImage = BrokenImage(i,j,di,dj)
                    }
                    brokenIndexMap.add(String.format("%d,%d",di,dj))
                    brokenImages.add(brokenImage)
                }
            }
            Log.d("brokenImages",""+brokenImages.size)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            brokenImages.forEach { brokenImage ->
                brokenImage.draw(bitmap,canvas,paint,size.toFloat(),state.scale)
            }
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class BrokenImageAnimator(var container:BrokenImageContainer,var view:BrokenImageView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update{ scale ->
                    animated = false
                    when(scale) {
                        0f -> view.listener?.resetListener?.invoke()
                        1f -> view.listener?.setListener?.invoke()
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun startUpdating() {
            if(!animated) {
                container.startUpdating{
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class BrokenImageRenderer(var view:BrokenImageView,var time:Int = 0) {
        var animator:BrokenImageAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                animator = BrokenImageAnimator(BrokenImageContainer(view.bitmap?:Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)),view)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun startUpdating() {
            animator?.startUpdating()
        }
    }
    companion object {
        fun create(activiy:Activity,bitmap: Bitmap):BrokenImageView {
            var view = BrokenImageView(activiy)
            view.setImageBitmap(bitmap)
            activiy.addContentView(view,ViewGroup.LayoutParams(bitmap.width,bitmap.height))
            return view
        }
    }
    data class BrokenImageSetListener(var setListener:()->Unit,var resetListener:()->Unit)
}
fun Int.toXRect(size:Float):Float = size*this
fun Int.toYRect(size:Float):Float = size*this