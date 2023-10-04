package com.dam.tiempoactiva

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.tiempoactiva.ui.theme.TiempoActivaTheme


class MainActivity : ComponentActivity() {

    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiempoActivaTheme() {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Texto("Tiempo en pausa")

                }
            }

        }

        // Restaura el tiempo acumulado si existe un valor guardado
        if (savedInstanceState != null) {
            elapsedTime = savedInstanceState.getLong("elapsedTime")
            isPaused = savedInstanceState.getBoolean("isPaused")
            if (!isPaused) {
                startTimer()
            }
        }


    }

    override fun onPause() {
        super.onPause()
        startTimer()
        isPaused= true
    }

    override fun onRestart() {
        super.onRestart()
        pauseTimer()
        isPaused=false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("elapsedTime", elapsedTime)
        outState.putBoolean("isPaused", isPaused)
    }

    private val handler = Handler()

    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            updateTimerText(elapsedTime)
            handler.postDelayed(this, 1000) // Actualiza cada segundo
        }
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime() - elapsedTime
        handler.post(timerRunnable)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }


    //@Composable
    private fun updateTimerText(time: Long) {
        val seconds = time / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        Log.d("Tiempo activo",formattedTime)
        setContent {
            Texto(tiempo = formattedTime)
        }

    }

}

@Composable
fun Texto(tiempo: String) {
        Text(
            text = tiempo,
            Modifier
                .fillMaxWidth()// Ocupa todo el ancho disponible
                .background(Color.Black),
            color = Color.White,
            fontSize = 50.sp
        )
}




