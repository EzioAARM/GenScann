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
public class Estado {
    public int Nombre;
    
    public List<Integer> Contenido = null;
    
    public boolean Aceptacion = false;
    
    public Estado(int nombre, List<Integer> contenido)
    {
        Nombre = nombre;
        Contenido = contenido;
    }
}
