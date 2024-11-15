/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2_lab5;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextArea;

/**
 *
 * @author Eztib
 */
public class Comandos {

    private File Actual = null; 
    private File Anterior = null; 
    private File Anterior2 = null; 
    private String Text = ""; 

    void Cd(String direccion) {
        Anterior2 = Anterior;
        Anterior = Actual;
        Actual = new File(direccion);
    }

    boolean Mkdir(JTextArea CMD) { 
        boolean resultado = Actual.mkdirs();
        if (resultado) {
            Text = "Carpeta creada: " + Actual.getName() + "\n";
        } else {
            Text = "Error al crear carpeta.\n";
        }
        añadirCMD(CMD);
        return resultado;
    }

    boolean Mfile(JTextArea CMD) throws IOException { 
        boolean resultado = Actual.createNewFile();
        if (resultado) {
            Text = "Archivo creado: " + Actual.getName() + "\n";
        } else {
            Text = "Error al crear archivo.\n";
        }
        añadirCMD(CMD);
        return resultado;
    }

    void Rm(JTextArea CMD) {
        if (antidoto(Actual)) {
            Text = "Borrado: " + Actual.getName() + "\n";
        } else {
            Text = "No se pudo borrar: " + Actual.getName() + "\n";
        }
        añadirCMD(CMD);
    }

    boolean antidoto(File mf) {
        if (mf.isDirectory()) {
            for (File child : mf.listFiles()) {
                antidoto(child);
            }
        }
        return mf.delete();
    }

    void regresar(JTextArea CMD) { 
        if (Anterior != null) {
            Actual = Anterior;
            Anterior = Anterior2;
            Anterior2 = null;
            if (Actual != null) {
                Text = "Regresaste al directorio: " + Actual.getAbsolutePath() + "\n";
            } else {
                Text = "Regresaste al directorio: Ninguno\n";
            }
        } else {
            Text = "No hay un directorio anterior al cual regresar.\n";
        }
        añadirCMD(CMD);
    }

    void dir(JTextArea CMD) {
        StringBuilder builder = new StringBuilder();
        if (Actual.isDirectory()) {
            builder.append("Directorio de: ").append(Actual.getAbsolutePath()).append("\n\n");

            // Contadores
            int cfiles = 0, cdirs = 0, tbytes = 0;

            // Recorrido
            for (File child : Actual.listFiles()) {
                if (!child.isHidden()) {
                    // Ultima modificacion
                    Date ultimo = new Date(child.lastModified());
                    builder.append(ultimo).append("\t");

                    // Si es File o Folder
                    if (child.isDirectory()) {
                        cdirs++;
                        builder.append("<DIR>\t\t");
                    } else {
                        cfiles++;
                        tbytes += child.length();
                        builder.append("    \t").append(child.length()).append("\t");
                    }

                    // Mostrar objetos
                    builder.append(child.getName()).append("\n");
                }
            }

            builder.append(cfiles).append(" archivos\t").append(tbytes).append(" bytes\n");
            builder.append(cdirs).append(" dirs\t\n");
        } else {
            builder.append("El directorio no es válido.\n");
        }
        Text = builder.toString();
        añadirCMD(CMD);
    }

    public void Date(JTextArea CMD) { // Fecha
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Text = "Fecha actual: " + date.format(formatter) + "\n";
        añadirCMD(CMD);
    }

    public void Time(JTextArea CMD) { // Hora
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Text = "Hora actual: " + time.format(formatter) + "\n";
        añadirCMD(CMD);
    }

    public void AddContenido(String texto, JTextArea CMD) { // Agregar
        try (FileWriter writer = new FileWriter(Actual, true); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(" " + texto);
            Text = "Texto añadido al archivo: " + Actual.getName() + "\n";
        } catch (IOException e) {
            Text = "Error al añadir contenido al archivo: " + e.getMessage() + "\n";
        }
        añadirCMD(CMD);
    }

    public void LeerBufferedReader(JTextArea CMD) { // Leer
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(Actual))) {
            String text;
            contenido.append("Mostrando contenido de: ").append(Actual.getName()).append("\n");
            while ((text = reader.readLine()) != null) {
                contenido.append(text).append("\n");
            }
        } catch (IOException e) {
            contenido.append("Error: ").append(e.getMessage()).append("\n");
        }
        Text = contenido.toString();
        añadirCMD(CMD);
    }

    public void help(JTextArea CMD) { //Lista Commandos
        StringBuilder TextBuild = new StringBuilder();
        TextBuild.append("Mkdir <nombre>: Nueva carpeta\n");
        TextBuild.append("Mfile <nombre.ext>: Nuevo archivo\n");
        TextBuild.append("Rm <nombre>: Eliminar carpeta y archivo\n");
        TextBuild.append("Cd <nombre carpeta>: Cambiar de carpeta actual\n");
        TextBuild.append("<...> Regresar de Carpeta\n");
        TextBuild.append("Dir: Listar todas las carpetas y archivos en la carpeta actual\n");
        TextBuild.append("Date: Ver fecha actual\n");
        TextBuild.append("Time: Ver hora actual\n");
        TextBuild.append("Escribir <wr>: Escribir en un archivo seleccionado\n");
        TextBuild.append("Leer <rd>: Leer el contenido del archivo seleccionado\n");
        TextBuild.append("Exit: Cierra el CMD\n\n");

        Text = TextBuild.toString();
        añadirCMD(CMD);
    }

    private void añadirCMD(JTextArea CMD) {
       CMD.append("\n" + Text);
    }
}

