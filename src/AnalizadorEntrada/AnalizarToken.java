/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorEntrada;

import static AnalizadorEntrada.Globales.*;
import Modelos.ElementoExpresion;
import Modelos.Token;
import static genscann.GenScann.Conjuntos;
import static genscann.GenScann.Tokens;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 *
 * @author Axel Rodriguez
 */
public final class AnalizarToken {
    
    boolean hayContenido = false; boolean debeContinuar = true;
    boolean encontroOr = false; int parentesis = 0;
    String nombreToken = ""; String contenidoToken = "";
    boolean haySimbolos = false; boolean estaEmpezando = true;
    boolean expresionCorrida = false; boolean vaPunto = false;
    boolean puntoParentesis = false;
    
    public AnalizarToken(String nombre, String caracter) throws IOException, Exception
    {
        boolean seEncontroToken = false;
        Matcher matcher = patronNUM.matcher(nombre);
        if (matcher.matches())
        {
            for (int i = 0; i < Tokens.size(); i++)
            {
                if (nombre.equals(Tokens.get(i).getNombre()))
                {
                    seEncontroToken = true;
                }
            }
            if (!seEncontroToken)
            {
                
                nombreToken = nombre;
                Analisis(caracter);
            }
            else
            {
                debeContinuar = false; Caracteres -= nombre.length();
                E1("El número que identifica el token fue utilizado antes.");
            }
        }
        else
        {
            debeContinuar = false; Caracteres -= nombre.length();
            E1("El nombre del token no es valido, solo se permiten números.");
        }
    }
    
