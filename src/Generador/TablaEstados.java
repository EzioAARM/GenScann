/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generador;

import Modelos.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Axel Rodriguez
 */
public final class TablaEstados {
    
    public List<Estado> Estados = null;
    
    List<Nodo> Hojas = null;
    
    List<Nodo> Nodos = null;
    
    public List<String> HojasUnicas = null;
    
    public List<Estado[]> TablaEstados = null;
    
    List<Follow> Follow = null;
    
    public List<Integer> FirstDeRaiz = null;
    
    int ContadorEstados = 0;
    
    public TablaEstados(List<Nodo> hojas, List<Nodo> nodos, List<String> hojasUnicas, List<Integer> FirstRaiz, List<Follow> follow)
    {
        Estados = new ArrayList();
        Hojas = hojas;
        Nodos = nodos;
        HojasUnicas = hojasUnicas;
        Follow = follow;
        TablaEstados = new ArrayList();
        TablaEstados.add(new Estado[hojasUnicas.size() + 1]);
        FirstRaiz = EliminarRepetidos(FirstRaiz);
        FirstDeRaiz = FirstRaiz;
        Estados.add(new Estado(ContadorEstados, FirstRaiz));
        ContadorEstados++;
        ValidarEstado(FirstRaiz);
        ContadorEstados++;
        int i = 1;
        while (i < Estados.size())
        {
            TablaEstados.add(new Estado[hojasUnicas.size() + 1]);
            ValidarEstado(Estados.get(i).Contenido);
            i++;
        }
        int campoAceptacion = -1;
        i = 0;
        while (i < Hojas.size())
        {
            if (Hojas.get(i).Valor.equals("#"))
            {
                campoAceptacion = Hojas.get(i).Id;
                i = Hojas.size();
            }
            i++;
        }
        for (i = 0; i < Estados.size(); i++)
        {
            for (int j = 0; j < Estados.get(i).Contenido.size(); j++)
            {
                if (Estados.get(i).Contenido.get(j) == campoAceptacion)
                {
                    Estados.get(i).Aceptacion = true;
                }
            }
        }
        ContadorEstados++;
    }
    
    public List<Integer> EliminarRepetidos(List<Integer> datos)
    {
        HashSet<Integer> hashSet = new HashSet<Integer>(datos);
        datos.clear();
        datos.addAll(hashSet);
        return datos;
    }
    
    public List<Integer> AgregarFollow(List<Integer> datos, int id)
    {
        for (int i = 0; i < Follow.size(); i++)
        {
            if (Follow.get(i).Dato == id)
            {
                for (int j = 0; j < Follow.get(i).Producciones.size(); j++)
                {
                    datos.add(Follow.get(i).Producciones.get(j));
                }
            }
        }
        return datos;
    }
    
    public void ValidarEstado(List<Integer> datos)
    {
        List<List<Integer>> estadosCreados = new ArrayList();
        for (int i = 0; i < HojasUnicas.size(); i++)
        {
            estadosCreados.add(new ArrayList());
            for (int j = 0; j < datos.size(); j++)
            {
                for (int k = 0; k < Hojas.size(); k++)
                {
                    Nodo nodo = Hojas.get(k);
                    boolean cond1 = datos.get(j) == nodo.Id;
                    boolean cond2 = HojasUnicas.get(i).equals(nodo.Valor);
                    if ((cond1) && (cond2))
                    {
                        estadosCreados.set(i, AgregarFollow(estadosCreados.get(i), datos.get(j)));
                        k = Hojas.size();
                    }
                }
            }
        }
        for (int i = 0; i < estadosCreados.size(); i++)
        {
            estadosCreados.set(i, EliminarRepetidos(estadosCreados.get(i)));
        }
        int[] retorno = Comparar(estadosCreados);
        int existeEstado = existe(datos);
        retorno[0] = existeEstado;
        for (int i = 0; i < retorno.length; i++)
        {
            for (int j = 0; j < Estados.size(); j++)
            {
                if (retorno[i] == Estados.get(j).Nombre)
                {
                    TablaEstados.get(TablaEstados.size() - 1)[i] = Estados.get(j);
                    j = Estados.size();
                }
            }
        }
    }
    
    public int[] Comparar(List<List<Integer>> EstadosCreados)
    {
        boolean continuar = true;
        int i = 0; int j = 0;
        int[] transiciones = new int[EstadosCreados.size() + 1];
        Estado aux = null; int numEstado = -1;
        while (i < EstadosCreados.size())
        {
            j = 0;
            while (j < Estados.size() && EstadosCreados.size() > 0)
            {
                aux = Estados.get(j);
                if (aux.Contenido.size() == EstadosCreados.get(i).size())
                {
                    for (int k = 0; k < Estados.get(j).Contenido.size(); k++)
                    {
                        if (EstadosCreados.get(i).get(k) != Estados.get(j).Contenido.get(k))
                        {
                            continuar = false;
                            k = Estados.get(j).Contenido.size();
                        }
                    }
                    if (continuar)
                    {
                        transiciones[i + 1] = Estados.get(j).Nombre;
                    }
                }
                else if (j == Estados.size() - 1)
                {
                    continuar = false;
                }
                j++;
            }
            if (!continuar)
            {
                if (EstadosCreados.get(i).size() > 0)
                {
                    numEstado = existe(EstadosCreados.get(i));
                    if (numEstado == -1)
                    {
                        Estados.add(new Estado(ContadorEstados, EstadosCreados.get(i)));
                        transiciones[i + 1] = existe(EstadosCreados.get(i));
                        continuar = true;
                        ContadorEstados++;
                    }
                    else
                    {
                        transiciones[i + 1] = numEstado;
                        continuar = true;
                        numEstado = -1;
                    }
                }
                else
                {
                    transiciones[i + 1] = -1;
                    continuar = true;
                }
            }
            i++;
        }
        return transiciones;
    }
    
    public int existe(List<Integer> estadoNuevo)
    {
        boolean coincidenTodos = true; int j = 0;
        int k = 0;
        for (int i = 0; i < Estados.size(); i++)
        {
            if (Estados.get(i).Contenido.size() == estadoNuevo.size())
            {
                j = 0; k = 0;
                while (j < Estados.get(i).Contenido.size() && coincidenTodos)
                {
                    if (Estados.get(i).Contenido.get(j) == estadoNuevo.get(j))
                    {
                        k++;
                    }
                    j++;
                }
                if (j == k)
                {
                    return Estados.get(i).Nombre;
                }
            }
        }
        return -1;
    }
}
