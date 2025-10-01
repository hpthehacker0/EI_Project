import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Main class for the Rocket Launch Simulator
 * Handles user interaction and coordinates the simulation
 */
public class RocketLaunchSimulator {
    private static final Logger LOGGER = Logger.getLogger(RocketLaunchSimulator.class.getName());
    
    private final RocketManager rocketManager;
    private final TargetManager targetManager;
    private final LaunchController launchController;
    private final Scanner scanner;
    private boolean isRunning;
    
    public RocketLaunchSimulator() {
        setupLogging();
        this.rocketManager = new RocketManager();
        this.targetManager = new TargetManager();
        this.launchController = new LaunchController();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        
        // Initialize with default rockets and targets
        initializeDefaults();
    }
    
    private void setupLogging() {
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(handler);
    }
    
    private void initializeDefaults() {
        // Create default rockets
        rocketManager.createRocket("Falcon Heavy", 1420000, 28000, 3);
        rocketManager.createRocket("Saturn V", 2970000, 39000, 3);
        rocketManager.createRocket("Starship", 1200000, 27000, 2);
        
        // Create default targets
        targetManager.createTarget("Earth Orbit", 408);
        targetManager.createTarget("Moon", 384400);
        targetManager.createTarget("Mars", 54600000);
    }
    
