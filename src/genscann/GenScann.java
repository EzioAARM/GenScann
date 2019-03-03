/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genscann;

import Modelos.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AxelAlejandro
 */
public class GenScann {

    // Datos guardados
    public static List<Token> Tokens = new ArrayList();
    public static List<Conjunto> Conjuntos = new ArrayList();
    public static List<Accion> Acciones = new ArrayList();
    public static Modelos.Error Error = null;
    public static List<ExpresionCompleta> expresionRegularCompleta = new ArrayList();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Principal principal = new Principal();
        principal.show();
        
        
        String array = "StreamReader lector = new StreamReader()";
        String ciclo = "while (i < array.lenght){";
    }
    
}
