/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import static AnalizadorEntrada.Globales.EGen;
import Generador.TablaEstados;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Axel Rodriguez
 */
public final class ExpresionCompleta {
    public String ExpresionRegular;
    
    public List<Follow> Follow = null;
    
    public String[] ExpresionSeparada = null;
    
    private Nodo Raiz = null;
    
    private int contador = 1;
    
    private List<Nodo> Hojas = new ArrayList();
    
    int cont = 0;
    
    public List<Nodo> nodos = new ArrayList();
    
    public TablaEstados TablaDeEstados = null;
    
    public ExpresionCompleta(String expresionRegular) throws Exception
    {
        ExpresionRegular = expresionRegular;
        Follow = new ArrayList();
        ExpresionSeparada = ExpresionRegular.split("\\ +");
        List<String> ExpresionSeparadaLista = new ArrayList();
        int i = 0;
        while (i < ExpresionSeparada.length)
        {
            ExpresionSeparadaLista.add(ExpresionSeparada[i]);
            i++;
        }
        i = 0;
        while (i < ExpresionSeparadaLista.size())
        {
            if (ExpresionSeparadaLista.get(i).equals(""))
            {
                ExpresionSeparadaLista.remove(i);
            }
            else
                i++;
        }
        List<String> PostFijo = deInfijoPostfijo(ExpresionSeparadaLista);
        PostFijo.add("#"); PostFijo.add(".");
        Raiz = dePostfijoArbol(PostFijo);
        PostOrden(Raiz);
        InOrden(Raiz);
        List<String> hojasTemp = new ArrayList();
        for (int j = 0; j < Hojas.size(); j++)
        {
            hojasTemp.add(Hojas.get(j).Valor);
        }
        HashSet<String> hashSet = new HashSet<String>(hojasTemp);
        hojasTemp.clear();
        hojasTemp.addAll(hashSet);
        TablaDeEstados = new TablaEstados(Hojas, nodos, hojasTemp, Raiz.First, Follow);
    }
    
    public boolean Precedencia(String E, String ultimoPila)
    {
        int numE = 0; int numUltimo = 0;
        if (E.equals("*") || E.equals("+") || E.equals("?"))
        {
            numE = 50;
        }
        else if (E.equals("."))
        {
            numE = 40;
        }
        else if (E.equals("|"))
        {
            numE = 30;
        }
        if (ultimoPila.equals("*") || ultimoPila.equals("+") || ultimoPila.equals("?"))
        {
            numUltimo = 50;
        }
        else if (ultimoPila.equals("."))
        {
            numUltimo = 40;
        }
        else if (ultimoPila.equals("|"))
        {
            numUltimo = 30;
        }
        return numUltimo >= numE;
    }
    
    public boolean esVacio(Nodo a)
    {
        return (a == null);
    }
    
    public boolean esHoja(Nodo nodo)
    {
        return (nodo.Izquierdo == null && nodo.Derecho == null);
    }
    
    public List<Integer> AgregarListas(List<Integer> datos, List<Integer> principal)
    {
        for (int i = 0; i < datos.size(); i++)
        {
            principal.add(datos.get(i));
        }
        return principal;
    }
    
    public void HacerFollow(List<Integer> c1, List<Integer> c2)
    {
        for (int i = 0; i < Follow.size(); i++)
        {
            for (int j = 0; j < c1.size(); j++)
            {
                if (Follow.get(i).Dato == c1.get(j))
                {
                    for (int k = 0; k < c2.size(); k++)
                    {
                        Follow.get(i).Producciones.add(c2.get(k));
                    }
                }
            }
        }
    }
    
    public void InOrden(Nodo nodo)
    {
        if (!esVacio(nodo))
        {
            InOrden(nodo.Izquierdo);
            nodos.add(nodo);
            cont++;
            InOrden(nodo.Derecho);
        }
    }
    
    public void PostOrden(Nodo nodo)
    {
        if (!esVacio(nodo))
        {
            if (esHoja(nodo))
            {
                nodo.Id = contador;
                Hojas.add(nodo);
                Follow.add(new Follow(contador));
                contador++;
            }
            
            PostOrden(nodo.Izquierdo);
            PostOrden(nodo.Derecho);
            switch (nodo.Valor)
            {
                case "*":
                    nodo.Nulabilidad = true;
                    nodo.First = AgregarListas(nodo.Derecho.First, nodo.First);
                    nodo.Last = AgregarListas(nodo.Derecho.Last, nodo.Last);
                    HacerFollow(nodo.Derecho.Last, nodo.Derecho.First);
                    break;
                case "?":
                    nodo.Nulabilidad = true;
                    nodo.First = AgregarListas(nodo.Derecho.First, nodo.First);
                    nodo.Last = AgregarListas(nodo.Derecho.Last, nodo.Last);
                    break;
                case "+":
                    nodo.Nulabilidad = false;
                    nodo.First = AgregarListas(nodo.Derecho.First, nodo.First);
                    nodo.Last = AgregarListas(nodo.Derecho.Last, nodo.Last);
                    HacerFollow(nodo.Derecho.Last, nodo.Derecho.First);
                    break;
                case "|":
                    nodo.Nulabilidad = false;
                    nodo.First = AgregarListas(nodo.Izquierdo.First, AgregarListas(nodo.Derecho.First, nodo.First));
                    nodo.Last = AgregarListas(nodo.Izquierdo.Last, AgregarListas(nodo.Derecho.Last, nodo.Last));
                    break;
                case ".":
                    nodo.Nulabilidad = false;
                    if (nodo.Izquierdo.Nulabilidad)
                    {
                        nodo.First = AgregarListas(nodo.Izquierdo.First, AgregarListas(nodo.Derecho.First, nodo.First));
                    }
                    else
                    {
                        nodo.First = AgregarListas(nodo.Izquierdo.First, nodo.First);
                    }
                    if (nodo.Derecho.Nulabilidad)
                    {
                        nodo.Last = AgregarListas(nodo.Izquierdo.Last, AgregarListas(nodo.Derecho.Last, nodo.Last));
                    }
                    else
                    {
                        nodo.Last = AgregarListas(nodo.Derecho.Last, nodo.Last);
                    }
                    HacerFollow(nodo.Izquierdo.Last, nodo.Derecho.First);
                    break;
                default:
                    nodo.Nulabilidad = false;
                    nodo.First.add(nodo.Id);
                    nodo.Last.add(nodo.Id);
                    break;
            }
        }
    }
    
