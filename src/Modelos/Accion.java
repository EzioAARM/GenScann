/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import AnalizadorEntrada.Globales;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Axel Rodriguez
 */
public class Accion {
    private String Nombre;
    
    public String getNombre() { return Nombre; }
    
    public void setNombre(String nombre) { Nombre = nombre; }
    
    public List<ElementoAccion> Items = new ArrayList();
    
    public void addElemento(ElementoAccion item) throws Exception { Items.add(item); }
    
    public ElementoAccion getElementoAccion(String ID) 
    { 
        try
        {
            for (int i = 0; i < Items.size(); i++)
            {
                if (Items.get(i).getID().equals(ID))
                {
                    return Items.get(i);
                }
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return null;
    }
    
    public Accion(String nombre) { Nombre = nombre; }
}
