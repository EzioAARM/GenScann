/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorEntrada;

import static AnalizadorEntrada.Globales.*;
import Modelos.Conjunto;
import static genscann.GenScann.Conjuntos;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;

/**
 *
 * @author Axel Rodriguez
 */
public final class AnalizarConjunto {
    
    String nombreConjunto = "";
    boolean hayContenido = false;
    boolean leerRango = false;
    boolean puedeVenirRango = true;
    public boolean debeContinuar = true;
    // Todos se guardan como int, cuando se van al objeto Conjunto hay que convertirlo a char y luego a String
    List<Integer> contenidoConjunto = new ArrayList();
    public Conjunto conjuntoObtenido = null;
    
    public AnalizarConjunto(String nombre, String caracter) throws IOException, Exception
    {
        boolean seEncontroConjunto = false;
        Matcher matcher = patronID.matcher(nombre);
        if (matcher.matches())
        {
            for (int i = 0; i < Conjuntos.size(); i++)
            {
                if (nombre.toUpperCase().equals(Conjuntos.get(i).getNombre().toUpperCase()))
                {
                    seEncontroConjunto = true;
                }
            }
            if (!seEncontroConjunto)
            {
                switch (nombre.toUpperCase())
                {
                    case "ACCIONES":
                        E1("El nombre del conjunto no puede ser 'ACCIONES'");
                        break;
                    case "TOKEN":
                        E1("El nombre del conjunto no puede ser 'TOKEN'");
                        break;
                    case "TOKENS":
                        E1("El nombre del conjunto no puede ser 'TOKENS'");
                        break;
                    case "ERROR":
                        E1("El nombre del conjunto no puede ser 'ERROR'");
                        break;
                    default:
                        nombreConjunto = nombre;
                        Analisis(caracter);
                        break;
                }
            }
            else
            {
                Caracteres -= nombre.length(); debeContinuar = false;
                E1("El conjunto ya fue definido antes.");
            }
        }
        else
        {
            Caracteres -= caracter.length(); debeContinuar = false;
            E1("El identificador del conjunto no es valido.");
        }
    }
    
