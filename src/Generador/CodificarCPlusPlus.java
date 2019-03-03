/*
 * To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generador;

import Modelos.Estado;
import static genscann.GenScann.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Axel Rodriguez
 */
public final class CodificarCPlusPlus {
    // Globales de la clase CodificarCSharp
    FileWriter escritor = null;
    // Contenido por defecto
    String[] Usings = { "#include \"stdafx.h\"", "#include <stdio.h>", "#include <stdlib.h>", "#include <iostream>", "using namespace std;", "\n"};
    String[] Propiedades = { "StreamReader lector { get; set; }", "StreamWriter escritor { get; set; }", 
       "public String ArchivoEntrada { get; private set; }", "public String ArchivoSalida { get; private set; }" };
    String NameSpace = "namespace PruebaGenScann";
    String ClassName = "public class GenScann";
    String ConjuntosTexto = "";
    String AccionesTexto = "";
    String VerificadorAcciones = "";
    String FuncionLeerCaracter = "        "
            
            
            + "std::wstring <missing_class_definition>::leerCaracter()\n"+
    "{\n"+
			"int caracter = lector::Read();\n"+
			"if (caracter == -1)\n"+
			"{\n"+
				"return L\"\";\n"+
			"}\n"+
			"else\n"+
			"{\n"+
				"wchar_t car = static_cast<wchar_t>(caracter);\n"+
				"return StringHelper::toString(car);\n"+
			"}\n"+
    "}\n";
            ;
    String FuncionLeerPalabra = "        "
            + "std::wstring GenScann::leerPalabra(const std::wstring &caracter)\n"+
				"{\n"+
					" std::wstring palabra = \"\";\n while (!caracter.Equals(\" \") && !caracter.Equals(\"\\t\") && !caracter.Equals(\"\\r\") && !caracter.Equals(\"\\n\") && !caracter.Equals(\"\")) {palabra += caracter; caracter = leerCaracter();} return palabra;\n"+
					"}; } }\n";

//cambiar ruta
    String Constructor = "        "
            + "FILE * flujo = fopen(\"C:/Users/Mario/Desktop/gen.txt\",\"rb\");\r\n" + 
				"	if (flujo == NULL) {\r\n" + 
				"		perror(\"Error en la apertura de archivo\");\r\n" + 
				"		return 1;\r\n" + 
				"	}\r\n" + 
				"\r\n";
    String FuncionVerificarConjunto = "        "
            + 	"bool GenScann::VerificarConjunto(std::vector<std::wstring> &Conjunto, const std::wstring &caracter)\n"+
	"{\n"+
					"for (int i = 0; i < Conjunto.size(); i++)\n"+
					"{\n"+
						"if (caracter == Conjunto[i])\n"+
						"{\n"+
							"return true;\n"+
						"}\n"+
					"}\n"+
					"return false;\n"+
	"}\n";
    String FuncionesAutomatas = "";
    
    String FuncionComerEspacios = "        "
            + "public String ComerEspacios()\n" +
            "        {\n" +
            "            String caracter = leerCaracter();\n" +
            "            while (caracter.Equals(\" \") || caracter.Equals(\"\\t\") || caracter.Equals(\"\\r\") || caracter.Equals(\"\\n\") || caracter.Equals(\"\"))\n" +
            "            {\n" +
            "                if (caracter.Equals(\"\"))\n" +
            "                    return \"\";\n" +
            "                else\n" +
            "                    caracter = leerCaracter();\n" +
            "            }\n" +
            "            return caracter;\n" +
            "        }";
    
    String FuncionComparar = "";
    
    public CodificarCPlusPlus(String ubicacion) throws IOException
    {
        escritor = new FileWriter(new File(ubicacion + "Codificado.cpp"));
        GenerarConjuntos();
        GenerarAcciones();
        GenerarVerificadorAcciones();
        GenerarAutomatas();
        GenerarComparador();
        EscribirCodigo();
    }
    
