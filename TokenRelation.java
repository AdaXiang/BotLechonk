package BotLechonk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TokenRelation {
    
    private Map<TipoSinonimo, List<String>> relaciones = new TreeMap<>();

    public void addRelacion(TipoSinonimo tipo, String sinonimo) {
        relaciones.computeIfAbsent(tipo, k -> new ArrayList<>()).add(sinonimo);
    }

    public Map<TipoSinonimo, List<String>> getRelaciones() {
        return relaciones;
    }

    public List<String> getTipoSinonimo(TipoSinonimo tipo) {
        return relaciones.getOrDefault(tipo, new ArrayList<>());
    }

}
