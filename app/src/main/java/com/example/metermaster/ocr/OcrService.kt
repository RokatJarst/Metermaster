package com.example.metermaster.ocr

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern

data class OcrResult(val numericReading: Double?, val detectedLabel: String?)

object OcrService {
    suspend fun analyzeBitmap(bitmap: Bitmap): OcrResult {
        val image = InputImage.fromBitmap(bitmap, 0)
        // barcode
        try {
            val scanner = BarcodeScanning.getClient()
            val barcodes = scanner.process(image).await()
            if (barcodes.isNotEmpty()) {
                for (b in barcodes) {
                    val raw = b.rawValue
                    if (!raw.isNullOrBlank()) {
                        val label = parseLabel(raw)
                        val num = parseNumber(raw)
                        if (label != null || num != null) return OcrResult(num, label)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("OcrService", "Barcode failed: $e")
        }
        // text
        try {
            val recognizer = TextRecognition.getClient()
            val result = recognizer.process(image).await()
            val text = result.text
            val num = parseNumber(text)
            val label = parseLabel(text)
            return OcrResult(num, label)
        } catch (e: Exception) {
            Log.w("OcrService", "Text recog failed: $e")
        }
        return OcrResult(null, null)
    }

    private fun parseNumber(text: String): Double? {
        val pattern = Pattern.compile("(\\d{1,3}(?:[ .,]\\d{3})*(?:[.,]\\d+)?|\\d+([.,]\\d+)?)")
        val matcher = pattern.matcher(text)
        var best: String? = null
        while (matcher.find()) {
            val candidate = matcher.group().replace(" ", "").replace(',', '.')
            if (best == null || candidate.length > best.length) best = candidate
        }
        return best?.toDoubleOrNull()
    }

    private fun parseLabel(text: String): String? {
        val lower = text.lowercase()
        return when {
            "woda" in lower || "water" in lower -> "woda"
            "gaz" in lower || "gas" in lower -> "gaz"
            "prad" in lower || "energia" in lower || "elek" in lower || "electric" in lower -> "prad"
            else -> null
        }
    }
}
