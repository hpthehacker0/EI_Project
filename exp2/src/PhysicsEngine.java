/**
 * Physics engine for realistic rocket flight calculations
 * Includes escape velocity, orbital mechanics, and atmospheric effects
 */
public class PhysicsEngine {
    
    // Physical constants
    private static final double EARTH_MASS = 5.972e24; // kg
    private static final double EARTH_RADIUS = 6371.0; // km
    private static final double GRAVITATIONAL_CONSTANT = 6.67430e-11; // m^3 kg^-1 s^-2
    private static final double EARTH_ESCAPE_VELOCITY = 11.18; // km/s at surface
    private static final double LEO_ORBITAL_VELOCITY = 7.8; // km/s for Low Earth Orbit
    
    // Atmospheric constants
    private static final double ATMOSPHERE_HEIGHT = 100.0; // km (Karman line)
    private static final double SEA_LEVEL_DENSITY = 1.225; // kg/m^3
    private static final double SCALE_HEIGHT = 8.4; // km
    
    /**
     * Calculates escape velocity at a given altitude
     * @param altitude altitude in km
     * @return escape velocity in km/s
     */
    public static double calculateEscapeVelocity(double altitude) {
        double distance = EARTH_RADIUS + altitude; // km
        double distanceMeters = distance * 1000; // convert to meters
        
        // v_escape = sqrt(2 * G * M / r)
        double escapeVelocityMS = Math.sqrt(2 * GRAVITATIONAL_CONSTANT * EARTH_MASS / distanceMeters);
        return escapeVelocityMS / 1000; // convert to km/s
    }
    
    /**
     * Calculates orbital velocity at a given altitude
     * @param altitude altitude in km
     * @return orbital velocity in km/s
     */
    public static double calculateOrbitalVelocity(double altitude) {
        double distance = EARTH_RADIUS + altitude; // km
        double distanceMeters = distance * 1000; // convert to meters
        
        // v_orbital = sqrt(G * M / r)
        double orbitalVelocityMS = Math.sqrt(GRAVITATIONAL_CONSTANT * EARTH_MASS / distanceMeters);
        return orbitalVelocityMS / 1000; // convert to km/s
    }
    
    /**
     * Calculates gravitational acceleration at a given altitude
     * @param altitude altitude in km
     * @return acceleration in m/s^2
     */
    public static double calculateGravitationalAcceleration(double altitude) {
        double distance = EARTH_RADIUS + altitude; // km
        double distanceMeters = distance * 1000; // convert to meters
        
        // g = G * M / r^2
        return GRAVITATIONAL_CONSTANT * EARTH_MASS / (distanceMeters * distanceMeters);
    }
    
    /**
     * Calculates atmospheric density at a given altitude
     * @param altitude altitude in km
     * @return density in kg/m^3
     */
    public static double calculateAtmosphericDensity(double altitude) {
        if (altitude > ATMOSPHERE_HEIGHT) {
            return 0; // No atmosphere in space
        }
        
        // Exponential atmosphere model: ρ = ρ₀ * e^(-h/H)
        return SEA_LEVEL_DENSITY * Math.exp(-altitude / SCALE_HEIGHT);
    }
    
    /**
     * Calculates drag force on the rocket
     * @param velocity velocity in km/s
     * @param altitude altitude in km
     * @param crossSectionalArea cross-sectional area in m^2
     * @param dragCoefficient drag coefficient (dimensionless)
     * @return drag force in Newtons
     */
    public static double calculateDragForce(double velocity, double altitude, 
                                          double crossSectionalArea, double dragCoefficient) {
        double density = calculateAtmosphericDensity(altitude);
        double velocityMS = velocity * 1000; // convert to m/s
        
        // F_drag = 0.5 * ρ * v^2 * Cd * A
        return 0.5 * density * velocityMS * velocityMS * dragCoefficient * crossSectionalArea;
    }
    
