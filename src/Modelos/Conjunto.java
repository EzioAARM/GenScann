/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Axel Rodriguez
 */
public class Conjunto {
    
    // Declaración de propiedades
    public List<Character> Contenido = new ArrayList();
    
    public char getContenido(int posicion) { return Contenido.get(posicion); }
    
    private void setContenido(char dato) { Contenido.add(dato); }
    
    private String Nombre;
    
    public String getNombre() { return Nombre; }
    
    private void setNombre(String nombre) { Nombre = nombre; }
    
    public Conjunto(String nombre, List<Integer> datos)
    {
        Nombre = nombre;
        int num = 0;
        for (int i = 0; i < datos.size(); i++)
        {
            num = datos.get(i);
            Contenido.add((char) num);
        }
    }
}
