import java.io.*;
import java.util.*;

public class LZW {
    public static void compress(InputStream is, OutputStream os) throws Exception {
        List<Caja> lista = new ArrayList<>();
        Map<String,Caja> map = new HashMap<>();
        char guardar;
        String obtenerValores = "", comparar = "";
        int index = 0;
        int contador = 0;
        byte[] bytes = is.readAllBytes();
        for (int i = 0; i < bytes.length; i++) {
            guardar = (char) (bytes[i] & 0xFF);
            comparar = obtenerValores + guardar;
            if (contador >= 256){
                map.clear();
                contador = 0;
            }
            if (!map.containsKey(comparar) || i == bytes.length -1){
                int guardarIndice;
                if (map.containsKey(obtenerValores)){
                    guardarIndice = map.get(obtenerValores).getTemporalIndex();
                } else {
                    guardarIndice = index;
                }
                Caja caja = new Caja((byte) (comparar.charAt(comparar.length()-1)), ++index, guardarIndice);
                if (comparar(lista, caja) && i + 1 < bytes.length || map.containsKey(obtenerValores)){
                    lista.add(caja);
                } else {
                    lista.add(new Caja((byte) (comparar.charAt(comparar.length()-1)), guardarIndice, 0));
                }
                map.put(comparar, caja);
                obtenerValores = "";
                contador++;
                continue;
            }
            obtenerValores = obtenerValores + guardar;
        }
        for (Caja caja : lista) {
            os.write(caja.getIndex());
            os.write(caja.getCaracter());
        }
        os.close();
    }

    private static boolean comparar(List<Caja> cajas, Caja caja){
        for (Caja c : cajas) {
            if (c.equals(caja)) return true;
        }
        return false;
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        List<Caja> cajaArrayList = list(is);

    }

    // Método que creará una lista con todos los tokens creados
    private static List<Caja> list(InputStream is) throws IOException {
        // Creamos la lista de cajas que será la que contendrá los tokens, y creamos las variables
        List<Caja> cajas = new ArrayList<>();
        int index = 0;
        char simbolo;
        // Recorremos el InputStream
        while (is.available() > 0){
            index = is.read();
            // Casteamos a char el simbolo y hacemos que no coja valores negativos
            simbolo = (char) (is.read() & 0xFF);
            // Creamos una nueva Caja con los valores
            Caja c = new Caja((byte)simbolo, index, index);
            // Añadimos a la lista la nueva caja
            cajas.add(c);
        }
        return cajas;
    }
}

class Caja{
    private Integer index;
    private Byte caracter;
    private Integer temporalIndex;

    Caja(Byte caracter, Integer temporalIndex, Integer index){
        this.index = index;
        this.caracter = caracter;
        this.temporalIndex = temporalIndex;
    }

    public Integer getIndex() {
        return index;
    }


    public Byte getCaracter() {
        return caracter;
    }

    public Integer getTemporalIndex() {
        return temporalIndex;
    }

    @Override
    public String toString() {
        return "Valor: " + this.caracter + " Index: " + this.temporalIndex + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Caja){
            Caja c = (Caja) obj;
            return this.caracter == c.caracter && this.index == c.index;
        }
        return false;
    }
}