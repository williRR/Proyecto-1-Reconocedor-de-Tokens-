package codigo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ArchivoSalida {

    public static void logStatistics(Lexer lexer, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            writer.println("\n--- Estadísticas ---");
            writer.println("Reservadas: " + lexer.reservadas);
            writer.println("Variables: " + lexer.variables);
            writer.println("Llaves abiertas: " + lexer.llaves);
            writer.println("Paréntesis abiertos: " + lexer.parentesis);
            writer.println("Errores: " + lexer.errores);

            System.out.println("Estadísticas guardadas en: " + filePath);

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de salida.");
            e.printStackTrace();
        }
    }
}