    public void GenerarComparador()
    {
        String tabs = "\t\t";
        FuncionComparar += tabs + "public void Comparador()\n" + tabs + "{\n";
        FuncionComparar += tabs + "\t";
        FuncionComparar += "String palabra = leerPalabra(leerCaracter()); String car = \"\";";
        FuncionComparar += "\n" + tabs + "\t";
        FuncionComparar += "escritor = new StreamWriter(ArchivoSalida);";
        FuncionComparar += "\n" + tabs + "\t";
        FuncionComparar += "while (palabra != \"\")";
        FuncionComparar += "\n" + tabs + "\t";
        FuncionComparar += "{";
        FuncionComparar += "\n" + tabs + tabs;
        FuncionComparar += "if (!VerificarAccion(palabra).Equals(\"-1\"))";
        FuncionComparar += "\n" + tabs + tabs;
        FuncionComparar += "{";
        FuncionComparar += "\n" + tabs + tabs + "\t";
        FuncionComparar += "escritor.WriteLine(palabra + \" = \" + VerificarAccion(palabra).ToString());";
        FuncionComparar += "\n" + tabs + tabs;
        FuncionComparar += "}";
        for (int i = 0; i < Tokens.size(); i++)
        {
            FuncionComparar += "\n" + tabs + tabs;
            FuncionComparar += "else if (Expresion_" + Tokens.get(i).getNombre() + "(palabra))";
            FuncionComparar += "\n" + tabs + tabs;
            FuncionComparar += "{";
            FuncionComparar += "\n\t" + tabs + tabs;
            FuncionComparar += "escritor.WriteLine(palabra + \" = " + Tokens.get(i).getNombre() + "\");";
            FuncionComparar += "\n" + tabs + tabs;
            FuncionComparar += "}";
        }
        FuncionComparar += "\n" + tabs + tabs;
        FuncionComparar += "else";
        FuncionComparar += "\n" + tabs + tabs;
        FuncionComparar += "{";
        FuncionComparar += "\n\t" + tabs + tabs;
        FuncionComparar += "escritor.WriteLine(palabra + \" = La palabra no pertenece a ningun Token.\");";
        FuncionComparar += "\n" + tabs + tabs;
        FuncionComparar += "}";
        FuncionComparar += "\n" + tabs + "\t";
        FuncionComparar += "car = ComerEspacios();";
        FuncionComparar += "palabra = leerPalabra(car);";
        FuncionComparar += "\n" + tabs + "\t";
        FuncionComparar += "}";
        FuncionComparar += "\n\t" + tabs;
        FuncionComparar += "escritor.Close();";
        FuncionComparar += "\n" + tabs + "}";
    }
    
