
package com.example.practico2snake
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el SnakeGameView como el contenido de la actividad
        setContentView(VistaJuego(this))
    }
}