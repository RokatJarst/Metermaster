package com.example.metermaster.camera

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun CameraPreviewCapture(modifier: Modifier = Modifier, onReady: (ImageCapture)->Unit, onError: (Exception)->Unit) {
    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val imageCapture = ImageCapture.Builder().setTargetRotation(previewView.display.rotation).build()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(ctx as androidx.lifecycle.LifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                onReady(imageCapture)
            } catch (e: Exception) {
                onError(e)
            }
        }, ContextCompat.getMainExecutor(ctx))
        previewView
    }, modifier = modifier)
}
