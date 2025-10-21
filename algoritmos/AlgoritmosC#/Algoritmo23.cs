

//*Algoritmo 3 Características de Personas*
using System;
using System.Collections.Generic;

// Definición de clase Persona
class Persona
{
    public string Nombre { get; set; }
    public int Edad { get; set; }

    // Constructor
    public Persona(string nombre, int edad)
    {
        Nombre = nombre;
        Edad = edad;
    }

    // Método para mostrar información
    public void MostrarInfo()
    {
        Console.WriteLine($"Nombre: {Nombre}, Edad: {Edad}");
    }
}

class Programa
{
    static void Main()
    {
        // Crear una lista para guardar personas
        List<Persona> listaPersonas = new List<Persona>();

        // Agregar personas a la lista
        listaPersonas.Add(new Persona("Ana", 30));
        listaPersonas.Add(new Persona("Luis", 25));
        listaPersonas.Add(new Persona("Sofía", 28));

        // Recorrer y mostrar la información de cada persona
        foreach (Persona p in listaPersonas)
        {
            p.MostrarInfo();
        }

        // Encontrar la persona más joven
        Persona personaMasJoven = listaPersonas[0];
        foreach (Persona p in listaPersonas)
        {
            if (p.Edad < personaMasJoven.Edad)
            {
                personaMasJoven = p;
            }
        }

        Console.WriteLine($"La persona más joven es {personaMasJoven.Nombre} con {personaMasJoven.Edad} años.");
    }
}