    public void Analisis(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "{":
                AnalizarContenido(leerCaracter());
                break;
            default:
                Caracteres -= caracter.length(); debeContinuar = false;
                E1("Se esperaba el inicio de un conjunto.");
                break;
        }
    }
    
    public void AnalizarContenido(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "'":
                hayContenido = true;
                CasoComilla(leerCaracter());
                break;
            case "\"":
                hayContenido = true;
                CasoComillas(leerCaracter());
                break;
            case "c":
                hayContenido = true;
                CasoChar(caracter);
                break;
            case "}":
                if (hayContenido)
                    TerminarAnalisis();
                else
                {
                    debeContinuar = false;
                    Caracteres--;
                    E1("Se esperaba contenido dentro del conjunto " + nombreConjunto + " ,no se puede definir como vacío.");
                }
                break;
            default:
                Caracteres -= caracter.length(); debeContinuar = false;
                E1("Se esperaba definición de un elemento del conjunto");
                break;
        }
    }
    
    // Analiza cuando el elemento está dentro de comillas simples
    public void CasoComilla(String caracter) throws IOException, Exception
    {
        String siguiente = leerCaracter();
        Matcher matcher = patronL.matcher(caracter);
        if (matcher.matches())
        {
            switch (siguiente)
            {
                case "'":
                    if (leerRango)
                    {
                        if (AnalizarRango(contenidoConjunto.get(contenidoConjunto.size() - 1), (int) caracter.charAt(0)))
                        {
                            int inicio = contenidoConjunto.size() - 1; int fin = (int) caracter.charAt(0);
                            for (int i = contenidoConjunto.get(inicio); i <= fin; i++)
                            {
                                contenidoConjunto.add(i);
                            }
                            leerRango = false;
                            AnalizarSimbolo(leerCaracter());
                        }
                    }
                    else
                    {
                        contenidoConjunto.add((int) caracter.charAt(0));
                        AnalizarSimbolo(leerCaracter());
                    }
                    break;
                default:
                    Caracteres--; debeContinuar = false;
                    E1("Se esperaba cierre de comilla para la definición del elemento del conjunto " + nombreConjunto);
                    break;
            }
        }
        else
        {
            Caracteres -= caracter.length(); debeContinuar = false;
            E1("Se definió un caracter no admitido dentro de un elemento del conjunto " + nombreConjunto);
        }
    }
    
    // Analiza cuando el elemento está dentro de comillas dobles
    public void CasoComillas(String caracter) throws IOException, Exception
    {
        String siguiente = leerCaracter();
        Matcher matcher = patronL.matcher(caracter);
        if (matcher.matches())
        {
            switch (siguiente)
            {
                case "\"":
                    if (leerRango)
                    {
                        if (AnalizarRango(contenidoConjunto.get(contenidoConjunto.size() - 1), (int) caracter.charAt(0)))
                        {
                            int inicio = contenidoConjunto.size() - 1; int fin = (int) caracter.charAt(0);
                            for (int i = contenidoConjunto.get(inicio); i <= fin; i++)
                            {
                                contenidoConjunto.add(i);
                            }
                            leerRango = false;
                            AnalizarSimbolo(leerCaracter());
                        }
                        else
                        {
                            debeContinuar = false;
                            E1("Hubo un error leyendo el rango del conjunto " + nombreConjunto);
                        }
                    }
                    else
                    {
                        contenidoConjunto.add((int) caracter.charAt(0));
                        AnalizarSimbolo(leerCaracter());
                    }
                    break;
                default:
                    Caracteres--; debeContinuar = false;
                    E1("Se esperaba cierre de comilla doble para la definición del elemento del conjunto " + nombreConjunto);
                    break;
            }
        }
        else
        {
            Caracteres -= caracter.length(); debeContinuar = false;
            E1("Se definió un caracter no admitido dentro de un elemento del conjunto " + nombreConjunto);
        }
    }
    
    // Analiza cuando se declara el elemento como un char
    public void CasoChar(String caracter) throws IOException, Exception
    {
        String palabra = caracter + leerCaracter() + leerCaracter() + leerCaracter();
        switch (palabra.toUpperCase())
        {
            case "CHR(":
                String[] numero = leerPalabra(leerCaracter(), ")");
                Matcher matcher = patronNUM.matcher(numero[0]);
                if (matcher.matches())
                {
                    switch (numero[1])
                    {
                        case ")":
                            if (leerRango)
                            {
                                if (AnalizarRango(contenidoConjunto.get(contenidoConjunto.size() - 1), Integer.parseInt(numero[0])))
                                {
                                    int inicio = contenidoConjunto.size() - 1; int fin = Integer.parseInt(numero[0]);
                                    for (int i = contenidoConjunto.get(inicio); i <= fin; i++)
                                    {
                                        contenidoConjunto.add(i);
                                    }
                                    leerRango = false;
                                    AnalizarSimbolo(leerCaracter());
                                }
                            }
                            else
                            {
                                contenidoConjunto.add(Integer.parseInt(numero[0]));
                                AnalizarSimbolo(leerCaracter());
                            }
                            break;
                        default:
                            Caracteres -= numero[1].length(); debeContinuar = false;
                            E1("Se esperaba final de la definición del caracter en ASCII, no se cerro el parentesis.");
                            break;
                    }
                }
                else
                {
                    Caracteres -= numero[0].length(); debeContinuar = false;
                    E1("No se definió de forma correcta el char por el método ASCII, se esperaba un número dentro de los parentesis.");
                }
                break;
            default:
                Caracteres -= palabra.length(); debeContinuar = false;
                E1("Se esperaba la definición por código ASCII del elemento del conjunto " + nombreConjunto);
                break;
        }
    }
    
    public void AnalizarSimbolo(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "}":
                AnalizarContenido(caracter);
                break;
            case "+":
                puedeVenirRango = true;
                caracter = leerCaracter();
                switch (caracter)
                {
                    case "}":
                        Caracteres--; debeContinuar = false;
                        E1("Se esperaba la definición de un elemento del conjunto.");
                        break;
                    default:
                        AnalizarContenido(caracter);
                        break;
                }
                break;
            case ".":
                
                switch (leerCaracter())
                {
                    case ".":
                        if (puedeVenirRango)
                        {
                            puedeVenirRango = false;
                            caracter = leerCaracter();
                            switch (caracter)
                            {
                                case "}":
                                    Caracteres--; debeContinuar = false;
                                    E1("Se esperaba la definición de un rango final para el conjunto.");
                                    break;
                                default:
                                    leerRango = true;
                                    AnalizarContenido(caracter);
                                    break;
                            }
                            break;
                        }
                        else
                        {
                            Caracteres -= 2;
                            debeContinuar = false;
                            E1("No puede venir un operador de rango seguido de otro operador de rango.");
                        }
                    default:
                        Caracteres -= 2; debeContinuar = false;
                        E1("El operador que se ingresó no es válido.");
                        break;
                }
                break;
            default:
                Caracteres--; debeContinuar = false;
                E1("Se esperaba un operador de rango, concatenación o cierre.");
                break;
        }
    }
    
    public void EliminarRepetidos()
    {
        HashSet<Integer> hashSet = new HashSet<Integer>(contenidoConjunto);
        contenidoConjunto.clear();
        contenidoConjunto.addAll(hashSet);
    }
    
    public void TerminarAnalisis()
    {
        EliminarRepetidos();
        Conjuntos.add(new Conjunto(nombreConjunto, contenidoConjunto));
    }
}
