using System;
using System.Collections.Generic;

public class AlgoritmosMediaDificultad
{
    public static int EncontrarPrimoMasGrande(int limiteSuperior)
    {
        if (limiteSuperior <= 1)
        {
            return -1;
        }

        int primoMasGrande = -1;
        for (int numero = 2; numero <= limiteSuperior; numero++)
        {
            if (EsPrimo(numero))
            {
                primoMasGrande = numero;
            }
        }
        return primoMasGrande;
    }

    private static bool EsPrimo(int numero)
    {
        if (numero <= 1)
        {
            return false;
        }

        // Optimización: solo necesitamos verificar hasta la raíz cuadrada del número
        for (int i = 2; i * i <= numero; i++)
        {
            if (numero % i == 0)
            {
                return false; // No es primo
            }
        }

        return true; // Es primo
    }
}