/**
 * Immutable status object for in-flight rockets
 */
public class FlightStatus {
    private final int currentStage;
    private final double fuelPercentage;
    private final double altitude;
    private final double speed;
    private final boolean hasStageSeparated;
    
    public FlightStatus(int currentStage, double fuelPercentage, double altitude, 
                       double speed, boolean hasStageSeparated) {
        this.currentStage = currentStage;
        this.fuelPercentage = fuelPercentage;
        this.altitude = altitude;
        this.speed = speed;
        this.hasStageSeparated = hasStageSeparated;
    }
    
    public int getCurrentStage() { return currentStage; }
    public double getFuelPercentage() { return fuelPercentage; }
    public double getAltitude() { return altitude; }
    public double getSpeed() { return speed; }
    public boolean hasStageSeparated() { return hasStageSeparated; }
}