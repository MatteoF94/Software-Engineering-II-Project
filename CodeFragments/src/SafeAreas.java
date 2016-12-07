import java.util.*;

/**
 * Created by Matteo on 06/12/16.
 */

/**
 * This class is an extremely semplified version of the safe areas software structure,
 * to help us now in the testing of algorithms and implementations
 */
public class SafeAreas {
    private Map<List<FixedPosition>, Boolean> safeAreas;

    public SafeAreas() {
        this.safeAreas = new HashMap<>();
    }

    public void addSafeArea(List<FixedPosition> positions, boolean isSpecial) {
        safeAreas.put(positions, isSpecial);
    }

    public Set<List<FixedPosition>> getSafeAreas() {
        return safeAreas.keySet();
    }
}
