/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorEntrada;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 *
 * @author Axel Rodriguez
 */
public class Globales {
    
    // Variable que lee el archivo
    public static FileReader Lector = null;
    // Variables que guardan el número de lineas y columnas por la que va leyendo
    public static int Caracteres = 1;
    public static int Linea = 1;
    // Expresiones regulares
    public static String NUM = "\\d+";
    public static Pattern patronNUM = Pattern.compile(NUM);
    
    public static String ID2 = "[a-zA-Z0-9_]+";
    public static Pattern patronID2 = Pattern.compile(ID2);
    
    public static String ID = "[a-zA-Z_][a-zA-Z0-9]*";
    public static Pattern patronID = Pattern.compile(ID);
    
    public static String P = "\\ |!|\\\"|\\#|\\$|\\%|\\&|\\(|\\)|\\*|\\+|\\,|\\-|\\.|\\/|[0-9]|\\:|\\;|\\<|\\=|\\>|\\?|\\@|[A-Z]|\\[|\\\\|\\]|\\^|\\_|\\`"
            + "|[a-z]|\\{|\\||\\}|\\~|\\|\\||\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\"
            + "|\\|\\|\\|\\|\\|\\|\\|\\ |\\¡|\\¢|\\£|\\¤|\\¥|\\¦|\\§|\\¨|\\©|\\ª|\\«|\\¬|\\­|\\®|\\¯|\\°|\\±|\\²|\\³|\\´|\\µ|\\¶|\\·"
            + "|\\¸|\\¹|\\º|\\»|\\¼|\\½|\\¾|\\¿|\\À|\\Á|\\Â|\\Ã|\\Ä|\\Å|\\Æ|\\Ç|\\È|\\É|\\Ê|\\Ë|\\Ì|\\Í|\\Î|\\Ï|\\Ð|\\Ñ|\\Ò|\\Ó|\\Ô|\\Õ"
            + "|\\Ö|\\×|\\Ø|\\Ù|\\Ú|\\Û|\\Ü|\\Ý|\\Þ|\\ß|\\à|\\á|\\â|\\ã|\\ä|\\å|\\æ|\\ç|\\è|\\é|\\ê|\\ë|\\ì|\\í|\\î|\\ï|\\ð|\\ñ|\\ò|\\ó"
            + "|\\ô|\\õ|\\ö|\\÷|\\ø|\\ù|\\ú|\\û|\\ü|\\ý|\\þ+";
    public static Pattern patronP = Pattern.compile(P);
    
    public static String L = "\\'|\\ |!|\\\"|\\#|\\$|\\%|\\&|\\(|\\)|\\*|\\+|\\,|\\-|\\.|\\/|[0-9]|\\:|\\;|\\<|\\=|\\>|\\?|\\@|[A-Z]|\\[|\\\\|\\]|\\^|\\_|\\`"
            + "|[a-z]|\\{|\\||\\}|\\~|\\|\\||\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\"
            + "|\\|\\|\\|\\|\\|\\|\\|\\ |\\¡|\\¢|\\£|\\¤|\\¥|\\¦|\\§|\\¨|\\©|\\ª|\\«|\\¬|\\­|\\®|\\¯|\\°|\\±|\\²|\\³|\\´|\\µ|\\¶|\\·"
            + "|\\¸|\\¹|\\º|\\»|\\¼|\\½|\\¾|\\¿|\\À|\\Á|\\Â|\\Ã|\\Ä|\\Å|\\Æ|\\Ç|\\È|\\É|\\Ê|\\Ë|\\Ì|\\Í|\\Î|\\Ï|\\Ð|\\Ñ|\\Ò|\\Ó|\\Ô|\\Õ"
            + "|\\Ö|\\×|\\Ø|\\Ù|\\Ú|\\Û|\\Ü|\\Ý|\\Þ|\\ß|\\à|\\á|\\â|\\ã|\\ä|\\å|\\æ|\\ç|\\è|\\é|\\ê|\\ë|\\ì|\\í|\\î|\\ï|\\ð|\\ñ|\\ò|\\ó"
            + "|\\ô|\\õ|\\ö|\\÷|\\ø|\\ù|\\ú|\\û|\\ü|\\ý|\\þ";
    public static Pattern patronL = Pattern.compile(L);
    