    /**
     * Determines if a rocket can achieve orbit at the target altitude
     * @param rocketSpeed current speed in km/s
     * @param altitude target altitude in km
     * @return orbital achievement result
     */
    public static OrbitalAnalysis analyzeOrbitalCapability(double rocketSpeed, double altitude) {
        double requiredOrbitalVelocity = calculateOrbitalVelocity(altitude);
        double requiredEscapeVelocity = calculateEscapeVelocity(altitude);
        
        boolean canOrbit = rocketSpeed >= requiredOrbitalVelocity;
        boolean canEscape = rocketSpeed >= requiredEscapeVelocity;
        
        OrbitalStatus status;
        if (canEscape) {
            status = OrbitalStatus.ESCAPE_TRAJECTORY;
        } else if (canOrbit) {
            status = OrbitalStatus.STABLE_ORBIT;
        } else {
            status = OrbitalStatus.SUBORBITAL;
        }
        
        double velocityDeficit = Math.max(0, requiredOrbitalVelocity - rocketSpeed);
        double escapeDeficit = Math.max(0, requiredEscapeVelocity - rocketSpeed);
        
        return new OrbitalAnalysis(canOrbit, canEscape, status, requiredOrbitalVelocity, 
                                 requiredEscapeVelocity, velocityDeficit, escapeDeficit);
    }
    
    /**
     * Calculates the delta-v (velocity change) required for a mission
     * @param startAltitude starting altitude in km
     * @param targetAltitude target altitude in km
     * @return required delta-v in km/s
     */
    public static double calculateRequiredDeltaV(double startAltitude, double targetAltitude) {
        double startOrbitalV = calculateOrbitalVelocity(startAltitude);
        double targetOrbitalV = calculateOrbitalVelocity(targetAltitude);
        
        // Simplified Hohmann transfer calculation
        double transferPeriapsis = EARTH_RADIUS + Math.min(startAltitude, targetAltitude);
        double transferApoapsis = EARTH_RADIUS + Math.max(startAltitude, targetAltitude);
        double transferSemiMajorAxis = (transferPeriapsis + transferApoapsis) / 2;
        
        double transferVelocityStart = Math.sqrt(GRAVITATIONAL_CONSTANT * EARTH_MASS * 1000 * 
                                               (2 / (transferPeriapsis * 1000) - 1 / (transferSemiMajorAxis * 1000)));
        double transferVelocityEnd = Math.sqrt(GRAVITATIONAL_CONSTANT * EARTH_MASS * 1000 * 
                                             (2 / (transferApoapsis * 1000) - 1 / (transferSemiMajorAxis * 1000)));
        
        transferVelocityStart /= 1000; // convert to km/s
        transferVelocityEnd /= 1000; // convert to km/s
        
        double deltaV1 = Math.abs(transferVelocityStart - startOrbitalV);
        double deltaV2 = Math.abs(targetOrbitalV - transferVelocityEnd);
        
        return deltaV1 + deltaV2;
    }
    
    /**
     * Checks if target is reachable considering orbital mechanics
     * @param rocket the rocket being used
     * @param target the target destination
     * @return reachability analysis
     */
    public static ReachabilityAnalysis analyzeReachability(Rocket rocket, Target target) {
        double targetDistance = target.getDistanceFromEarth();
        double maxRocketSpeed = rocket.getMaxSpeed() / 3600.0; // convert km/h to km/s
        
        // Calculate required velocities
        double requiredEscapeVelocity = calculateEscapeVelocity(0); // at surface
        double requiredOrbitalVelocity = calculateOrbitalVelocity(targetDistance);
        double requiredDeltaV = calculateRequiredDeltaV(0, targetDistance);
        
        // Estimate rocket's actual achievable velocity based on fuel and efficiency
        double achievableVelocity = estimateAchievableVelocity(rocket);
        
        boolean canReachTarget = achievableVelocity >= requiredDeltaV;
        boolean canOrbitTarget = achievableVelocity >= requiredOrbitalVelocity;
        boolean canEscapeEarth = achievableVelocity >= requiredEscapeVelocity;
        
        return new ReachabilityAnalysis(canReachTarget, canOrbitTarget, canEscapeEarth,
                                      requiredDeltaV, achievableVelocity, requiredOrbitalVelocity,
                                      requiredEscapeVelocity);
    }
    
    /**
     * Estimates the maximum velocity a rocket can achieve based on its specifications
     * @param rocket the rocket to analyze
     * @return estimated maximum velocity in km/s
     */
    private static double estimateAchievableVelocity(Rocket rocket) {
        // Simplified rocket equation: Δv = Isp * g0 * ln(m0/mf)
        // Where Isp is specific impulse, g0 is standard gravity, m0 is initial mass, mf is final mass
        
        double specificImpulse = estimateSpecificImpulse(rocket);
        double massRatio = estimateMassRatio(rocket);
        double standardGravity = 9.80665 / 1000; // km/s^2
        
        return specificImpulse * standardGravity * Math.log(massRatio);
    }
    
