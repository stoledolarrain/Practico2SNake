
package com.example.practico2snake
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practico2serpiente.VistaJuego

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el Vista Juego como el contenido de la actividad
        setContentView(VistaJuego(this))
    }
}