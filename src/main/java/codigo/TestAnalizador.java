package codigo;

import java.util.List;

public class TestAnalizador {
    public static void main(String[] args) throws Exception {
        String ruta = args.length > 0 ? args[0] : "valid.cs";
        boolean dump = args.length > 1 && "dump".equalsIgnoreCase(args[1]);
        Analizador analizador = new Analizador();
        boolean ok = analizador.analizarArchivo(ruta);
        System.out.println("analizarArchivo returned: " + ok);

        if (dump) {
            System.out.println("----- DUMP TOKENS for: " + ruta + " -----");
            List<Token> tokens = analizador.tokenizarArchivo(ruta);
            for (Token t : tokens) {
                System.out.printf("%s | '%s' | line=%d col=%d\n", t.getTipo(), t.getLexema(), t.getLinea(), t.getColumna());
            }
            System.out.println("----- END DUMP -----");
        }
    }
}