    public void GenerarAutomatas()
    {
        String automata = ""; String tabs = "\t\t";
        String ifGenerado = ""; TablaEstados tEstados = null;
        List<Transicion> estadosTemp = new ArrayList(); int k = 0;
        for (int i = 0; i < expresionRegularCompleta.size(); i++)
        {
            tEstados = expresionRegularCompleta.get(i).TablaDeEstados;
            automata += tabs + "public bool Expresion_" + Tokens.get(i).getNombre() + "(String palabra)\n";
            automata += tabs + "{\n";
            automata += tabs + "\t";
            automata += "int Estado = 0; int posicion = 0;\n\t" + tabs;
            automata += "for (posicion = 0; posicion < palabra.Length; posicion++)\n\t" + tabs;
            automata += "{\n" + tabs + tabs;
            automata += "switch (Estado)\n" + tabs + tabs + "{";
            for (int j = 0; j < tEstados.TablaEstados.size(); j++)
            {
                automata += "\n\t" + tabs + tabs;
                automata += "case " + tEstados.TablaEstados.get(j)[0].Nombre + ":";
                Estado[] estadosT = tEstados.TablaEstados.get(j);
                for (k = 1; k < estadosT.length; k++)
                {
                    if (estadosT[k] != null)
                    {
                        estadosTemp.add(new Transicion(estadosT[k], tEstados.HojasUnicas.get(k - 1)));
                    }
                }
                ifGenerado = "";
                for (k = 0; k < estadosTemp.size(); k++)
                {
                    if (k == 0)
                    {
                        if (estadosTemp.size() == 1)
                        {
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            if (estadosTemp.get(k).esCaracter)
                                ifGenerado += "if (palabra[posicion].Equals(\"" + estadosTemp.get(k).ConQue + "\"))";
                            else
                                ifGenerado += "if (VerificarConjunto(" + estadosTemp.get(k).ConQue.toUpperCase() + ", palabra[posicion].ToString()))";
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            ifGenerado += "{";
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            if (!tEstados.TablaEstados.get(j)[0].Aceptacion)
                            {
                                ifGenerado += "\tEstado = " + estadosTemp.get(k).Estado.Nombre + ";";
                            }
                            else
                            {
                                ifGenerado += "\tif (posicion == palabra.Length - 1)";
                                ifGenerado += "\n\t" + tabs + tabs + tabs;
                                ifGenerado += "{";
                                ifGenerado += "\n\t" + tabs + tabs + tabs;
                                ifGenerado += "\treturn true;";
                                ifGenerado += "\n\t" + tabs + tabs + tabs;
                                ifGenerado += "}";
                                ifGenerado += "\n\t" + tabs + tabs + tabs;
                                ifGenerado += "else";
                                ifGenerado += "\n\t" + tabs + tabs + tabs;
                                ifGenerado += "{";
                                ifGenerado += "\n" + tabs + tabs + tabs;
                                ifGenerado += tabs + "Estado = " + estadosTemp.get(k).Estado.Nombre + ";";
                                ifGenerado += "\n\t" + tabs + tabs + tabs;
                                ifGenerado += "}";
                            }
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            ifGenerado += "}";
                        }
                        else
                        {
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            if (estadosTemp.get(k).esCaracter)
                                ifGenerado += "if (palabra[posicion].Equals(\"" + estadosTemp.get(k).ConQue + "\"))";
                            else
                                ifGenerado += "if (VerificarConjunto(" + estadosTemp.get(k).ConQue.toUpperCase() + ", palabra[posicion].ToString()))";
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            ifGenerado += "{";
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            ifGenerado += "\tEstado = " + estadosTemp.get(k).Estado.Nombre + ";";
                            ifGenerado += "\n" + tabs + tabs + tabs;
                            ifGenerado += "}";
                        }
                    }
                    else
                    {
                        ifGenerado += "\n" + tabs + tabs + tabs;
                        if (estadosTemp.get(k).esCaracter)
                                ifGenerado += "else if (palabra[posicion].Equals(\"" + estadosTemp.get(k).ConQue + "\"))";
                            else
                                ifGenerado += "else if (VerificarConjunto(" + estadosTemp.get(k).ConQue.toUpperCase() + ", palabra[posicion].ToString()))";
                        ifGenerado += "\n" + tabs + tabs + tabs;
                        ifGenerado += "{";
                        ifGenerado += "\n" + tabs + tabs + tabs;
                        ifGenerado += "\tEstado = " + estadosTemp.get(k).Estado.Nombre + ";";
                        ifGenerado += "\n" + tabs + tabs + tabs;
                        ifGenerado += "}";
                    }
                }
                if (tEstados.TablaEstados.get(j)[0].Aceptacion && estadosTemp.size() == 0)
                {
                    ifGenerado += "\n" + tabs + tabs + tabs;
                    ifGenerado += "if (posicion == palabra.Length - 1)";
                    ifGenerado += "\n" + tabs + tabs + tabs;
                    ifGenerado += "{";
                    ifGenerado += "\n" + tabs + tabs + tabs;
                    ifGenerado += "\treturn true;";
                    ifGenerado += "\n" + tabs + tabs + tabs;
                    ifGenerado += "}";
                }
                ifGenerado += "\n" + tabs + tabs + tabs;
                ifGenerado += "else";
                ifGenerado += "\n" + tabs + tabs + tabs;
                ifGenerado += "{";
                ifGenerado += "\n" + tabs + tabs + tabs;
                ifGenerado += "\treturn false;";
                ifGenerado += "\n" + tabs + tabs + tabs;
                ifGenerado += "}";
                automata += ifGenerado;
                ifGenerado = "";
                estadosTemp.clear();
                automata += "\n" + tabs + tabs + tabs;
                automata += "break;";
            }
            automata += "\n\t" + tabs + tabs;
            automata += "default:";
            automata += "\n" + tabs + tabs + tabs;
            automata += "return false;";
            automata += "\n" + tabs + tabs + "}";
            automata += "\n" + tabs + "\t}";
            automata += "\n" + tabs;
            automata += "return false;";
            automata += "\n" + tabs + "}\n\n";
            FuncionesAutomatas += automata;
            automata = "";
        }
    }
    
    public void GenerarConjuntos()
    {
        String conjunto;
        int i = 0;
        while (i < Conjuntos.size())
        {
            conjunto = "\t\tpublic String[] " + Conjuntos.get(i).getNombre().toUpperCase() + " = \n\t\t\t{ ";
            for (int j = 0; j < Conjuntos.get(i).Contenido.size(); j++)
            {
                switch (Conjuntos.get(i).Contenido.get(j))
                {
                    case '\"':
                        if (j == Conjuntos.get(i).Contenido.size() - 1)
                            conjunto += "\"\\" + Conjuntos.get(i).Contenido.get(j) + "\" ";
                        else
                            conjunto += "\"\\" + Conjuntos.get(i).Contenido.get(j) + "\", ";
                        break;
                    case '\\':
                        if (j == Conjuntos.get(i).Contenido.size() - 1)
                            conjunto += "\'" + Conjuntos.get(i).Contenido.get(j) + Conjuntos.get(i).Contenido.get(j) + "\' ";
                        else
                            conjunto += "\'" + Conjuntos.get(i).Contenido.get(j) + Conjuntos.get(i).Contenido.get(j) + "\', ";
                        break;
                    default:
                        if (j == Conjuntos.get(i).Contenido.size() - 1)
                            conjunto += "\"" + Conjuntos.get(i).Contenido.get(j) + "\" ";
                        else
                            conjunto += "\"" + Conjuntos.get(i).Contenido.get(j) + "\", ";
                        break;
                }
            }
            conjunto += "};";
            ConjuntosTexto += conjunto + "\n";
            i++;
        }
    }
    
