package com.example.qrgenerator

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qrgenerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val writeExternalStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val camera = android.Manifest.permission.CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (
            ContextCompat.checkSelfPermission(applicationContext, writeExternalStorage)
            != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(applicationContext, camera)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(writeExternalStorage, camera), 0)
        }

        binding.apply {
            buttonGenerate.setOnClickListener {
                try {
                    val text = editTextQR.text.toString().trim()
                    val qrgEncoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 400)
                    val bitmap = qrgEncoder.bitmap
                    imageViewQR.setImageBitmap(bitmap)
                    QRGSaver().save(
                        getExternalFilesDir(null)?.absolutePath,
                        "qrcode",
                        bitmap,
                        QRGContents.ImageType.IMAGE_JPEG
                    )
                } catch (e: Exception) {}
            }

            buttonScan.setOnClickListener {
                startActivity(Intent(applicationContext, SimpleScannerActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editTextQR.setText(Bridge.data)
    }
}