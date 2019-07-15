package syntacticanalyzer.backend;

import java.util.ArrayList;
import java.util.Stack;
import syntacticanalyzer.backend.archivos.ControladorArchivo;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorSintactico;
import syntacticanalyzer.backend.lexemas.TokenValido;
import syntacticanalyzer.backend.otros.EstadoPila;
import syntacticanalyzer.backend.otros.Identificador;
import syntacticanalyzer.ui.Interfaz;

public class SyntacticControl {

    private final ControladorArchivo file = new ControladorArchivo();
    private final ArrayList<TokenValido> tokens;
    private ArrayList<ErrorSintactico> errores = new ArrayList();
    private ArrayList<EstadoPila> pilas = new ArrayList();
    private ArrayList<Identificador> ids = new ArrayList();
    private Stack<String> pila = new Stack();
    private int[] opciones = new int[10];
    private int opcion = 1;
    private int veces = 1;
    private boolean repeatNumber = false;
    private boolean repeat = false;
    private boolean condicion = true;
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

        //arrayList con los token validos
        this.tokens = tokens;
        //pila que manejara el analisis sintactico
        pila.push("Z");
        //numero total de tokens validos
        int noTokens = tokens.size();
        //estadoActual en el automata de Pila
        int estadoActual = 0;
        //iterador usado para recuperar los datos de los tokens
        int i = 0;
        for (int j = 0; j < opciones.length; j++) {
            opciones[j] = 1;
            
        }
                
