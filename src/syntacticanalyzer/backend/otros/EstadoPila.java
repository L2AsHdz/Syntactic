package syntacticanalyzer.backend.otros;

import java.util.Stack;

public class EstadoPila {
    private final Stack pilaActual;
    private final int id;
    private final int index;
    private int noCaminos;

    public EstadoPila(Stack pilaActual, int id, int index, int noCaminos) {
        this.pilaActual = pilaActual;
        this.id = id;
        this.index = index;
        this.noCaminos = noCaminos;
    }

    public int getNoCaminos() {
        return noCaminos;
    }

    public void setNoCaminos(int noCaminos) {
        this.noCaminos = noCaminos;
    }

    public Stack getPilaActual() {
        return pilaActual;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }
}
