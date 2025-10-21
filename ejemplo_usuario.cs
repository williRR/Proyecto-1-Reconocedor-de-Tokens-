using System;
class Programa {
    static void Main()holaaa
    {
        Console.WriteLine("¡Hola! Vamos a hacer unas operaciones básicas.");

        // Declarar variables
        int a = 5;
        int b = 3;

        // Suma
        int suma = a + b;
        Console.WriteLine($"La suma de {a} y {b} es {suma}.");

        // Resta
        int resta = a - b;
        Console.WriteLine($"La resta de {a} y {b} es {resta}.");

        // Multiplicación
        int multiplicacion = a * b;
        Console.WriteLine($"La multiplicación de {a} y {b} es {multiplicacion}.");

        // División
        double division = (double)a / b;
        Console.WriteLine($"La división de {a} entre {b} es {division}.");

        // Condicional simple
        if (a > b)
        {
            Console.WriteLine($"{a} es mayor que {b}.");
        }
        else
        {
            Console.WriteLine($"{b} es mayor o igual que {a}.");
        }
    }
}

