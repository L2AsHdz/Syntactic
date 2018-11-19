package syntacticanalyzer.backend.archivos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ControladorArchivo {

    public ControladorArchivo() {
    }

    public String readFile(String path) {
        String texto = "";
        ArrayList<String> textos = null;
        try {
            File archivo = new File(path);
            Scanner lectura = new Scanner(archivo);
            textos = new ArrayList<>();
            while (lectura.hasNext()) {
                textos.add(lectura.nextLine());
            }
            for (String t : textos) {
                texto += t + "\n";
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontró el archivo");
        }
        return texto;
    }

    public void saveFile(String path, String texto) {
        try {
            PrintWriter escribir = new PrintWriter(path);
            escribir.print(texto);
            escribir.close();
        } catch (IOException ex) {
            System.out.println("No se encontró el archivo");
        }
    }

    public void agregar(String path, String texto) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            pw.print(texto);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void eliminar(String path) {
        File fichero = new File(path);
        if (fichero.delete()) {
            System.out.println("El fichero ha sido borrado satisfactoriamente");
        } else {
            System.out.println("El fichero no puede ser borrado");
        }
    }

    public boolean verifyFile(String archivo) {
        File file = new File(archivo);
        boolean exists = file.exists();
        return exists;
    }
}
