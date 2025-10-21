package codigo;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;

public class ColoreadorGUI extends JFrame {
    // ... (Variables de instancia y métodos initComponents(), initEstilos(), abrirArchivo() son correctos)
    private JTextPane textPane;
    private JLabel statusLabel;
    private StyledDocument doc;

    private Style estiloReservada;
    private Style estiloNumero;
    private Style estiloAgrupacion;
    private Style estiloComparacion;
    private Style estiloCadena;
    private Style estiloIdentificador;
    private Style estiloComentario;
    private Style estiloError;
    private Style estiloNormal;

    public ColoreadorGUI() {
        setTitle("Coloreador Léxico - C#");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        initEstilos();
    }

    // ... (El resto de initComponents y initEstilos son correctos, se omiten para brevedad)
    private void initComponents() {
        setLayout(new BorderLayout());
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        doc = textPane.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);
        JPanel topPanel = new JPanel();
        JButton btnAbrir = new JButton("Abrir Archivo");
        JButton btnLimpiar = new JButton("Limpiar");
        btnAbrir.addActionListener(e -> abrirArchivo());
        btnLimpiar.addActionListener(e -> limpiar());
        topPanel.add(btnAbrir);
        topPanel.add(btnLimpiar);
        add(topPanel, BorderLayout.NORTH);
        statusLabel = new JLabel("Listo para analizar archivo...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(240, 240, 240));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void initEstilos() {
        estiloReservada = textPane.addStyle("Reservada", null);
        StyleConstants.setForeground(estiloReservada, new Color(0, 0, 255));
        StyleConstants.setBold(estiloReservada, true);
        estiloNumero = textPane.addStyle("Numero", null);
        StyleConstants.setForeground(estiloNumero, new Color(255, 140, 0));
        estiloAgrupacion = textPane.addStyle("Agrupacion", null);
        StyleConstants.setForeground(estiloAgrupacion, Color.BLACK);
        estiloComparacion = textPane.addStyle("Comparacion", null);
        StyleConstants.setForeground(estiloComparacion, Color.BLACK);
        estiloCadena = textPane.addStyle("Cadena", null);
        StyleConstants.setForeground(estiloCadena, new Color(0, 128, 0));
        estiloIdentificador = textPane.addStyle("Identificador", null);
        StyleConstants.setForeground(estiloIdentificador, Color.BLACK);
        estiloComentario = textPane.addStyle("Comentario", null);
        StyleConstants.setForeground(estiloComentario, new Color(128, 0, 128));
        StyleConstants.setItalic(estiloComentario, true);
        estiloError = textPane.addStyle("Error", null);
        StyleConstants.setForeground(estiloError, Color.WHITE);
        StyleConstants.setBackground(estiloError, Color.RED);
        StyleConstants.setBold(estiloError, true);
        estiloNormal = textPane.addStyle("Normal", null);
        StyleConstants.setForeground(estiloNormal, Color.BLACK);
    }

    private void abrirArchivo() {
        File archivo = SeleccionarArchivo.seleccionarArchivo();
        if (archivo != null) {
            analizarYColorear(archivo);
        }
    }

