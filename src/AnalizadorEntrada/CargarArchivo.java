/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorEntrada;

import static AnalizadorEntrada.Globales.*;
import Modelos.Accion;
import Modelos.ExpresionCompleta;
import static genscann.GenScann.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 *
 * @author Axel Rodriguez
 */
public final class CargarArchivo {
    
    boolean buscarAcciones = false; boolean encontroTokens = false;
    boolean encontroAcciones = false; int cantidadAcciones = 0;
    public CargarArchivo(String direccionArchivo) throws Exception
    {
        try
        {
            Lector = new FileReader(new File(direccionArchivo));
            Analizar();
        }
        catch (FileNotFoundException ex)
        { EGen("El archivo no se encontró en la dirección que se ingresó.\n Error:\n\n" + ex); } 
        catch (IOException ex) 
        { EGen("Se dio un error inesperado cuando se cargaba el archivo.\n Error:\n\n" + ex); }
    }
    
    public void Analizar() throws IOException, Exception
    {
        String[] datos = leerPalabra(leerCaracter());
        switch (datos[0].toUpperCase())
        {
            case "TOKENS":
                Clasificar(datos[1]);
                break;
            default:
                Caracteres -= datos[0].length();
                E1("Se esperaba la palabra TOKENS.");
                break;
        }
    }
    
    public void Clasificar(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        if (caracter.equals("["))
        {
            caracter = Espacios(leerCaracter());
            String[] datos = leerPalabra(caracter, "(");
            Matcher matcher = patronID.matcher(datos[0]);
            if (matcher.matches())
            {
                switch (datos[1])
                {
                    case "(":
                        caracter = leerCaracter();
                        switch (caracter)
                        {
                            case ")":
                                buscarAcciones = true;
                                Acciones.add(new Accion(datos[0]));
                                caracter = Espacios(leerCaracter());
                                switch (caracter)
                                {
                                    case "]":
                                        Clasificar(leerCaracter());
                                        break;
                                    default:
                                        E1("Se esperaba ']' para cierre de la declaración de una acción.");
                                        break;
                                }
                                break;
                            default:
                                E1("Se encontró un caracter no admitido, se esperaba ')'.");
                                break;
                        }
                        break;
                    default:
                        Caracteres--;
                        E1("Se encontró un caracter no admitido, se esperaba '('.");
                        break;
                }
            }
            else
            {
                Caracteres -= datos[0].length();
                E1("El nombre de la acción contiene caracteres no admitidos por un ID.");
            }
        }
        else
        {
            String[] datos = leerPalabra(caracter, "{");
            switch (datos[0].toUpperCase())
            {
                case "TOKEN":
                    encontroTokens = true;
                    caracter = Espacios(datos[1]);
                    datos = leerPalabra(caracter, "=");
                    AnalizarToken analizadorDeTokens = new AnalizarToken(datos[0], datos[1]);
                    if (analizadorDeTokens.debeContinuar)
                        Clasificar(leerCaracter());
                    break;
                case "ACCIONES":
                    encontroAcciones = true;
                    caracter = Espacios(datos[1]);
                    datos = leerPalabra(caracter, "(");
                    AnalizarAcciones analizadorDeAcciones = new AnalizarAcciones(datos[0], datos[1]);
                    cantidadAcciones++;
                    if (analizadorDeAcciones.debeContinuar)
                        Decidir(leerCaracter());
                    break;
                default:
                    if (datos[0].toUpperCase().equals("ERROR"))
                    {
                        Error(leerCaracter());
                    }
                    else
                    {
                        AnalizarConjunto analizadorDeConjuntos = new AnalizarConjunto(datos[0], datos[1]);
                        if (analizadorDeConjuntos.debeContinuar)
                            Clasificar(leerCaracter());
                    }
                    break;
            }
        }
    }
    
    public void Decidir(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        String[] datos = leerPalabra(caracter, "=", "(");
        switch (datos[0].toUpperCase())
        {
            case "ERROR":
                if (cantidadAcciones != Acciones.size())
                {
                    Caracteres--;
                    E1("No se encontraron todas las definiciones de las acciones.");
                }
                else
                    Error(datos[1]);
                break;
            default:
                
                AnalizarAcciones analizadorDeAcciones = new AnalizarAcciones(datos[0], datos[1]);
                cantidadAcciones++;
                if (analizadorDeAcciones.debeContinuar)
                    Decidir(leerCaracter());
                break;
        }
    }
    
    public void Error(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        String[] datos = leerPalabra(leerCaracter());
        switch (caracter)
        {
            case "=":
                datos = leerPalabra(leerCaracter());
                Matcher matcher = patronNUM.matcher(datos[0]);
                if (matcher.matches())
                {
                    boolean yaExiste = false;
                    for (int i = 0; i < Tokens.size(); i++)
                    {
                        if (Tokens.get(i).getNombre().equals(datos[0]))
                        {
                            yaExiste = true;
                        }
                    }
                    for (int i = 0; i < Acciones.size(); i++)
                    {
                        if (Acciones.get(i).getElementoAccion(datos[0]) != null)
                        {
                            yaExiste = true;
                        }
                    }
                    if (yaExiste)
                    {
                        Caracteres -= datos[0].length();
                        E1("El número que se le asigno al error ya está siendo usado.");
                    }
                    else
                    {
                        Error = new Modelos.Error(datos[0]);
                        String expresion = "";
                        for (int i = 0; i < Tokens.size(); i++)
                        {
                            if (i == Tokens.size() - 1)
                            {
                                expresion += Tokens.get(i).getExpresionRegular();
                            }
                            else
                            {
                                expresion += Tokens.get(i).getExpresionRegular() + " | ";
                            }
                        }
                        //expresionRegularCompleta = new ExpresionCompleta(expresion);
                    }
                }
                else
                {
                    Caracteres -= datos[0].length();
                    E1("Se esperaba un número para el ERROR.");
                }
                break;
            default:
                Caracteres--;
                E1("Se esperaba signo '='.");
                break;
        }
    }
}
