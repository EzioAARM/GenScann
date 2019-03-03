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
public class Nodo {
    public Nodo Derecho = null;
    
    public Nodo Izquierdo = null;
    
    public List<Integer> First = null;
    
    public List<Integer> Last = null;
    
    public String Valor = "";
    
    public int Id = -1;
    
    public boolean Nulabilidad = false;
    
    public Nodo()
    {
        First = new ArrayList();
        Last = new ArrayList();
    }
    
    public String FirstString()
    {
        String cadena = "";
        for (int i = 0; i < First.size(); i++)
        {
            if (i == First.size() - 1)
                cadena += First.get(i);
            else
                cadena += First.get(i) + ", ";
        }
        return cadena;
    }
    
    public String LastString()
    {
        String cadena = "";
        for (int i = 0; i < Last.size(); i++)
        {
            if (i == Last.size() - 1)
                cadena += Last.get(i);
            else
                cadena += Last.get(i) + ", ";
        }
        return cadena;
    }
}
