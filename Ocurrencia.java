package BotLechonk;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Ocurrencia implements Serializable, Comparable <Ocurrencia>{
    
    private Integer FTG;
	private Map <Integer, Integer> diccionarioParcial = new TreeMap <Integer, Integer> ();
    private FATManager fatManager = FATManager.getInstance();

    // Constructor
    public Ocurrencia (Integer FTG, Map <Integer, Integer> diccionarioParcial) {
        this.FTG = FTG;
        this.diccionarioParcial = diccionarioParcial;
    }

    // Getters y setters
    public Integer getFTG() {
        return FTG;
    }

    public void setFTG(Integer FTG) {
        this.FTG = FTG;
    }

    public Map<Integer, Integer> getDiccionarioParcial() {
        return diccionarioParcial;
    }

    public void setDiccionarioParcial(Map<Integer, Integer> diccionarioParcial) {
        this.diccionarioParcial = diccionarioParcial;
    }

    // Utils
    public void insertarOcurrencia (Integer fichero) {
        this.FTG += 1; // Incrementamos el FTG
        this.diccionarioParcial.put(fichero, this.diccionarioParcial.getOrDefault(fichero, 0) + 1);
    }

    public void eliminarOcurrencia (Integer fichero) {
        this.FTG -= this.diccionarioParcial.getOrDefault(fichero, 0); // Decrementamos el FTG en la cantidad de ocurrencias del fichero
        this.diccionarioParcial.remove(fichero);
    }

    public Integer consultarOcurrenciaFichero (Integer fichero) {
        return this.diccionarioParcial.getOrDefault(fichero, 0);
    }

    public void showDiccionarioParcial () {
        for (Integer clave : diccionarioParcial.keySet()) {
            System.out.println("\t"+fatManager.getPathById(clave) + ": " + diccionarioParcial.get(clave));
        }
    }

    public void showDiccionarioModo (Integer modo){
        System.out.println(Colores.AMARILLO + "Modo " + modo + Colores.RESET);
        System.out.println("\tFTG: " + this.FTG);
        switch (modo) {
            case 1: // De mayor a menor FTG, y en caso de empate, orden alfabético
                this.diccionarioParcial.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .forEach(e ->
                    System.out.println(Colores.MAGENTA+"\t"+fatManager.getPathById(e.getKey()) + " -> " + e.getValue()+Colores.RESET)
                );

                break;

            case 2: // De menor a mayor FTG, y en caso de empate, orden alfabético
                this.diccionarioParcial.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .forEach(e ->
                    System.out.println(Colores.MAGENTA+"\t"+fatManager.getPathById(e.getKey()) + " -> " + e.getValue()+Colores.RESET)
                );
                break;

            case 3: // De más cerca a más cerca al directorio local, y en caso de empate, orden alfabético
                this.diccionarioParcial.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(
                (Map.Entry<Integer,Integer> e) ->
                    (int) fatManager.getPathById(e.getKey()).chars().filter(c -> c == '\\').count()
                 )
                .thenComparing(Map.Entry.comparingByKey())
                )
                .forEach(e ->
                    System.out.println(Colores.MAGENTA+"\t"+fatManager.getPathById(e.getKey()) + " -> " + e.getValue()+Colores.RESET)
                );
                break;

           case 4: // De más lejos al directorio local a más cerca al directorio local, y en caso de empate, orden alfabético
                this.diccionarioParcial.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(
                    (Map.Entry<Integer,Integer> e) ->
                        (int) fatManager.getPathById(e.getKey()).chars().filter(c -> c == '\\').count() 
                )
                .reversed()
                .thenComparing(Map.Entry.comparingByKey())
                )
                .forEach(e ->
                    System.out.println(Colores.MAGENTA+"\t"+fatManager.getPathById(e.getKey()) + " -> " + e.getValue()+Colores.RESET)
                );
                break;
        
            default:
                
                break;
        }
    }

    @Override
    public int compareTo(Ocurrencia other) {
        return this.diccionarioParcial.equals(other.diccionarioParcial) ? 0 : 1;
    }
}