    private void analizarYColorear(File archivo) {
        limpiar();
        try (FileReader fileReader = new FileReader(archivo)) {
// Se asume que Lexer es una clase existente (ej. JFlex)
            Lexer lexer = new Lexer(fileReader);

            List<Token> tokens = new ArrayList<>();
            Token token;
            boolean hayError = false;
            Token primerError = null; // Almacena el primer error para el reporte

// Fase 1: Analizar todo el archivo y recolectar todos los tokens
            while ((token = lexer.yylex()) != null) {
                tokens.add(token);

                if (token.getTipo().startsWith("ERROR") && !hayError) {
                    hayError = true;
                    primerError = token; // Guarda el primer error encontrado
                }
            }

// Nueva validación: balance de paréntesis/llaves/corchetes
            if (!hayError) {
                Token balanceErr = validarBalance(tokens);
                if (balanceErr != null) {
                    hayError = true;
                    primerError = balanceErr;
                }
            }

// Fase 2: Mostrar todos los tokens en el JTextPane
            for (Token t : tokens) {
// Si este token es el primer error detectado, forzar estilo de error
                Style estilo;
                if (hayError && primerError != null && t.getLinea() == primerError.getLinea() && t.getColumna() == primerError.getColumna() && t.getLexema().equals(primerError.getLexema())) {
                    estilo = estiloError;
                } else {
                    estilo = obtenerEstilo(t.getTipo());
                }

                try {
                    doc.insertString(doc.getLength(), t.getLexema(), estilo);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }

// Fase 3: Reportar el resultado del análisis
            if (hayError) {
// Si hay errores, reporta el primer error encontrado
                statusLabel.setText(String.format(
                        "ERROR en línea %d, columna %d: '%s'",
                        primerError.getLinea(), primerError.getColumna(), primerError.getLexema()));
                statusLabel.setForeground(Color.WHITE);
                statusLabel.setBackground(Color.RED);
            } else {
// Si no hay errores, el archivo es válido
                statusLabel.setText("\u2713 Archivo válido - Análisis completado exitosamente");
                statusLabel.setForeground(new Color(0, 128, 0));
                statusLabel.setBackground(new Color(240, 240, 240));

// Generar reporte solo si el análisis es exitoso
                generarReporte(lexer, archivo.getName());
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Archivo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida el balance de paréntesis/llaves/corchetes. Devuelve un Token de tipo ERROR
     * con la posición del primer símbolo desequilibrado, o null si todo está balanceado.
     */
    private Token validarBalance(List<Token> tokens) {
        Deque<Token> stack = new ArrayDeque<>();

        for (Token t : tokens) {
            String tipo = t.getTipo();
            if ("PARENTESIS_ABIERTO".equals(tipo) || "LLAVE_ABIERTA".equals(tipo) || "CORCHETE_ABIERTO".equals(tipo)) {
                stack.push(t);
            } else if ("PARENTESIS_CERRADO".equals(tipo)) {
                if (stack.isEmpty() || !"PARENTESIS_ABIERTO".equals(stack.peek().getTipo())) {
                    return new Token("ERROR", t.getLexema(), t.getLinea(), t.getColumna());
                }
                stack.pop();
            } else if ("LLAVE_CERRADA".equals(tipo)) {
                if (stack.isEmpty() || !"LLAVE_ABIERTA".equals(stack.peek().getTipo())) {
                    return new Token("ERROR", t.getLexema(), t.getLinea(), t.getColumna());
                }
                stack.pop();
            } else if ("CORCHETE_CERRADO".equals(tipo)) {
                if (stack.isEmpty() || !"CORCHETE_ABIERTO".equals(stack.peek().getTipo())) {
                    return new Token("ERROR", t.getLexema(), t.getLinea(), t.getColumna());
                }
                stack.pop();
            }
        }

        if (!stack.isEmpty()) {
            Token open = stack.peek();
            return new Token("ERROR", open.getLexema(), open.getLinea(), open.getColumna());
        }
        return null;
    }

    private Style obtenerEstilo(String tipoToken) {
        switch (tipoToken) {
            case "RESERVADA": return estiloReservada;
            case "NUMERO": return estiloNumero;
            case "PARENTESIS_ABIERTO":
            case "PARENTESIS_CERRADO":
            case "LLAVE_ABIERTA":
            case "LLAVE_CERRADA":
            case "CORCHETE_ABIERTO":
            case "CORCHETE_CERRADO": return estiloAgrupacion;
            case "COMPARACION":
            case "OPERADOR":
            case "PUNTO_COMA":
            case "COMA":
            case "PUNTO":
            case "OPERADOR_DOLAR": return estiloComparacion;
            case "CADENA_NORMAL":
            case "CADENA_VERBATIM": return estiloCadena;
            case "IDENTIFICADOR": return estiloIdentificador;
            case "COMENTARIO": return estiloComentario;
            case "ESPACIO": return estiloNormal; // <-- ¡NUEVA LÍNEA! Aplica estilo normal para mantener el formato.
            case "ERROR":
            case "ERROR_CADENA":
            case "ERROR_COMENTARIO":
            case "ERROR_CADENA_SIN_CERRAR":
            case "ERROR_COMENTARIO_SIN_CERRAR":
            case "ERROR_LEXICO_SIMBOLO_NO_VALIDO": return estiloError;
            default: return estiloNormal;
        }
    }

    /**
     * CORRECCIÓN: Este método ahora usa ArchivoSalida para generar el reporte.
     */
    private void generarReporte(Lexer lexer, String nombreArchivo) {
        String nombreReporte = "Reporte_" + nombreArchivo + ".txt";

// Se define el formato del reporte, pero se usa la clase externa para escribirlo.
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreReporte))) {
            writer.println("=".repeat(60));
            writer.println("REPORTE DE ANÁLISIS LÉXICO");
            writer.println("=".repeat(60));
            writer.println("Archivo: " + nombreArchivo);

// Llama al método estático de la clase ArchivoSalida
            ArchivoSalida.logStatistics(lexer, writer);

            System.out.println("Reporte generado: " + nombreReporte);
        } catch (IOException e) {
            System.err.println("Error al generar reporte: " + e.getMessage());
        }
    }

    private void limpiar() {
        textPane.setText("");
        statusLabel.setText("Listo para analizar archivo...");
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setBackground(new Color(240, 240, 240));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ColoreadorGUI gui = new ColoreadorGUI();
            gui.setVisible(true);
        });
    }
}