package syntacticanalyzer.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CambiosListener implements DocumentListener{

    public CambiosListener() {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        Interfaz.setCambio(false);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        Interfaz.setCambio(false);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        Interfaz.setCambio(false);
    }
    
}
