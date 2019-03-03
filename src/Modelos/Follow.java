/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Axel Rodriguez
 */
public class Follow {
    public int Dato = -1;
    public List<Integer> Producciones = new ArrayList();
    
    public Follow(int dato)
    {
        Dato = dato;
    }
    
    public void EliminarRepetidos()
    {
        HashSet<Integer> hashSet = new HashSet<Integer>(Producciones);
        Producciones.clear();
        Producciones.addAll(hashSet);
    }
    
    public String ProduccionesString()
    {
        String cadena = "";
        EliminarRepetidos();
        for (int i = 0; i < Producciones.size(); i++)
        {
            if (i == Producciones.size() - 1)
                cadena += Producciones.get(i);
            else
                cadena += Producciones.get(i) + ", ";
        }
        return cadena;
    }
}
