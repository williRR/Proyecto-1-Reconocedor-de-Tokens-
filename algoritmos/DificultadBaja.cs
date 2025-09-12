using System;
using System.Text;

public class AlgoritmosBajaDificultad
{
    public static string InvertirCadena(string texto)
    {
        if (string.IsNullOrEmpty(texto))
        {
            return texto;
        }

        StringBuilder cadenaInvertida = new StringBuilder();
        for (int i = texto.Length - 1; i >= 0; i--)
        {
            cadenaInvertida.Append(texto[i]);
        }

        return cadenaInvertida.ToString();
    }
}