/**
 * Represents a rocket with its specifications and current state
 * Follows Builder pattern for complex object creation
 */
public class Rocket {
    private final String name;
    private final double fuelCapacity; // in kg
    private final double maxSpeed; // in km/h
    private final int totalStages;
    
    // Current state
    private double currentFuel;
    private double currentSpeed;
    private double altitude;
    private int currentStage;
    private boolean isLaunched;
    
    // Constants for simulation
    private static final double FUEL_CONSUMPTION_RATE = 0.02; // % per second
    private static final double ALTITUDE_GAIN_RATE = 2.5; // km per second base rate
    private static final double SPEED_GAIN_RATE = 500; // km/h per second base rate
    private static final double STAGE_EFFICIENCY_MULTIPLIER = 1.2;
    
    public Rocket(String name, double fuelCapacity, double maxSpeed, int totalStages) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Rocket name cannot be null or empty");
        }
        if (fuelCapacity <= 0) {
            throw new IllegalArgumentException("Fuel capacity must be positive");
        }
        if (maxSpeed <= 0) {
            throw new IllegalArgumentException("Max speed must be positive");
        }
        if (totalStages <= 0) {
            throw new IllegalArgumentException("Total stages must be positive");
        }
        
        this.name = name;
        this.fuelCapacity = fuelCapacity;
        this.maxSpeed = maxSpeed;
        this.totalStages = totalStages;
        
        // Initialize state
        resetState();
    }
    
    /**
     * Resets rocket to initial state for new mission
     */
    public void resetState() {
        this.currentFuel = fuelCapacity;
        this.currentSpeed = 0;
        this.altitude = 0;
        this.currentStage = 1;
        this.isLaunched = false;
    }
    
    /**
     * Launches the rocket (changes state to launched)
     */
    public void launch() {
        if (isLaunched) {
            throw new IllegalStateException("Rocket is already launched");
        }
        this.isLaunched = true;
    }
    
    /**
     * Updates rocket state based on time elapsed using realistic physics
     * @param deltaTime time in seconds
     * @return true if rocket is still operational, false if out of fuel
     */
    public boolean update(double deltaTime) {
        if (!isLaunched || currentFuel <= 0) {
            return false;
        }
        
        // Calculate fuel consumption
        double fuelUsed = (fuelCapacity * FUEL_CONSUMPTION_RATE * deltaTime) / getCurrentStageEfficiency();
        currentFuel = Math.max(0, currentFuel - fuelUsed);
        
        if (currentFuel <= 0) {
            return false;
        }
        
        // Realistic physics-based calculations
        double currentSpeedKmS = currentSpeed / 3600.0; // Convert km/h to km/s
        double gravityAccel = PhysicsEngine.calculateGravitationalAcceleration(altitude);
        double atmosphericDensity = PhysicsEngine.calculateAtmosphericDensity(altitude);
        
        // Calculate thrust (simplified)
        double thrust = calculateThrust();
        double rocketMass = calculateCurrentMass();
        double dragForce = PhysicsEngine.calculateDragForce(currentSpeedKmS, altitude, 10.0, 0.3);
        
        // Net acceleration = (Thrust - Drag - Gravity*Mass) / Mass
        double netAcceleration = (thrust - dragForce - gravityAccel * rocketMass) / rocketMass;
        
        // Update velocity (convert back to km/h)
        double velocityChangeKmS = netAcceleration * deltaTime / 1000; // m/s to km/s
        double velocityChangeKmH = velocityChangeKmS * 3600; // km/s to km/h
        currentSpeed = Math.max(0, Math.min(maxSpeed, currentSpeed + velocityChangeKmH));
        
        // Update altitude based on average velocity during this time step
        double avgVelocityKmH = currentSpeed - (velocityChangeKmH / 2);
        double altitudeChange = (avgVelocityKmH / 3600.0) * deltaTime; // km
        altitude += altitudeChange;
        
        return true;
    }
    
    /**
     * Calculates current thrust based on fuel and stage
     */
    private double calculateThrust() {
        if (currentFuel <= 0) return 0;
        
        // Base thrust varies by stage and fuel remaining
        double baseThrust = fuelCapacity * 0.01; // N per kg of fuel capacity
        double stageMultiplier = Math.pow(1.3, currentStage);
        double fuelEfficiency = Math.min(1.0, currentFuel / (fuelCapacity * 0.1));
        
        return baseThrust * stageMultiplier * fuelEfficiency;
    }
    
    /**
     * Calculates current rocket mass including remaining fuel
     */
    private double calculateCurrentMass() {
        double dryMass = fuelCapacity * 0.1; // Assume dry mass is 10% of fuel capacity
        return dryMass + currentFuel;
    }
    
    /**
     * Attempts to separate to next stage
     * @return true if separation successful, false if no more stages
     */
    public boolean separateStage() {
        if (currentStage >= totalStages) {
            return false;
        }
        
        currentStage++;
        // Stage separation gives efficiency boost and fuel redistribution
        double remainingFuelRatio = currentFuel / fuelCapacity;
        double stageBoost = 1.0 + (0.1 * currentStage); // Each stage gives 10% boost
        
        return true;
    }
    
    /**
     * Calculates estimated maximum range based on current fuel and efficiency
     */
    public double calculateEstimatedRange() {
        // Simplified range calculation based on fuel, efficiency, and stages
        double baseRange = (fuelCapacity / 1000) * 50; // Base: 50km per 1000kg fuel
        double stageMultiplier = 1 + (totalStages - 1) * 0.3; // Each additional stage adds 30%
        double speedMultiplier = maxSpeed / 25000; // Speed factor
        
        return baseRange * stageMultiplier * speedMultiplier;
    }
    
    /**
     * Checks if stage separation should occur based on fuel level
     */
    public boolean shouldSeparateStage() {
        if (currentStage >= totalStages) {
            return false;
        }
        
        double fuelPercentage = (currentFuel / fuelCapacity) * 100;
        double separationThreshold = 100.0 * (totalStages - currentStage + 1) / totalStages / 2;
        
        return fuelPercentage <= separationThreshold;
    }
    
    private double getCurrentStageEfficiency() {
        return Math.pow(STAGE_EFFICIENCY_MULTIPLIER, currentStage - 1);
    }
    
    // Getters
    public String getName() { return name; }
    public double getFuelCapacity() { return fuelCapacity; }
    public double getMaxSpeed() { return maxSpeed; }
    public int getTotalStages() { return totalStages; }
    public double getCurrentFuel() { return currentFuel; }
    public double getCurrentSpeed() { return currentSpeed; }
    public double getAltitude() { return altitude; }
    public int getCurrentStage() { return currentStage; }
    public boolean isLaunched() { return isLaunched; }
    
    /**
     * Gets current fuel as percentage
     */
    public double getFuelPercentage() {
        return (currentFuel / fuelCapacity) * 100;
    }
    
    /**
     * Checks if rocket has fuel remaining
     */
    public boolean hasFuel() {
        return currentFuel > 0;
    }
    
    @Override
    public String toString() {
        return String.format("Rocket{name='%s', fuel=%.0f kg, maxSpeed=%.0f km/h, stages=%d}",
                           name, fuelCapacity, maxSpeed, totalStages);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rocket rocket = (Rocket) obj;
        return name.equals(rocket.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}