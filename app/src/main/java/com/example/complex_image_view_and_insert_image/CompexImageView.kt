package com.example.complex_image_view_and_insert_image

import android.R.attr
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.MediaStore
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.complex_image_view_and_insert_image.databinding.ActivityCompexImageViewBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class CompexImageView : AppCompatActivity(){


    private lateinit var binding:ActivityCompexImageViewBinding
    var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompexImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

     binding.chooseimg.setOnClickListener {

         val intent = Intent()
         intent.type = "image/*"
         intent.action = Intent.ACTION_GET_CONTENT

         startActivityForResult(
             Intent.createChooser(
                 intent,"Pick your image to upload"
             ),22
         )

      }

        binding.uploadimg.setOnClickListener {

            uploadimage()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null){
            fileUri = data.data

            try {
                val bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,fileUri)
                binding.img.setImageBitmap(bitmap)
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    private fun uploadimage() {

        if (fileUri != null){

            val progressDialog = ProgressDialog(this)

            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Upload Your Image..")
            progressDialog.show()


            val ref : StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())

            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "failed to Upload Image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*class CircularImageView : androidx.appcompat.widget.AppCompatImageView {
        constructor(context: Context?) : super(context!!) {}
        constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
        constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
            context!!,
            attrs,
            defStyle
        ) {
        }

        override fun onDraw(canvas: Canvas) {
            val drawable = drawable ?: return
            if (width == 0 || height == 0) {
                return
            }
            val b = (drawable as BitmapDrawable).bitmap
            val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
            val w = width
            val h = height
            val roundBitmap = getRoundBitmap(bitmap, w)
            canvas.drawBitmap(roundBitmap, 0F, 0F, null)
        }

        companion object {
            fun getRoundBitmap(bmp: Bitmap, radius: Int): Bitmap {
                val sBmp: Bitmap
                sBmp = if (bmp.width != radius || bmp.height != radius) {
                    val smallest = Math.min(bmp.width, bmp.height).toFloat()
                    val factor = smallest / radius
                    Bitmap.createScaledBitmap(
                        bmp,
                        (bmp.width / factor).toInt(),
                        (bmp.height / factor).toInt(),
                        false
                    )
                } else {
                    bmp
                }
                val output = Bitmap.createBitmap(
                    radius, radius,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(output)
                val color = "#BAB399"
                val paint = Paint()
                val rect = Rect(0, 0, radius, radius)
                paint.isAntiAlias = true
                paint.isFilterBitmap = true
                paint.isDither = true
                canvas.drawARGB(0, 0, 0, 0)
                paint.color = Color.parseColor(color)
                canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f, radius / 2 + 0.1f, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(sBmp, rect, rect, paint)
                return output
            }
        }
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap
                .height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }*/
}