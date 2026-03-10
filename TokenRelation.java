package BotLechonk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TokenRelation implements java.io.Serializable {
    
    private Map<TipoSinonimo, List<String>> relaciones = new TreeMap<>();

    public void addRelacion(TipoSinonimo tipo, String sinonimo) {
        relaciones.computeIfAbsent(tipo, k -> new ArrayList<>());
        if (!relaciones.get(tipo).contains(sinonimo)) {
            relaciones.get(tipo).add(sinonimo);
        }
    }

    public List<String> getNombresRelaciones() {
        return relaciones.values().stream().flatMap(List::stream).toList();
    }

    public void addRelaciones(Map<TipoSinonimo, List<String>> nuevasRelaciones, String palabraVetada) {
        for (Map.Entry<TipoSinonimo, List<String>> entry : nuevasRelaciones.entrySet()) {
            TipoSinonimo tipo = entry.getKey();
            List<String> sinonimos = entry.getValue();
            for (String sinonimo : sinonimos) {
                if (!sinonimo.equals(palabraVetada)) {
                    addRelacion(tipo, sinonimo);
                }
            }
        }
    }

    public void addRelaciones(List<String> sinonimos, String palabraVetada) {
        for (String sinonimo : sinonimos) {
            String[] partes = sinonimo.split(" ");
            if (sinonimo.equals(palabraVetada)) {
                continue; // No mostrar la palabra consultada como sinonimo de sí misma
            }
            if (partes.length == 1) {
                addRelacion(TipoSinonimo.RAE, sinonimo);
                continue;
            }
            switch (partes[1]) {
                case "(fig.)":
                    addRelacion(TipoSinonimo.FIG, partes[0]);
                    break;

                case "(vulg.)":
                    addRelacion(TipoSinonimo.VULG, partes[0]);
                    break;

                case "(loc.)":
                    addRelacion(TipoSinonimo.LOC, partes[0]);
                    break;

                case "(p. us.)":
                    addRelacion(TipoSinonimo.P_US, partes[0]);
                    break;

                case "(NoRAE)":
                    addRelacion(TipoSinonimo.NO_RAE, partes[0]);
                    break;

                default:
                    addRelacion(TipoSinonimo.RAE, partes[0]);
                    break;
            }
        }
    }

    public Map<TipoSinonimo, List<String>> getRelaciones() {
        return relaciones;
    }

    public List<String> getTipoSinonimo(TipoSinonimo tipo) {
        return relaciones.getOrDefault(tipo, new ArrayList<>());
    }

    public void showSinonimos() {
        for (Map.Entry<TipoSinonimo, List<String>> entry : relaciones.entrySet()) {
            TipoSinonimo tipo = entry.getKey();
            List<String> sinonimos = entry.getValue();
            System.out.println(tipo + "\n\t: " + sinonimos);
        }
    }

}
