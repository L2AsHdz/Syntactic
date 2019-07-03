package syntacticanalyzer.backend.otros;

public class Variable {
    private final String nombre;
    private int dato;

    public Variable(String nombre) {
        this.nombre = nombre;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    public int getDato() {
        return dato;
    }
}
