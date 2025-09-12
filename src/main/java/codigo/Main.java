package codigo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        File archivoSeleccionado = SeleccionarArchivo.seleccionarArchivo();

        if (archivoSeleccionado != null) {
            // Data structures to store the counts
            Map<String, Integer> palabrasReservadas = new HashMap<>();
            Map<String, Integer> signosAgrupacion = new HashMap<>();
            Map<String, Integer> operadoresAritmeticos = new HashMap<>();

            // Initialize the maps with the specific words and symbols to count
            palabrasReservadas.put("using", 0);
            palabrasReservadas.put("public", 0);
            palabrasReservadas.put("class", 0);
            palabrasReservadas.put("int", 0);
            palabrasReservadas.put("new", 0);
            palabrasReservadas.put("while", 0);
            palabrasReservadas.put("if", 0);
            palabrasReservadas.put("return", 0);
            palabrasReservadas.put("static", 0);
            palabrasReservadas.put("in", 0);
            palabrasReservadas.put("foreach", 0);
            palabrasReservadas.put("private", 0);

            signosAgrupacion.put("(", 0);
            signosAgrupacion.put(")", 0);
            signosAgrupacion.put("{", 0);
            signosAgrupacion.put("}", 0);

            // Here we'll just count the '=' operator as an example
            operadoresAritmeticos.put("=", 0);

            try (PrintWriter writer = new PrintWriter(new FileWriter("Salida.txt"))) {
                if (archivoSeleccionado.exists() && archivoSeleccionado.canRead()) {
                    FileReader fileReader = new FileReader(archivoSeleccionado);
                    Lexer lexer = new Lexer(fileReader);
                    String token;

                    System.out.println("--- Proceso de Análisis ---");

                    // Process all tokens and count them
                    while ((token = lexer.yylex()) != null) {
                        System.out.println("Token: " + token);

                        // Use a regex pattern to extract the token type and value
                        Pattern p = Pattern.compile("RESERVADA\\((.*)\\)|LLAVE_ABIERTA|OTRO\\((.*)\\)|PARENTESIS_ABIERTO|IDENTIFICADOR\\((.*)\\)");
                        Matcher m = p.matcher(token);

                        if (m.find()) {
                            String tokenType = token.split("\\(")[0];
                            String tokenValue = "";
                            if (m.groupCount() > 0 && m.group(1) != null) {
                                tokenValue = m.group(1);
                            } else if (m.groupCount() > 1 && m.group(2) != null) {
                                tokenValue = m.group(2);
                            }

                            // Counting Logic
                            if (tokenType.equals("RESERVADA")) {
                                if (palabrasReservadas.containsKey(tokenValue)) {
                                    palabrasReservadas.put(tokenValue, palabrasReservadas.get(tokenValue) + 1);
                                }
                            } else if (tokenType.equals("OTRO")) {
                                if (tokenValue.equals("=")) {
                                    operadoresAritmeticos.put("=", operadoresAritmeticos.get("=") + 1);
                                } else if (signosAgrupacion.containsKey(tokenValue)) {
                                    signosAgrupacion.put(tokenValue, signosAgrupacion.get(tokenValue) + 1);
                                }
                            } else if (tokenType.equals("LLAVE_ABIERTA")) {
                                signosAgrupacion.put("{", signosAgrupacion.get("{") + 1);
                            } else if (tokenType.equals("PARENTESIS_ABIERTO")) {
                                signosAgrupacion.put("(", signosAgrupacion.get("(") + 1);
                            }
                            // You can add more logic for other token types here
                        }
                    }

                    // Write the report to the file
                    writer.println("Resumen de Conteo por Palabra");
                    writer.println("-----------------------------");
                    writer.printf("%-25s %-25s %s%n", "Elemento", "Palabra", "Conteo");
                    writer.println("------------------------------------------------------------------");

                    writer.println("Palabras Reservadas");
                    for (Map.Entry<String, Integer> entry : palabrasReservadas.entrySet()) {
                        writer.printf("%-25s %-25s %d%n", "", entry.getKey(), entry.getValue());
                    }

                    writer.println("Signos de Agrupación");
                    for (Map.Entry<String, Integer> entry : signosAgrupacion.entrySet()) {
                        writer.printf("%-25s %-25s %d%n", "", entry.getKey(), entry.getValue());
                    }

                    writer.println("Operadores Aritméticos");
                    for (Map.Entry<String, Integer> entry : operadoresAritmeticos.entrySet()) {
                        writer.printf("%-25s %-25s %d%n", "", entry.getKey(), entry.getValue());
                    }

                    System.out.println("Reporte de estadísticas generado en: Salida.txt");
                } else {
                    System.out.println("Error: El archivo seleccionado no existe o no se puede leer.");
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error: No se encontró el archivo seleccionado.");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error al leer/escribir el archivo.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Selección de archivo cancelada por el usuario.");
        }
    }
}