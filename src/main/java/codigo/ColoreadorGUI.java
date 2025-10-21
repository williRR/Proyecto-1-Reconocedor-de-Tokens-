// Archivo: codigo/ColoreadorGUI.java

package codigo;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            Token primerError = null;

            while ((token = lexer.yylex()) != null) {
                tokens.add(token);

                if (token.getTipo().startsWith("ERROR") && !hayError) {
                    hayError = true;
                    primerError = token;
                }
            }

            for (Token t : tokens) {
                // Manejo de espacios (asumiendo que Lexer los clasifica como "ESPACIO")
                if ("ESPACIO".equals(t.getTipo())) {
                    try {
                        doc.insertString(doc.getLength(), t.getLexema(), estiloNormal);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                Style estilo = obtenerEstilo(t.getTipo());

                try {
                    doc.insertString(doc.getLength(), t.getLexema(), estilo);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

                if (hayError && t == primerError) {
                    statusLabel.setText(String.format(
                            "ERROR en línea %d, columna %d: '%s'",
                            t.getLinea(), t.getColumna(), t.getLexema()));
                    statusLabel.setForeground(Color.WHITE);
                    statusLabel.setBackground(Color.RED);
                    break;
                }
            }

            if (!hayError) {
                statusLabel.setText("✓ Archivo válido - Análisis completado exitosamente");
                statusLabel.setForeground(new Color(0, 128, 0));
                statusLabel.setBackground(new Color(240, 240, 240));

                // DELEGAR A ArchivoSalida: Llama al método estático aquí
                generarReporte(lexer, archivo.getName());
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Archivo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            case "PUNTO": return estiloComparacion;
            case "CADENA": return estiloCadena;
            case "IDENTIFICADOR": return estiloIdentificador;
            case "COMENTARIO": return estiloComentario;
            case "ERROR":
            case "ERROR_CADENA":
            case "ERROR_COMENTARIO": return estiloError;
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