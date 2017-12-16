package ui.anwesome.com.kotlinbrokenimageview

import android.app.Activity
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ui.anwesome.com.brokenimageview.BrokenImageView
import ui.anwesome.com.lineindicatorview.LineIndicatorView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var view = BrokenImageView.create(this,BitmapFactory.decodeResource(resources,R.drawable.nature))
        val lineIndicatorView = LineIndicatorView.create(this)
        view.addBrokenImageListener({Toast.makeText(this,"image is set",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"image is disoriented",Toast.LENGTH_LONG).show()})
        view.addUpdateListener{ scale ->
            lineIndicatorView.update(scale)
        }
        fullScreen()
        hideActionBar()
    }
}
fun AppCompatActivity.hideActionBar() {
    supportActionBar?.hide()
}
fun Activity.fullScreen() {
    this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}