    public void GenerarAcciones()
    {
        String accion;
        int i = 0; String contenido;
        while (i < Acciones.size())
        {
            accion = "\t\tpublic String[,] " + Acciones.get(i).getNombre() + " = \n\t\t\t{";
            for (int j = 0; j < Acciones.get(i).Items.size(); j++)
            {
                contenido = Acciones.get(i).Items.get(j).getContenido().replaceAll("\\\"", "\\\"");
                if (j == Acciones.get(i).Items.size() - 1)
                    accion += " { \"" + Acciones.get(i).Items.get(j).getID() + "\", \"" + contenido + "\" } ";
                else
                    accion += " { \"" + Acciones.get(i).Items.get(j).getID() + "\", \"" + contenido + "\" }, ";
            }
            accion += "};";
            AccionesTexto += accion + "\n";
            i++;
        }
    }
    
    public void GenerarVerificadorAcciones()
    {
        String tabs = "\t\t";
        String funcion = tabs + "public String VerificarAccion(String verificar)\n" + tabs + "{\n";
        for (int i = 0; i < Acciones.size(); i++)
        {
            funcion += tabs + "\tfor (int i = 0; i < " + Acciones.get(i).Items.size() + "; i++)\n" + tabs + "\t{\n";
            funcion += tabs + tabs + "if (" + Acciones.get(i).getNombre() + "[i, 1].ToUpper().Equals(verificar.ToUpper()))\n" + tabs + tabs + "{\n";
            funcion += tabs + tabs + "\treturn " + Acciones.get(i).getNombre() + "[i, 0];\n";
            funcion += tabs + tabs + "}\n";
            funcion += tabs + "\t}\n";
        }
        funcion += tabs + "\treturn \"-1\";\n";
        funcion += tabs + "}\n";
        VerificadorAcciones = funcion;
    }
    
    public void EscribirCodigo() throws IOException
    {
        // Escribir Usings
        for (int i = 0; i < Usings.length; i++)
        {
            escritor.write(Usings[i] + "\n");
        }
        // Escribir Namespace
        escritor.write(NameSpace + "\n{\n");
        // Escribir definición de la clase 
        escritor.write("\t" + ClassName + "\n\t{\n");
        // Escribir Propiedades
        for (int i = 0; i < Propiedades.length; i++)
        {
            escritor.write("\t\t" +Propiedades[i] + "\n");
        }
        // Escribir Conjuntos
        escritor.write(ConjuntosTexto);
        // Escribir Acciones
        escritor.write(AccionesTexto + "\n");
        // Escribir Constructor
        escritor.write(Constructor + "\n\n");
        // Escribir función ComerEspacios
        escritor.write(FuncionComerEspacios);
        // Escribir función VerificarConjunto
        escritor.write(FuncionVerificarConjunto);
        // Escribir función leerCaracter
        escritor.write(FuncionLeerCaracter + "\n\n");
        // Escribir función leerPalabra
        escritor.write(FuncionLeerPalabra + "\n\n");
        // Escribir el Verificador de Acciones
        escritor.write(VerificadorAcciones + "\n\n");
        // Escribe los Autómatas
        escritor.write(FuncionesAutomatas);
        // Escribe la función comparadora
        escritor.write(FuncionComparar);
        // Escribir Cierre de Clase
        escritor.write("\n\t}");
        // Escribir Cierre de Namespace
        escritor.write("\n}");
        escritor.close();
    }
    private class Transicion
    {
        public Estado Estado = null;
        
        public String ConQue = "";
        
        public boolean esCaracter = false;
        public Transicion(Estado estado, String conQue)
        {
            Estado = estado;
            ConQue = conQue;
            if ((char) ConQue.charAt(0) == '"' || (char) ConQue.charAt(0) == '\'')
            {
                ConQue = String.valueOf((char) ConQue.charAt(1));
                esCaracter = true;
            }
        }
    }
}