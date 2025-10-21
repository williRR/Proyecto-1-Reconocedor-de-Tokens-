package codigo;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica del coloreador léxico
        SwingUtilities.invokeLater(() -> {
            ColoreadorGUI gui = new ColoreadorGUI();
            gui.setVisible(true);
        });
    }
}