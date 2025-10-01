import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manages target destinations using Repository pattern
 * Handles CRUD operations for targets
 */
public class TargetManager {
    private static final Logger LOGGER = Logger.getLogger(TargetManager.class.getName());
    
    private final Map<String, Target> targets;
    
    public TargetManager() {
        this.targets = new HashMap<>();
    }
    
    /**
     * Creates a new target and adds it to the collection
     * @param name unique name for the target
     * @param distanceFromEarth distance in kilometers
     * @return true if created successfully, false if name already exists
     */
    public boolean createTarget(String name, double distanceFromEarth) {
        try {
            if (name == null || name.trim().isEmpty()) {
                LOGGER.log(Level.WARNING, "Attempted to create target with empty name");
                return false;
            }
            
            String normalizedName = name.trim();
            if (targets.containsKey(normalizedName)) {
                LOGGER.log(Level.INFO, "Target with name '{}' already exists", normalizedName);
                return false;
            }
            
            Target target = new Target(normalizedName, distanceFromEarth);
            targets.put(normalizedName, target);
            
            LOGGER.log(Level.INFO, "Created target: {}", target);
            return true;
            
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Failed to create target due to invalid parameters: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error creating target", e);
            return false;
        }
    }
    
    /**
     * Deletes a target from the collection
     * @param name name of the target to delete
     * @return true if deleted successfully, false if not found
     */
    public boolean deleteTarget(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }
            
            String normalizedName = name.trim();
            Target removed = targets.remove(normalizedName);
            
            if (removed != null) {
                LOGGER.log(Level.INFO, "Deleted target: {}", normalizedName);
                return true;
            } else {
                LOGGER.log(Level.INFO, "Target '{}' not found for deletion", normalizedName);
                return false;
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error deleting target", e);
            return false;
        }
    }
    
    /**
     * Retrieves a target by name
     * @param name name of the target
     * @return Target object or null if not found
     */
    public Target getTarget(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return targets.get(name.trim());
    }
    
    /**
     * Gets all targets in the collection
     * @return unmodifiable collection of targets
     */
    public Collection<Target> getAllTargets() {
        return Collections.unmodifiableCollection(targets.values());
    }
    
    /**
     * Gets all target names
     * @return unmodifiable set of target names
     */
    public Set<String> getTargetNames() {
        return Collections.unmodifiableSet(targets.keySet());
    }
    
    /**
     * Checks if a target exists
     * @param name target name
     * @return true if exists, false otherwise
     */
    public boolean targetExists(String name) {
        return name != null && targets.containsKey(name.trim());
    }
    
    /**
     * Gets the number of targets in collection
     */
    public int getTargetCount() {
        return targets.size();
    }
    
    /**
     * Lists all targets in a formatted way
     */
    public void listTargets() {
        if (targets.isEmpty()) {
            System.out.println("No targets available.");
            return;
        }
        
        System.out.println("\n=== AVAILABLE TARGETS ===");
        int index = 1;
        
        // Sort targets by distance for better display
        List<Target> sortedTargets = new ArrayList<>(targets.values());
        sortedTargets.sort(Comparator.comparing(Target::getDistanceFromEarth));
        
        for (Target target : sortedTargets) {
            System.out.printf("%d. %s - Distance: %s (%s)%n",
                            index++, target.getName(), target.getFormattedDistance(), 
                            target.getDifficultyLevel());
        }
        System.out.println("==========================");
    }
    
    /**
     * Gets targets within a specific range
     * @param maxDistance maximum distance to include
     * @return list of targets within range
     */
    public List<Target> getTargetsWithinRange(double maxDistance) {
        return targets.values().stream()
                .filter(target -> target.getDistanceFromEarth() <= maxDistance)
                .sorted(Comparator.comparing(Target::getDistanceFromEarth))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Gets targets by difficulty level
     * @param difficulty the difficulty level
     * @return list of targets with specified difficulty
     */
    public List<Target> getTargetsByDifficulty(Target.DifficultyLevel difficulty) {
        return targets.values().stream()
                .filter(target -> target.getDifficultyLevel() == difficulty)
                .sorted(Comparator.comparing(Target::getDistanceFromEarth))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Finds the closest target to a given distance
     * @param distance reference distance
     * @return closest target or null if no targets exist
     */
    public Target findClosestTarget(double distance) {
        return targets.values().stream()
                .min(Comparator.comparing(target -> 
                    Math.abs(target.getDistanceFromEarth() - distance)))
                .orElse(null);
    }
    
    /**
     * Clears all targets from collection
     */
    public void clearAll() {
        targets.clear();
        LOGGER.log(Level.INFO, "All targets cleared from collection");
    }
}