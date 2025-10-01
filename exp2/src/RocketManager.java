import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manages rocket inventory using Repository pattern
 * Handles CRUD operations for rockets
 */
public class RocketManager {
    private static final Logger LOGGER = Logger.getLogger(RocketManager.class.getName());
    
    private final Map<String, Rocket> rockets;
    
    public RocketManager() {
        this.rockets = new HashMap<>();
    }
    
    /**
     * Creates a new rocket and adds it to the inventory
     * @param name unique name for the rocket
     * @param fuelCapacity fuel capacity in kg
     * @param maxSpeed maximum speed in km/h
     * @param stages number of stages
     * @return true if created successfully, false if name already exists
     */
    public boolean createRocket(String name, double fuelCapacity, double maxSpeed, int stages) {
        try {
            if (name == null || name.trim().isEmpty()) {
                LOGGER.log(Level.WARNING, "Attempted to create rocket with empty name");
                return false;
            }
            
            String normalizedName = name.trim();
            if (rockets.containsKey(normalizedName)) {
                LOGGER.log(Level.INFO, "Rocket with name '{}' already exists", normalizedName);
                return false;
            }
            
            Rocket rocket = new Rocket(normalizedName, fuelCapacity, maxSpeed, stages);
            rockets.put(normalizedName, rocket);
            
            LOGGER.log(Level.INFO, "Created rocket: {}", rocket);
            return true;
            
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Failed to create rocket due to invalid parameters: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error creating rocket", e);
            return false;
        }
    }
    
    /**
     * Deletes a rocket from the inventory
     * @param name name of the rocket to delete
     * @return true if deleted successfully, false if not found
     */
    public boolean deleteRocket(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }
            
            String normalizedName = name.trim();
            Rocket removed = rockets.remove(normalizedName);
            
            if (removed != null) {
                LOGGER.log(Level.INFO, "Deleted rocket: {}", normalizedName);
                return true;
            } else {
                LOGGER.log(Level.INFO, "Rocket '{}' not found for deletion", normalizedName);
                return false;
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error deleting rocket", e);
            return false;
        }
    }
    
    /**
     * Retrieves a rocket by name
     * @param name name of the rocket
     * @return Rocket object or null if not found
     */
    public Rocket getRocket(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return rockets.get(name.trim());
    }
    
    /**
     * Gets all rockets in the inventory
     * @return unmodifiable collection of rockets
     */
    public Collection<Rocket> getAllRockets() {
        return Collections.unmodifiableCollection(rockets.values());
    }
    
    /**
     * Gets all rocket names
     * @return unmodifiable set of rocket names
     */
    public Set<String> getRocketNames() {
        return Collections.unmodifiableSet(rockets.keySet());
    }
    
    /**
     * Checks if a rocket exists
     * @param name rocket name
     * @return true if exists, false otherwise
     */
    public boolean rocketExists(String name) {
        return name != null && rockets.containsKey(name.trim());
    }
    
    /**
     * Gets the number of rockets in inventory
     */
    public int getRocketCount() {
        return rockets.size();
    }
    
    /**
     * Lists all rockets in a formatted way
     */
    public void listRockets() {
        if (rockets.isEmpty()) {
            System.out.println("No rockets available.");
            return;
        }
        
        System.out.println("\n=== AVAILABLE ROCKETS ===");
        int index = 1;
        for (Rocket rocket : rockets.values()) {
            System.out.printf("%d. %s - Fuel: %.0f kg, Max Speed: %.0f km/h, Stages: %d, Est. Range: %.2f km%n",
                            index++, rocket.getName(), rocket.getFuelCapacity(), 
                            rocket.getMaxSpeed(), rocket.getTotalStages(), rocket.calculateEstimatedRange());
        }
        System.out.println("========================");
    }
    
    /**
     * Clears all rockets from inventory
     */
    public void clearAll() {
        rockets.clear();
        LOGGER.log(Level.INFO, "All rockets cleared from inventory");
    }
}