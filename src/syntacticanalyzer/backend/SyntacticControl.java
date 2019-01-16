package syntacticanalyzer.backend;

import java.util.ArrayList;
import java.util.Stack;
import syntacticanalyzer.backend.archivos.ControladorArchivo;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorSintactico;
import syntacticanalyzer.backend.lexemas.TokenValido;

public class SyntacticControl {

    private final ControladorArchivo file = new ControladorArchivo();
    private final ArrayList<TokenValido> tokens;
    private ArrayList<ErrorSintactico> errores = new ArrayList();
    private Stack<String> pila = new Stack();

    public SyntacticControl(ArrayList<TokenValido> tokens) {

        this.tokens = tokens;
        pila.push("Z");        
        
    }

    public ArrayList<ErrorSintactico> getErrores() {
        return errores;
    }
}
