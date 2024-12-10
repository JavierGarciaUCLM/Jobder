package com.example.jobder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.provider.Settings
import android.nfc.Tag
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
object SharedStateLock{
    lateinit var activity: ComponentActivity
}
class LockScreenNFC : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        setContent {

//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
                    SharedStateLock.activity = this
                    NFCReaderScreen(nfcAdapter)
                //}

        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val intentFilter = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilter, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let {
                val tagId = it.id.joinToString("") { byte -> "%02x".format(byte) }
                //Toast.makeText(this, "Access granted. NFC Tag ID: $tagId", Toast.LENGTH_SHORT).show()
                val intent = Intent(SharedStateLock.activity, LockScreenFingerPrint::class.java)
                SharedStateLock.activity.startActivity(intent)
            }
        }
    }
}

@Composable
fun NFCReaderScreen(nfcAdapter: NfcAdapter?) {
    val context = LocalContext.current
    //val scaffoldState = rememberScaffoldState()

    //Scaffold(
    //  scaffoldState = scaffoldState,
    //content = {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (nfcAdapter == null) {
            Text("NFC no está disponible en este dispositivo", style = MaterialTheme.typography.headlineLarge)
        } else if (!nfcAdapter.isEnabled) {
            Text("NFC está deshabilitado. Por favor, habilítalo en la configuración.", style = MaterialTheme.typography.headlineLarge)
            Button(onClick = {
                context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }) {
                Text("Abrir Configuración NFC")
            }
        } else {
            Text("Aproxime una tarjeta NFC para leerla", style = MaterialTheme.typography.headlineLarge)
        }
    }
    //}
    //)
}