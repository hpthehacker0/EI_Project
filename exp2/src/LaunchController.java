import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Controls rocket launches and mission execution
 * Follows Command pattern and State pattern
 */
public class LaunchController {
    private static final Logger LOGGER = Logger.getLogger(LaunchController.class.getName());
    
    private Rocket currentRocket;
    private Target currentTarget;
    private MissionState missionState;
    private int missionTime;
    private boolean stageSeparatedThisCycle;
    private MissionResult finalResult;
    private double maxAltitudeAchieved;
    
    public LaunchController() {
        this.missionState = MissionState.IDLE;
        this.missionTime = 0;
        this.stageSeparatedThisCycle = false;
    }
    
    /**
     * Sets the rocket for the mission
     */
    public void setRocket(Rocket rocket) {
        if (missionState == MissionState.IN_FLIGHT) {
            throw new IllegalStateException("Cannot change rocket during active mission");
        }
        
        this.currentRocket = rocket;
        if (rocket != null) {
            rocket.resetState();
        }
        LOGGER.log(Level.INFO, "Rocket set for mission: {}", rocket != null ? rocket.getName() : "null");
    }
    
    /**
     * Sets the target for the mission
     */
    public void setTarget(Target target) {
        if (missionState == MissionState.IN_FLIGHT) {
            throw new IllegalStateException("Cannot change target during active mission");
        }
        
        this.currentTarget = target;
        LOGGER.log(Level.INFO, "Target set for mission: {}", target != null ? target.getName() : "null");
    }
    
    /**
     * Performs comprehensive pre-launch checks including orbital mechanics
     */
    public PreLaunchCheckResult performPreLaunchChecks() {
        boolean hasRocket = currentRocket != null;
        boolean hasTarget = currentTarget != null;
        
        String rocketName = hasRocket ? currentRocket.getName() : null;
        String targetName = hasTarget ? currentTarget.getName() : null;
        double targetDistance = hasTarget ? currentTarget.getDistanceFromEarth() : 0;
        double estimatedRange = hasRocket ? currentRocket.calculateEstimatedRange() : 0;
        
        // Enhanced physics-based analysis
        PhysicsEngine.ReachabilityAnalysis reachabilityAnalysis = null;
        PhysicsEngine.OrbitalAnalysis orbitalAnalysis = null;
        
        if (hasRocket && hasTarget) {
            reachabilityAnalysis = PhysicsEngine.analyzeReachability(currentRocket, currentTarget);
            
            // Estimate final velocity for orbital analysis
            double estimatedFinalVelocity = currentRocket.getMaxSpeed() / 3600.0 * 0.8; // km/s, with efficiency factor
            orbitalAnalysis = PhysicsEngine.analyzeOrbitalCapability(estimatedFinalVelocity, targetDistance);
        }
        
        boolean isReachable = reachabilityAnalysis != null && reachabilityAnalysis.canReachTarget();
        double shortfall = reachabilityAnalysis != null ? reachabilityAnalysis.getDeltaVDeficit() : 0;
        
        LOGGER.log(Level.INFO, "Pre-launch checks completed - Rocket: " + rocketName + ", Target: " + targetName + ", Reachable: " + isReachable);
        
        return new EnhancedPreLaunchCheckResult(hasRocket, hasTarget, rocketName, targetName, 
                                               targetDistance, estimatedRange, isReachable, shortfall,
                                               reachabilityAnalysis, orbitalAnalysis);
    }
    
    /**
     * Launches the rocket
     */
    public MissionResult launch() {
        if (currentRocket == null || currentTarget == null) {
            throw new IllegalStateException("Both rocket and target must be selected before launching.");
        }
        
        if (missionState == MissionState.IN_FLIGHT) {
            throw new IllegalStateException("Mission already in progress");
        }
        
        // Reset mission state
        currentRocket.resetState();
        missionState = MissionState.IN_FLIGHT;
        missionTime = 0;
        stageSeparatedThisCycle = false;
        finalResult = null;
        maxAltitudeAchieved = 0;
        
        // Launch the rocket
        currentRocket.launch();
        
        LOGGER.log(Level.INFO, "Mission launched - Rocket: " + currentRocket.getName() + ", Target: " + currentTarget.getName());
        
        return new MissionResult(false, 0, 0, 0, currentTarget.getDistanceFromEarth(), 0);
    }
    
    /**
     * Updates the simulation by the specified time
     * @param deltaTime time to advance in seconds
     */
    public void updateSimulation(int deltaTime) {
        if (missionState != MissionState.IN_FLIGHT || currentRocket == null) {
            return;
        }
        
        stageSeparatedThisCycle = false;
        
        for (int i = 0; i < deltaTime; i++) {
            missionTime++;
            
            // Check for stage separation
            if (currentRocket.shouldSeparateStage()) {
                if (currentRocket.separateStage()) {
                    stageSeparatedThisCycle = true;
                }
            }
            
            // Update rocket state
            boolean stillFlying = currentRocket.update(1.0);
            
            // Track maximum altitude
            maxAltitudeAchieved = Math.max(maxAltitudeAchieved, currentRocket.getAltitude());
            
            if (!stillFlying) {
                // Mission ended
                completeMission();
                break;
            }
            
            // Check if target reached
            if (currentRocket.getAltitude() >= currentTarget.getDistanceFromEarth()) {
                completeMission();
                break;
            }
        }
    }
    
    private void completeMission() {
        missionState = MissionState.COMPLETED;
        
        boolean successful = currentRocket.getAltitude() >= currentTarget.getDistanceFromEarth();
        double finalAltitude = currentRocket.getAltitude();
        double finalSpeed = currentRocket.getCurrentSpeed();
        double distanceToTarget = Math.max(0, currentTarget.getDistanceFromEarth() - finalAltitude);
        
        // Create enhanced orbital mission result
        double finalVelocityKmS = finalSpeed / 3600.0;
        PhysicsEngine.OrbitalAnalysis orbitalAnalysis = PhysicsEngine.analyzeOrbitalCapability(finalVelocityKmS, finalAltitude);
        
        finalResult = new OrbitalMissionResult(successful, finalAltitude, finalSpeed, 
                                             missionTime, currentTarget.getDistanceFromEarth(), 
                                             distanceToTarget, orbitalAnalysis, maxAltitudeAchieved);
        
       LOGGER.log(Level.INFO, "Mission completed - Success: " + successful + ", Final Altitude: " + finalAltitude + ", Duration: " + missionTime + "s");
    }
    
    /**
     * Gets current flight status
     */
    public FlightStatus getFlightStatus() {
        if (currentRocket == null || missionState != MissionState.IN_FLIGHT) {
            return new FlightStatus(0, 0, 0, 0, false);
        }
        
        return new FlightStatus(
            currentRocket.getCurrentStage(),
            currentRocket.getFuelPercentage(),
            currentRocket.getAltitude(),
            currentRocket.getCurrentSpeed(),
            stageSeparatedThisCycle
        );
    }
    
    /**
     * Gets the final mission result
     */
    public MissionResult getFinalResult() {
        return finalResult;
    }
    
    // Getters
    public Rocket getCurrentRocket() { return currentRocket; }
    public Target getCurrentTarget() { return currentTarget; }
    public MissionState getMissionState() { return missionState; }
    public boolean isInFlight() { return missionState == MissionState.IN_FLIGHT; }
    
    /**
     * Enum for mission states
     */
    public enum MissionState {
        IDLE("Idle - Ready for mission setup"),
        IN_FLIGHT("In Flight - Mission in progress"),
        COMPLETED("Completed - Mission finished");
        
        private final String description;
        
        MissionState(String description) {
            this.description = description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
}
