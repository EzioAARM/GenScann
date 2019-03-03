/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import AnalizadorEntrada.Globales;
import java.util.regex.Matcher;

/**
 *
 * @author Axel Rodriguez
 */
public final class Token {
    
    private String Nombre;
    
    private void setNombre(String nombre) { Nombre = nombre; }
    
    public String getNombre() { return Nombre; }
    
    private String ExpresionRegular;
    
    private void setExpresionRegular(String expresionRegular) { ExpresionRegular = expresionRegular; }
    
    public String getExpresionRegular() { return ExpresionRegular; }
       
    public Token(String nombre, String expresionRegular)
    {
        Nombre = nombre;
        ExpresionRegular = expresionRegular;
        int pos = ExpresionRegular.length() - 1;
        while (pos > 0)
        {
            switch (ExpresionRegular.charAt(pos))
            {
                case ' ':
                    ExpresionRegular = ExpresionRegular.substring(0, pos);
                    break;
                case '.':
                    ExpresionRegular = ExpresionRegular.substring(0, pos);
                    break;
                default:
                    pos = 0;
                    break;
            }
            pos--;
        }
        AnalizarExpresionRegular();
        AnalizarOrs();
    }
    
    public void AnalizarOrs()
    {
        String p = "";
        String[] Ors = ExpresionRegular.split("\\.\\ +\\|\\ *");
        for (int i = 0; i < Ors.length; i++)
        {
            if (i == Ors.length - 1)
                p += Ors[i];
            else
                p += Ors[i] + " | ";
        }
        ExpresionRegular = p;
    }
    
    public void AnalizarExpresionRegular()
    {
        String[] ParentesisCierre = ExpresionRegular.split("\\.\\ +\\)\\ *");
        String p = ParentesisCierre[0]; String[] sep = null;
        if (ParentesisCierre.length <= 1)
        {
            if (ParentesisCierre[0].contains(" ( "))
                p += " ) ";
        }
        else
        {
            for (int i = 1; i < ParentesisCierre.length; i++)
            {
                sep = ParentesisCierre[i].split("\\ +");
                switch (sep[0])
                {
                    case "*":
                        p += " ) " + ParentesisCierre[i];
                        break;
                    case "+":
                        p += " ) " + ParentesisCierre[i];
                        break;
                    case "?":
                        p += " ) " + ParentesisCierre[i];
                        break;
                    case "|":
                        p += " ) " + ParentesisCierre[i];
                        break;
                    default:
                        int j = 0; p += " ) ";
                        while (j < sep.length) 
                        {
                            switch (sep[j])
                            {
                                case ")":
                                    p += " ) ";
                                    break;
                                case "*":
                                    p += " * ";
                                    break;
                                case "+":
                                    p += " + ";
                                    break;
                                case "?":
                                    p += " ? ";
                                    break;
                                case "|":
                                    p += " | ";
                                    break;
                                default:
                                    while (j < sep.length)
                                    {
                                        p += " " + sep[j];
                                        j++;
                                    }
                                    break;
                            }
                            j++;
                        }
                        break;
                }
            }
        }
        ExpresionRegular = p;
    }
}
