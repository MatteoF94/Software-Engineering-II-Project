import java.util.List;
import java.util.Set;

/**
 * Created by Matteo on 06/12/16.
 */

/**
 * The first declaration and the static methods are quite dangerous, due to the fact that model may be yet to
 * instantiate, so that the methods works on an object...that doesn't still exist.
 * In this version of the implementation, it is trusted that the methods invocation follows the creation of the
 * ServiceCore object, and so the model is not a void reference anymore.
 */
public class ServiceCore {
    private static Model model;

    public ServiceCore(Model model) {
        ServiceCore.model = model;
    }

    public static boolean isInSafeArea(Position carPosition) {
        Set<List<FixedPosition>> safeAreas = model.getSafeAreas();
        for(List<FixedPosition> safeArea : safeAreas) {
            for(FixedPosition position : safeArea) {
                if(carPosition.equals(position)) return true;
            }
        }
        return false;
    }

    public static boolean isMoreThan3kmDistant(Position position) {
        Set<List<FixedPosition>> safeAreas = model.getSafeAreas();
        for(List<FixedPosition> safeArea : safeAreas) {
            for(FixedPosition pos : safeArea) {
                double gradX = Math.pow((pos.getX() - position.getX()),2);
                double gradY = Math.pow((pos.getY() - position.getY()),2);
                double gradZ = Math.pow((pos.getZ() - position.getZ()),2);
                if (gradX + gradY + gradZ <= 9) return false;
            }
        }
        return true;
    }
}