        //el bucle se encarga de procesar todos los tokens validos
        while (i < noTokens) {
            //cada caso representa un estado del automata de pila general
            switch (estadoActual) {
                case 0:
                    //Si en la pila hay una Z se agrega el simbolo inicial
                    //y nos movemos al estado 1
                    if (verPila("Z")) {
                        pila.push("S0");
                        estadoActual = 1;
                    }
                    break;
                case 1:
                    //Sin leer nada y con S0 en la pila se desapila y se sustituye
                    //por la primera estructura, en dado caso que no sea la correcta
                    //se resetea la pila y se continua con la siguiente estructura
                    //hasta encontrar una correcta o generar un error
                    if (verPila("S0")) {
                        if (opciones[0] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(),1,i,2,0));
                            pila.pop();
                            pila.push(fin);
                            pila.push("E1");
                            pila.push(escribir);
                        } else if (opciones[0] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,2,0));
                            pila.pop();
                            pila.push("R0*");
                            pila.push(iniciar);
                            pila.push("R1");
                            pila.push(repetir);
                            opciones[0] = 1;
                        }
                    } else if (verPila("E1")) {
                        if (opciones[1] == 1) {
                            //pilas es un array list de; objeto estadoPila
                            //q se usa para guardar el estado actual de la pila
                            //cuando hay varios caminos por tomar
                            //en dado caso el camino no funcionara la pila se 
                            //resetea y prueba con el siguiente camino
                            pilas.add(new EstadoPila(pila.toArray(),1,i,3,1));
                            pila.pop();
                            pila.push(literal);
                        } else if (opciones[1] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,3,1));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[1] == 3) {
                            pilas.add(new EstadoPila(pila.toArray(),3,i,3,1));
                            pila.pop();
                            pila.push(id);
                            opciones[1] = 1;
                        }  
                    } else if (verPila("R1")) {
                        if (opciones[2] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(),1,i,2,2));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[2] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,2,2));
                            pila.pop();
                            pila.push(id);
                            opciones[2] = 1;
                        }
                    } else if (verPila("R0*")) {
                        if (opciones[3] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(),1,i,2,3));
                            pila.pop();
                            pila.push(fin);
                        } else if (opciones[3] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,2,3));
                            pila.pop();
                            pila.push(fin);
                            pila.push("R2");
                            opciones[3] = 1;
                        }
                    } else if (verPila("R2")) {
                        pila.pop();
                        pila.push("R2*");
                        pila.push("R3");
                        pila.push(escribir);
                    } else if (verPila("R2*")) {
                        if (opciones[4] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(),1,i,2,4));
                            pila.pop();
                            pila.push(fin);
                        } else if (opciones[4] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,2,4));
                            pila.pop();
                            pila.push("R2");
                            pila.push(fin);
                            opciones[4] = 1;
                        }
                    } else if (verPila("R3")) {
                        if (opciones[5] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(),1,i,3,5));
                            pila.pop();
                            pila.push(literal);
                        } else if (opciones[5] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(),2,i,3,5));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[5] == 3) {
                            pilas.add(new EstadoPila(pila.toArray(),3,i,3,5));
                            pila.pop();
                            pila.push(id);
                            opciones[5] = 1;
                        }  
                    } else if (compararToken(escribir,i) && verPila(escribir)) {
                        pila.pop();
                        i++;
                    } else if (compararToken(fin,i) && verPila(fin)) {
                        pila.pop();
                        i++;
                        //si es el ultimo token de la lista es necesario disminuir
                        //el indice para que se pueda entrar una vez mas al bucle
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
                        if (repeatNumber) {
                            veces = Integer.parseInt(texto);
                            repeatNumber = false;
                        }
                        i++;
                    } else if (compararToken(id,i) && verPila(id)) {
                        String nameId = tokens.get(i).getLexema();
                        boolean existe = false;
                        for (Identificador idd : ids) {
                            if ((nameId.equals(idd.getNombre()))) {
                                existe = true;
                            }
                        }
                        if (!existe) {
                            ids.add(new Identificador(nameId));
                        }
                        for (Identificador idd : ids) {
                            if (nameId.equals(idd.getNombre())) {
                                if (idd.getValor() == -1) {
                                    texto = nameId;
                                }else{
                                    texto = String.valueOf(idd.getValor());
                                }
                                if (repeatNumber) {
                                    veces = idd.getValor();
                                }
                            }
                        }
                        pila.pop();
                        i++;
                    } else if (compararToken(repetir,i) && verPila(repetir)) {
                        pila.pop();
                        i++;
                        repeatNumber = true;
                    } else if (compararToken(iniciar,i) && verPila(iniciar)) {
                        pila.pop();
                        i++;
                    } else if (verPila("Z")) {
                        //en el estado 1 y con Z en la pila significa que hemos 
                        //reconocido una estructura, por lo cual nos movemos al 
                        //estado final 2, reseteamos el numero de opcion y limpiamos
                        //las pilas si las hay
                        estadoActual = 2;
                        for (int j = 0; j < opciones.length; j++) {
                                opciones[j] = 1;
                            }
                        pilas.clear();
                    } else {
                        //en dado caso no hay ninguna transicion correcta para realizar
                        //si el arrayList pilas no esta vacio entonces se obtienen
                        //los datos de la pila, el indice, numero de opciones y
                        //numero de opcion actual. Se restauran los datos y se pasa
                        //a la siguiente opcion hasta encontrar una opcion correcta 
                        //o generar un error sintactico.
                        if (!pilas.isEmpty()) {
                            int size = pilas.size();
                            int numOptions = pilas.get((size-1)).getNumOptions();
                            int option = pilas.get(size-1).getOption();
                            int index = pilas.get(size-1).getIndex();
                            int idOp = pilas.get(size-1).getIdOpcion();
                            Object[] temp = pilas.get(size-1).getPilaActual();
                            if (option <= numOptions) {
                                i = index;
                                pila.clear();
                                for (Object o : temp) {
                                    pila.push(o.toString());
                                }
                                pilas.remove(size-1);
                                switch (idOp) {
                                    case 0:
                                        opciones[0]++;
                                        break;
                                    case 1:
                                        opciones[1]++;
                                        break;
                                    case 2:
                                        opciones[2]++;
                                        break;
                                    case 3:
                                        opciones[3]++;
                                        break;
                                    case 4:
                                        opciones[4]++;
                                        break;
                                    case 5:
                                        opciones[5]++;
                                        break;
                                }
                            }
                        } else {
                            //crear nuevo error sintactico
                            i++;
                            estadoActual = 0;
                            repeatNumber = false;
                            veces = 1;
                            for (int j = 0; j < opciones.length; j++) {
                                opciones[j] = 1;
                            }
                            pila.clear();
                            pila.push("Z");
                        }
                    }
                    break;
                case 2:
                    System.out.println("Estructura Correcta!");
                    if (!texto.equals("")) {
                        for (int j = 0; j < veces; j++) {
                        System.out.println(texto);
                        file.agregar(Interfaz.getPath(), texto);
                        System.out.println("escribiendo en archivo");
                        }
                    }
                    texto = "";
                    if ((noTokens-i)==1) {
                        i++;
                    }
                    estadoActual = 0;
                    veces = 1;
                    repeatNumber = false;
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
