package ui.anwesome.com.kotlinbrokenimageview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.brokenimageview.BrokenImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BrokenImageView.create(this,BitmapFactory.decodeResource(resources,R.drawable.nature))
    }
}
