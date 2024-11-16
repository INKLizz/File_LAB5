/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2_lab5;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ConsolaFRAME extends JFrame {

    Comandos comandos = new Comandos();
    private static final String Direccion = "C:/Program Files/Java/";
    private static String userInput ="";
    private static String archivo="";
    private static String escribir="";

    public ConsolaFRAME() {

        setTitle("Aministrador: Command Prompt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JTextArea salida = new JTextArea();
        salida.setFont(new Font("Monospaced", Font.PLAIN, 16));
        salida.setBackground(Color.BLACK);
        salida.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(salida);
        add(scrollPane, BorderLayout.CENTER);

        mostrarInformacionInicial(salida);

        salida.append("\n" + Direccion);

        salida.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();

                    String comando = salida.getText().substring(salida.getText().lastIndexOf(Direccion) + Direccion.length()).trim();

                    String[] partes = comando.split("\\s+");

                    if (partes.length > 0) {
                        userInput = partes[0];
                    }
                    if (partes.length > 1) {
                        archivo = partes[1];
                    }
                    if (partes.length > 2) {
                        for (int i = 2; i < partes.length; i++) {
                            escribir += partes[i] + (i < partes.length - 1 ? " " : ""); 
                        }
                    }

                    System.out.println("Comando: " + userInput);
                    System.out.println("Archivo: " + archivo);
                    System.out.println("Escribir: " + escribir);

                    try {
                        procesarComando(userInput, salida);
                    } catch (IOException ex) {
                        Logger.getLogger(ConsolaFRAME.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    salida.setEditable(true);
                    salida.append("\n" + Direccion);

                    salida.setCaretPosition(salida.getDocument().getLength());
                }
            }
        });

        salida.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (salida.getText().equals(Direccion)) {
                    salida.setEditable(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (salida.getText().equals(Direccion)) {
                    salida.setEditable(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

    }

    private void mostrarInformacionInicial(JTextArea area) {
        area.append("Microsoft Windows [Version " + System.getProperty("os.version") + "]\n");
        area.append("(c) Microsoft Corporation. All rights reserved\n\n");
        area.append(System.getProperty("user.dir") + "\n");
        area.setCaretPosition(area.getDocument().getLength());
        area.append("\n/help para ver lista de commandos.\n");
    }

    private void procesarComando(String comando, JTextArea salida) throws IOException {
        switch (comando.toLowerCase()) {
            case "clear":
                salida.setText("");
                break;

            case "help":
                comandos.help(salida);
                break;

            case "exit":
                System.exit(0);
                break;

            case "mkdir":
                comandos.Cd(archivo);
                comandos.Mkdir(salida);

                break;

            case "mfile":
                
                comandos.Cd(archivo);
                comandos.Mfile(salida);

                break;

            case "rm":
                
                comandos.Cd(archivo);
                comandos.Rm(salida);

                break;

            case "cd":
                
                comandos.Cd(archivo);

                break;

            case "...":
                
                comandos.Cd(archivo);
                comandos.regresar(salida);

                break;

            case "dir":
                comandos.Cd(archivo);
                comandos.dir(salida);
                
                break;

            case "date":
                comandos.Date(salida);

                break;

            case "time":
                comandos.Time(salida);
                
                break;

            case "escribir":
                comandos.Cd(archivo);
                comandos.AddContenido(salida,escribir);
                break;
            case "leer":
                comandos.Cd(archivo);
                comandos.LeerBufferedReader(salida);
                break;

            default:
                salida.append("\nComando no reconocido: " + "'" + comando + "'" + "\n");
                break;
        }
    }

    public static void main(String[] args) {
        new ConsolaFRAME().setVisible(true);
    }

}
