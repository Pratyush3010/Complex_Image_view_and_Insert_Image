package com.example.complex_image_view_and_insert_image

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.complex_image_view_and_insert_image.databinding.ActivityImageInsertSqliteBinding
import com.example.complex_image_view_and_insert_image.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.File

class image_insert_SQLite : AppCompatActivity() {

    var fileUri: Uri? = null

        var desFile: File? = null
        var myFile: File? = null
        var db: SQLiteDatabase? = null
        protected lateinit var xx: ByteArray
        private val stream = ByteArrayOutputStream()

        private lateinit var binding:ActivityImageInsertSqliteBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityImageInsertSqliteBinding.inflate(layoutInflater)
            setContentView(binding.root)
            createDatabase()


            binding.upload.setOnClickListener {
                insertImg()
            }
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null){
            fileUri = data.data

            try {
                val bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,fileUri)
                binding.pic.setImageBitmap(bitmap)
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
    }
        fun createDb(view: View) {
            createDatabase()
        }
        fun createDatabase(){
            val folder = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+"/Database")
            if (!folder.exists()) {
                folder.mkdir()
            } else {
                //Toast.makeText(this, folder.absolutePath.toString(), Toast.LENGTH_LONG).show()
            }
            myFile = File(folder,"Test.db")
            db = SQLiteDatabase.openOrCreateDatabase(myFile!!.absolutePath,null,null)
            CreateTable()
            @SuppressLint("Recycle")
            var cursor =
                db!!.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ", null)
            if (cursor.getCount() != 0)
            {
                Toast.makeText(this,"Conncted To DataBase)", Toast.LENGTH_LONG).show()

            }
        }

        fun CreateTable(){
            var create_table : String
            create_table =
                "CREATE TABLE IF NOT EXISTS item (itmid INTEGER PRIMARY KEY AUTOINCREMENT, classid INTEGER, itmname TEXT," +
                        "rate REAL,itmunit TEXT, itmimg BLOB) "
            db!!.execSQL(create_table)
            create_table =
                "CREATE TABLE IF NOT EXISTS category (catid INTEGER PRIMARY KEY AUTOINCREMENT, catname TEXT, classid INTEGER)"
            db!!.execSQL(create_table)
            create_table =
                "CREATE TABLE IF NOT EXISTS ledgers (ldid INTEGER PRIMARY KEY AUTOINCREMENT, classid INTEGER, ldname TEXT, ldaddrs TEXT, ldstate TEXT," +
                        " mobileno TEXT opbl REAL)"
            db!!.execSQL(create_table)

        }
        fun InsertData(view: View){
            val image = xx
            var itmName : String = binding.txtItmname.text.toString()
            var itmRate : Double = binding.txtRate.text.toString().toDouble()
            var itmUnit : String = binding.txtUnit.text.toString()
            val values = ContentValues()
            values.put("itmname",itmName)
            values.put("rate", itmRate)
            values.put("itmunit", itmUnit)
            values.put("itmimg", image)
            db!!.insert("item",null,values)
            binding.txtItmname.setText("")
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()

        }

        fun UpdateData(view: View){
            val itemid = binding.edtid.text.toString().toInt()
            val image = xx
            var itmName : String = binding.txtItmname.text.toString()
            var itmRate : Double = binding.txtRate.text.toString().toDouble()
            var itmUnit : String = binding.txtUnit.text.toString()
            val values = ContentValues()
            values.put("itmname",itmName)
            values.put("rate", itmRate)
            values.put("itmunit", itmUnit)
            values.put("itmimg", image)
            values.put("itmid",itemid)
            db!!.update("item", values, "itmid", null)
            binding.txtItmname.setText("")
            Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()

        }
        fun DeleteData(view: View){
            val sqlcmd =
                "DELETE FROM ITEM  WHERE  (itmid = 2 )"
            db!!.execSQL(sqlcmd)
            binding.txtItmname.setText("")
            Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()

        }

        fun showData(view: View) {
            val itmid = binding.edtid.text.toString().toInt()
            val cursor = db!!.rawQuery(
                "SELECT itmid, itmname, rate, itmunit,itmimg FROM item WHERE  (itmid = $itmid )",
                null
            )
            if (cursor.count != 0) {
                cursor.moveToFirst()
                binding.txtItmname .setText(cursor.getString(1).toString())
                binding.txtRate .setText(cursor.getString(2).toString())
                binding.txtUnit .setText(cursor.getString(3).toString())
                if(cursor.getBlob((4)) != null){
                    val x = cursor.getBlob(4)
                    xx = x
                    val bmp = BitmapFactory.decodeByteArray(x,0,x.size)
                    binding.pic.setImageBitmap(bmp)
                }

            }



        }

        fun insertImg() {
            var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.setType("image/*")

            startActivityForResult(
                Intent.createChooser(
                    myfileintent,"Pick your image to upload"
                ),22
            )

        }





    }
