// Archivo: codigo/ArchivoSalida.java

package codigo;

import java.io.PrintWriter;

public class ArchivoSalida {

    /**
     * CORRECCIÓN: Modificado para recibir un PrintWriter existente,
     * permitiendo que otra clase maneje el nombre y la apertura del archivo.
     * Mantiene el formato de las estadísticas.
     */
    public static void logStatistics(Lexer lexer, PrintWriter writer) {
        // No se usa try-with-resources aquí ya que el escritor lo maneja la clase llamadora.
        writer.println("\n--- Estadísticas ---");
        writer.println("Reservadas: " + lexer.reservadas);
        writer.println("Variables: " + lexer.variables);
        writer.println("Llaves: " + lexer.llaves); // Ajustado a solo "Llaves"
        writer.println("Paréntesis: " + lexer.parentesis); // Ajustado a solo "Paréntesis"
        writer.println("Errores: " + lexer.errores);
        writer.println("=".repeat(60));
    }
}