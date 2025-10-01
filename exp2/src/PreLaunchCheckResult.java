/**
 * Immutable result object for pre-launch checks
 */
public class PreLaunchCheckResult {
    private final boolean hasRocket;
    private final boolean hasTarget;
    private final String rocketName;
    private final String targetName;
    private final double targetDistance;
    private final double estimatedRange;
    private final boolean isReachable;
    private final double shortfall;
    
    public PreLaunchCheckResult(boolean hasRocket, boolean hasTarget, String rocketName, 
                              String targetName, double targetDistance, double estimatedRange, 
                              boolean isReachable, double shortfall) {
        this.hasRocket = hasRocket;
        this.hasTarget = hasTarget;
        this.rocketName = rocketName;
        this.targetName = targetName;
        this.targetDistance = targetDistance;
        this.estimatedRange = estimatedRange;
        this.isReachable = isReachable;
        this.shortfall = shortfall;
    }
    
    public boolean hasRocket() { return hasRocket; }
    public boolean hasTarget() { return hasTarget; }
    public String getRocketName() { return rocketName; }
    public String getTargetName() { return targetName; }
    public double getTargetDistance() { return targetDistance; }
    public double getEstimatedRange() { return estimatedRange; }
    public boolean isReachable() { return isReachable; }
    public double getShortfall() { return shortfall; }
}