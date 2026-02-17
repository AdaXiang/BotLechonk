# BotLechonk


# 👨‍💻 Autores

**Nombre:** Manuel Solís Gómez  
**Nombre:** Ada Xiang Ramos Grano de Oro  
**Asignatura:** RIBW 2025/2026  
**Práctica:** Tarea 1 – BotLechonk  
**Fecha limite:** 18/02/2026  

------------------------------------------------------------------------

# TAREA 1 - MIÉRCOLES 18/02/2026

## 📌 Descripción

Desarrollo del bot **BotLechonk** para el scrapping de directorios,
conteo de palabras y persistencia mediante serialización en `fI.dir`.

El proyecto implementa:

- Recorrido iterativo (BFS) y recursivo (DFS)
- Filtrado por extensión
- Tokenización y conteo de palabras
- Uso de `TreeMap` para orden automático
- Serialización y deserialización de objetos

------------------------------------------------------------------------

## 🏗 Implementación Real: `BotLechonk.java`

### 📁 Estructuras utilizadas

``` java
private Queue<String> queueFicheros = new LinkedList<>();
private Map<String, Integer> map = new TreeMap<>();
private List<String> extensiones = new ArrayList<>(Arrays.asList("txt", "java", "c", "cpp"));
private int modo = 0; // 0 iterativo | 1 recursivo
private static final String FICHERO_SALIDA = "fI.dir";
```

### ✔ Explicación

-   `Queue<String>` → Guarda rutas absolutas de archivos válidos.
-   `TreeMap<String,Integer>` → Guarda tokens y frecuencia (ordenado por
    clave).
-   `extensiones` → Solo procesa: `txt`, `java`, `c`, `cpp`.
-   `modo`:
    -   `0` → Iterativo
    -   `1` → Recursivo

------------------------------------------------------------------------

# 🔁 Recorrido de Directorios

## 🟢 Modo Iterativo (BFS con Cola)

``` java
public void listIt(File directorioRaiz) throws Exception {
    Queue<File> queue = new LinkedList<File>();
    queue.add(directorioRaiz);

    while (!queue.isEmpty()) {
        File actual = queue.poll();

        if (actual.isDirectory()) {
            File[] lista = actual.listFiles();
            if (lista != null) {
                for (File f : lista) {
                    queue.add(f);
                }
            }
        } else {
            if (this.esExtensionValida(actual.getName())) {
                this.queueFicheros.add(actual.getAbsolutePath());
            }
        }
    }
}
```

------------------------------------------------------------------------

## 🔵 Modo Recursivo (DFS)

``` java
public void listItRecursivo(File directorioRaiz) throws Exception {
    if (directorioRaiz.isDirectory()) {
        File[] lista = directorioRaiz.listFiles();
        if (lista != null) {
            for (File f : lista) {
                this.listItRecursivo(f);
            }
        }
    } else {
        if (this.esExtensionValida(directorioRaiz.getName())) {
            this.queueFicheros.add(directorioRaiz.getAbsolutePath());
        }
    }
}
```

------------------------------------------------------------------------

# 🔤 Conteo de Palabras

``` java
public void contPalabras (File fichEntrada) throws IOException {
    BufferedReader br = new BufferedReader (new FileReader (fichEntrada));
    String linea;

    while ((linea = br.readLine()) != null) {
        linea = linea.toLowerCase();

        StringTokenizer st = new StringTokenizer(
            linea,
            " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~"
        );

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            Object o = map.get(s);
            if (o == null) map.put(s, 1);
            else {
                Integer cont = (Integer) o;
                map.put(s, cont.intValue() + 1);
            }
        }
    }
    br.close();
}
```

------------------------------------------------------------------------

# 💾 Serialización

## Guardar objeto

``` java
public void salvarObjeto (String nombreFichero) {
    Map<String, Integer> h = new TreeMap<>();
    h.putAll(map);
    try {
        FileOutputStream fos = new FileOutputStream(nombreFichero);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(h);
        oos.close();
    }
    catch (Exception e) { System.out.println(e); }
}
```

------------------------------------------------------------------------

## Cargar objeto

