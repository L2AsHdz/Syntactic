package syntacticanalyzer.backend;

import java.util.ArrayList;
import java.util.Stack;
import syntacticanalyzer.backend.archivos.ControladorArchivo;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorSintactico;
import syntacticanalyzer.backend.lexemas.TokenValido;
import syntacticanalyzer.backend.otros.EstadoPila;
import syntacticanalyzer.ui.Interfaz;

public class SyntacticControl {

    private final ControladorArchivo file = new ControladorArchivo();
    private final ArrayList<TokenValido> tokens;
    private ArrayList<ErrorSintactico> errores = new ArrayList();
    private ArrayList<EstadoPila> pilas = new ArrayList();
    private Stack<String> pila = new Stack();
    private int opcion = 1;
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
        int noTokens = tokens.size();
        int estadoActual = 0;
        int i = 0;
                
        while (i < noTokens) {
            switch (estadoActual) {
                case 0:
                    if (verPila("Z")) {
                        pila.push("S0");
                        estadoActual = 1;
                    }
                    break;
                case 1:
                    if (verPila("S0")) {
                        pila.pop();
                        pila.push(fin);
                        pila.push("S1");
                        pila.push(escribir);
                    } else if (verPila("S1")) {
                        if (opcion == 1) {
                            pilas.add(new EstadoPila(pila.toArray(),1,i,3));
                            pila.pop();
                            pila.push(literal);
                        } else if (opcion == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,3));
                            pila.pop();
                            pila.push(numero);
                        } else if (opcion == 3) {
                            pilas.add(new EstadoPila(pila.toArray(),3,i,3));
                            pila.pop();
                            pila.push(id);
                        }  
                    } else if (compararToken(escribir,i) && verPila(escribir)) {
                        pila.pop();
                        i++;
                    } else if (compararToken(fin,i) && verPila(fin)) {
                        pila.pop();
                        i++;
                        if ((noTokens-i) == 0) {
                            i--;
                        }
                    } else if (compararToken(literal,i) && verPila(literal)) {
                        texto = tokens.get(i).getLexema();
                        pila.pop();
                        i++;
                    } else if (compararToken(numero,i) && verPila(numero)) {
                        texto = tokens.get(i).getLexema();
                        pila.pop();
                        i++;
                    } else if (compararToken(id,i) && verPila(id)) {
                        texto = tokens.get(i).getLexema();
                        pila.pop();
                        i++;
                    } else if (verPila("Z")) {
                        estadoActual = 2;
                        opcion = 1;
                        pilas.clear();
                    } else {
                        if (!pilas.isEmpty()) {
                            int size = pilas.size();
                            int noCaminos = pilas.get((size-1)).getNoCaminos();
                            int ids = pilas.get(size-1).getId();
                            int index = pilas.get(size-1).getIndex();
                            Object[] temp = pilas.get(size-1).getPilaActual();
                            if (ids <= noCaminos) {
                                i = index;
                                pila.clear();
                                for (Object o : temp) {
                                    pila.push(o.toString());
                                }
                                pilas.remove(size-1);
                                opcion++;
                            }
                        }
                    }
                    break;
                case 2:
                    System.out.println("Estructura Correcta!");
                    if (!texto.equals("")) {
                        System.out.println(texto);
                        file.agregar(Interfaz.getPath(), texto);
                        System.out.println("escribiendo en archivo");
                    }
                    texto = "";
                    if ((noTokens-i)==1) {
                        i++;
                    }
                    estadoActual = 0;
                    break;
            }
        }
    }

    private boolean verPila(String txt) {
        return pila.peek().equals(txt);
    }

    private boolean compararToken(String txt, int i) {
        return this.tokens.get(i).getNombreToken().equals(txt);
    }
    
    public ArrayList<ErrorSintactico> getErrores() {
        return errores;
    }
}
