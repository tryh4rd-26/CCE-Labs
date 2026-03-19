# Cisco Packet Tracer (Lab 8) Notes

## 1. Cabling Rules

| Type                   | Use                                                          |
| ---------------------- | ------------------------------------------------------------ |
| Crossover Cable        | Connect like devices (PC ↔ PC, Router ↔ Router, PC ↔ Router) |
| Straight-Through Cable | Connect unlike devices (PC ↔ Switch, Router ↔ Switch)        |

Note: PC → Router is considered **like devices**, so use **crossover cable**.
Router → Router usually requires **crossover cable** or **DCE module** (to provide clocking).

## 2. CLI Basics

| Command / Symbol | Description                                           |
| ---------------- | ----------------------------------------------------- |
| '?'              | Displays list of available commands                   |
| '>'              | User mode prompt                                      |
| 'enable'         | Switches from User Mode ('>') to Privilege Mode ('#') |
| '#'              | Privilege mode prompt                                 |
| 'ctrl+6'         | Abort current command                                 |

## 3. Configuration Modes

| Mode               | Command                                                | Description                                                    |
| ------------------ | ------------------------------------------------------ | -------------------------------------------------------------- |
| Privilege Mode     | 'config t'                                             | Enter Global Configuration Mode                                |
| Global Config Mode | 'interface <interface>'                                | Enter interface configuration mode to set IP, clock rate, etc. |
| Saving Config      | 'write memory' or 'copy running-config startup-config' | Saves configuration so it persists after reboot                |

## 4. IP and Network Configuration

* Router Interfaces

  * Each interface must have an IP address assigned.
  * Connected devices must be in the same subnet as that interface.
* PC Default Gateway

  * Should be the IP of the Router interface the PC is connected to.

## 5. Routing

* RIP Configuration

  * Each router interface should be configured in RIP with its network ID.
  * Ensures all routers can communicate with each other.

## 6. Typical Scenarios

| Scenario           | Notes                                                                                |
| ------------------ | ------------------------------------------------------------------------------------ |
| PC ↔ PC via Switch | All PCs must be in the same subnet                                                   |
| PC ↔ Router ↔ PC   | PCs have different subnets; router interfaces configured accordingly                 |
| Router ↔ Router    | Each router interface configured with IP & RIP; DCE provides clocking if serial link |

## Note

* Save config before turning off routers: 'write memory' or 'copy running-config startup-config'
* Simulation mode to visualize
