package com.pad.repitdoit.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.core.content.IntentCompat

private const val EQUIPMENT_MIME_TYPE = "application/vnd.com.example.repitdoit"

internal fun Intent.readNfcEquipmentId(): String? {
    if (action != NfcAdapter.ACTION_NDEF_DISCOVERED) return null

    // Las etiquetas de la demo ya grabadas contienen una URI personalizada.
    val uri = data
    if (uri?.scheme == "repitdoit" && uri.pathSegments.isEmpty()) {
        return uri.host?.takeIf { it.isNotBlank() }
    }

    if (type != EQUIPMENT_MIME_TYPE) return null
    val messages = IntentCompat.getParcelableArrayExtra(
        this,
        NfcAdapter.EXTRA_NDEF_MESSAGES,
        NdefMessage::class.java
    )
    val message = messages?.firstOrNull() as? NdefMessage ?: return null
    val record = message.records.firstOrNull() ?: return null
    if (record.toMimeType() != EQUIPMENT_MIME_TYPE) return null

    return record.payload.toString(Charsets.UTF_8).trim().takeIf { it.isNotEmpty() }
}
