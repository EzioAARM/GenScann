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
public class Error {
    
    private String Numero = "";
    
    public String getNumero() { return Numero; }
    
    public void setNumero(String numero) { Numero = numero; }
    
    public Error(String numero)
    {
        Numero = numero;
    }
}
