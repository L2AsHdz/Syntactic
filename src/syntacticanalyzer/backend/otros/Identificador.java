package syntacticanalyzer.backend.otros;

public class Identificador {
    private final String nombre;
    private int valor = -1;

    public Identificador(String nombre) {
        this.nombre = nombre;
    }

    public Identificador(String nombre, int dato) {
        this.nombre = nombre;
        this.valor = dato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