    private static double estimateSpecificImpulse(Rocket rocket) {
        // Estimate based on rocket characteristics
        // Higher fuel capacity and more stages generally mean better Isp
        double baseIsp = 300; // seconds (typical for chemical rockets)
        double stageBonus = (rocket.getTotalStages() - 1) * 50;
        double fuelBonus = Math.log(rocket.getFuelCapacity() / 100000) * 20;
        
        return Math.max(250, Math.min(450, baseIsp + stageBonus + fuelBonus));
    }
    
    private static double estimateMassRatio(Rocket rocket) {
        // Estimate mass ratio based on fuel capacity and stages
        // More stages allow for better mass ratios
        double baseMassRatio = 3.0; // typical for single stage
        double stageMultiplier = Math.pow(2.5, rocket.getTotalStages() - 1);
        
        return Math.min(20, baseMassRatio * stageMultiplier);
    }
    
    /**
     * Result class for orbital analysis
     */
    public static class OrbitalAnalysis {
        private final boolean canOrbit;
        private final boolean canEscape;
        private final OrbitalStatus status;
        private final double requiredOrbitalVelocity;
        private final double requiredEscapeVelocity;
        private final double velocityDeficit;
        private final double escapeDeficit;
        
        public OrbitalAnalysis(boolean canOrbit, boolean canEscape, OrbitalStatus status,
                             double requiredOrbitalVelocity, double requiredEscapeVelocity,
                             double velocityDeficit, double escapeDeficit) {
            this.canOrbit = canOrbit;
            this.canEscape = canEscape;
            this.status = status;
            this.requiredOrbitalVelocity = requiredOrbitalVelocity;
            this.requiredEscapeVelocity = requiredEscapeVelocity;
            this.velocityDeficit = velocityDeficit;
            this.escapeDeficit = escapeDeficit;
        }
        
        // Getters
        public boolean canOrbit() { return canOrbit; }
        public boolean canEscape() { return canEscape; }
        public OrbitalStatus getStatus() { return status; }
        public double getRequiredOrbitalVelocity() { return requiredOrbitalVelocity; }
        public double getRequiredEscapeVelocity() { return requiredEscapeVelocity; }
        public double getVelocityDeficit() { return velocityDeficit; }
        public double getEscapeDeficit() { return escapeDeficit; }
    }
    
    /**
     * Result class for reachability analysis
     */
    public static class ReachabilityAnalysis {
        private final boolean canReachTarget;
        private final boolean canOrbitTarget;
        private final boolean canEscapeEarth;
        private final double requiredDeltaV;
        private final double achievableVelocity;
        private final double requiredOrbitalVelocity;
        private final double requiredEscapeVelocity;
        
        public ReachabilityAnalysis(boolean canReachTarget, boolean canOrbitTarget, 
                                  boolean canEscapeEarth, double requiredDeltaV, 
                                  double achievableVelocity, double requiredOrbitalVelocity,
                                  double requiredEscapeVelocity) {
            this.canReachTarget = canReachTarget;
            this.canOrbitTarget = canOrbitTarget;
            this.canEscapeEarth = canEscapeEarth;
            this.requiredDeltaV = requiredDeltaV;
            this.achievableVelocity = achievableVelocity;
            this.requiredOrbitalVelocity = requiredOrbitalVelocity;
            this.requiredEscapeVelocity = requiredEscapeVelocity;
        }
        
        // Getters
        public boolean canReachTarget() { return canReachTarget; }
        public boolean canOrbitTarget() { return canOrbitTarget; }
        public boolean canEscapeEarth() { return canEscapeEarth; }
        public double getRequiredDeltaV() { return requiredDeltaV; }
        public double getAchievableVelocity() { return achievableVelocity; }
        public double getRequiredOrbitalVelocity() { return requiredOrbitalVelocity; }
        public double getRequiredEscapeVelocity() { return requiredEscapeVelocity; }
        
        public double getDeltaVDeficit() {
            return Math.max(0, requiredDeltaV - achievableVelocity);
        }
    }
    
    /**
     * Enum for orbital status
     */
    public enum OrbitalStatus {
        SUBORBITAL("Suborbital - Will fall back to Earth"),
        STABLE_ORBIT("Stable Orbit - Circling Earth"),
        ESCAPE_TRAJECTORY("Escape Trajectory - Leaving Earth's gravity well");
        
        private final String description;
        
        OrbitalStatus(String description) {
            this.description = description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
}
