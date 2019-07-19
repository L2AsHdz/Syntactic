package syntacticanalyzer.backend;

import java.util.ArrayList;
import java.util.Stack;
import syntacticanalyzer.backend.archivos.ControladorArchivo;
import syntacticanalyzer.backend.enums.Token;
import syntacticanalyzer.backend.lexemas.ErrorSintactico;
import syntacticanalyzer.backend.lexemas.TokenValido;
import syntacticanalyzer.backend.otros.EstadoPila;
import syntacticanalyzer.backend.otros.Identificador;
import syntacticanalyzer.backend.otros.OperadorExpresion;
import syntacticanalyzer.ui.Interfaz;

public class SyntacticControl {

    private final ControladorArchivo file = new ControladorArchivo();
    private final ArrayList<TokenValido> tokens;
    private ArrayList<ErrorSintactico> errores = new ArrayList();
    private ArrayList<EstadoPila> pilas = new ArrayList();
    private ArrayList<Identificador> ids = new ArrayList();
    private ArrayList<String> expression2 = new ArrayList();
    private Stack<String> pila = new Stack();
    private int[] opciones = new int[14];
    private int veces = 1;
    private boolean repeatNumber = false;
    private boolean repeat = false;
    private boolean condicion = true;
    private boolean startExpression = false;
    private boolean allOptionsTested = false;
    private String texto = "";
    private String idToAssign = "";
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
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 4, 0));
                            pila.pop();
                            pila.push(fin);
                            pila.push("E1");
                            pila.push(escribir);
                        } else if (opciones[0] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(), 2, i, 4, 0));
                            pila.pop();
                            pila.push("R0*");
                            pila.push(iniciar);
                            pila.push("R1");
                            pila.push(repetir);
                        } else if (opciones[0] == 3) {
                            pilas.add(new EstadoPila(pila.toArray(), 3, i, 4, 0));
                            pila.pop();
                            pila.push("C0*");
                            pila.push(entonces);
                            pila.push("C1");
                            pila.push(si);
                        } else if (opciones[0] == 4) {
                            pila.pop();
                            pila.push(fin);
                            pila.push("EX0");
                            pila.push(asignacion);
                            pila.push(id);
                            allOptionsTested = true;
                        }
                    } else if (verPila("E1")) {
                        if (opciones[1] == 1) {
                            //pilas es un array list del objeto estadoPila
                            //que se usa para guardar el estado actual de la pila
                            //cuando hay varios caminos por tomar
                            //en dado caso el camino no funcionara la pila se 
                            //resetea y prueba con el siguiente camino
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 3, 1));
                            pila.pop();
                            pila.push(literal);
                        } else if (opciones[1] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(), 2, i, 3, 1));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[1] == 3) {
                            pila.pop();
                            pila.push(id);
                            allOptionsTested = true;
                        }
                    } else if (verPila("R1")) {
                        if (opciones[2] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 2));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[2] == 2) {
                            pila.pop();
                            pila.push(id);
                        }
                        allOptionsTested = true;
                    } else if (verPila("R0*")) {
                        if (opciones[3] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 3));
                            pila.pop();
                            pila.push(fin);
                            pila.push("R2");
                        } else if (opciones[3] == 2) {
                            pila.pop();
                            pila.push(fin);
                            allOptionsTested = true;
                        }
                    } else if (verPila("R2")) {
                        pila.pop();
                        pila.push("R2*");
                        pila.push("R3");
                        pila.push(escribir);
                        allOptionsTested = true;
                    } else if (verPila("R2*")) {
                        if (opciones[4] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 4));
                            pila.pop();
                            pila.push(fin);
                        } else if (opciones[4] == 2) {
                            pila.pop();
                            pila.push("R2");
                            pila.push(fin);
                            allOptionsTested = true;
                        }
                    } else if (verPila("R3")) {
                        if (opciones[5] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 3, 5));
                            pila.pop();
                            pila.push(literal);
                        } else if (opciones[5] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(), 2, i, 3, 5));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[5] == 3) {
                            pila.pop();
                            pila.push(id);
                            allOptionsTested = true;
                        }
                    } else if (verPila("C1")) {
                        if (opciones[6] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 6));
                            pila.pop();
                            pila.push(verdadero);
                        } else if (opciones[6] == 2) {
                            pila.pop();
                            pila.push(falso);
                            opciones[6] = 1;
                            allOptionsTested = true;
                        }
                    } else if (verPila("C0*")) {
                        if (opciones[7] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 7));
                            pila.pop();
                            pila.push(fin);
                            pila.push("C2");
                        } else if (opciones[7] == 2) {
                            pila.pop();
                            pila.push(fin);
                            allOptionsTested = true;
                        }
                    } else if (verPila("C2")) {
                        pila.pop();
                        pila.push(fin);
                        pila.push("C3");
                        pila.push(escribir);
                        allOptionsTested = true;
                    } else if (verPila("C3")) {
                        if (opciones[8] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 3, 8));
                            pila.pop();
                            pila.push(literal);
                        } else if (opciones[8] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(), 2, i, 3, 8));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[5] == 3) {
                            pila.pop();
                            pila.push(id);
                            allOptionsTested = true;
                        }
                    } else if (verPila("EX0")) {
                        if (opciones[9] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 9));
                            pila.pop();
                            pila.push("EX0*");
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                            pila.push(pa);
                        } else if (opciones[9] == 2) {
                            pila.pop();
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                            allOptionsTested = true;
                        }
                    } else if (verPila("EX0*")) {
                        if (opciones[10] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 10));
                            pila.pop();
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push(pc);
                        } else if (opciones[10] == 2) {
                            pila.pop();
                            pila.push(pc);
                            allOptionsTested = true;
                        }
                    } else if (verPila("EX3")) {
                        if (opciones[11] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 4, 11));
                            pila.pop();
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                        } else if (opciones[11] == 2) {
                            pilas.add(new EstadoPila(pila.toArray(), 2, i, 4, 11));
                            pila.pop();
                            pila.push("EX0*");
                            pila.push("EX3");
                            pila.push("EX2");
                            pila.push("EX1");
                            pila.push(pa);
                        } else if (opciones[11] == 3) {
                            pilas.add(new EstadoPila(pila.toArray(), 3, i, 4, 11));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[11] == 4) {
                            pila.pop();
                            pila.push(id);
                            allOptionsTested = true;
                        }
                    } else if (verPila("EX2")) {
                        if (opciones[12] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 12));
                            pila.pop();
                            pila.push(suma);
                        } else if (opciones[12] == 2) {
                            pila.pop();
                            pila.push(mult);
                            allOptionsTested = true;
                            if (startExpression) {
                                opciones[12] = 1;
                            }
                        }
                    } else if (verPila("EX1")) {
                        if (opciones[13] == 1) {
                            pilas.add(new EstadoPila(pila.toArray(), 1, i, 2, 13));
                            pila.pop();
                            pila.push(numero);
                        } else if (opciones[13] == 2) {
                            pila.pop();
                            pila.push(id);
                            allOptionsTested = true;
                            if (startExpression) {
                                opciones[13] = 1;
                            }
                        }
                    } else if (compararToken(escribir, i) && verPila(escribir)) {
                        pila.pop();
                        i++;
                    } else if (compararToken(fin, i) && verPila(fin)) {
                        pila.pop();
                        i++;
                        //si es el ultimo token de la lista es necesario disminuir
                        //el indice para que se pueda entrar una vez mas al bucle
                        if ((noTokens - i) == 0) {
                            i--;
                        }
                        for (int j = 0; j < 14; j++) {
                            opciones[j] =1;
                        }
                        if (startExpression) {
                            for (String s : expression2) {
                                System.out.print(s + " ");
                            }
                            for (Identificador idd : ids) {
                                if (idToAssign.equals(idd.getNombre())) {
                                    idd.setValor(OperadorExpresion.Operar(expression2));
                                }
                            }
                            startExpression = false;
                            //texto = "";
                            condicion = false;
                        }
                    } else if (compararToken(literal, i) && verPila(literal)) {
                        //si la variable repeat es true significa que estamos dentro
                        //de una posible estructura REPETIR por lo cual el texto se
                        //tiene que acumular. De lo contrario puede ser cualquier otra
                        //estructura y el texto se reemplaza
                        if (repeat) {
                            texto += tokens.get(i).getLexema();
                        } else {
                            texto = tokens.get(i).getLexema();
                        }
                        pila.pop();
                        i++;
                    } else if (compararToken(numero, i) && verPila(numero)) {
                        if (repeat) {
                            texto += tokens.get(i).getLexema();
                        } else {
                            texto = tokens.get(i).getLexema();
                        }
                        if (startExpression) {
                            if (expression2.isEmpty()) {
                                expression2.add(tokens.get(i).getLexema());
                            } else if (!OperadorExpresion.isNumeric(expression2.get(expression2.size()-1 ))) {
                                expression2.add(tokens.get(i).getLexema());
                            }
                        }
                        pila.pop();
                        //si la variable repeatNumber es true significa que es
                        //una posible estructura REPETIR por lo cual el primer numero
                        //encontrado es la cantidad de veces que se repetira el texto
                        if (repeatNumber) {
                            veces = Integer.parseInt(texto);
                            texto = "";
                            repeatNumber = false;
                        }
                        i++;
                    } else if (compararToken(id, i) && verPila(id)) {
                        String nameId = tokens.get(i).getLexema();
                        boolean existe = false;
                        //Verifica si el identificador ya existe
                        for (Identificador idd : ids) {
                            if ((nameId.equals(idd.getNombre()))) {
                                existe = true;
                            }
                        }
                        //si no existe el identificador crea un nuevo objeto
                        //con el nombre dado y el valor predeterminado
                        if (!existe) {
                            ids.add(new Identificador(nameId));
                        }
                        //Busca el identificador en el ArrayList, si el valor sigue
                        //siendo el predeterminado entonces el texto a imprimir sera
                        //el nombre del identificador. Si el valor es distinto el
                        //texto a imprimir sera el valor que tenga el identificador
                        for (Identificador idd : ids) {
                            if (nameId.equals(idd.getNombre())) {
                                if (idd.getValor() == -1) {
                                    if (repeat) {
                                        texto += nameId;
                                    } else {
                                        texto = nameId;
                                    }
                                } else {
                                    if (repeat) {
                                        texto += String.valueOf(idd.getValor());
                                    } else {
                                        texto = String.valueOf(idd.getValor());
                                    }
                                    if (startExpression) {
                                        if (expression2.isEmpty()) {
                                            expression2.add(String.valueOf(idd.getValor()));
                                            opciones[11] = 1;
                                        } else if (!OperadorExpresion.isNumeric(expression2.get(expression2.size()-1 ))) {
                                            expression2.add(String.valueOf(idd.getValor()));
                                            opciones[11] = 1;
                                        }
                                    }
                                }
                                if (repeatNumber) {
                                    veces = idd.getValor();
                                }
                            }
                        }
                        pila.pop();
                        i++;
                    } else if (compararToken(repetir, i) && verPila(repetir)) {
                        pila.pop();
                        i++;
                        repeatNumber = true;
                        repeat = true;
                    } else if (compararToken(iniciar, i) && verPila(iniciar)) {
                        pila.pop();
                        i++;
                    } else if (compararToken(si, i) && verPila(si)) {
                        pila.pop();
                        i++;
                    } else if (compararToken(verdadero, i) && verPila(verdadero)) {
                        pila.pop();
                        i++;
                        condicion = true;
                    } else if (compararToken(falso, i) && verPila(falso)) {
                        pila.pop();
                        i++;
                        condicion = false;
                    } else if (compararToken(entonces, i) && verPila(entonces)) {
                        pila.pop();
                        i++;
                    } else if (compararToken(mult, i) && verPila(mult)) {
                        pila.pop();
                        if (startExpression) {
                            expression2.add(tokens.get(i).getLexema());
                            //opciones[12] = 1;
                        }
                        i++;
                    } else if (compararToken(suma, i) && verPila(suma)) {
                        pila.pop();
                        if (startExpression) {
                            expression2.add(tokens.get(i).getLexema());
                        }
                        i++;
                    } else if (compararToken(pa, i) && verPila(pa)) {
                        pila.pop();
                        if (startExpression) {
                            expression2.add(tokens.get(i).getLexema());
                            opciones[11] = 1;
                        }
                        i++;
                    } else if (compararToken(pc, i) && verPila(pc)) {
                        pila.pop();
                        if (startExpression) {
                            expression2.add(tokens.get(i).getLexema());
                        }
                        i++;
                    } else if (compararToken(asignacion, i) && verPila(asignacion)) {
                        idToAssign = tokens.get(i-1).getLexema();
                        pila.pop();
                        i++;
                        startExpression = true;
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
                            int numOptions = pilas.get((size - 1)).getNumOptions();
                            int option = pilas.get(size - 1).getOption();
                            int index = pilas.get(size - 1).getIndex();
                            int idOp = pilas.get(size - 1).getIdOpcion();
                            Object[] temp = pilas.get(size - 1).getPilaActual();
                            if (option <= numOptions) {
                                i = index;
                                pila.clear();
                                for (Object o : temp) {
                                    pila.push(o.toString());
                                }
                                pilas.remove(size - 1);
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
                                    case 6:
                                        opciones[6]++;
                                        break;
                                    case 7:
                                        opciones[7]++;
                                        break;
                                    case 8:
                                        opciones[8]++;
                                        break;
                                    case 9:
                                        opciones[9]++;
                                        break;
                                    case 10:
                                        opciones[10]++;
                                        break;
                                    case 11:
                                        opciones[11]++;
                                        break;
                                    case 12:
                                        opciones[12]++;
                                        break;
                                    case 13:
                                        opciones[13]++;
                                        break;
                                }
                            }
                        } else if (allOptionsTested){
                            //crear nuevo error sintactico
                            i++;
                            estadoActual = 0;
                            repeatNumber = false;
                            repeat = false;
                            startExpression = false;
                            veces = 1;
                            for (int j = 0; j < opciones.length; j++) {
                                opciones[j] = 1;
                            }
                            pila.clear();
                            pila.push("Z");
                            System.out.println("HUBO UN ERROR SINTACTICO EN " + i);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Estructura Correcta!");
                    if (!texto.equals("") && condicion) {
                        for (int j = 0; j < veces; j++) {
                            System.out.println(texto);
                            file.agregar(Interfaz.getPath(), texto);
                            System.out.println("escribiendo en archivo");
                        }
                    }
                    texto = "";
                    if ((noTokens - i) == 1) {
                        i++;
                    }
                    estadoActual = 0;
                    veces = 1;
                    repeatNumber = false;
                    repeat = false;
                    startExpression = false;
                    condicion = true;
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