    public void start() {
        displayWelcome();
        
        while (isRunning) {
            try {
                displayMenu();
                String command = scanner.nextLine().trim().toLowerCase();
                processCommand(command);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing command", e);
                System.out.println("An error occurred. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private void displayWelcome() {
        System.out.println("========================================");
        System.out.println("    ROCKET LAUNCH SIMULATOR v2.0");
        System.out.println("========================================");
    }
    
    private void displayMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. create_rocket - Create a new rocket");
        System.out.println("2. delete_rocket - Delete a rocket");
        System.out.println("3. list_rockets - Show all rockets");
        System.out.println("4. create_target - Create a new target");
        System.out.println("5. delete_target - Delete a target");
        System.out.println("6. list_targets - Show all targets");
        System.out.println("7. select_rocket - Select rocket for mission");
        System.out.println("8. select_target - Select target for mission");
        System.out.println("9. start_checks - Perform pre-launch checks");
        System.out.println("10. physics_info - Show orbital mechanics info");
        System.out.println("11. launch - Begin rocket launch");
        System.out.println("12. fast_forward X - Advance simulation by X seconds");
        System.out.println("13. status - Show current mission status");
        System.out.println("14. exit - Exit simulator");
        System.out.print("\nEnter command: ");
    }
    
    private void processCommand(String command) {
        String[] parts = command.split(" ");
        String action = parts[0];
        
        switch (action) {
            case "create_rocket":
                createRocketInteractive();
                break;
            case "delete_rocket":
                deleteRocketInteractive();
                break;
            case "list_rockets":
                rocketManager.listRockets();
                break;
            case "create_target":
                createTargetInteractive();
                break;
            case "delete_target":
                deleteTargetInteractive();
                break;
            case "list_targets":
                targetManager.listTargets();
                break;
            case "select_rocket":
                selectRocketInteractive();
                break;
            case "select_target":
                selectTargetInteractive();
                break;
            case "start_checks":
                performPreLaunchChecks();
                break;
            case "physics_info":
                showPhysicsInfo();
                break;
            case "launch":
                launchRocket();
                break;
            case "fast_forward":
                if (parts.length > 1) {
                    try {
                        int seconds = Integer.parseInt(parts[1]);
                        fastForward(seconds);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format for fast_forward command.");
                    }
                } else {
                    System.out.println("Usage: fast_forward X (where X is number of seconds)");
                }
                break;
            case "status":
                showStatus();
                break;
            case "exit":
                isRunning = false;
                System.out.println("Thank you for using Rocket Launch Simulator!");
                break;
            default:
                System.out.println("Unknown command. Please try again.");
        }
    }
    
    private void createRocketInteractive() {
        try {
            System.out.print("Enter rocket name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Enter fuel capacity (kg): ");
            double fuelCapacity = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Enter max speed (km/h): ");
            double maxSpeed = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Enter number of stages: ");
            int stages = Integer.parseInt(scanner.nextLine().trim());
            
            if (rocketManager.createRocket(name, fuelCapacity, maxSpeed, stages)) {
                System.out.println("Rocket '" + name + "' created successfully!");
            } else {
                System.out.println("Failed to create rocket. Name might already exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        }
    }
    
    private void deleteRocketInteractive() {
        rocketManager.listRockets();
        System.out.print("Enter rocket name to delete: ");
        String name = scanner.nextLine().trim();
        
        if (rocketManager.deleteRocket(name)) {
            System.out.println("Rocket '" + name + "' deleted successfully!");
        } else {
            System.out.println("Rocket not found.");
        }
    }
    
    private void createTargetInteractive() {
        try {
            System.out.print("Enter target name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Enter distance from Earth (km): ");
            double distance = Double.parseDouble(scanner.nextLine().trim());
            
            if (targetManager.createTarget(name, distance)) {
                System.out.println("Target '" + name + "' created successfully!");
            } else {
                System.out.println("Failed to create target. Name might already exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
    
    private void deleteTargetInteractive() {
        targetManager.listTargets();
        System.out.print("Enter target name to delete: ");
        String name = scanner.nextLine().trim();
        
        if (targetManager.deleteTarget(name)) {
            System.out.println("Target '" + name + "' deleted successfully!");
        } else {
            System.out.println("Target not found.");
        }
    }
    
    private void selectRocketInteractive() {
        rocketManager.listRockets();
        System.out.print("Enter rocket name to select: ");
        String name = scanner.nextLine().trim();
        
        Rocket rocket = rocketManager.getRocket(name);
        if (rocket != null) {
            launchController.setRocket(rocket);
            System.out.println("Rocket '" + name + "' selected for mission!");
        } else {
            System.out.println("Rocket not found.");
        }
    }
    
    private void selectTargetInteractive() {
        targetManager.listTargets();
        System.out.print("Enter target name to select: ");
        String name = scanner.nextLine().trim();
        
        Target target = targetManager.getTarget(name);
        if (target != null) {
            launchController.setTarget(target);
            System.out.println("Target '" + name + "' selected for mission!");
        } else {
            System.out.println("Target not found.");
        }
    }
    
    private void performPreLaunchChecks() {
        try {
            PreLaunchCheckResult result = launchController.performPreLaunchChecks();
            
            System.out.println("\n=== PRE-LAUNCH CHECKS ===");
            System.out.println("Rocket: " + (result.hasRocket() ? result.getRocketName() : "NOT SELECTED"));
            System.out.println("Target: " + (result.hasTarget() ? result.getTargetName() : "NOT SELECTED"));
            
            if (result.hasRocket() && result.hasTarget()) {
                System.out.println("Target Distance: " + result.getTargetDistance() + " km");
                System.out.println("Estimated Max Range: " + String.format("%.2f", result.getEstimatedRange()) + " km");
                
                // Display enhanced physics analysis if available
                if (result instanceof EnhancedPreLaunchCheckResult) {
                    EnhancedPreLaunchCheckResult enhancedResult = (EnhancedPreLaunchCheckResult) result;
                    System.out.println(enhancedResult.getPhysicsAnalysis());
                }
                
                if (result.isReachable()) {
                    System.out.println("✓ All systems are 'Go' for launch.");
                } else {
                    System.out.println("⚠ WARNING: Mission parameters indicate potential challenges!");
                    System.out.println("You may still proceed with launch to test rocket capabilities.");
                }
            } else {
                System.out.println("❌ ERROR: Both rocket and target must be selected before launch checks!");
            }
            
            System.out.println("========================");
        } catch (IllegalStateException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void showPhysicsInfo() {
        System.out.println("\n=== ORBITAL MECHANICS REFERENCE ===");
        
        // Earth escape velocity
        double escapeVelocity = PhysicsEngine.calculateEscapeVelocity(0);
        System.out.printf("Earth Escape Velocity (surface): %.2f km/s (%.0f km/h)\n", 
                         escapeVelocity, escapeVelocity * 3600);
        
        // LEO orbital velocity
        double leoVelocity = PhysicsEngine.calculateOrbitalVelocity(408);
        System.out.printf("LEO Orbital Velocity (408 km): %.2f km/s (%.0f km/h)\n", 
                         leoVelocity, leoVelocity * 3600);
        
        // Common orbital altitudes
        System.out.println("\nCommon Orbital Velocities:");
        double[] altitudes = {200, 408, 35786}; // LEO, ISS, GEO
        String[] names = {"Low Earth Orbit", "ISS Altitude", "Geostationary Orbit"};
        
        for (int i = 0; i < altitudes.length; i++) {
            double orbitalV = PhysicsEngine.calculateOrbitalVelocity(altitudes[i]);
            double escapeV = PhysicsEngine.calculateEscapeVelocity(altitudes[i]);
            System.out.printf("  %s (%.0f km): Orbital %.2f km/s, Escape %.2f km/s\n", 
                             names[i], altitudes[i], orbitalV, escapeV);
        }
        
        // Atmospheric info
        System.out.println("\nAtmospheric Information:");
        System.out.println("  Karman Line (space boundary): 100 km");
        System.out.println("  Significant atmosphere ends: ~200 km");
        System.out.println("  No atmospheric drag above: ~300 km");
        
        // Delta-V requirements
        System.out.println("\nTypical Delta-V Requirements:");
        System.out.println("  Earth surface to LEO: ~9.4 km/s");
        System.out.println("  LEO to Moon: ~3.2 km/s");
        System.out.println("  LEO to Mars transfer: ~3.6 km/s");
        
        System.out.println("\nKey Physics Concepts:");
        System.out.println("  • Escape velocity: Minimum speed to escape gravity well");
        System.out.println("  • Orbital velocity: Speed needed to maintain circular orbit");
        System.out.println("  • Delta-V: Total velocity change capability of rocket");
        System.out.println("  • Higher altitude = lower required orbital velocity");
        System.out.println("  • Atmospheric drag decreases exponentially with altitude");
        
        System.out.println("====================================");
    }
    
    private void launchRocket() {
        try {
            MissionResult result = launchController.launch();
            
            System.out.println("\n🚀 LAUNCHING ROCKET!");
            System.out.println("Mission in progress...");
            
            // Simulate real-time updates
            simulateLaunch();
            
        } catch (IllegalStateException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void simulateLaunch() {
        try {
            while (launchController.isInFlight()) {
                FlightStatus status = launchController.getFlightStatus();
                displayFlightStatus(status);
                
                if (status.hasStageSeparated()) {
                    System.out.println("🔥 Stage " + (status.getCurrentStage() - 1) + 
                                     " complete. Separating stage. Entering Stage " + 
                                     status.getCurrentStage() + ".");
                }
                
                Thread.sleep(1000); // 1-second delay
                launchController.updateSimulation(1);
            }
            
            // Final status
            MissionResult finalResult = launchController.getFinalResult();
            displayMissionResult(finalResult);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Simulation interrupted.");
        }
    }
    
    private void fastForward(int seconds) {
        if (!launchController.isInFlight()) {
            System.out.println("No active mission to fast forward.");
            return;
        }
        
        System.out.println("Fast forwarding " + seconds + " seconds...");
        launchController.updateSimulation(seconds);
        
        if (launchController.isInFlight()) {
            FlightStatus status = launchController.getFlightStatus();
            displayFlightStatus(status);
        } else {
            MissionResult result = launchController.getFinalResult();
            displayMissionResult(result);
        }
    }
    
    private void displayFlightStatus(FlightStatus status) {
        System.out.printf("Stage: %d, Fuel: %.1f%%, Altitude: %.1f km, Speed: %.0f km/h%n",
                          status.getCurrentStage(),
                          status.getFuelPercentage(),
                          status.getAltitude(),
                          status.getSpeed());
    }
    
    private void displayMissionResult(MissionResult result) {
        System.out.println("\n=== MISSION COMPLETE ===");
        System.out.println("Final Altitude: " + String.format("%.2f", result.getFinalAltitude()) + " km");
        System.out.println("Final Speed: " + String.format("%.0f", result.getFinalSpeed()) + " km/h");
        System.out.printf("Final Velocity: %.2f km/s\n", result.getFinalSpeed() / 3600.0);
        System.out.println("Mission Duration: " + result.getMissionDuration() + " seconds");
        
        // Enhanced orbital analysis if available
        if (result instanceof OrbitalMissionResult) {
            OrbitalMissionResult orbitalResult = (OrbitalMissionResult) result;
            System.out.println(orbitalResult.getDetailedAnalysis());
        }
        
        // Basic success/failure determination with orbital considerations
        boolean achievedOrbit = false;
        boolean achievedEscape = false;
        
        if (result instanceof OrbitalMissionResult) {
            OrbitalMissionResult orbitalResult = (OrbitalMissionResult) result;
            achievedOrbit = orbitalResult.achievedOrbit();
            achievedEscape = orbitalResult.achievedEscape();
        }
        
        if (result.isSuccessful()) {
            if (achievedEscape) {
                System.out.println("🚀 ESCAPE VELOCITY ACHIEVED! Mission Successful - Leaving Earth's gravity!");
            } else if (achievedOrbit) {
                System.out.println("🌍 ORBITAL VELOCITY ACHIEVED! Mission Successful - Stable orbit established!");
            } else {
                System.out.println("🎯 TARGET REACHED! Mission Successful!");
            }
        } else {
            System.out.println("💥 Mission Failed due to insufficient fuel/velocity.");
            if (result.getTargetDistance() > 0) {
                System.out.println("Distance to target: " + 
                                 String.format("%.2f", result.getDistanceToTarget()) + " km remaining");
            }
            
            // Provide specific failure reasons
            double finalVelocityKmS = result.getFinalSpeed() / 3600.0;
            double escapeVelocity = PhysicsEngine.calculateEscapeVelocity(result.getFinalAltitude());
            double orbitalVelocity = PhysicsEngine.calculateOrbitalVelocity(result.getFinalAltitude());
            
            if (finalVelocityKmS < escapeVelocity) {
                System.out.printf("⚠ Did not achieve escape velocity (needed %.2f km/s)\n", escapeVelocity);
            }
            if (finalVelocityKmS < orbitalVelocity) {
                System.out.printf("⚠ Did not achieve orbital velocity (needed %.2f km/s)\n", orbitalVelocity);
            }
        }
        
        System.out.println("========================");
    }
    
    private void showStatus() {
        System.out.println("\n=== CURRENT STATUS ===");
        
        Rocket currentRocket = launchController.getCurrentRocket();
        Target currentTarget = launchController.getCurrentTarget();
        
        System.out.println("Selected Rocket: " + (currentRocket != null ? currentRocket.getName() : "None"));
        System.out.println("Selected Target: " + (currentTarget != null ? currentTarget.getName() : "None"));
        System.out.println("Mission Status: " + launchController.getMissionState());
        
        if (launchController.isInFlight()) {
            FlightStatus status = launchController.getFlightStatus();
            displayFlightStatus(status);
        }
        
        System.out.println("======================");
    }
    
    public static void main(String[] args) {
        RocketLaunchSimulator simulator = new RocketLaunchSimulator();
        simulator.start();
    }
}