/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

/**
 *
 * @author Axel Rodriguez
 */
public class ElementoAccion {
    private String ID;
    
    private void setID(String id) { ID = id; }
    
    public String getID() { return ID; }
    
    private String Contenido;
    
    private void setContenido(String contenido) { Contenido = contenido; }
    
    public String getContenido() { return Contenido; }
    
    public ElementoAccion(String id, String contenido)
    {
        ID = id;
        Contenido = contenido;
    }
}
