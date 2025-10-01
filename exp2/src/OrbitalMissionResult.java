/**
 * Enhanced mission result with orbital mechanics data
 */
public class OrbitalMissionResult extends MissionResult {
    private final PhysicsEngine.OrbitalAnalysis finalOrbitalAnalysis;
    private final double finalVelocityKmS;
    private final double maxAltitudeAchieved;
    private final boolean achievedOrbit;
    private final boolean achievedEscape;
    
    public OrbitalMissionResult(boolean successful, double finalAltitude, double finalSpeed,
                               int missionDuration, double targetDistance, double distanceToTarget,
                               PhysicsEngine.OrbitalAnalysis finalOrbitalAnalysis,
                               double maxAltitudeAchieved) {
        super(successful, finalAltitude, finalSpeed, missionDuration, targetDistance, distanceToTarget);
        this.finalOrbitalAnalysis = finalOrbitalAnalysis;
        this.finalVelocityKmS = finalSpeed / 3600.0; // Convert to km/s
        this.maxAltitudeAchieved = maxAltitudeAchieved;
        this.achievedOrbit = finalOrbitalAnalysis != null && finalOrbitalAnalysis.canOrbit();
        this.achievedEscape = finalOrbitalAnalysis != null && finalOrbitalAnalysis.canEscape();
    }
    
    public PhysicsEngine.OrbitalAnalysis getFinalOrbitalAnalysis() {
        return finalOrbitalAnalysis;
    }
    
    public double getFinalVelocityKmS() {
        return finalVelocityKmS;
    }
    
    public double getMaxAltitudeAchieved() {
        return maxAltitudeAchieved;
    }
    
    public boolean achievedOrbit() {
        return achievedOrbit;
    }
    
    public boolean achievedEscape() {
        return achievedEscape;
    }
    
    /**
     * Gets orbital status description
     */
    public String getOrbitalStatusDescription() {
        if (finalOrbitalAnalysis == null) {
            return "Orbital analysis not available";
        }
        
        return finalOrbitalAnalysis.getStatus().toString();
    }
    
    /**
     * Gets detailed mission analysis
     */
    public String getDetailedAnalysis() {
        StringBuilder analysis = new StringBuilder();
        analysis.append("\n=== DETAILED MISSION ANALYSIS ===\n");
        
        analysis.append(String.format("Final Velocity: %.2f km/s (%.0f km/h)\n", 
                                     finalVelocityKmS, getFinalSpeed()));
        analysis.append(String.format("Maximum Altitude: %.2f km\n", maxAltitudeAchieved));
        analysis.append(String.format("Mission Duration: %d seconds (%.1f minutes)\n", 
                                     getMissionDuration(), getMissionDuration() / 60.0));
        
        if (finalOrbitalAnalysis != null) {
            analysis.append(String.format("Orbital Status: %s\n", finalOrbitalAnalysis.getStatus()));
            
            double requiredOrbitalV = finalOrbitalAnalysis.getRequiredOrbitalVelocity();
            double requiredEscapeV = finalOrbitalAnalysis.getRequiredEscapeVelocity();
            
            analysis.append(String.format("Required Orbital Velocity: %.2f km/s\n", requiredOrbitalV));
            analysis.append(String.format("Required Escape Velocity: %.2f km/s\n", requiredEscapeV));
            
            if (achievedEscape) {
                analysis.append("🚀 ESCAPE ACHIEVED - Rocket has left Earth's gravity well!\n");
            } else if (achievedOrbit) {
                analysis.append("🌍 ORBIT ACHIEVED - Rocket is circling Earth!\n");
            } else {
                analysis.append("📉 SUBORBITAL - Rocket will fall back to Earth\n");
                analysis.append(String.format("   Velocity deficit: %.2f km/s for orbit\n", 
                               Math.max(0, requiredOrbitalV - finalVelocityKmS)));
            }
        }
        
        if (getTargetDistance() > 0) {
            if (isSuccessful()) {
                analysis.append("🎯 TARGET REACHED - Mission successful!\n");
            } else {
                analysis.append(String.format("❌ TARGET MISSED - %.2f km remaining\n", 
                               getDistanceToTarget()));
            }
        }
        
        analysis.append("=================================");
        return analysis.toString();
    }
}