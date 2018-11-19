package syntacticanalyzer.backend.lexemas;

import syntacticanalyzer.backend.enums.Token;

public class ErrorSintactico {
    private final  Token token;
    private final String lexema;
    private final String posicion;

    public ErrorSintactico(Token token, String lexema, String posicion) {
        this.token = token;
        this.lexema = lexema;
        this.posicion = posicion;
    }

    public String getToken() {
        return token.toString();
    }

    public String getLexema() {
        return lexema;
    }

    public String getPosicion() {
        return posicion;
    }
}
