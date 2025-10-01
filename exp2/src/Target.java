/**
 * Represents a target destination for the rocket mission
 * Immutable value object following best practices
 */
public class Target {
    private final String name;
    private final double distanceFromEarth; // in kilometers
    
    public Target(String name, double distanceFromEarth) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Target name cannot be null or empty");
        }
        if (distanceFromEarth < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        
        this.name = name.trim();
        this.distanceFromEarth = distanceFromEarth;
    }
    
    /**
     * Gets the name of the target
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the distance from Earth in kilometers
     */
    public double getDistanceFromEarth() {
        return distanceFromEarth;
    }
    
    /**
     * Determines the difficulty level based on distance
     */
    public DifficultyLevel getDifficultyLevel() {
        if (distanceFromEarth <= 1000) {
            return DifficultyLevel.EASY;
        } else if (distanceFromEarth <= 50000) {
            return DifficultyLevel.MEDIUM;
        } else if (distanceFromEarth <= 1000000) {
            return DifficultyLevel.HARD;
        } else {
            return DifficultyLevel.EXTREME;
        }
    }
    
    /**
     * Gets a formatted distance string with appropriate units
     */
    public String getFormattedDistance() {
        if (distanceFromEarth < 1000) {
            return String.format("%.1f km", distanceFromEarth);
        } else if (distanceFromEarth < 1000000) {
            return String.format("%.0f km", distanceFromEarth);
        } else {
            return String.format("%.2f million km", distanceFromEarth / 1000000.0);
        }
    }
    
    /**
     * Checks if the target is reachable by a rocket with given range
     */
    public boolean isReachableBy(double rocketRange) {
        return rocketRange >= distanceFromEarth;
    }
    
    /**
     * Calculates the shortfall if the rocket cannot reach this target
     */
    public double calculateShortfall(double rocketRange) {
        return Math.max(0, distanceFromEarth - rocketRange);
    }
    
    @Override
    public String toString() {
        return String.format("Target{name='%s', distance=%s, difficulty=%s}",
                           name, getFormattedDistance(), getDifficultyLevel());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Target target = (Target) obj;
        return name.equals(target.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /**
     * Enum for difficulty levels based on distance
     */
    public enum DifficultyLevel {
        EASY("Easy - Low Earth Orbit"),
        MEDIUM("Medium - Beyond LEO"),
        HARD("Hard - Interplanetary"),
        EXTREME("Extreme - Deep Space");
        
        private final String description;
        
        DifficultyLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
}
