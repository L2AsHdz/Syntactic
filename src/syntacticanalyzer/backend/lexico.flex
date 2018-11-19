/*Codigo de usuario*/
package syntacticanalyzer.backend;

import syntacticanalyzer.backend.lexemas.TokenValido;
import syntacticanalyzer.backend.lexemas.ErrorLexico;
import syntacticanalyzer.backend.enums.Token;
import java.util.ArrayList;

%%

/* Declaraciones y opciones JFlex*/

%class Lexer
%public
%type Token
%unicode
%line
%column

LETRA = [a-zA-z]
DIGITO = [0-9]
DIGITO2 = [1-9]
SEPARADORES = [ \t\r\n]

%{
    private StringBuffer string = new StringBuffer();
    private String lexema;
    
    public String getLexema() {
        return lexema;
    }
    public int getyyline() {
        return yyline;
    }

    public int getyycolumn() {
        return yycolumn;
    }
%}

%state STRING

%%

/*Reglas lexicas*/

<YYINITIAL> "ESCRIBIR"                      {return Token.ESCRIBIR;}     
<YYINITIAL> "FIN"                           {return Token.FIN;}                  
<YYINITIAL> "REPETIR"                       {return Token.REPETIR;}                  
<YYINITIAL> "INICIAR"                       {return Token.INICIAR;}                  
<YYINITIAL> "SI"                            {return Token.SI;}             
<YYINITIAL> "VERDADERO"                     {return Token.VERDADERO;}                  
<YYINITIAL> "FALSO"                         {return Token.FALSO;}                  
<YYINITIAL> "ENTONCES"                      {return Token.ENTONCES;}             


<YYINITIAL> {
    {SEPARADORES}                           {/*ignorar*/}
    ({LETRA}|"_")({LETRA}|{DIGITO}|"-"|"_")* {lexema = yytext(); return Token.IDENTIFICADOR;}
    "0"|("+"|"-")?({DIGITO2}{DIGITO}*)      {lexema = yytext(); return Token.NUMERO;}
    \"                                      {string.setLength(0); yybegin(STRING);}
    "//"[^\n]*                              {lexema = yytext(); return Token.COMENTARIO;}
    "+"                                     {return Token.SUMA;}
    "*"                                     {return Token.MULTIPLICACION;}
    "="                                     {return Token.ASIGNACION;}
    "("                                     {return Token.PA;}
    ")"                                     {return Token.PC;}
}

<STRING> {
    \"                                      { yybegin(YYINITIAL); 
                                            lexema = string.toString(); return Token.LITERAL;}
    [^\n\r\"\\]+                            { string.append( yytext() ); }
    \\t                                     { string.append('\t'); }
    \\n                                     { string.append('\n'); }
    \\r                                     { string.append('\r'); }
    \\\"                                    { string.append('\"'); }
    \\                                      { string.append('\\'); }
}
[^]                                         {lexema = yytext(); return Token.ERROR;}
