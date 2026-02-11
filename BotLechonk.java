package BotLechonk;
import java.io.*;
import java.util.*;

// Scrapping de ficheros, contabilización de palabras, serialización y deserialización de objetos

public class BotLechonk {
    private Queue <String> q = new LinkedList <String> ();
    private Map <String, Integer> map = new TreeMap <String, Integer> (); // TreeMap ordena por clave, HashMap no lo hace
    private List <String> extensiones = new ArrayList <String> (Arrays.asList("txt", "java", "c", "cpp")); // Extensiones de ficheros a procesar

    private final String ficheroSalida = new String ("fI.dir");

    public static void main (String [] args) throws Exception {
        
       
    }

    public void listIt (File fichero) throws Exception {
        if (!fichero.exists() || !fichero.canRead()) {
            System.out.println("ERROR. No puedo leer " + fichero);
            return;
        }
        if (fichero.isDirectory()) {
            String [] listaFicheros = fichero.list();
            for (int i=0; i<listaFicheros.length; i++)
                // Añadimos a la cola de rutas los ficheros contenidos en el directorio, para posteriormente procesarlos
                this.q.add(fichero.getPath() + "/" + listaFicheros[i]); 
        }
        else try {
            if(this.esExtensionValida(fichero.getName())){ 
                FileReader fr = new FileReader(fichero);
                BufferedReader br = new BufferedReader(fr);
                String linea;
                while ((linea=br.readLine()) != null)
                    System.out.println(linea);
                br.close();
            }
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("ERROR. Fichero desaparecido en combate  ;-)");
        }
    }

    public void contPalabras (File fichEntrada) throws IOException {
        BufferedReader br = new BufferedReader (new FileReader (fichEntrada));
        String linea;

        while ( (linea = br.readLine () ) != null) {
            // Normalizar a minúscula
            linea.toLowerCase();
            // Quitar stopwords, como "el", "la", "de", "y", etc., que no aportan nada al análisis de texto
            StringTokenizer st = new StringTokenizer (linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens () ) {
                String s = st.nextToken();
                Object o = map.get(s);
                if (o == null) map.put (s, 1);
                else {
                    Integer cont = (Integer) o;
                    map.put (s, cont.intValue () + 1);
                }
            }
        }
        br.close ();

        List <String> claves = new ArrayList <String> (map.keySet ());
        Collections.sort (claves);
    }

    public void salvarObjeto (){

    }

    public void cargarObjeto (){

    }

    public boolean esExtensionValida (String nombreFichero) {
        String extension = nombreFichero.substring(nombreFichero.lastIndexOf('.') + 1);
        return this.extensiones.contains(extension);
    }

}