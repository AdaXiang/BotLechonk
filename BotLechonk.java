package BotLechonk;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.text.Normalizer;

// Scrapping de ficheros, contabilización de palabras, serialización y deserialización de objetos
public class BotLechonk {
    // Cola de rutas de ficheros a procesar
    private Queue <String> frontier = new LinkedList <String> (); 
    // TreeMap ordena por clave, HashMap no lo hace
    private Map <String, Ocurrencia> diccionario = new TreeMap <String, Ocurrencia> (); 
    // Extensiones de ficheros a procesar
    private List <String> extensiones = new ArrayList <String> (Arrays.asList("txt", "java", "c", "cpp")); 
    // Thesaurus
    private Map<String, TokenRelation> thesauro = new TreeMap<String, TokenRelation>();
    
    // 0: iterativo, 1: recursivo
    private int modo = 0; 

    // Instancia del singleton para gestionar la FAT (File Allocation Table)
    private FATManager fatManager = FATManager.getInstance(); 

    // Constantes
    private static final String FICHERO_SALIDA = "diccionario.dir";
    private static final String FICHERO_THESAURO = "thesauro.rex";
    private static final String THESAURO_TXT = "Thesaurus_es_ES.txt";

    public void setMode (int modo){
        this.modo = modo;
    }

    public void listIt(File directorioRaiz) throws Exception {
        Queue<File> queue = new LinkedList<File>();
        queue.add(directorioRaiz);

        while (!queue.isEmpty()) {
            File actual = queue.poll(); // Saca y elimina el primer elemento

            if (actual.isDirectory()) {
                File[] lista = actual.listFiles(); 
                if (lista != null) {
                    for (File f : lista) {
                        queue.add(f); // Añadimos carpetas y archivos a la cola
                    }
                }
            } else {
                // Si es un archivo, comprobamos extensión y guardamos la RUTA ABSOLUTA
                if (this.esExtensionValida(actual.getName())) {
                    this.frontier.add(actual.getAbsolutePath());
                }
            }
        }
    }

     public void listRec(File directorioRaiz) throws Exception {
        if (directorioRaiz.isDirectory()) {
            File[] lista = directorioRaiz.listFiles(); 
            if (lista != null) {
                for (File f : lista) {
                    // Llamada recursiva para procesar subdirectorios
                    this.listRec(f); 
                }
            }
        } else {
            // Si es un archivo, comprobamos extensión y guardamos la RUTA ABSOLUTA
            if (this.esExtensionValida(directorioRaiz.getName())) {
                this.frontier.add(directorioRaiz.getAbsolutePath());
            }
        }
    }

