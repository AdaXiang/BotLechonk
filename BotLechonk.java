package BotLechonk;
import java.io.*;
import java.util.*;


// Scrapping de ficheros, contabilización de palabras, serialización y deserialización de objetos

public class BotLechonk {
    private Queue <String> queueFicheros = new LinkedList <String> (); // Cola de rutas de ficheros a procesar
    private Map <String, Integer> map = new TreeMap <String, Integer> (); // TreeMap ordena por clave, HashMap no lo hace
    private List <String> extensiones = new ArrayList <String> (Arrays.asList("txt", "java", "c", "cpp")); // Extensiones de ficheros a procesar
    private int modo = 0; // 0: iterativo, 1: recursivo
    // Constante
    private static final String FICHERO_SALIDA = "fI.dir";

    public void setMode (int modo){
        this.modo = modo;
    }

    public void listIt(File directorioRaiz) throws Exception {
        Queue<File> queue = new LinkedList<File>();
        queue.add(directorioRaiz);

        while (!queue.isEmpty()) {
            File actual = queue.poll(); // Saca y elimina el primer elemento

            if (actual.isDirectory()) {
                File[] lista = actual.listFiles(); // Mejor que .list() porque da objetos File
                if (lista != null) {
                    for (File f : lista) {
                        queue.add(f); // Añadimos carpetas y archivos a la cola
                    }
                }
            } else {
                // Si es un archivo, comprobamos extensión y guardamos la RUTA ABSOLUTA
                if (this.esExtensionValida(actual.getName())) {
                    this.queueFicheros.add(actual.getAbsolutePath());
                }
            }
        }
    }

     public void listItRecursivo(File directorioRaiz) throws Exception {
        if (directorioRaiz.isDirectory()) {
            File[] lista = directorioRaiz.listFiles(); // Mejor que .list() porque da objetos File
            if (lista != null) {
                for (File f : lista) {
                    this.listItRecursivo(f); // Llamada recursiva para procesar subdirectorios
                }
            }
        } else {
            // Si es un archivo, comprobamos extensión y guardamos la RUTA ABSOLUTA
            if (this.esExtensionValida(directorioRaiz.getName())) {
                this.queueFicheros.add(directorioRaiz.getAbsolutePath());
            }
        }
    }

    public void contPalabras (File fichEntrada) throws IOException {
        BufferedReader br = new BufferedReader (new FileReader (fichEntrada));
        String linea;

        while ( (linea = br.readLine () ) != null) {
            // Normalizar a minúscula
            linea = linea.toLowerCase();
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

        // Se ha quitado porque al ser un TreeMap, ya ordena por clave automáticamente
        //List <String> claves = new ArrayList <String> (map.keySet ());
        //Collections.sort (claves);
    }

    public void salvarObjeto (String nombreFichero) {
        System.out.println("Salvando objeto en " + nombreFichero);
        Map<String, Integer> h = new TreeMap <String, Integer> ();
        h.putAll(map);
        /* 
         * en el caso de nuestro PC-Crawler ha de utilizarse la estructura Heap
         * Map <String, Integer> map
         */
        try {
            FileOutputStream fos = new FileOutputStream(nombreFichero);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(h);
            oos.close();
        }
        catch (Exception e) { System.out.println(e); }
    }

    @SuppressWarnings("unchecked")
    public void cargarObjeto (String nombreFichero) {
        System.out.println("Cargando objeto desde " + nombreFichero);
        try {
            FileInputStream fis = new FileInputStream(nombreFichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Se guarda el objeto deserializado en la estructura de datos correspondiente, para posteriormente procesarlo
            map = (Map<String, Integer>) ois.readObject();
            /* en el caso de nuestro PC-Crawler ha de utilizarse la estructura Heap:
             * Map <String, Integer> map = TreeMap <String, Integer> ois.readObject();
             */
        }
        catch (Exception e) { System.out.println(e); }
    }

    public boolean esExtensionValida (String nombreFichero) {
        String extension = nombreFichero.substring(nombreFichero.lastIndexOf('.') + 1);
        return this.extensiones.contains(extension);
    }

    public void showMap () {
        for (String clave : map.keySet()) {
            System.out.println(clave + ": " + map.get(clave));
        }
    }

    public void scrapping (String rutaDirectorio) throws Exception {
        File fichero = new File(rutaDirectorio);
        if (modo == 0) {
            System.out.println("Modo iterativo");
            this.listIt(fichero);
        }
        else {
            System.out.println("Modo recursivo");
            this.listItRecursivo(fichero);
        }
           
        for (String rutaFichero : this.queueFicheros) {
            this.contPalabras(new File(rutaFichero));
        }
        this.salvarObjeto(FICHERO_SALIDA);
    }
    public static void main (String [] args) throws Exception {
        if(args.length < 1 || args.length > 2){
            System.out.println("ERROR. Ejecutar: >java BotLechonk nombre_directorio [modo]");
            return;
        } 
        BotLechonk bot = new BotLechonk();
        File ficheroSalida = new File(FICHERO_SALIDA);
        if(args.length == 2 && (Integer.parseInt(args[1]) == 0 || Integer.parseInt(args[1]) == 1))
            bot.setMode(Integer.parseInt(args[1]));
        else
            System.out.println("WARNING. Modo tiene que ser 0 o 1. Por defecto será iterativo"); // Por defecto es 0
            
        // Si el fichero de salida ya existe, se carga el objeto serializado previamente, en lugar de volver a procesar los ficheros del directorio
        if (ficheroSalida.exists()) {
            bot.cargarObjeto(FICHERO_SALIDA);
        }
        else { //scrapping
            bot.scrapping(args[0]);
        }
    
       bot.showMap();
    }

}