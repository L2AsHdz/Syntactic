package syntacticanalyzer.backend.otros;


public class EstadoPila {
    private final Object[] pilaActual;
    private final int option;
    private final int index;
    private int numOptions;

    public EstadoPila(Object[] pilaActual, int option, int index, int numOptions) {
        this.pilaActual = pilaActual;
        this.option = option;
        this.index = index;
        this.numOptions = numOptions;
    }

    public int getNumOptions() {
        return numOptions;
    }

    public void setNumOptions(int numOptions) {
        this.numOptions = numOptions;
    }

    public Object[] getPilaActual() {
        return pilaActual;
    }

    public int getOption() {
        return option;
    }

    public int getIndex() {
        return index;
    }
}
