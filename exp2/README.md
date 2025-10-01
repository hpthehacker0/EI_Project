# 🚀 Rocket Launch Simulator

This project implements a console-based **Rocket Launch Simulator** designed to model the process of selecting a rocket and a target, performing pre-launch checks based on orbital mechanics, and simulating a realistic, physics-driven flight to the destination. The focus is on demonstrating strong logic, code quality, and the application of classical object-oriented programming (OOP) principles and design patterns.

-----

## Use Case Overview

The simulator allows a user to manage an inventory of rockets and space targets. For a given mission, the user selects a **Rocket** and a **Target**. Before launch, the system performs an **Enhanced Pre-Launch Check** that uses a `PhysicsEngine` to calculate $\Delta v$ (change in velocity) requirements for the mission and predicts the rocket's capability to achieve orbit or escape trajectory. Once launched, the mission enters a time-based simulation (`IN_FLIGHT` state) where the rocket's altitude, speed, and fuel are updated every second, incorporating forces like gravity and atmospheric drag. The simulation tracks stage separation and concludes when the fuel runs out or the target altitude is reached, providing a detailed **Orbital Mission Result**.

-----

## Functionalities

  * **Rocket Management**: Create, delete, list, and select rockets with customizable parameters (fuel capacity, max speed, stages).
  * **Target Management**: Create, delete, list, and select targets, specifying their distance from Earth.
  * **Enhanced Pre-Launch Checks**:
      * Verifies that both a rocket and a target are selected.
      * Estimates the rocket's maximum achievable velocity ($\Delta v$).
      * Calculates the required $\Delta v$ and orbital/escape velocities for the target.
      * Determines if the mission is *Reachable*, *Orbital*, or *Escape*-capable.
  * **Realistic Flight Simulation**:
      * **Time-Based Update**: Advances the rocket state in discrete time steps (seconds).
      * **Physics Modeling**: Calculates thrust, mass, gravitational acceleration, and atmospheric drag to determine net acceleration and updates to speed and altitude.
      * **Staging**: Automatically attempts stage separation based on internal fuel thresholds.
  * **Mission Reporting**: Provides real-time flight status and a final, detailed `OrbitalMissionResult` that includes maximum altitude, final velocity in $\text{km/s}$, and the final orbital status (Suborbital, Stable Orbit, Escape Trajectory).

-----

## 🧩 Design Patterns Used

This project utilizes several design patterns to manage complexity and ensure modularity:

1.  **Repository Pattern**

      * **Purpose**: Decouples the application logic from the data access logic (in-memory storage).
      * **Relation to Use Case**: Manages the persistent (in-memory `HashMap`) collection of `Rocket` and `Target` objects.
      * **Classes Involved**: `RocketManager`, `TargetManager`.

2.  **Command Pattern** (Implemented via `LaunchController` methods)

      * **Purpose**: Encapsulates a request (a method call) as an object or simply a defined, decoupled action.
      * **Relation to Use Case**: The `LaunchController` acts as the invoker, and methods like `launch()` and `updateSimulation()` are the concrete commands issued by the `RocketLaunchSimulator` (Client).
      * **Classes Involved**: `LaunchController`, `RocketLaunchSimulator` (Client).

3.  **State Pattern**

      * **Purpose**: Allows an object to alter its behavior when its internal state changes. The object appears to change its class.
      * **Relation to Use Case**: The **Mission State** (`IDLE`, `IN_FLIGHT`, `COMPLETED`) governs which actions are valid (e.g., cannot `setRocket` while `IN_FLIGHT`).
      * **Classes Involved**: `LaunchController.MissionState` enum, `LaunchController` (Context).

4.  **Builder Pattern** (Implicit in `Rocket` and Result objects)

      * **Purpose**: Separates the construction of a complex object from its representation, allowing the same construction process to create different representations.
      * **Relation to Use Case**: Although a formal `Builder` class isn't used, the `Rocket` and result classes (`PreLaunchCheckResult`, `MissionResult`) are designed as complex, **immutable** Value Objects, with all properties passed in a single, comprehensive constructor.

5.  **Strategy Pattern** (Implicit in `PhysicsEngine`)

      * **Purpose**: Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
      * **Relation to Use Case**: The `PhysicsEngine` provides various interchangeable calculation strategies (e.g., `calculateEscapeVelocity`, `calculateOrbitalVelocity`) that the `Rocket` and `LaunchController` can call based on the mission context.

-----

## 🗂️ Classes and Their Responsibilities

| Class Name | Responsibility | Design Role |
| :--- | :--- | :--- |
| `RocketLaunchSimulator` | Application entry point. Handles user input, menu display, and orchestrates the other managers/controllers. | Client/View |
| `Rocket` | Stores rocket specifications and its mutable flight state (fuel, speed, altitude). Contains the core `update()` logic for flight dynamics and staging. | Model/Entity |
| `Target` | Stores target name and distance. Calculates difficulty and formatted distance. | Model/Value Object |
| `RocketManager` | Manages the inventory of `Rocket` objects (creation, retrieval, deletion). | Repository |
| `TargetManager` | Manages the inventory of `Target` objects. | Repository |
| `LaunchController` | **Central orchestrator**. Manages mission state, sets up rocket/target, executes pre-launch checks, and runs the time-based simulation. | Controller/State Machine |
| `PhysicsEngine` | Provides all constants and static methods for orbital mechanics, gravitation, atmospheric effects, and $\Delta v$ analysis. | Service/Utility |
| `EnhancedPreLaunchCheckResult` | Immutable result of pre-launch analysis, including reachability status and detailed $\Delta v$ deficits. | Value Object |
| `OrbitalMissionResult` | Immutable result of a completed mission, with enhanced orbital status (e.g., escape achieved) and final flight statistics. | Value Object |
| `FlightStatus` | Snapshot of the rocket's current state (altitude, speed, fuel) during the `IN_FLIGHT` state. | Value Object |

