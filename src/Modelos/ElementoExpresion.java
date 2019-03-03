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
public class ElementoExpresion {
    
    public boolean Anulable = false;
    
    public List<Integer> First = null;
    
    public List<Integer> Last = null;
    
    public String Valor = "";
    
    public int Id = -1;
    
    public ElementoExpresion(String valor, boolean anulable)
    {
        Anulable = anulable;
        First = new ArrayList();
        Last = new ArrayList();
        Valor = valor;
    }
    public ElementoExpresion(String valor, int id)
    {
        First = new ArrayList();
        Last = new ArrayList();
        Valor = valor;
        Id = id;
    }
    
    public String LastString()
    {
        String cadena = "";
        for (int j = 0; j < Last.size(); j++)
        {
            if (j == Last.size() - 1)
            {
                cadena = cadena + Last.get(j);
            }
            else
            {
                cadena = cadena + Last.get(j) + ", ";
            }
        }
        return cadena;
    }
    
    public String FirstString()
    {
        String cadena = "";
        for (int j = 0; j < First.size(); j++)
        {
            if (j == First.size() - 1)
            {
                cadena = cadena + First.get(j);
            }
            else
            {
                cadena = cadena + First.get(j) + ", ";
            }
        }
        return cadena;
    }
}
