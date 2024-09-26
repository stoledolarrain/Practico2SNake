
import com.example.practico2snake.PuntoSerpiente
import com.example.practico2snake.direcciones
import kotlin.random.Random

class LogicaJuego(val width: Int, val height: Int) {

    var serpiente = mutableListOf(PuntoSerpiente(width / 2, height / 2)) // Inicializar la serpiente
    private var currentdirecciones = direcciones.RIGHT // Dirección inicial de la serpiente
    var comida: PuntoSerpiente = generarComida()
    var juegoTerminado = false // indica si el juego terminó

    // Cambiar la dirección de la serpiente
    fun setdirecciones(newdirecciones: direcciones) {
        if (canChangedireccionesTo(newdirecciones)) {
            currentdirecciones = newdirecciones
        }
    }

    // Verificar si la serpiente puede cambiar a una nueva dirección sin hacer un giro de 180 grados
    private fun canChangedireccionesTo(newdirecciones: direcciones): Boolean {
        return when (newdirecciones) {
            direcciones.UP -> currentdirecciones != direcciones.DOWN
            direcciones.DOWN -> currentdirecciones != direcciones.UP
            direcciones.LEFT -> currentdirecciones != direcciones.RIGHT
            direcciones.RIGHT -> currentdirecciones != direcciones.LEFT
        }
    }

    // Movimiento de la serpiente
    fun actualizarJuego() {
        if (juegoTerminado) return // Si el juego terminó, no hacer nada

        // Obtener la cabeza actual de la serpiente
        val cabezaSerpiente = serpiente.first()

        // Movimiento de la serpiente en la dirección actual
        val newcabezaSerpiente = when (currentdirecciones) {
            direcciones.UP -> PuntoSerpiente(cabezaSerpiente.x, (cabezaSerpiente.y - 1 + height) % height)
            direcciones.DOWN -> PuntoSerpiente(cabezaSerpiente.x, (cabezaSerpiente.y + 1) % height)
            direcciones.LEFT -> PuntoSerpiente((cabezaSerpiente.x - 1 + width) % width, cabezaSerpiente.y)
            direcciones.RIGHT -> PuntoSerpiente((cabezaSerpiente.x + 1) % width, cabezaSerpiente.y)
        }

        // Verificar si la nueva cabeza colisiona con el cuerpo de la serpiente
        if (serpiente.contains(newcabezaSerpiente)) {
            juegoTerminado = true
            return
        }

        // Añadir la nueva cabeza a la serpiente
        serpiente.add(0, newcabezaSerpiente)

        // Verificar si la serpiente ha comido la comida
        if (newcabezaSerpiente == comida) {
            comida = generarComida() // Generar nueva comida si ha comido
        }
        else {
            // Si no ha comido, eliminar la cola (mantener el tamaño de la serpiente)
            serpiente.removeAt(serpiente.size - 1)
        }
    }

    // Generar un nuevo punto de comida en una posición aleatoria que no esté ocupada por la serpiente
    private fun generarComida(): PuntoSerpiente {
        var newcomida: PuntoSerpiente
        do {
            newcomida = PuntoSerpiente(Random.nextInt(width), Random.nextInt(height))
        } while (serpiente.contains(newcomida)) // Asegurarse de que la comida no aparezca en el cuerpo de la serpiente
        return newcomida
    }

    // Reiniciar el estado del juego
    fun reiniciarJuego() {
        serpiente = mutableListOf(PuntoSerpiente(width / 2, height / 2)) // Reiniciar la serpiente en el centro
        currentdirecciones = direcciones.RIGHT // Reiniciar la dirección a la derecha
        comida = generarComida() // Generar nueva comida
        juegoTerminado = false // Reiniciar el estado de "Game Over"
    }
}