-----
## 📐 SOLID Principles Applied

The code structure adheres to the SOLID principles, promoting maintainable, scalable, and understandable code:

| Principle | Description | Application in Project |
| :--- | :--- | :--- |
| **S**ingle **R**esponsibility | A class should have only one reason to change. | **`Rocket`** handles *flight dynamics*. **`PhysicsEngine`** handles *astrodynamics calculations*. **`RocketManager`** handles *data persistence/inventory*. |
| **O**pen/**C**losed | Software entities should be open for extension but closed for modification. | The result objects, like `PreLaunchCheckResult`, are extended by **`EnhancedPreLaunchCheckResult`** and **`OrbitalMissionResult`** to add new features (orbital analysis) without changing the base classes. |
| **L**iskov **S**ubstitution | Subtypes must be substitutable for their base types. | `EnhancedPreLaunchCheckResult` and `OrbitalMissionResult` can be used wherever their base classes (`PreLaunchCheckResult` and `MissionResult`) are expected, such as in the `LaunchController`'s return types. |
| **I**nterface **S**egregation | Clients shouldn't be forced to depend on methods they don't use. | *Applied conceptually*: Classes are small and focused. For instance, **`FlightStatus`** only exposes the data needed for runtime monitoring, separating it from the full `Rocket` interface. |
| **D**ependency **I**nversion | Depend on abstractions, not concretions. | `LaunchController` depends on the public interfaces of `Rocket` and `Target` (via methods like `calculateEstimatedRange()` and `getDistanceFromEarth()`), rather than the internal implementation of the data managers. The managers themselves rely on the concrete `Rocket` and `Target` objects. |

-----

## 🛠️ Compiling and Executing

### Tech Stack

  * **Language**: Java (Utilizes standard Java libraries)
  * **Environment**: Console/Terminal application (No fancy UI)
  * **Logging**: `java.util.logging` for internal operation logs.

### Execution

1.  **Compile**: Ensure all `.java` files are in the same directory and compile them using a Java compiler:
    ```bash
    javac *.java
    ```
2.  **Execute**: Run the main class:
    ```bash
    java RocketLaunchSimulator
    ```
3.  **Interact**: The application will start in the console, prompting for commands from the main menu.

-----

## Sample Output

The initial setup creates default rockets and targets (Falcon Heavy, Saturn V, Starship; Earth Orbit, Moon, Mars).

### 1\. Pre-Launch Checks (`start_checks`)

```
Enter command: select_rocket Falcon Heavy
Rocket 'Falcon Heavy' selected for mission!

Enter command: select_target Moon
Target 'Moon' selected for mission!

Enter command: start_checks

=== PRE-LAUNCH CHECKS ===
Rocket: Falcon Heavy
Target: Moon
Target Distance: 384400.0 km
Estimated Max Range: 673428.57 km

=== ORBITAL MECHANICS ANALYSIS ===
Earth Escape Velocity: 11.18 km/s (40248 km/h)
Required Delta-V: 11.75 km/s
Achievable Delta-V: 12.04 km/s
Target Orbital Velocity: 1.02 km/s

=== MISSION CAPABILITY ===
Can Escape Earth: ✓ YES
Can Reach Target: ✓ YES
Can Orbit Target: ✓ YES
===================================
✓ All systems are 'Go' for launch.
========================
```

### 2\. Launch and Fast Forward (`launch`, `fast_forward`)

```
Enter command: launch

🚀 LAUNCHING ROCKET!
Mission in progress...
Stage: 1, Fuel: 99.8%, Altitude: 0.1 km, Speed: 180 km/h
Stage: 1, Fuel: 99.6%, Altitude: 0.4 km, Speed: 360 km/h
...
Stage: 1, Fuel: 50.0%, Altitude: 500.0 km, Speed: 12000 km/h
🔥 Stage 1 complete. Separating stage. Entering Stage 2.
...

Enter command: fast_forward 3000
Fast forwarding 3000 seconds...
Stage: 3, Fuel: 98.7%, Altitude: 350000.0 km, Speed: 36720 km/h

Enter command: fast_forward 500

=== MISSION COMPLETE ===
Final Altitude: 384400.00 km
Final Speed: 36800 km/h
Final Velocity: 10.22 km/s
Mission Duration: 4015 seconds (66.9 minutes)

=== DETAILED MISSION ANALYSIS ===
Final Velocity: 10.22 km/s (36800 km/h)
Maximum Altitude: 384400.00 km
Mission Duration: 4015 seconds (66.9 minutes)
Orbital Status: Escape Trajectory - Leaving Earth's gravity well
Required Orbital Velocity: 1.02 km/s
Required Escape Velocity: 1.44 km/s
🚀 ESCAPE ACHIEVED - Rocket has left Earth's gravity well!
🎯 TARGET REACHED - Mission successful!
=================================
🚀 ESCAPE VELOCITY ACHIEVED! Mission Successful - Leaving Earth's gravity!
========================
```

-----

## Contact Info

For questions, issues, or collaboration, please contact:

> **[Your Name/Contact]**
> *E-mail:* `[Your Email Address]`
> *GitHub:* `[Your GitHub Profile URL]`



