// Archivo: codigo/ArchivoSalida.java

package codigo;

import java.io.PrintWriter;

public class ArchivoSalida {

    public static void logStatistics(Lexer lexer, PrintWriter writer) {
        writer.println("\n--- Estadísticas ---");
        writer.println("Reservadas: " + lexer.reservadas);
        writer.println("Variables: " + lexer.variables);
        writer.println("Llaves: " + lexer.llaves);
        writer.println("Paréntesis: " + lexer.parentesis);
        writer.println("Errores: " + lexer.errores);
        writer.println("=".repeat(60));

        writer.println("\nVARIABLES DETECTADAS EN LOS ALGORITMOS:");
        if (lexer.nombresVariables.isEmpty()) {
            writer.println("No se detectaron variables.");
        } else {
            lexer.nombresVariables.stream()
                    .sorted()
                    .forEach(var -> writer.println("- " + var));
        }
    }
}
