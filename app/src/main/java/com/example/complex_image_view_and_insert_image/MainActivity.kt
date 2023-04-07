package com.example.complex_image_view_and_insert_image

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(){

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button : Button = findViewById(R.id.btnfirebase)
        button.setOnClickListener {
            val intent = Intent(this, CompexImageView::class.java)
            startActivity(intent)
        }


        // for crashlatics
        val crashButton = Button(this)
            crashButton.text = "Test Crash"
            crashButton.setOnClickListener {
                throw RuntimeException("Test Crash") // Force a crash
            }

            addContentView(crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

    }
}