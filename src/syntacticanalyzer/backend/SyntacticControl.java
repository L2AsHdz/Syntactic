package syntacticanalyzer.backend;

import java.util.ArrayList;
import java.util.Stack;
import syntacticanalyzer.backend.archivos.ControladorArchivo;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorSintactico;
import syntacticanalyzer.backend.lexemas.TokenValido;
import syntacticanalyzer.backend.variables.Variable;
import syntacticanalyzer.ui.Interfaz;

public class SyntacticControl {

    private final ControladorArchivo file = new ControladorArchivo();
    private final ArrayList<TokenValido> tokens;
    private ArrayList<Variable> variables = new ArrayList();
    private ArrayList<ErrorSintactico> errores = new ArrayList();
    private Stack<String> pila = new Stack();
    private int estadoActual;
    private int veces = 1;
    private boolean errorLexico;
    private boolean seguir;
    private boolean condicion = true;
    private String textoArchivo = "";
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
        errorLexico = false;
        seguir = true;
        estadoActual = 0;
        pila.push("Z");
        int i = 0;

        while (seguir) {
            switch (estadoActual) {
                case 0:
                    if (verPila("Z") && compararToken(escribir, (i))) {
                        pila.push("E0");
                        estadoActual = 1;
                    } else if (verPila("Z") && compararToken(repetir, (i))) {
                        pila.push("R0");
                        estadoActual = 1;
                    } else if (verPila("Z") && compararToken(si, i)) {
                        pila.push("C0");
                        estadoActual = 1;
                    } else if (verPila("Z") && compararToken(id, i) && compararToken(asignacion, (i + 1))) {
                        pila.push("A0");
                        estadoActual = 1;
                        variables.add(new Variable(tokens.get(i).getLexema()));
                    } else {
                        errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                        tokens.remove(i);
                        estadoActual = 0;
                    }
                    break;
                case 1:
                    if (verPila("E0")) {
                        pila.pop();
                        pila.push(fin);
                        pila.push("E1");
                        pila.push(escribir);
                    } else if (verPila("E1")) {
                        if (compararToken(numero, (i))) {
                            pila.pop();
                            pila.push(numero);
                        } else if (compararToken(id, (i))) {
                            pila.pop();
                            pila.push(id);
                        } else if (compararToken(literal, (i))) {
                            pila.pop();
                            pila.push(literal);
                        } else {
                            //agregar manejo de errores
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                            ///////////////////////////
                        }
                    } else if (verPila(escribir)) {
                        desapilar(i, escribir);
                    } else if (verPila(numero)) {
                        if (compararToken(fin, (i + 1))) {
                            textoArchivo = this.tokens.get(i).getLexema();
                        } else if (compararToken(iniciar, (i + 1))) {
                            veces = Integer.parseInt(this.tokens.get(i).getLexema());
                        } else if (compararToken(suma, (i + 1)) || compararToken(mult, (i + 1))) {
                            //opcion para operar las expresiones
                        }
                        desapilar(i, numero);
                    } else if (verPila(id)) {
                        if (compararToken(fin, (i + 1))) {
                            textoArchivo = this.tokens.get(i).getLexema();
                        } else if (compararToken(suma, (i + 1)) || compararToken(mult, (i + 1))) {
                            //aqui va el codigo para operar el valor en el id

                            /////////////////////////////////////////////////
                        }
                        desapilar(i, id);
                    } else if (verPila(literal)) {
                        if (compararToken(fin, (i + 1))) {
                            textoArchivo += this.tokens.get(i).getLexema() + "\n";
                        }
                        desapilar(i, literal);
                    } else if (verPila(fin)) {
                        if (compararToken(fin, i)) {
                            desapilar(i, fin);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                        
                    } else if (verPila("R0")) {
                        pila.pop();
                        pila.push("R0'");
                        pila.push(iniciar);
                        pila.push("R1");
                        pila.push(repetir);
                    } else if (verPila("R0'")) {
                        if (compararToken(escribir, i)) {
                            pila.pop();
                            pila.push(fin);
                            pila.push("R2");
                        } else if (compararToken(fin, i)) {
                            pila.pop();
                            pila.push(fin);
                        }else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("R1")) {
                        if (compararToken(numero, (i))) {
                            pila.pop();
                            pila.push(numero);
                        } else if (compararToken(id, (i))) {
                            pila.pop();
                            pila.push(id);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("R2")) {
                        pila.pop();
                        pila.push("R2'");
                        pila.push("R3");
                        pila.push(escribir);
                    } else if (verPila("R2'")) {
                        if (compararToken(fin, i) && compararToken(escribir, (i + 1))) {
                            pila.pop();
                            pila.push("R2");
                            pila.push(fin);
                        } else if (compararToken(fin, i) && !compararToken(escribir, (i + 1))) {
                            pila.pop();
                            pila.push(fin);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("R3")) {
                        if (compararToken(numero, (i))) {
                            pila.pop();
                            pila.push(numero);
                        } else if (compararToken(id, (i))) {
                            pila.pop();
                            pila.push(id);
                        } else if (compararToken(literal, (i))) {
                            pila.pop();
                            pila.push(literal);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila(repetir)) {
                        desapilar(i, repetir);
                    } else if (verPila(iniciar)) {
                        desapilar(i, iniciar);
                    } else if (verPila("C0")) {
                        pila.pop();
                        pila.push("C0'");
                        pila.push(entonces);
                        pila.push("C1");
                        pila.push(si);
                    } else if (verPila("C1")) {
                        if (compararToken(verdadero, (i))) {
                            pila.pop();
                            pila.push(verdadero);
                        } else if (compararToken(falso, (i))) {
                            pila.pop();
                            pila.push(falso);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("C0'")) {
                        if (compararToken(escribir, i)) {
                            pila.pop();
                            pila.push(fin);
                            pila.push("C2");
                        } else if (compararToken(fin, i)) {
                            pila.pop();
                            pila.push(fin);
                        }else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("C2")) {
                        pila.pop();
                        pila.push(fin);
                        pila.push("C3");
                        pila.push(escribir);
                    } else if (verPila("C3")) {
                        if (compararToken(numero, (i))) {
                            pila.pop();
                            pila.push(numero);
                        } else if (compararToken(id, (i))) {
                            pila.pop();
                            pila.push(id);
                        } else if (compararToken(literal, (i))) {
                            pila.pop();
                            pila.push(literal);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila(si)) {
                        desapilar(i, si);
                    } else if (verPila(entonces)) {
                        desapilar(i, entonces);
                    } else if (verPila(verdadero)) {
                        condicion = true;
                        desapilar(i, verdadero);
                    } else if (verPila(falso)) {
                        condicion = false;
                        desapilar(i, falso);
                    } else if (verPila("A0")) {
                        pila.pop();
                        pila.push(fin);
                        pila.push("EX0");
                        pila.push(asignacion);
                        pila.push(id);
                    } else if (verPila("EX0")) {
                        if (compararToken(pa, i)) {
                            pila.pop();
                            pila.push("EX0'");
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                            pila.push(pa);
                        } else if (compararToken(numero, i) || compararToken(id, i)) {
                            pila.pop();
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("EX0'")) {
                        if (compararToken(suma, (i + 1)) || compararToken(mult, (i + 1))) {
                            pila.pop();
                            pila.push("EX0");
                            pila.push("EX2");
                            pila.push(pc);
                        } else if (compararToken(pc, i)) {
                            pila.pop();
                            pila.push(pc);
                        }
                    } else if (verPila("EX1")) {
                        if (compararToken(numero, (i))) {
                            pila.pop();
                            pila.push(numero);
                        } else if (compararToken(id, (i))) {
                            pila.pop();
                            pila.push(id);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("EX2")) {
                        if (compararToken(suma, (i))) {
                            pila.pop();
                            pila.push(suma);
                        } else if (compararToken(mult, (i))) {
                            pila.pop();
                            pila.push(mult);
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila("EX3")) {
                        if (compararToken(numero, (i)) && !compararToken(suma, (i + 1))
                                && !compararToken(mult, (i + 1))) {
                            pila.pop();
                            pila.push(numero);
                        } else if (compararToken(id, (i)) && !compararToken(suma, (i + 1))
                                && !compararToken(mult, (i + 1))) {
                            pila.pop();
                            pila.push(id);
                        } else if (compararToken(pa, i)) {
                            pila.pop();
                            pila.push("EX0'");
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                            pila.push(pa);
                        } else if (compararToken(suma, (i + 1)) || compararToken(mult, (i + 1))) {
                            pila.pop();
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                        } else {
                            errorLexico = true;
                            errores.add(new ErrorSintactico(tokens.get(i).getToken(), 
                                tokens.get(i).getLexema(), 
                                tokens.get(i).getPosicion()));
                            pila.clear();
                            pila.push("Z");
                            tokens.remove(i);
                            estadoActual = 0;
                        }
                    } else if (verPila(asignacion)) {
                        desapilar(i, asignacion);
                    } else if (verPila(suma)) {
                        desapilar(i, suma);
                    } else if (verPila(mult)) {
                        desapilar(i, mult);
                    } else if (verPila(pa)) {
                        desapilar(i, pa);
                    } else if (verPila(pc)) {
                        desapilar(i, pc);
                    } else if (verPila("Z")) {
                        estadoActual = 2;
                    }
                    break;
                case 2:
                    System.out.println("Estructura correcta");
                    if (!textoArchivo.equals("") && condicion) {
                        for (int j = 0; j < veces; j++) {
                            System.out.println(textoArchivo);
                            file.agregar(Interfaz.getPath(), textoArchivo);
                        }
                    }
                    System.out.println("\n");
                    veces = 1;
                    textoArchivo = "";
                    condicion = true;
                    estadoActual = 0;
                    if (this.tokens.isEmpty()) {
                        seguir = false;
                    }
                    break;
            }
        }
        System.out.println("analisis terminado");
    }

    private boolean verPila(String txt) {
        return pila.peek().equals(txt);
    }

    private boolean compararToken(String txt, int i) {
        return this.tokens.get(i).getNombreToken().equals(txt);
    }

    private void desapilar(int i, String token) {
        if (compararToken(token, i)) {
            System.out.println("desapilando: " + token);
            pila.pop();
            this.tokens.remove(i);
        } else {
            errorLexico = true;
        }
    }

    public ArrayList<ErrorSintactico> getErrores() {
        return errores;
    }
}
