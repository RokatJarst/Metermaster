package com.example.metermaster.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.metermaster.camera.CameraPreviewCapture
import com.example.metermaster.data.Counter
import com.example.metermaster.ui.viewmodel.MeterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@Composable
fun MeterDetailsScreen(counter: Counter, viewModel: MeterViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var manualValue by remember { mutableStateOf("") }
    var showCamera by remember { mutableStateOf(false) }
    var imageCaptureRef by remember { mutableStateOf<ImageCapture?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(counter.name, style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        Row {
            OutlinedTextField(value = manualValue, onValueChange={manualValue=it}, label={Text("Ręczny odczyt")})
            Spacer(Modifier.width(8.dp))
            Button(onClick={
                val v = manualValue.replace(',', '.').toDoubleOrNull()
                if (v!=null) { viewModel.addReadingFor(counter.id, v); manualValue=""; Toast.makeText(context,"Odczyt zapisany", Toast.LENGTH_SHORT).show() }
                else Toast.makeText(context,"Wprowadź poprawną liczbę", Toast.LENGTH_SHORT).show()
            }){ Text("Zapisz") }
        }
        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick={ showCamera = true }){ Text("Otwórz kamerę (CameraX)") }
            Spacer(Modifier.width(8.dp))
            Button(onClick={ }){ Text("Wybierz zdjęcie") }
        }
        Spacer(Modifier.height(12.dp))
        if (showCamera) {
            CameraPreviewCapture(modifier = Modifier.height(400.dp).fillMaxWidth(), onReady = { ic -> imageCaptureRef = ic }, onError = { e -> Toast.makeText(context,"Błąd kamery: ${'$'}e", Toast.LENGTH_LONG).show() })
            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick={
                    val imageCapture = imageCaptureRef
                    if (imageCapture==null) { Toast.makeText(context,"Kamera nie jest gotowa", Toast.LENGTH_SHORT).show(); return@Button }
                    val filename = "meter_${System.currentTimeMillis()}.jpg"
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MeterMaster")
                    }
                    val resolver = context.contentResolver
                    val uri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    if (uri==null) { Toast.makeText(context,"Nie można utworzyć pliku obrazu", Toast.LENGTH_SHORT).show(); return@Button }
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(resolver, uri, contentValues).build()
                    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object: ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            scope.launch {
                                val bmp = loadBitmapFromUri(context, uri)
                                if (bmp!=null) {
                                    val (num,label) = viewModel.analyzeBitmap(bmp)
                                    if (num!=null) {
                                        viewModel.addReadingFor(counter.id, num)
                                        Toast.makeText(context,"Odczyt zapisany: ${'$'}num", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context,"OCR: liczba nie znaleziona", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(context,"Nie można wczytać obrazu", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(context,"Błąd zapisu zdjęcia: ${'$'}{exception.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                }) { Text("Zrób zdjęcie i odczytaj") }
                Spacer(Modifier.width(8.dp))
                Button(onClick={ showCamera = false }) { Text("Zamknij kamerę") }
            }
        }
    }
}

suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val input: InputStream? = resolver.openInputStream(uri)
            input.use { BitmapFactory.decodeStream(it) }
        } catch (e: Exception) { null }
    }
}
