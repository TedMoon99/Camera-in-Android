package com.example.camerapractice.analysis

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.camerapractice.LumaListener
import java.nio.ByteBuffer

class LuminosityAnalyzer(private val listener: LumaListener): ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)
        image.close()
    }

    private fun ByteBuffer.toByteArray(): ByteArray{
        rewind() // Buffer를 0으로 되감기
        val data = ByteArray(remaining())
        get(data) // Buffer를 Byte Array로 복사한다
        return data
    }

}