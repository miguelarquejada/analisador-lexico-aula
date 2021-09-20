package lexer;
%%
%class Lex
%type Token
EspacoOuNovaLinha = " "|\r\n|\t
PalavraReservada = "int"|"float"|"if"|"while"|"for"|"boolean"|"string"|"char"|"main"|"return"|"else if"|"else"|"String"|"public"|"protected"|"private"|"class"|"interface"|"static"|"void"|"long"|"short"|"double"|"byte"|"true"|"false"
Digito = \d
Digitos = [-+]?{Digito}*\.?{Digito}+([eE][-+]?{Digito}+)?
Operador = "+" | "-" | "*" | "/" | "%" | "=" | "==" | ">" | "<" | "!=" | ">=" | "<=" | "&&" | "||" | "!" | "+=" | "-=" | "++" | "--"
Caractere = [a-zA-Z_]
Identificador = {Caractere}[{Caractere}{Digito}]*
Simbolo = "(" | ")" | "[" | "]" | "{" | "}" | ";" | ","
Invalido = {Digitos}{Caractere}+
QualquerCaractereExcetoNovaLinha = [^\r\n]
ComentarioFimDeLinha = "//" {QualquerCaractereExcetoNovaLinha}*
ComentarioVariasLinhas = "/*"[^*]*"*/"
Comentario = {ComentarioVariasLinhas} | {ComentarioFimDeLinha}
Texto = \".*\"
%%
<YYINITIAL> {Comentario} {/*não fazer nada*/}
<YYINITIAL> {Invalido} {throw new Error("Identificador invalido: \"" + yytext() + "\" na linha " + yyline + " e coluna " + yycolumn);}
<YYINITIAL> {Texto} {return new Token(yytext(),Type.STRING);}
<YYINITIAL> {PalavraReservada} {return new Token(yytext(),Type.PALAVRA_RESERVADA);}
<YYINITIAL> {Simbolo} {return new Token(yytext(),Type.SIMBOLO);}
<YYINITIAL> {Identificador} {return new Token(yytext(),Type.IDENTIFICADOR);}
<YYINITIAL> {Operador} {return new Token(yytext(),Type.OPERADOR);}
<YYINITIAL> {Digitos} {return new Token(yytext(),Type.CONSTANTE);}
<YYINITIAL> {EspacoOuNovaLinha} {/*não fazer nada*/}
<YYINITIAL> [^] {throw new Error("IllegalExpression \"" + yytext() + "\" na linha " + yyline + " e coluna " + yycolumn);}