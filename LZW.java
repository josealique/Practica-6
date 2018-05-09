import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LZW {

    public static void main(String[] args) throws Exception {
        InputStream is = new ByteArrayInputStream("aa".getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LZW.compress(is, baos);
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        ArrayList<Byte> lista = insertarBytes(is.readAllBytes());
        Map<String, Integer> map = new HashMap<>();
        String string = "";
        String lapalabra = "";
        int index = 0;
        for (int i = 0; i < lista.size(); i++) {
            char c = (char) (lista.get(i) & 0xFF);
            string = lapalabra + c;
            lapalabra = lapalabra + c;
            if (!map.containsKey(string)){
                map.put(string, index);
                os.write(index);
                os.write(string.charAt(string.length()-1));
                index = map.containsKey(lapalabra) ? index : ++index;
                lapalabra = "";
            } else if (map.containsKey(string)){
                map.put(string, index);
                index++;
            }
        } if (map.containsKey(string)){

        }
        System.out.println(map);
    }

    private static ArrayList<Byte> insertarBytes(byte[] is) {
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < is.length; i++) {
            bytes.add(is[i]);
        }
        return bytes;
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {

    }
}
