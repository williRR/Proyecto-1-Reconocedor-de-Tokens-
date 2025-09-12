using System;
using System.Collections.Generic;

public class AlgoritmosAltaDificultad
{
    public class Nodo
    {
        public int X, Y;
        public int CostoG; // Costo del camino desde el inicio
        public int CostoH; // Costo heurístico estimado hasta el final
        public int CostoF => CostoG + CostoH;
        public Nodo Padre;

        public Nodo(int x, int y)
        {
            X = x;
            Y = y;
        }
    }

    public static List<Nodo> EncontrarCaminoAStar(int[,] laberinto, Nodo inicio, Nodo final)
    {
        int ancho = laberinto.GetLength(0);
        int alto = laberinto.GetLength(1);

        // Colas de prioridad y sets para optimización
        var colaAbierta = new PriorityQueue<Nodo, int>();
        var nodosCerrados = new HashSet<(int, int)>();

        inicio.CostoG = 0;
        inicio.CostoH = DistanciaManhattan(inicio, final);
        colaAbierta.Enqueue(inicio, inicio.CostoF);

        while (colaAbierta.Count > 0)
        {
            Nodo actual = colaAbierta.Dequeue();

            if (actual.X == final.X && actual.Y == final.Y)
            {
                return ReconstruirCamino(actual);
            }

            nodosCerrados.Add((actual.X, actual.Y));

            // Explorar vecinos (arriba, abajo, izquierda, derecha)
            int[,] direcciones = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
            foreach (var dir in direcciones)
            {
                int nuevoX = actual.X + dir[0];
                int nuevoY = actual.Y + dir[1];

                if (nuevoX >= 0 && nuevoX < ancho && nuevoY >= 0 && nuevoY < alto && laberinto[nuevoX, nuevoY] == 0 && !nodosCerrados.Contains((nuevoX, nuevoY)))
                {
                    int nuevoCostoG = actual.CostoG + 1;

                    // Si el vecino ya está en la cola abierta, revisar si la nueva ruta es mejor
                    // (Lógica simplificada para el ejemplo)
                    Nodo vecino = new Nodo(nuevoX, nuevoY);
                    vecino.CostoG = nuevoCostoG;
                    vecino.CostoH = DistanciaManhattan(vecino, final);
                    vecino.Padre = actual;

                    colaAbierta.Enqueue(vecino, vecino.CostoF);
                }
            }
        }
        return new List<Nodo>(); // No se encontró camino
    }

    private static int DistanciaManhattan(Nodo a, Nodo b)
    {
        return Math.Abs(a.X - b.X) + Math.Abs(a.Y - b.Y);
    }

    private static List<Nodo> ReconstruirCamino(Nodo nodoFinal)
    {
        List<Nodo> camino = new List<Nodo>();
        Nodo actual = nodoFinal;
        while (actual != null)
        {
            camino.Add(actual);
            actual = actual.Padre;
        }
        camino.Reverse();
        return camino;
    }
}