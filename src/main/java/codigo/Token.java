package codigo;

public class Token {
    private final String tipo;
    private final String lexema;
    private final int linea;
    private final int columna;

    public Token(String tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return String.format("Token{tipo='%s', lexema='%s', linea=%d, columna=%d}",
                tipo, lexema, linea, columna);
    }
}

