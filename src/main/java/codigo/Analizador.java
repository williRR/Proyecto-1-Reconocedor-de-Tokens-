package codigo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Analizador {
    /**
     * Analiza el archivo completo usando el Lexer generado por JFlex.
     * Procesar el archivo línea a línea rompe tokens multilínea (cadenas, comentarios),
     * por eso aquí usamos un Reader y el método yylex() del Lexer.
     *
     * @param ruta ruta al archivo a analizar
     * @return true si el archivo es válido (sin errores léxicos), false si hubo error
     * @throws IOException si hay un problema de I/O
     */
    public boolean analizarArchivo(String ruta) throws IOException {
        try (InputStreamReader fr = new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8)) {
            Lexer lexer = new Lexer(fr);
            List<Token> tokens = new ArrayList<>();
            Token token;

            while ((token = lexer.yylex()) != null) {
                tokens.add(token);
                // Si encontramos un error léxico, detenemos el análisis y reportamos
                if (token.getTipo() != null && token.getTipo().startsWith("ERROR")) {
                    System.err.printf("ERROR léxico en %s -> línea %d, columna %d: '%s'\n",
                            ruta, token.getLinea(), token.getColumna(), token.getLexema());
                    return false;
                }
            }

            // Si llegamos aquí no hubo errores
            System.out.println("Archivo válido: " + ruta);
            return true;
        }
    }

    // Método auxiliar opcional para obtener la lista de tokens (útil para pruebas)
    public List<Token> tokenizarArchivo(String ruta) throws IOException {
        try (InputStreamReader fr = new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8)) {
            Lexer lexer = new Lexer(fr);
            List<Token> tokens = new ArrayList<>();
            Token token;
            while ((token = lexer.yylex()) != null) {
                tokens.add(token);
            }
            return tokens;
        }
    }
}
