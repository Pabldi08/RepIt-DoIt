package com.pad.repitdoit.camera

import android.net.Uri
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executor

internal fun String.readQrEquipmentId(): String? {
    val uri = Uri.parse(this)
    if (uri.scheme != "repitdoit" || uri.pathSegments.isNotEmpty()) return null
    return uri.host?.takeIf { it.isNotBlank() }
}

internal class QrCodeAnalyzer(executor: Executor, onEquipmentId: (String) -> Unit) : AutoCloseable {
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
    )

    val analyzer = MlKitAnalyzer(
        listOf(scanner),
        ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL,
        executor
    ) { result ->
        val equipmentId = result.getValue(scanner)?.firstNotNullOfOrNull { barcode ->
            barcode.rawValue?.readQrEquipmentId()
        }
        if (equipmentId != null) onEquipmentId(equipmentId)
    }

    override fun close() {
        scanner.close()
    }
}
