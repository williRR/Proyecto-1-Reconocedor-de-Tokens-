package codigo;

%%

%class Lexer
%unicode
%public
%type String
%line
%column

%{
  // Contadores globales
  public int reservadas = 0;
  public int variables = 0;
  public int llaves = 0;
  public int parentesis = 0;
  public int errores = 0;
%}

// ======================
// Expresiones regulares
// ======================
DIGITO     = [0-9]
LETRA      = [a-zA-Z_]
ID         = {LETRA}({LETRA}|{DIGITO})*

RESERVADAS = ("abstract"|"as"|"base"|"bool"|"break"|"byte"|"case"|"catch"|"char"|"checked"|"class"|"const"|"continue"|
              "decimal"|"default"|"delegate"|"do"|"double"|"else"|"enum"|"event"|"explicit"|"extern"|
              "false"|"finally"|"fixed"|"float"|"for"|"foreach"|"goto"|"if"|"implicit"|"in"|"int"|"interface"|
              "internal"|"is"|"lock"|"long"|"namespace"|"new"|"null"|"object"|"operator"|"out"|
              "override"|"params"|"private"|"protected"|"public"|"readonly"|"ref"|"return"|
              "sbyte"|"sealed"|"short"|"sizeof"|"stackalloc"|"static"|"string"|"struct"|"switch"|
              "this"|"throw"|"true"|"try"|"typeof"|"uint"|"ulong"|"unchecked"|"unsafe"|"ushort"|"using"|
              "virtual"|"void"|"volatile"|"while")

// ======================
// Reglas del analizador
// ======================
%%

{RESERVADAS} {
    reservadas++;
    return "RESERVADA(" + yytext() + ")";
}

{ID} {
    variables++;
    return "IDENTIFICADOR(" + yytext() + ")";
}

"{" {
    llaves++;
    return "LLAVE_ABIERTA";
}

"(" {
    parentesis++;
    return "PARENTESIS_ABIERTO";
}

\"([^\"\\]|\\.)*\" {
    return "CADENA";
}

// Cadena mal cerrada (error)
\"([^\"\\]|\\.)* {
    errores++;
    return "ERROR_CADENA";
}

// Números
{DIGITO}+ {
    return "NUMERO(" + yytext() + ")";
}

// Comentarios de línea //
"//".* { /* ignorar */ }

// Comentarios de bloque /* ... */
"/*"([^*]|\*+[^*/])*\*+"/" { /* ignorar */ }

// Espacios y saltos de línea
[ \t\r\n\f]+ { /* ignorar */ }

// Cualquier otro símbolo
. { return "OTRO(" + yytext() + ")"; }
