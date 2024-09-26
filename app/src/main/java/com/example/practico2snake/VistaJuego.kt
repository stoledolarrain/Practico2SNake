package com.example.practico2snake

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

// Esta clase representa la vista del juego donde se dibuja la serpiente y la comida
class VistaJuego(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint()  // Herramienta de dibujo
    private val logicaJuego = LogicaJuego(20, 35) // Lógica del juego con una matriz de 20x35
    private val handler = Handler(Looper.getMainLooper()) // Handler para el movimiento automático
    private val updateInterval = 300L // Tiempo en milisegundos para mover la serpiente (0.3 segundos)

    init {
        paint.color = Color.GRAY // Configurar el color de la serpiente
        iniciarJuego() // Iniciar el juego
    }

    // Iniciar el movimiento automático de la serpiente
    private fun iniciarJuego() {
        // Usar un Runnable para actualizar el juego cada cierto tiempo
        handler.postDelayed(object : Runnable {
            override fun run() {
                logicaJuego.updateGame() // Actualiza el estado del juego (mover la serpiente)

                if (logicaJuego.isGameOver) {
                    showGameOverDialog() // Mostrar diálogo de "Juego Terminado" si la serpiente muere
                } else {
                    invalidate() // Redibujar la vista (actualiza la pantalla)
                    handler.postDelayed(this, updateInterval) // Repetir la actualización cada 300ms
                }
            }
        }, updateInterval)
    }

    // Método que dibuja la serpiente y la comida en la pantalla
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calcular el tamaño de cada celda en función del tamaño de la pantalla
        val cellWidth = width / logicaJuego.width
        val cellHeight = height / logicaJuego.height

        // Dibujar cada parte del cuerpo de la serpiente
        for (part in logicaJuego.snake) {
            canvas.drawRect(
                part.x * cellWidth.toFloat(),
                part.y * cellHeight.toFloat(),
                (part.x + 1) * cellWidth.toFloat(),
                (part.y + 1) * cellHeight.toFloat(),
                paint
            )
        }

        // Cambiar el color para dibujar la comida
        paint.color = Color.RED
        canvas.drawRect(
            logicaJuego.comida.x * cellWidth.toFloat(),
            logicaJuego.comida.y * cellHeight.toFloat(),
            (logicaJuego.comida.x + 1) * cellWidth.toFloat(),
            (logicaJuego.comida.y + 1) * cellHeight.toFloat(),
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
            val screenWidth = width
            val screenHeight = height

            // Determinar la dirección de la serpiente según la parte de la pantalla tocada
            if (x < screenWidth / 3) {
                // Parte izquierda de la pantalla -> Cambiar a dirección izquierda
                logicaJuego.setDirection(Direction.LEFT)
            } else if (x > 2 * screenWidth / 3) {
                // Parte derecha de la pantalla -> Cambiar a dirección derecha
                logicaJuego.setDirection(Direction.RIGHT)
            } else if (y < screenHeight / 3) {
                // Parte superior de la pantalla -> Cambiar a dirección arriba
                logicaJuego.setDirection(Direction.UP)
            } else if (y > 2 * screenHeight / 3) {
                // Parte inferior de la pantalla -> Cambiar a dirección abajo
                logicaJuego.setDirection(Direction.DOWN)
            }
        }
        return true
    }

    // Mostrar un diálogo cuando el juego termine (cuando la serpiente muera)
    private fun showGameOverDialog() {
        AlertDialog.Builder(context).apply {
            setMessage("Te has muerto")
            setPositiveButton("Jugar de nuevo ") { _, _ ->
                reiniciarJuego()
            }
            setNegativeButton("Terminar el juego") { _, _ ->
                (context as MainActivity).finish()
            }
            setCancelable(false) // No permitir que el diálogo se cierre sin elegir una opción
            show()
        }
    }

    // Reiniciar el juego cuando el usuario elija "Reiniciar"
    private fun reiniciarJuego() {
        logicaJuego.resetGame() // Resetear el estado del juego
        iniciarJuego() // Iniciar el loop del movimiento nuevamente
        invalidate() // Redibujar la vista
    }
}