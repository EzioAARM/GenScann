/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorEntrada;

import static AnalizadorEntrada.Globales.*;
import Modelos.*;
import static genscann.GenScann.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 *
 * @author Axel Rodriguez
 */
public final class AnalizarAcciones {
    
    boolean debeContinuar = true; boolean hayContenido = false;
    String nombreAccion = ""; int posicionAccion = 0;
    List<String> IDs = new ArrayList(); List<String> contenidos = new ArrayList();
    public AnalizarAcciones(String nombre, String caracter) throws IOException, Exception
    {
        Matcher matcher = patronID.matcher(nombre);
        if (matcher.matches())
        {
            boolean existeAccion = false;
            for (int i = 0; i < Acciones.size(); i++)
            {
                if (Acciones.get(i).getNombre().equals(nombre))
                {
                    existeAccion = true;
                    posicionAccion = i;
                }
            }
            if (existeAccion)
            {
                caracter = Espacios(caracter);
                String siguiente = leerCaracter();
                switch (caracter)
                {
                    case "(":
                        switch (siguiente)
                        {
                            case ")":
                                Analisis(leerCaracter());
                                break;
                            default:
                                debeContinuar = false; Caracteres--;
                                E1("Se esperaba parentesis de cierre ')'.");
                                break;
                        }
                        break;
                    default:
                        debeContinuar = false; Caracteres--;
                        E1("Se esperaba parentesis de apertura '('.");
                        break;
                }
            }
            else
            {
                debeContinuar = false; Caracteres -= nombre.length();
                E1("La acción no fue definida antes.");
            }
        }
        else
        {
            debeContinuar = false; Caracteres -= nombre.length();
            E1("La acción no fue definida antes.");
        }
    }
    
    public void Analisis(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "{":
                AnalizarNombreItem(leerCaracter());
                break;
            default:
                Caracteres--; debeContinuar = false;
                E1("Se esperaba llave '{' para inicio de la definición de la acción.");
        }
    }
    
    public void AnalizarNombreItem(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "}":
                TerminarAnalisis();
                break;
            default:
                String[] datos = leerPalabra(caracter, "=");
                Matcher matcher = patronNUM.matcher(datos[0]);
                if (matcher.matches())
                {
                    boolean esUsada = false;
                    for (int i = 0; i < Tokens.size(); i++)
                    {
                        if (Tokens.get(i).getNombre().equals(datos[0]))
                        {
                            esUsada = true;
                        }
                    }
                    for (int i = 0; i < Acciones.size(); i++)
                    {
                        if (Acciones.get(i).getElementoAccion(datos[0]) != null)
                        {
                            esUsada = true;
                        }
                    }
                    if (esUsada)
                    {
                        Caracteres -= datos[0].length(); debeContinuar = false;
                        E1("El número que se asigno para el item ya está en uso por un token o por otro item de acción.");
                    }
                    else
                    {
                        IDs.add(datos[0]);
                        caracter = Espacios(datos[1]);
                        switch (caracter)
                        {
                            case "=":
                                Clasificar(leerCaracter());
                                break;
                            default:
                                Caracteres--; debeContinuar = false;
                                E1("Se esperaba signo '='.");
                                break;
                        }
                    }
                }
                else
                {
                    E1("Se esperaba que el identificador del item de la acción fuera un número.");
                }
                break;
        }
    }
    
    public void Clasificar(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "'":
                AnalizarContenidoComilla(leerCaracter());
                break;
            case "\"":
                AnalizarContenidoComillas(leerCaracter());
                break;
            default:
                Caracteres--; debeContinuar = false;
                E1("Se esperaba una comilla simple \"'\" o comillas dobles '\"'.");
                break;
        }
    }
    
    public void AnalizarContenidoComilla(String caracter) throws IOException, Exception
    {
        String contenido = ObtenerContenido(caracter, "'");
        if (contenido.length() != 0)
        {
            contenidos.add(contenido);
            AnalizarNombreItem(leerCaracter());
        }
    }
    
    public void AnalizarContenidoComillas(String caracter) throws IOException, Exception
    {
        String contenido = ObtenerContenido(caracter, "\"");
        if (contenido.length() != 0)
        {
            contenidos.add(contenido);
            AnalizarNombreItem(leerCaracter());
        }
    }
    
    public String ObtenerContenido(String caracter, String limitante) throws IOException, Exception
    {
        String contenido = "";
        if (caracter.equals(limitante))
        {
            Caracteres--; debeContinuar = false;
            E1("Se esperaba contenido para el item de la acción.");
        }
        else
        {
            boolean huboError = false;
            Matcher matcher = patronESP.matcher(caracter);
            while (!caracter.equals(limitante) && !huboError)
            {
                if (matcher.matches() || caracter.equals(" ") || caracter.equals("\t") || caracter.equals("\r") || caracter.equals("\n"))
                {
                    switch (caracter)
                    {
                        case "\r":
                            contenido += caracter;
                            caracter = leerCaracter();
                            switch (caracter)
                            {
                                case "\n":
                                    Caracteres = 1;
                                    Linea++;
                                    break;
                            }
                            break;
                        case "\n":
                            Caracteres = 1; Linea++;
                            break;
                    }
                    contenido += caracter;
                    caracter = leerCaracter();
                    matcher = patronESP.matcher(caracter);
                }
                else
                {
                    huboError = true;
                }
            }
            if (huboError)
            {
                Caracteres--; debeContinuar = false;
                E1("Se encontró un caracter no válido.");
            }
            else
            {
                return contenido;
            }
        }
        return "";
    }
    
    public void TerminarAnalisis() throws Exception
    {
        for (int i = 0; i < IDs.size(); i++)
        {
            Acciones.get(posicionAccion).addElemento(new ElementoAccion(IDs.get(i), contenidos.get(i)));
        }
    }
}
