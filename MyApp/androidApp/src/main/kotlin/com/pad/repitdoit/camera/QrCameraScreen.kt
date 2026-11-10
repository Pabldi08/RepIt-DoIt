package com.pad.repitdoit.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun QrCameraScreen() {
    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Consultar máquina por QR")

                TextButton(onClick = { backDispatcher?.onBackPressed() }) {
                    Text("Cerrar")
                }
            }

            if (permissionGranted) {
                CameraPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Necesitamos permiso para mostrar la cámara.")

                    TextButton(onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                        Text("Dar permiso")
                    }

                    Text(
                        "Si has bloqueado el permiso, puedes activarlo " +
                            "en los ajustes de la aplicación."
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var viewSurfaceProvider by remember {
        mutableStateOf<Preview.SurfaceProvider?>(null)
    }
    var error by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { viewContext ->
                PreviewView(viewContext).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    viewSurfaceProvider = surfaceProvider
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (error) {
            Text(
                text = "No se ha podido abrir la cámara.",
                color = Color.White
            )
        }
    }

    val previewSurfaceProvider = viewSurfaceProvider ?: return
    DisposableEffect(context, lifecycleOwner, previewSurfaceProvider) {
        val preview = Preview.Builder().build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        var cameraProvider: ProcessCameraProvider? = null
        var closed = false

        cameraProviderFuture.addListener(Runnable {
            if (!closed) {
                try {
                    val provider = cameraProviderFuture.get()
                    cameraProvider = provider

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    preview.setSurfaceProvider(previewSurfaceProvider)
                    provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                } catch (exception: Exception) {
                    error = true
                }
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            closed = true
            cameraProvider?.unbind(preview)
            preview.setSurfaceProvider(null)
        }
    }
}
