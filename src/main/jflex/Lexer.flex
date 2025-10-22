package codigo;

  import java.util.HashSet;
    import java.util.Set;
%%

%class Lexer
%unicode
%public
%type Token
%line
%column

%{

  // Contadores globales
  public int reservadas = 0;
  public int variables = 0;
  public int llaves = 0;
  public int parentesis = 0;
  public int errores = 0;

   public Set<String> nombresVariables = new HashSet<>();

  private Token crearToken(String tipo, String lexema) {
      return new Token(tipo, lexema, yyline + 1, yycolumn + 1);
  }

  private Token crearToken(String tipo) {
      return new Token(tipo, yytext(), yyline + 1, yycolumn + 1);
  }
%}

// ======================
// Expresiones regulares
// ======================
DIGITO     = [0-9]
LETRA      = [a-zA-Z_]
ID         = {LETRA}({LETRA}|{DIGITO})*
NUMERO     = {DIGITO}+("."{DIGITO}+)?

RESERVADAS = ("abstract"|"as"|"base"|"bool"|"break"|"byte"|"case"|"catch"|"char"|"checked"|"class"|"const"|"continue"|
              "decimal"|"default"|"delegate"|"do"|"double"|"else"|"enum"|"event"|"explicit"|"extern"|
              "false"|"finally"|"fixed"|"float"|"for"|"foreach"|"goto"|"if"|"implicit"|"in"|"int"|"interface"|
              "internal"|"is"|"lock"|"long"|"namespace"|"new"|"null"|"object"|"operator"|"out"|
              "override"|"params"|"private"|"protected"|"public"|"readonly"|"ref"|"return"|
              "sbyte"|"sealed"|"short"|"sizeof"|"stackalloc"|"static"|"string"|"struct"|"switch"|
              "this"|"throw"|"true"|"try"|"typeof"|"uint"|"ulong"|"unchecked"|"unsafe"|"ushort"|"using"|
              "virtual"|"void"|"volatile"|"while")

COMPARACION = (">"|"<"|"=="|"!="|">="|"<="|"&&"|"||"|"!")
OPERADOR = ("+"|"-"|"*"|"/"|"%"|"="|"?"|":")

// ======================
// Reglas del analizador
// ======================
%%

{RESERVADAS} {
    reservadas++;
    return crearToken("RESERVADA");
}

{ID} {
    variables++;
    nombresVariables.add(yytext());
    return crearToken("IDENTIFICADOR");
}



{NUMERO} {
    return crearToken("NUMERO");
}

"(" {
    parentesis++;
    return crearToken("PARENTESIS_ABIERTO");
}

")" {
    parentesis++;
    return crearToken("PARENTESIS_CERRADO");
}

"{" {
    llaves++;
    return crearToken("LLAVE_ABIERTA");
}

"}" {
    llaves++;
    return crearToken("LLAVE_CERRADA");
}

"[" {
    return crearToken("CORCHETE_ABIERTO");
}

"]" {
    return crearToken("CORCHETE_CERRADO");
}

{COMPARACION} {
    return crearToken("COMPARACION");
}

{OPERADOR} {
    return crearToken("OPERADOR");
}

"$" {
    return crearToken("OPERADOR_DOLAR");
}

// Regla 1: Cadenas verbatim y/o interpoladas con @ (soporta "" como escape interno)
(\$@|@\$|@)\"([^\"]|\"\")*\" {
    return crearToken("CADENA_VERBATIM");
}

// Caso de cadena verbatim sin cerrar
(\$@|@\$|@)\"([^\"]|\"\")* {
    errores++;
    return crearToken("ERROR_CADENA_SIN_CERRAR");
}

// Regla 2: Cadenas normales (pueden ser interpoladas con prefijo $)
(\$)?\"([^\"\\]|\\.)*\" {
    return crearToken("CADENA_NORMAL");
}

// Caso de cadena normal sin cerrar (opcional $)
(\$)?\"([^\"\\]|\\.)* {
    errores++;
    return crearToken("ERROR_CADENA_SIN_CERRAR");
}

// Comentario de una sola línea
"//".* {
    return crearToken("COMENTARIO");
}

// Comentario de múltiples líneas (cerrado)
"/*" [^*]* \*+ ([^*/][^*]* \*+)* "/" {
    return crearToken("COMENTARIO");
}

// Comentario de múltiples líneas (sin cerrar)
"/*" [^*]* \*+ ([^*/][^*]* \*+)* {
    errores++;
    return crearToken("ERROR_COMENTARIO_SIN_CERRAR");
}

// **¡CORRECCIÓN CLAVE! Ahora se devuelve un token "ESPACIO" en lugar de ignorarlo.**
[ \t\r\n\f]+ {
    return crearToken("ESPACIO");
}

";" {
    return crearToken("PUNTO_COMA");
}

"," {
    return crearToken("COMA");
}

"." {
    return crearToken("PUNTO");
}

// Regla de error al final para atrapar cualquier cosa no reconocida.
. {
    errores++;
    return crearToken("ERROR_LEXICO_SIMBOLO_NO_VALIDO");
}