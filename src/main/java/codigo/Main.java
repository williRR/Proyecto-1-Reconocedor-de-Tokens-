
// EJEMPLO DE RUTA DONDE SE GUARDA EL PROYECTO  /home/willirr/IdeaProjects/AnalizadorLexico/algoritmos/holamundo.cs

package codigo;

import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileReader("ruta donde se guardara el proyecto"));
        String token;
        while ((token = lexer.yylex()) != null) {
            System.out.println("Token: " + token);
        }

        System.out.println("\n--- Estadísticas ---");
        System.out.println("Reservadas: " + lexer.reservadas);
        System.out.println("Variables: " + lexer.variables);
        System.out.println("Llaves abiertas: " + lexer.llaves);
        System.out.println("Paréntesis abiertos: " + lexer.parentesis);
        System.out.println("Errores: " + lexer.errores);
    }
}
