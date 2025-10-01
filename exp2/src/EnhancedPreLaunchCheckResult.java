/**
 * Enhanced pre-launch check result with orbital mechanics analysis
 */
public class EnhancedPreLaunchCheckResult extends PreLaunchCheckResult {
    private final PhysicsEngine.ReachabilityAnalysis reachabilityAnalysis;
    private final PhysicsEngine.OrbitalAnalysis orbitalAnalysis;
    
    public EnhancedPreLaunchCheckResult(boolean hasRocket, boolean hasTarget, String rocketName,
                                       String targetName, double targetDistance, double estimatedRange,
                                       boolean isReachable, double shortfall,
                                       PhysicsEngine.ReachabilityAnalysis reachabilityAnalysis,
                                       PhysicsEngine.OrbitalAnalysis orbitalAnalysis) {
        super(hasRocket, hasTarget, rocketName, targetName, targetDistance, estimatedRange, isReachable, shortfall);
        this.reachabilityAnalysis = reachabilityAnalysis;
        this.orbitalAnalysis = orbitalAnalysis;
    }
    
    public PhysicsEngine.ReachabilityAnalysis getReachabilityAnalysis() {
        return reachabilityAnalysis;
    }
    
    public PhysicsEngine.OrbitalAnalysis getOrbitalAnalysis() {
        return orbitalAnalysis;
    }
    
    /**
     * Gets detailed physics analysis as formatted string
     */
    public String getPhysicsAnalysis() {
        if (reachabilityAnalysis == null) {
            return "Physics analysis not available - select rocket and target first.";
        }
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("\n=== ORBITAL MECHANICS ANALYSIS ===\n");
        
        // Escape velocity analysis
        double escapeVelocity = PhysicsEngine.calculateEscapeVelocity(0);
        analysis.append(String.format("Earth Escape Velocity: %.2f km/s (%.0f km/h)\n", 
                                     escapeVelocity, escapeVelocity * 3600));
        
        // Required velocities
        analysis.append(String.format("Required Delta-V: %.2f km/s\n", reachabilityAnalysis.getRequiredDeltaV()));
        analysis.append(String.format("Achievable Delta-V: %.2f km/s\n", reachabilityAnalysis.getAchievableVelocity()));
        
        if (getTargetDistance() > 0) {
            double orbitalVelocity = PhysicsEngine.calculateOrbitalVelocity(getTargetDistance());
            analysis.append(String.format("Target Orbital Velocity: %.2f km/s\n", orbitalVelocity));
        }
        
        // Capability analysis
        analysis.append("\n=== MISSION CAPABILITY ===\n");
        analysis.append(String.format("Can Escape Earth: %s\n", 
                                     reachabilityAnalysis.canEscapeEarth() ? "✓ YES" : "❌ NO"));
        analysis.append(String.format("Can Reach Target: %s\n", 
                                     reachabilityAnalysis.canReachTarget() ? "✓ YES" : "❌ NO"));
        analysis.append(String.format("Can Orbit Target: %s\n", 
                                     reachabilityAnalysis.canOrbitTarget() ? "✓ YES" : "❌ NO"));
        
        if (!reachabilityAnalysis.canEscapeEarth()) {
            double escapeDeficit = reachabilityAnalysis.getRequiredEscapeVelocity() - 
                                  reachabilityAnalysis.getAchievableVelocity();
            analysis.append(String.format("⚠ Escape Velocity Deficit: %.2f km/s\n", escapeDeficit));
        }
        
        if (!reachabilityAnalysis.canReachTarget()) {
            analysis.append(String.format("⚠ Delta-V Deficit: %.2f km/s\n", 
                                         reachabilityAnalysis.getDeltaVDeficit()));
        }
        
        analysis.append("===================================");
        
        return analysis.toString();
    }
}