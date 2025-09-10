package codigo;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SeleccionarArchivo {

    public static File seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();

        // Optional: Filter files by type
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "C# Code Files (*.cs)", "cs");
        fileChooser.setFileFilter(filter);

        // Show dialog window
        int resultado = fileChooser.showOpenDialog(null);

        // Return selected file if operation is successful
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null; // Return null if user cancels
        }
    }
}
