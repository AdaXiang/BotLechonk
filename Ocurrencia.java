package BotLechonk;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Ocurrencia implements Serializable, Comparable <Ocurrencia>{
    
    private Integer FTG;
	private Map <String, Integer> diccionarioParcial = new TreeMap <String, Integer> ();

    // Constructor
    public Ocurrencia (Integer FTG, Map <String, Integer> diccionarioParcial) {
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

    public Map<String, Integer> getDiccionarioParcial() {
        return diccionarioParcial;
    }

    public void setDiccionarioParcial(Map<String, Integer> diccionarioParcial) {
        this.diccionarioParcial = diccionarioParcial;
    }

    // Utils
    public void insertarOcurrencia (String fichero) {
        this.FTG += 1; // Incrementamos el FTG
        this.diccionarioParcial.put(fichero, this.diccionarioParcial.getOrDefault(fichero, 0) + 1);
    }

    public void eliminarOcurrencia (String fichero) {
        this.FTG -= this.diccionarioParcial.getOrDefault(fichero, 0); // Decrementamos el FTG en la cantidad de ocurrencias del fichero
        this.diccionarioParcial.remove(fichero);
    }

    public Integer consultarOcurrenciaFichero (String fichero) {
        return this.diccionarioParcial.getOrDefault(fichero, 0);
    }

    public void showDiccionarioParcial () {
        for (String clave : diccionarioParcial.keySet()) {
            System.out.println("\t"+clave + ": " + diccionarioParcial.get(clave));
        }
    }

    @Override
    public int compareTo(Ocurrencia other) {
        return this.diccionarioParcial.equals(other.diccionarioParcial) ? 0 : 1;
    }
}
