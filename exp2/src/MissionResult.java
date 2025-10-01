/**
 * Immutable result object for completed missions
 */
public class MissionResult {
    private final boolean successful;
    private final double finalAltitude;
    private final double finalSpeed;
    private final int missionDuration;
    private final double targetDistance;
    private final double distanceToTarget;
    
    public MissionResult(boolean successful, double finalAltitude, double finalSpeed, 
                        int missionDuration, double targetDistance, double distanceToTarget) {
        this.successful = successful;
        this.finalAltitude = finalAltitude;
        this.finalSpeed = finalSpeed;
        this.missionDuration = missionDuration;
        this.targetDistance = targetDistance;
        this.distanceToTarget = distanceToTarget;
    }
    
    public boolean isSuccessful() { return successful; }
    public double getFinalAltitude() { return finalAltitude; }
    public double getFinalSpeed() { return finalSpeed; }
    public int getMissionDuration() { return missionDuration; }
    public double getTargetDistance() { return targetDistance; }
    public double getDistanceToTarget() { return distanceToTarget; }
}