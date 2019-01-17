package syntacticanalyzer.backend;

import java.util.ArrayList;
import java.util.Stack;
import syntacticanalyzer.backend.archivos.ControladorArchivo;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorSintactico;
import syntacticanalyzer.backend.lexemas.TokenValido;
import syntacticanalyzer.ui.Interfaz;

public class SyntacticControl {

    private final ControladorArchivo file = new ControladorArchivo();
    private final ArrayList<TokenValido> tokens;
    private ArrayList<ErrorSintactico> errores = new ArrayList();
    private Stack<String> pila = new Stack();
    private Stack<String> tempStack = new Stack();
    private boolean seguir = true;
    private int opcion = 0;
    private String texto = "";
    private String escribir = Token.ESCRIBIR.toString();
    private String id = Token.IDENTIFICADOR.toString();
    private String literal = Token.LITERAL.toString();
    private String numero = Token.NUMERO.toString();
    private String fin = Token.FIN.toString();
    private String repetir = Token.REPETIR.toString();
    private String iniciar = Token.INICIAR.toString();
    private String si = Token.SI.toString();
    private String entonces = Token.ENTONCES.toString();
    private String verdadero = Token.VERDADERO.toString();
    private String falso = Token.FALSO.toString();
    private String asignacion = Token.ASIGNACION.toString();
    private String suma = Token.SUMA.toString();
    private String mult = Token.MULTIPLICACION.toString();
    private String pa = Token.PA.toString();
    private String pc = Token.PC.toString();

    public SyntacticControl(ArrayList<TokenValido> tokens) {

        this.tokens = tokens;
        pila.push("Z");

        
        while (!tokens.isEmpty()) {
            
            if (verPila("Z")) {
            pila.push("E0");
            } else if (verPila("E0")) {
                pila.pop();
                pila.push(fin);
                pila.push("E1");
                pila.push(escribir);
            } else if (verPila("E1")) {
                tempStack = pila;
                pila.pop();
                if (opcion == 0) {
                    pila.push(literal);
                }else if (opcion == 1) {
                    pila.push(numero);
                }else if (opcion == 2) {
                    pila.push(id);
                }
            } else if (compararToken(escribir) && verPila(escribir)) {
                pila.pop();
                tokens.remove(0);
            } else if (compararToken(literal) && verPila(literal)) {
                pila.pop();
                texto = tokens.get(0).getLexema();
                tokens.remove(0);
            } else if (compararToken(numero) && verPila(numero)) {
                pila.pop();
                texto = tokens.get(0).getLexema();
                tokens.remove(0);
            } else if (compararToken(id) && verPila(id)) {
                pila.pop();
                texto = tokens.get(0).getLexema();
                tokens.remove(0);
            } else if (compararToken(fin) && verPila(fin)) {
                pila.pop();
                tokens.remove(0);
            }else if (verPila("Z")) {
                System.out.println(texto);
                //file.agregar(Interfaz.getPath(), texto);
            } else {
                opcion++;
                pila = tempStack;
                if (opcion == 3) {
                    pila.clear();
                    pila.push("E0");
                    tokens.remove(0);
                }
            }
        }

    }

    private boolean verPila(String txt) {
        return pila.peek().equals(txt);
    }

    private boolean compararToken(String txt) {
        return this.tokens.get(0).getNombreToken().equals(txt);
    }
    
    public ArrayList<ErrorSintactico> getErrores() {
        return errores;
    }
}
