package com.example.practico2serpiente

import LogicaJuego
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.practico2snake.MainActivity
import com.example.practico2snake.direcciones


class VistaJuego(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint()
    private val logicaJuego = LogicaJuego(20, 35)
    private val handler = Handler(Looper.getMainLooper())
    private val velocidad = 300L

    init {
        paint.color = Color.GRAY // Configurar el color de la serpiente
        iniciarJuego() // Iniciar el juego
    }

    // Iniciar el movimiento automático de la serpiente
    private fun iniciarJuego() {

        handler.postDelayed(object : Runnable {
            override fun run() {
                logicaJuego.actualizarJuego() // Actualiza el estado del juego (mover la serpiente)

                if (logicaJuego.juegoTerminado) {
                    showJuegoTerminado() // Mostrar "Juego Terminado" si la serpiente muere
                } else {
                    invalidate()
                    handler.postDelayed(this, velocidad) // Repetir la actualización cada 300ms
                }
            }
        }, velocidad)
    }

    // Método que dibuja la serpiente y la comida en la pantalla
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calcular el tamaño de cada celda en función del tamaño de la pantalla
        val celdaWidth = width / logicaJuego.width
        val celdaHeight = height / logicaJuego.height

        // Dibujar cada parte del cuerpo de la serpiente
        for (part in logicaJuego.serpiente) {
            canvas.drawRect(
                part.x * celdaWidth.toFloat(),
                part.y * celdaHeight.toFloat(),
                (part.x + 1) * celdaWidth.toFloat(),
                (part.y + 1) * celdaHeight.toFloat(),
                paint
            )
        }

        // color comida
        paint.color = Color.RED
        canvas.drawRect(
            logicaJuego.comida.x * celdaWidth.toFloat(),
            logicaJuego.comida.y * celdaHeight.toFloat(),
            (logicaJuego.comida.x + 1) * celdaWidth.toFloat(),
            (logicaJuego.comida.y + 1) * celdaHeight.toFloat(),
            paint
        )
        // Restaurar el color de la serpiente
        paint.color = Color.GRAY
    }

    // Método que detecta toques en la pantalla para cambiar la dirección de la serpiente
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Obtener las coordenadas del toque
            val x = event.x
            val y = event.y
            val pantallaWidth = width
            val pantallaHeight = height

            // Determinar la dirección de la serpiente según la parte de la pantalla tocada
            if (x < pantallaWidth / 3) {
                // Parte izquierda de la pantalla -> Cambiar a dirección izquierda
                logicaJuego.setdirecciones(direcciones.LEFT)
            } else if (x > 2 * pantallaWidth / 3) {
                // Parte derecha de la pantalla -> Cambiar a dirección derecha
                logicaJuego.setdirecciones(direcciones.RIGHT)
            } else if (y < pantallaHeight / 3) {
                // Parte superior de la pantalla -> Cambiar a dirección arriba
                logicaJuego.setdirecciones(direcciones.UP)
            } else if (y > 2 * pantallaHeight / 3) {
                // Parte inferior de la pantalla -> Cambiar a dirección abajo
                logicaJuego.setdirecciones(direcciones.DOWN)
            }
        }
        return true
    }

    // Mostrar un diálogo cuando el juego termine (cuando la serpiente muera)
    private fun showJuegoTerminado() {
        AlertDialog.Builder(context).apply {
            setMessage("Te has muerto")
            setPositiveButton("Jugar de nuevo ") { _, _ -> //El setPositiveButton hace referencia al botón de la derecha
                reiniciarJuego()
            }
            setNegativeButton("Terminar el juego") { _, _ ->
                (context as MainActivity).finish()
            }
            setCancelable(false)
            show()
        }
    }

    // Reiniciar el juego cuando el usuario elija "Reiniciar"
    private fun reiniciarJuego() {
        logicaJuego.reiniciarJuego() // Resetear el estado del juego
        iniciarJuego() // Iniciar el loop del movimiento nuevamente
        invalidate() // Redibujar la vista
    }
}