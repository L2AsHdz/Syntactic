package syntacticanalyzer.backend;

import java.io.StringReader;
import java.util.ArrayList;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorLexico;
import syntacticanalyzer.backend.lexemas.TokenValido;

public class LexicControl {
    
    private Lexer analizador;
    private ArrayList<TokenValido> tokensValidos = new ArrayList();
    private ArrayList<ErrorLexico> errores = new ArrayList();


    public LexicControl(String texto) {
        analizador = new Lexer(new StringReader(texto));
        boolean seguir = true;
        
        while(seguir){
            try {
                Token token = analizador.yylex();
                String nameToken = token.toString();
                String lexema = analizador.getLexema();
                String lexema2 = analizador.yytext();
                int linea = analizador.getyyline()+1;
                int columna = analizador.getyycolumn()+1;
                String posicion = "("+linea+","+columna+")";
                
                if (token == null) {
                    break;
                }
                switch (token) {
                    case ERROR:
                        errores.add(new ErrorLexico(token, lexema2, posicion));
                        System.out.println("Lexema Error: "+lexema2+" "
                                + "Posicion: "+posicion);
                        break;
                    case IDENTIFICADOR:
                    case NUMERO:
                    case COMENTARIO:
                    case LITERAL:
                        tokensValidos.add(new TokenValido(token, lexema, posicion));
                        System.out.println("Token: "+nameToken+" Lexema: "+lexema+" "
                                + "Posicion: "+posicion);
                        break;
                    default:
                        tokensValidos.add(new TokenValido(token, lexema2, posicion));
                        System.out.println("Token: "+nameToken+" Lexema: "+lexema2+" "
                                + "Posicion: "+posicion);
                        break;
                }
            } catch (Exception e) {
                break;
            }
        }
    }

    public ArrayList<TokenValido> getTokensValidos() {
        return tokensValidos;
    }

    public ArrayList<ErrorLexico> getErrores() {
        return errores;
    }
}