    public void contPalabras (File fichEntrada) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichEntrada), "UTF-8"));
        String linea;

        while ( (linea = br.readLine () ) != null) {
            // Normalizar a minúscula
            linea = linea.toLowerCase();
            // Eliminar acentos y caracteres especiales
            linea = Normalizer.normalize(linea, Normalizer.Form.NFD); 
            // Eliminar caracteres no ASCII
            linea = linea.replaceAll("[^\\p{ASCII}]", ""); 

            // Quitar stopwords, como "el", "la", "de", "y", etc., que no aportan nada al análisis de texto
            StringTokenizer st = new StringTokenizer (linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~¡¿");

            while (st.hasMoreTokens () ) {
                String s = st.nextToken();
                
                if(!thesauro.containsKey(s)) {
                    continue; // Si la palabra no está en el thesauro, se ignora
                }

                // Se consulta el diccionario para ver si la palabra ya existe
                Object o = diccionario.get(s);

                // Si la palabra no existe en el diccionario:
                if (o == null){
                    // Se crea una ocurrencia con FTG=0 y se añade el fichero al diccionario parcial
                    Ocurrencia oc = new Ocurrencia(0, new TreeMap<Integer, Integer>());
                    // Se inserta la ocurrencia del fichero en el diccionario parcial y se incrementa el FTG
                    oc.insertarOcurrencia(fatManager.getIdByPath(fichEntrada.getAbsolutePath()));
                    diccionario.put (s, oc);
                }
                // Si la palabra ya existe en el diccionario, se actualiza la ocurrencia del fichero 
                // en el diccionario parcial y se incrementa el FTG
                else {
                    Ocurrencia oc = (Ocurrencia) o;
                    oc.insertarOcurrencia(fatManager.getIdByPath(fichEntrada.getAbsolutePath()));
                }
            }
        }
        br.close ();
    }

    public void salvarObjeto (String nombreFichero) {
        System.out.println(Colores.VERDE + "Salvando objeto en " + nombreFichero + Colores.RESET);
        Map<String, Ocurrencia> h = new TreeMap <String, Ocurrencia> ();
        h.putAll(diccionario);
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
        System.out.println(Colores.VERDE + "Cargando objeto desde " + nombreFichero + Colores.RESET);
        try {
            FileInputStream fis = new FileInputStream(nombreFichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Se guarda el objeto deserializado en la estructura de datos correspondiente, para posteriormente procesarlo
            diccionario = (Map<String, Ocurrencia>) ois.readObject();
            ois.close();
        }
        catch (Exception e) { System.out.println(e); }
    }

    public void salvarThesauro (String nombreFichero) {
        System.out.println(Colores.VERDE + "Salvando objeto en " + nombreFichero + Colores.RESET);
        Map<String, TokenRelation> h = new TreeMap <String, TokenRelation> ();
        h.putAll(thesauro);
        try {
            FileOutputStream fos = new FileOutputStream(nombreFichero);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(h);
            oos.close();
        }
        catch (Exception e) { System.out.println(e); }
    }

    @SuppressWarnings("unchecked")
    public void cargarThesauro (String nombreFichero) {
        System.out.println(Colores.VERDE + "Cargando objeto desde " + nombreFichero + Colores.RESET);
        try {
            FileInputStream fis = new FileInputStream(nombreFichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Se guarda el objeto deserializado en la estructura de datos correspondiente, para posteriormente procesarlo
            thesauro = (Map<String, TokenRelation>) ois.readObject();
            ois.close();
        }
        catch (Exception e) { System.out.println(e); }
    }

    public boolean esExtensionValida (String nombreFichero) {
        String extension = nombreFichero.substring(nombreFichero.lastIndexOf('.') + 1);
        return this.extensiones.contains(extension);
    }

    public void showDiccionario () {
        for (String clave : diccionario.keySet()) {
            System.out.println(Colores.AZUL + clave + ": FTG=" + diccionario.get(clave).getFTG() + Colores.RESET);
            diccionario.get(clave).showDiccionarioParcial();
        }
    }

    public void scrapping (String rutaDirectorio) throws Exception {
        File fichero = new File(rutaDirectorio);
        if (modo == 0) {
            System.out.println(Colores.AMARILLO + "Modo iterativo" + Colores.RESET);
            this.listIt(fichero);
        }
        else {
            System.out.println(Colores.AMARILLO + "Modo recursivo" + Colores.RESET);
            this.listRec(fichero);
        }
           
        for (String rutaFichero : this.frontier) {
            this.contPalabras(new File(rutaFichero));
        }
        this.salvarObjeto(FICHERO_SALIDA);
    }

    public void showResultados(String palabra, int modo){
        Object object = this.diccionario.get(palabra);

        if(object == null){
            System.out.println("No se encontro la palabra. Pruebe con otra.");
        }
        else {
            ((Ocurrencia) object).showDiccionarioModo(modo);
            if (thesauro.containsKey(palabra)) {
                    System.out.println(Colores.VERDE + "Resultados sinonimos de " + palabra + ": " + Colores.RESET);
                    TokenRelation tr = thesauro.get(palabra);
                    for (Map.Entry<TipoSinonimo, List<String>> entry : tr.getRelaciones().entrySet()) {
                        TipoSinonimo tipo = entry.getKey();
                        List<String> sinonimos = entry.getValue();
                       
                        for (String sinonimo : sinonimos) {
                            Object objectSinonimo = this.diccionario.get(sinonimo);
                            // if (palabra.equals(sinonimo)) {
                            //     continue; // No mostrar la palabra consultada como sinonimo de sí misma
                            // }
                            if(objectSinonimo != null){
                                System.out.println(Colores.VERDE + tipo + ": " + sinonimo + Colores.RESET);
                                ((Ocurrencia) objectSinonimo).showDiccionarioModo(modo);
                            }
                        }
                    }
            }
            else {
                System.out.println(Colores.AMARILLO + "No se encontraron sinonimos para la palabra " + palabra + Colores.RESET);
            }
        }
    }

    public void menuConsulta(){
        Scanner scanner = new Scanner(System.in);
        Boolean exit = false;
        showDiccionario();
        System.out.println("Elige como prefieres que muestre la informacion:");
        System.out.println("[1] De mayor a menor FTP");
        System.out.println("[2] De menor a mayor FTP");
        System.out.println("[3] De mas cerca al directorio local");
        System.out.println("[4] De mas lejos al directorio local");

        Integer modo = scanner.nextInt();
        scanner.nextLine(); 

        if (modo < 1 || modo > 4) {
            System.out.println(Colores.AMARILLO + "Modo no valido. Por defecto se mostrara de mayor a menor FTG" + Colores.RESET);
            modo = 1;
        }

        while (!exit){
            System.out.println(Colores.AZUL + "Palabra a consultar : " + Colores.RESET);
            String palabra = scanner.nextLine();
        
            if(palabra.equalsIgnoreCase("q")){
                System.out.println(Colores.AMARILLO + "Saliendo del programa" + Colores.RESET);
                exit = true;
                continue;
            }
            showResultados(palabra, modo);
        }
        scanner.close();
    }

    public void thesauroToMap () {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(THESAURO_TXT), "UTF-8"));
        String linea;

        while ( (linea = br.readLine () ) != null) {
            if (linea.substring(0).equals("#")) {
                continue; // Ignorar líneas que comienzan con "#"
            }

            // Normalizar a minúscula
            linea = linea.toLowerCase();
            // Eliminar acentos y caracteres especiales
            linea = Normalizer.normalize(linea, Normalizer.Form.NFD); 
            // Eliminar caracteres no ASCII
            linea = linea.replaceAll("[^\\p{ASCII}]", ""); 

            List<String> partes = new ArrayList<>(
            Arrays.stream(linea.split("[;,]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList())
            );
            partes.removeIf(parte -> parte.contains(" ") && !parte.contains(" ("));
            for (String parte : partes) {
                if(thesauro.containsKey(parte)){
                    TokenRelation tr = thesauro.get(parte);
                    tr.addRelaciones(partes, parte);

                    // for (String sinonimo : tr.getNombresRelaciones()) {
                    //     if (thesauro.containsKey(sinonimo)) {
                    //         TokenRelation trSinonimo = thesauro.get(sinonimo);
                    //         trSinonimo.addRelaciones(tr.getRelaciones(), sinonimo);
                    //     }
                    // }
                }
                else {
                    TokenRelation tr = new TokenRelation();
                    tr.addRelaciones(partes, parte);
                    thesauro.put(parte, tr);
                }
            }
        }
        br.close ();
        }
        catch (Exception e) { System.out.println(e); }
    }

    public void showThesauro () {
        for (String clave : thesauro.keySet()) {
            System.out.println(Colores.AZUL + clave + ": " + Colores.RESET);
            for (String sinonimo : thesauro.get(clave).getNombresRelaciones()) {
                System.out.println("\t" + sinonimo);
            }
        }
    }

    public static void main (String [] args) throws Exception {
        if(args.length < 1 || args.length > 2){
            System.out.println(Colores.ROJO + "ERROR. Ejecutar: >java BotLechonk nombre_directorio [modo]" + Colores.RESET);
            return;
        } 

        BotLechonk bot = new BotLechonk();

        // Vertir el Thesaurus en un TreeMap si no existe
        File ficheroThesauro = new File(FICHERO_THESAURO);
        if (!ficheroThesauro.exists()) {
            bot.thesauroToMap(); //indexar el thesaurus y guardarlo en un TreeMap
            bot.salvarThesauro(FICHERO_THESAURO); //serializar el TreeMap para futuras consultas
            //bot.showThesauro(); //mostrar el thesauro por consola
        }
        else {
            bot.cargarThesauro(FICHERO_THESAURO);
        }

        File ficheroSalida = new File(FICHERO_SALIDA);
        if(args.length == 2 && (Integer.parseInt(args[1]) == 0 || Integer.parseInt(args[1]) == 1))
            bot.setMode(Integer.parseInt(args[1]));
        else
            System.out.println(Colores.AMARILLO + "WARNING. Modo tiene que ser 0 o 1. Por defecto sera iterativo" + Colores.RESET); // Por defecto es 0
            
        // Si el fichero de salida ya existe, se carga el objeto serializado previamente, en lugar de volver a procesar los ficheros del directorio
        if (ficheroSalida.exists()) {
            bot.cargarObjeto(FICHERO_SALIDA);
        }
        else { //scrapping
            bot.scrapping(args[0]);
        }
    
        bot.menuConsulta();
    }

}