package com.example.jobder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity

class LockScreenFingerPrint: FragmentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            LockScreen(this)
        }
    }
}
@Composable
fun LockScreen(activity: FragmentActivity) {
    //val scaffoldState = rememberScaffoldState()

    //Scaffold(
    //scaffoldState = scaffoldState,
    //content = {

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    //) {
        //Button(onClick = {
            authenticateWithFingerprint(activity)
        //}) {
          //  Text("Desbloquear con Huella Dactilar")
        //}
    //}
    //}
    //)
}

fun authenticateWithFingerprint(activity: FragmentActivity) {

    val biometricManager = BiometricManager.from(activity)
    when (biometricManager.canAuthenticate()) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // Autenticación exitosa
                        //Toast.makeText(activity, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, MainActivity::class.java)
                        activity.startActivity(intent)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Error en la autenticación
                        Toast.makeText(activity, "Error de autenticación: $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        // Fallo en la autenticación
                        Toast.makeText(activity, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación Biométrica")
                .setSubtitle("Usa tu huella dactilar para desbloquear")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
            Toast.makeText(activity, "No hay hardware biométrico en el dispositivo", Toast.LENGTH_SHORT).show()
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
            Toast.makeText(activity, "El hardware biométrico no está disponible actualmente", Toast.LENGTH_SHORT).show()
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
            Toast.makeText(activity, "No hay datos biométricos registrados", Toast.LENGTH_SHORT).show()
    }
}