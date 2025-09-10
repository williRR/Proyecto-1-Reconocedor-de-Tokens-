package codigo;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File archivoSeleccionado = SeleccionarArchivo.seleccionarArchivo();

        if (archivoSeleccionado != null) {
            try {
                if (archivoSeleccionado.exists() && archivoSeleccionado.canRead()) {
                    FileReader fileReader = new FileReader(archivoSeleccionado);
                    Lexer lexer = new Lexer(fileReader);
                    String token;

                    while ((token = lexer.yylex()) != null) {
                        System.out.println("Token: " + token);
                    }

                    // Llama a la clase FileLogger para guardar las estadísticas
                    ArchivoSalida.logStatistics(lexer, "Salida.txt");

                    fileReader.close();
                } else {
                    System.out.println("Error: El archivo seleccionado no existe o no se puede leer.");
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error: No se encontró el archivo seleccionado.");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error al leer el archivo.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Selección de archivo cancelada por el usuario.");
        }
    }
}