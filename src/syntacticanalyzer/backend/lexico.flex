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

%{
    private ArrayList<TokenValido> tokensValidos = new ArrayList();
    private ArrayList<ErrorLexico> errores = new ArrayList();
%}

NUMERO = [0|(("+"|"-")?[1-9][0-9])]
IDENTIFICADOR = [(_|[a-z]|[A-Z])(a-z|A-Z|0-9|"_"|"-")*]
LITERAL = ["(^|n)"]
COMENTARIO = [//[^](\n)]
LineTerminator = [\r|\n|\t]
SEPARADORES = {LineTerminator}|[ \n\t\r]

%{
    public ArrayList<TokenValido> getTokensValidos() {
        return tokensValidos;
    }

    public ArrayList<ErrorLexico> getErrores() {
        return errores;
    }
%}

%%

/*Reglas Lexicas*/

<YYINITIAL> {
        {SEPARADORES} {/*ignorar*/}
        {IDENTIFICADOR} {tokensValidos.add(new TokenValido(Token.IDENTIFICADOR, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                        return Token.IDENTIFICADOR;}
        {NUMERO} {tokensValidos.add(new TokenValido(Token.NUMERO, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                 return Token.NUMERO;}
        {LITERAL} {tokensValidos.add(new TokenValido(Token.LITERAL, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                  return Token.LITERAL;}
        {COMENTARIO} {tokensValidos.add(new TokenValido(Token.COMENTARIO, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                     return Token.COMENTARIO;}   
        "ESCRIBIR" {tokensValidos.add(new TokenValido(Token.ESCRIBIR, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                   return Token.ESCRIBIR;}
        "FIN" {tokensValidos.add(new TokenValido(Token.FIN, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
              return Token.FIN;}
        "REPETIR" {tokensValidos.add(new TokenValido(Token.REPETIR, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                  return Token.REPETIR;}
        "INICIAR" {tokensValidos.add(new TokenValido(Token.INICIAR, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                  return Token.INICIAR;}
        "SI" {tokensValidos.add(new TokenValido(Token.SI, yytext(), ("("+yyline+","+yycolumn+")")));
             return Token.SI;}
        "VERDADERO" {tokensValidos.add(new TokenValido(Token.VERDADERO, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                    return Token.VERDADERO;}
        "FALSO" {tokensValidos.add(new TokenValido(Token.FALSO, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                return Token.FALSO;}
        "ENTONCES" {tokensValidos.add(new TokenValido(Token.ENTONCES, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                   return Token.ENTONCES;}
}
<YYINITIAL> "+" {tokensValidos.add(new TokenValido(Token.SUMA, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                return Token.SUMA;}
<YYINITIAL> "*" {tokensValidos.add(new TokenValido(Token.MULTIPLICACION, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                return Token..MULTIPLICACION;}
<YYINITIAL> "=" {tokensValidos.add(new TokenValido(Token.IGUAL, yytext(), ("("+(yyline)+","+(yycolumn)+")")));
                return Token.IGUAL;}

[^] {errores.add(new ErrorLexico(Token.ERROR, yytext(), (""+(yyline)+","+(yycolumn)+"")));
    return Token.ERROR;}