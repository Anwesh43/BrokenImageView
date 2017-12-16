package ui.anwesome.com.kotlinbrokenimageview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ui.anwesome.com.brokenimageview.BrokenImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var view = BrokenImageView.create(this,BitmapFactory.decodeResource(resources,R.drawable.nature))
        view.addBrokenImageListener({Toast.makeText(this,"image is set",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"image is disoriented",Toast.LENGTH_LONG).show()})
    }
}