``` java
@SuppressWarnings("unchecked")
public void cargarObjeto (String nombreFichero) {
    try {
        FileInputStream fis = new FileInputStream(nombreFichero);
        ObjectInputStream ois = new ObjectInputStream(fis);
        map = (Map<String, Integer>) ois.readObject();
    }
    catch (Exception e) { System.out.println(e); }
}
```

------------------------------------------------------------------------

# ⚙ Método Scrapping

``` java
public void scrapping (String rutaDirectorio) throws Exception {
    File fichero = new File(rutaDirectorio);

    if (modo == 0) {
        this.listIt(fichero);
    } else {
        this.listItRecursivo(fichero);
    }

    for (String rutaFichero : this.queueFicheros) {
        this.contPalabras(new File(rutaFichero));
    }

    this.salvarObjeto(FICHERO_SALIDA);
}
```

------------------------------------------------------------------------

# 🚀 Método `main`

``` java
public static void main (String [] args) throws Exception {
    if(args.length < 1 || args.length > 2){
        System.out.println("ERROR. Ejecutar: >java BotLechonk nombre_directorio [modo]");
        return;
    } 

    BotLechonk bot = new BotLechonk();
    File ficheroSalida = new File(FICHERO_SALIDA);

    if(args.length == 2 && 
       (Integer.parseInt(args[1]) == 0 || Integer.parseInt(args[1]) == 1))
        bot.setMode(Integer.parseInt(args[1]));
    else
        System.out.println("WARNING. Modo tiene que ser 0 o 1. Por defecto será iterativo");

    if (ficheroSalida.exists()) {
        bot.cargarObjeto(FICHERO_SALIDA);
    }
    else {
        bot.scrapping(args[0]);
    }

    bot.showMap();
}
```

------------------------------------------------------------------------

# ✅ Resumen Técnico

-   ✔ BFS (cola) o DFS (recursión)
-   ✔ Filtrado por extensión configurable
-   ✔ Tokenización con `StringTokenizer`
-   ✔ Uso de `TreeMap`
-   ✔ Serialización / Deserialización
-   ✔ Persistencia en `fI.dir`
-   ✔ Evita reprocesar si ya existe el fichero

------------------------------------------------------------------------

# ▶️ Cómo Ejecutarlo

## 📦 1. Compilar

Situarse en el directorio donde está el paquete `BotLechonk` y ejecutar:

``` bash
javac BotLechonk/BotLechonk.java
```

------------------------------------------------------------------------

## 🚀 2. Ejecutar

``` bash
java BotLechonk.BotLechonk ruta_directorio [modo]
```

### 📌 Parámetros

-   `ruta_directorio` → Ruta del directorio que se desea analizar.
-   `modo` (opcional):
    -   `0` → Modo iterativo (BFS con cola) **(por defecto)**
    -   `1` → Modo recursivo (DFS)

------------------------------------------------------------------------

## 🧪 Ejemplos

### Modo iterativo (por defecto)

``` bash
java BotLechonk.BotLechonk DIR1
```

### Modo recursivo

``` bash
java BotLechonk.BotLechonk DIR1 1
```

### 📁 Estructura del Directorio ejemplo

    DIR1
    │
    ├── DIR2
    │     ├── fich1.txt  ("otra cosa")
    │     └── fich2.txt  ("otra cosa")
    │
    ├── DIR3
    │     └── fich1.txt  ("otra cosa")
    │
    ├── fich1.txt  ("mi mamá es la 
    │               mejor. La mía.
    │               Mi mamá")
    │
    ├── fich2.txt  ("otra cosa")
    └── fich3.txt  (copia fich1.txt)

------------------------------------------------------------------------

## 🧠 Funcionamiento Interno

1.  Buscar si existe el fichero `fI.dir`
    -   Si **existe** → cargar el objeto serializado.
    -   Si **NO existe**:
        -   Ejecutar el scraping (recorrer directorios).
        -   Construir la estructura de datos.
        -   Guardar el objeto en `fI.dir`.
2.  Imprimir el resultado en **InOrder**..

------------------------------------------------------------------------

## ⚠️ Notas Importantes

-   El modo solo puede ser `0` o `1`.
-   Si se introduce un valor incorrecto, se usará el modo iterativo.
-   Solo se procesan archivos con extensión:
    -   `txt`
    -   `java`
    -   `c`
    -   `cpp`

------------------------------------------------------------------------