    public void Analisis(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "=":
                AnalizarExpresion(leerCaracter());
                break;
            default:
                Caracteres--; debeContinuar = false;
                E1("Se esperaba un signo igual.");
                break;
        }
    }
    
    public void AnalizarExpresion(String caracter) throws IOException, Exception
    {
        caracter = Espacios(caracter);
        switch (caracter)
        {
            case "'":
                if (!haySimbolos && !estaEmpezando)
                {
                    contenidoToken += " ";
                }
                else
                {
                    estaEmpezando = false;
                    haySimbolos = false;
                }
                if (encontroOr)
                    encontroOr = false;
                if (expresionCorrida || puntoParentesis)
                {
                    contenidoToken += " . ";
                }
                else
                {
                    contenidoToken += " ";
                }
                contenidoToken += caracter;
                hayContenido = true;
                expresionCorrida = true;
                vaPunto = true;
                puntoParentesis = false;
                CasoComilla(leerCaracter());
                break;
            case "\"":
                if (!haySimbolos && !estaEmpezando)
                {
                    contenidoToken += " ";
                }
                else
                {
                    estaEmpezando = false;
                    haySimbolos = false;
                }
                if (encontroOr)
                    encontroOr = false;
                if (expresionCorrida || puntoParentesis)
                {
                    contenidoToken += " . ";
                }
                else
                {
                    contenidoToken += " ";
                }
                contenidoToken += caracter;
                hayContenido = true;
                expresionCorrida = true;
                vaPunto = true;
                puntoParentesis = false;
                CasoComillas(leerCaracter());
                break;
            case ";":
                if (hayContenido)
                    if (encontroOr)
                    {
                        Caracteres--; debeContinuar = false;
                        E1("No todos los parentesis fueron cerrados.");
                    }
                    else if (parentesis != 0)
                    {
                        Caracteres--; debeContinuar = false;
                        E1("Despues de un operador '|' debe haber parte de la expresión regular.");
                    }
                    else
                    {
                        TerminarAnalisis();
                    }
                else
                {
                    debeContinuar = false;
                    E1("Se esperaba una expresión regular como contenido del Token.");
                }
                break;
            case "(":
                estaEmpezando = true;
                if (encontroOr)
                    encontroOr = false;
                if (vaPunto)
                    contenidoToken += " . ";
                contenidoToken += " " + caracter + " ";
                hayContenido = true;
                puntoParentesis = false;
                CasoParentesis(caracter);
                break;
            case ")":
                vaPunto = false;
                estaEmpezando = true;
                if (encontroOr)
                {
                    Caracteres--; debeContinuar = false;
                    E1("Despues de un operador '|' no puede venir un parentesis de cierre, se esperaba una expresión.");
                }
                contenidoToken += " " + caracter + " ";
                hayContenido = true;
                puntoParentesis = true;
                CasoParentesis(caracter);
                break;
            default:
                if (!haySimbolos && !estaEmpezando)
                {
                    contenidoToken += " ";
                }
                else
                {
                    estaEmpezando = false;
                    haySimbolos = false;
                }
                if (encontroOr)
                    encontroOr = false;
                if (caracter.equals("*") || caracter.equals("+") || caracter.equals("|") || caracter.equals("?"))
                {
                    vaPunto = false;
                    puntoParentesis = false;
                    if (hayContenido)
                    {
                        caracter = BuscarSimbolos(caracter);
                        AnalizarExpresion(caracter);
                    }
                    else
                    {
                        Caracteres--; debeContinuar = false;
                        E1("Se esperaba la definición de un elemento del conjunto.");
                    }
                }
                else
                {
                    hayContenido = true;
                    if (expresionCorrida || puntoParentesis)
                    {
                        contenidoToken += " . ";
                    }
                    else
                    {
                        contenidoToken += " ";
                    }
                    expresionCorrida = true;
                    vaPunto = true;
                    puntoParentesis = false;
                    CasoConjunto(caracter);
                }
                break;
        }
    }
    
    public void CasoComilla(String caracter) throws IOException, Exception
    {
        String siguiente = leerCaracter();
        Matcher matcher = patronL.matcher(caracter);
        if (matcher.matches())
        {
            contenidoToken += caracter;
            switch (siguiente)
            {
                case "'":
                    contenidoToken += siguiente + " ";
                    siguiente = leerCaracter();
                    AnalizarExpresion(BuscarSimbolos(siguiente));
                    break;
                default:
                    Caracteres--; debeContinuar = false;
                    E1("Se esperaba cierre de comilla para la definición del elemento de la expresión regular.");
                    break;
            }
        }
        else
        {
            debeContinuar = false; Caracteres--;
            E1("Se definió un caracter no admitido en la expresión regular.");
        }
    }
    
    public void CasoComillas(String caracter) throws IOException, Exception
    {
        String siguiente = leerCaracter();
        Matcher matcher = patronL.matcher(caracter);
        if (matcher.matches())
        {
            contenidoToken += caracter;
            switch (siguiente)
            {
                case "\"":
                    contenidoToken += siguiente + " ";
                    siguiente = leerCaracter();
                    AnalizarExpresion(BuscarSimbolos(siguiente));
                    break;
                default:
                    Caracteres--; debeContinuar = false;
                    E1("Se esperaba cierre de comilla para la definición del elemento de la expresión regular.");
                    break;
            }
        }
        else
        {
            debeContinuar = false; Caracteres--;
            E1("Se definió un caracter no admitido en la expresión regular.");
        }
    }
    
    public void CasoConjunto(String caracter) throws IOException, Exception
    {
        String palabra = "";
        Matcher matcher = patronID2.matcher(caracter);
        boolean siAplica = true;
        while (siAplica)
        {
            if (matcher.matches())
            {
                palabra += caracter;
                caracter = leerCaracter();
                matcher = patronID2.matcher(caracter);
            }
            else
            {
                siAplica = false;
            }
        }
        if (palabra.length() != 0)
        {
            boolean encontroConjunto = false;
            for (int i = 0; i < Conjuntos.size(); i++)
            {
                if (palabra.toUpperCase().equals(Conjuntos.get(i).getNombre().toUpperCase()))
                {
                    encontroConjunto = true;
                }
            }
            if (encontroConjunto)
            {
                contenidoToken += palabra + " ";
                
                AnalizarExpresion(BuscarSimbolos(caracter));
            }
            else
            {
                Caracteres -= palabra.length(); debeContinuar = false;
                E1("El conjunto no ha sido definido.");
            }
        }
        else
        {
            debeContinuar = false; Caracteres--;
            E1("Se encontró un caracter no admitido en la expresión regular.");
        }
    }
    
    public void CasoParentesis(String caracter) throws IOException, Exception
    {
        switch (caracter)
        {
            case "(":
                expresionCorrida = false;
                parentesis++;
                break;
            case ")":
                expresionCorrida = false;
                parentesis--;
                break;
        }
        caracter = leerCaracter();
        AnalizarExpresion(caracter);
    }
    
    public String BuscarSimbolos(String caracter) throws IOException
    {
        switch(caracter){
        case "*":
            expresionCorrida = false;
            haySimbolos = true;
            return BuscarAsteriscosMas(caracter);
        case "+":
            expresionCorrida = false;
            haySimbolos = true;
            return BuscarAsteriscosMas(caracter);
        case "?":
            expresionCorrida = false;
            haySimbolos = true;
            return BuscarAsteriscosMas(caracter);
        case "|":
            vaPunto = false;
            expresionCorrida = false;
            haySimbolos = true;
            contenidoToken += " " + caracter + " ";
            encontroOr = true;
            puntoParentesis = false;
            caracter = leerCaracter();
            return caracter;
        }
        return caracter;
    }
    
    public String BuscarAsteriscosMas(String caracter) throws IOException
    {
        vaPunto = false;
        puntoParentesis = false;
        String retorno = "";
        switch (caracter)
        {
            case "*":
                while (caracter.equals("*") || caracter.equals("+"))
                {
                    switch (caracter)
                    {
                        case "*":
                            if (retorno.equals("+"))
                                retorno = "+";
                            else
                                retorno = "*";
                            break;
                        case "+":
                            retorno = "+";
                            break;
                    }
                    caracter = leerCaracter();
                }
                contenidoToken += retorno + " . ";
                return caracter;
            case "+":
                while (caracter.equals("*") || caracter.equals("+"))
                {
                    retorno = "+";
                    caracter = leerCaracter();
                }
                contenidoToken += retorno  + " . ";
                return caracter;
            case "?":
                while (caracter.equals("?"))
                {
                    caracter = leerCaracter();
                }
                contenidoToken += "?"  + " . ";
                return caracter;
            default:
                return caracter;
        }
    }
    
    public void TerminarAnalisis()
    {
        Tokens.add(new Token(nombreToken, contenidoToken));
    }
}
