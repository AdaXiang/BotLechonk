package BotLechonk;
import java.util.*;
import java.io.Serializable;

// Singleton para gestionar la FAT (File Allocation Table)
public class FATManager implements Serializable {

    private static FATManager instance;
    
    // clave → identificador
    // valor → path
    private Map<Integer, String> idToPath = new HashMap<>();
    
    // clave → path
    // valor → identificador
    private Map<String, Integer> pathToId = new HashMap<>();

    private Integer contId = 0; 
   
    private FATManager() {}

    public static FATManager getInstance() {
        if (instance == null)
            instance = new FATManager();
        return instance;
    }

    public String getPathById(Integer id) {
        return idToPath.get(id);
    }

    public Integer getIdByPath(String path) {
        if (pathToId.containsKey(path)) {
            return pathToId.get(path);
        } else { 
            Integer newId = contId++;
            pathToId.put(path, newId);
            idToPath.put(newId, path);
            return newId;
        }
    }
}