    public static String ESP = " |\\'|!|\\\"|\\#|\\$|\\%|\\&|\\(|\\)|\\*|\\+|\\,|\\-|\\.|\\/|[0-9]|\\:|\\;|\\<|\\=|\\>|\\?|\\@|[A-Z]|\\[|\\\\|\\]|\\^|\\_|\\`"
            + "|[a-z]|\\{|\\||\\}|\\~|\\|\\||\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\|\\"
            + "|\\|\\|\\|\\|\\|\\|\\|\\ |\\¡|\\¢|\\£|\\¤|\\¥|\\¦|\\§|\\¨|\\©|\\ª|\\«|\\¬|\\­|\\®|\\¯|\\°|\\±|\\²|\\³|\\´|\\µ|\\¶|\\·"
            + "|\\¸|\\¹|\\º|\\»|\\¼|\\½|\\¾|\\¿|\\À|\\Á|\\Â|\\Ã|\\Ä|\\Å|\\Æ|\\Ç|\\È|\\É|\\Ê|\\Ë|\\Ì|\\Í|\\Î|\\Ï|\\Ð|\\Ñ|\\Ò|\\Ó|\\Ô|\\Õ"
            + "|\\Ö|\\×|\\Ø|\\Ù|\\Ú|\\Û|\\Ü|\\Ý|\\Þ|\\ß|\\à|\\á|\\â|\\ã|\\ä|\\å|\\æ|\\ç|\\è|\\é|\\ê|\\ë|\\ì|\\í|\\î|\\ï|\\ð|\\ñ|\\ò|\\ó"
            + "|\\ô|\\õ|\\ö|\\÷|\\ø|\\ù|\\ú|\\û|\\ü|\\ý|\\þ";
    public static Pattern patronESP = Pattern.compile(ESP);
    
    public static String SYM = "\\?|\\+||\\*|\\|\r|\n|\t";
    public static Pattern patronSYM = Pattern.compile(SYM);
    
    // Muestra el error, recibe el error que se detectó
    public static void E1(String error) throws Exception
    {
        throw new Exception("Error en la linea: " + Linea + ", columna: " + Caracteres + ". " + error);
    }
    
    public static void EGen(String error) throws Exception
    {
        throw new Exception(error);
    }
    
    // Lee un caracter del archivo y aumenta la linea
    public static String leerCaracter() throws IOException
    {
        Caracteres++;
        int caracter = Lector.read();
        if (caracter == -1)
        {
            return " ";
        }
        else
        {
            return String.valueOf((char) caracter);
        }
    }
    
    // Elimina los espacios que no son necesarios y retorna el último caracter encontrado
    public static String Espacios(String caracter) throws IOException
    {
        while (caracter.equals(" ") || caracter.equals("\t") || caracter.equals("\r") || caracter.equals("\n") || caracter.equals(""))
        {
            switch (caracter)
            {
                case "\r":
                    switch (leerCaracter())
                    {
                        case "\n":
                            Caracteres = 1;
                            Linea++;
                            break;
                    }
                    break;
                case " ":
                    break;
                case "\n":
                    Caracteres = 1;
                    Linea++;
                    break;
                case "\t":
                    break;
                default:
                    return caracter;
            }
            caracter = leerCaracter();
        }
        return caracter;
    }
    
    // Busca una palabra y recibe un caracter que la puede limitar
    public static String[] leerPalabra(String caracter, String limite1, String limite2) throws IOException
    {
        String palabra = "";
        while (!caracter.equals(" ") && !caracter.equals("\t") && !caracter.equals("\r") && !caracter.equals("\n") && !caracter.equals(limite1) && !caracter.equals(limite2))
        {
            palabra += caracter;
            caracter = leerCaracter();
        }
        String[] retorno = {palabra, caracter};
        return retorno;
    }
    
    public static String[] leerPalabra(String caracter, String limite) throws IOException
    {
        String palabra = "";
        while (!caracter.equals(" ") && !caracter.equals("\t") && !caracter.equals("\r") && !caracter.equals("\n") && !caracter.equals(limite))
        {
            palabra += caracter;
            caracter = leerCaracter();
        }
        String[] retorno = {palabra, caracter};
        return retorno;
    }
    
    public static String[] leerPalabra(String caracter) throws IOException
    {
        String palabra = "";
        while (!caracter.equals(" ") && !caracter.equals("\t") && !caracter.equals("\r") && !caracter.equals("\n") && !caracter.equals(""))
        {
            palabra += caracter;
            caracter = leerCaracter();
        }
        String[] retorno = {palabra, caracter};
        return retorno;
    }
    
    public static boolean AnalizarRango(int rangoInicial, int rangoFinal) throws Exception
    {
        if (rangoInicial < rangoFinal)
        {
            if (rangoInicial >= 1 && rangoInicial <= 255)
            {
                if (rangoFinal >= 1 && rangoFinal <= 255)
                {
                    return true;
                }
                else
                {
                    Caracteres--;
                    E1("El rango de fin está fuera de los límites permitidos (32 y 254).");
                }
            }
            else
            {
                Caracteres--;
                E1("El rango de inicio está fuera de los límites permitidos (32 y 254).");
            }
        }
        else
        {
            Caracteres--;
            E1("El rango de inicio no puede ser mayor al rango de fin.");
        }
        return false;
    }
}