    public List<String> deInfijoPostfijo(List<String> infijo)
    {
        Stack<String> Pila = new Stack<String>();
        List<String> Salida = new ArrayList(); boolean error = false;
        String E = ""; int i = 0;
        while (infijo.size() > 0 && !error)
        {
            E = infijo.get(i); 
            infijo.remove(i);
            switch (E)
            {
                case "*":
                    while (Pila.size() != 0 && Precedencia(E, Pila.peek()))
                    {
                        Salida.add(Pila.pop());
                    }
                    Pila.push(E);
                    break;
                case ".":
                    while (Pila.size() != 0 && Precedencia(E, Pila.peek()))
                    {
                        Salida.add(Pila.pop());
                    }
                    Pila.push(E);
                    break;
                case "?":
                    while (Pila.size() != 0 && Precedencia(E, Pila.peek()))
                    {
                        Salida.add(Pila.pop());
                    }
                    Pila.push(E);
                    break;
                case "+":
                    while (Pila.size() != 0 && Precedencia(E, Pila.peek()))
                    {
                        Salida.add(Pila.pop());
                    }
                    Pila.push(E);
                    break;
                case "|":
                    while (Pila.size() != 0 && Precedencia(E, Pila.peek()))
                    {
                        Salida.add(Pila.pop());
                    }
                    Pila.push(E);
                    break;
                case "(":
                    Pila.push(E);
                    break;
                case ")":
                    while (Pila.size() != 0 && !Pila.peek().equals("("))
                    {
                        Salida.add(Pila.pop());
                    }
                    if (Pila.size() != 0 && Pila.peek().equals("("))
                        Pila.pop();
                    else
                        error = true;
                    break;
                default:
                    if (!E.equals(""))
                        Salida.add(E);
                    break;
            }
        }
        while (Pila.size() != 0)
        {
            Salida.add(Pila.pop());
        }
        return Salida;
    }
    
    public Nodo dePostfijoArbol(List<String> postfijo) throws Exception
    {
        Stack<Nodo> Pila = new Stack<Nodo>();
        boolean error = false;
        Nodo E = null;
        while (postfijo.size() > 0 && !error)
        {
            E = new Nodo();
            E.Valor = postfijo.get(0); postfijo.remove(0);
            switch (E.Valor)
            {
                case "*":
                    if (Pila.size() < 1)
                    {
                        error = true;
                    }
                    else
                    {
                        Nodo nodoA2 = Pila.pop();
                        Nodo nodo = E;
                        nodo.Derecho = nodoA2;
                        Pila.push(nodo);
                    }
                    break;
                case "?":
                    if (Pila.size() < 1)
                    {
                        error = true;
                    }
                    else
                    {
                        Nodo nodoA2 = Pila.pop();
                        Nodo nodo = E;
                        nodo.Derecho = nodoA2;
                        Pila.push(nodo);
                    }
                    break;
                case "+":
                    if (Pila.size() < 1)
                    {
                        error = true;
                    }
                    else
                    {
                        Nodo nodoA2 = Pila.pop();
                        Nodo nodo = E;
                        nodo.Derecho = nodoA2;
                        Pila.push(nodo);
                    }
                    break;
                case ".":
                    if (Pila.size() < 2)
                    {
                        error = true;
                    }
                    else
                    {
                        Nodo nodoA2 = Pila.pop();
                        Nodo nodoA1 = Pila.pop();
                        Nodo nodo = E;
                        nodo.Izquierdo = nodoA1;
                        nodo.Derecho = nodoA2;
                        Pila.push(nodo);
                    }
                    break;
                case "|":
                    if (Pila.size() < 2)
                    {
                        error = true;
                    }
                    else
                    {
                        Nodo nodoA2 = Pila.pop();
                        Nodo nodoA1 = Pila.pop();
                        Nodo nodo = E;
                        nodo.Izquierdo = nodoA1;
                        nodo.Derecho = nodoA2;
                        Pila.push(nodo);
                    }
                    break;
                case "(":
                    error = true;
                    break;
                default:
                    Pila.push(E);
                    break;
            }
        }
        while (Pila.size() != 0)
        {
            Pila.pop();
        }
        return E;
    }
}
