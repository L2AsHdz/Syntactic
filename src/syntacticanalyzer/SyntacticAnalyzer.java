package syntacticanalyzer;

import javax.swing.JFrame;
import syntacticanalyzer.ui.Interfaz;

public class SyntacticAnalyzer {

    public static void main(String[] args) {
        Interfaz ui = new Interfaz();
        ui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ui.setLocationRelativeTo(null);
        ui.setTitle("Analizador Lexico");
        ui.setVisible(true);
    }
    
}
