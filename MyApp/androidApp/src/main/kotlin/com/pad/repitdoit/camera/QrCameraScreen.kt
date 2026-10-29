package com.pad.repitdoit.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun QrCameraScreen(onClose: () -> Unit) {
    val context = LocalContext.current

    var permissionGranted by remember {
        mutableStateOf(
            context.checkSelfPermission(Manifest.permission.CAMERA) ==
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

                TextButton(onClick = onClose) {
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

    var surfaceRequest by remember {
        mutableStateOf<SurfaceRequest?>(null)
    }
    var error by remember { mutableStateOf(false) }

    DisposableEffect(context, lifecycleOwner) {
        val preview = Preview.Builder().build()
        val cameraFuture = ProcessCameraProvider.getInstance(context)

        var cameraProvider: ProcessCameraProvider? = null
        var closed = false

        // Se ejecuta cuando CameraX está preparado.
        cameraFuture.addListener({
            if (!closed) {
                try {
                    val provider = cameraFuture.get()
                    cameraProvider = provider

                    preview.setSurfaceProvider { request ->
                        surfaceRequest = request
                    }

                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview
                    )
                } catch (exception: Exception) {
                    error = true
                }
            }
        }, context.mainExecutor)

        // Se ejecuta al salir de la pantalla.
        onDispose {
            closed = true
            cameraProvider?.unbind(preview)
            preview.surfaceProvider = null
        }
    }

    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (error) {
            Text(
                text = "No se ha podido abrir la cámara.",
                color = Color.White
            )
        } else {
            surfaceRequest?.let { request ->
                CameraXViewfinder(
                    surfaceRequest = request,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
