import java.io.*;
import java.util.*;

public class RLE {

    public static void compress(InputStream is, OutputStream os) throws Exception {
        // Creamos una ArrayList que llamará al metodo insertarBytes y los añadirá en la lista
        ArrayList<Byte> list = insertarBytes(is.readAllBytes());
        int contador = 0;
        // Recorremos la lista
        for (int i = 0; i < list.size(); i++) {
            // Guardamos el último indice de la lista
            byte guardarIndice = list.get(i);
            for (int j = i; j < list.size(); j++) {
                if (list.get(j) == guardarIndice) {
                    if (contador <= 255) {
                        contador++;
                        i = j;
                    } else {
                        Bytes(os, guardarIndice, contador + 1);
                        contador = 0;
                    }
                } else {
                    break;
                }
            }
            Bytes(os, guardarIndice, contador);
            contador = 0;
        }
        os.close();
    }

    // Método que insertará los bytes dentro de la lista
    private static ArrayList<Byte> insertarBytes(byte[] is) {
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < is.length; i++) {
            bytes.add(is[i]);
        }
        return bytes;
    }

    private static void Bytes(OutputStream os, byte lastByte, int count) throws IOException {
        if (count >= 2) {
            os.write(lastByte);
            os.write(lastByte);
            os.write(count == 2 ? (byte) 0 : (byte) count - 2);
        } else {
            os.write(lastByte);
        }
    }


    public static void decompress(InputStream is, OutputStream os) throws Exception {
        ArrayList<Byte> lista = insertarBytes(is.readAllBytes());
        for (int i = 0; i < lista.size(); i++) {
            // Comprobamos que no llega al final, el i+1 es para coger el siguiente indice
            if (i + 1 < lista.size()) {
                byte comprobar = lista.get(i+1);
                if (lista.get(i) == comprobar){
                    os.write(lista.get(i));
                    for (int j = 0; j <= (lista.get(i+2) & 0xFF); j++) {
                        os.write(lista.get(i));
                    }
                    i++;
                    lista.remove(i+1);
                } else {
                    os.write(lista.get(i));
                }
            } else {
                os.write(lista.get(i));
            }
        }
    }
}