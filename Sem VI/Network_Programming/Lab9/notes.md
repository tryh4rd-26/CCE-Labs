Here’s the **full set of notes rewritten in your detailed, step‑by‑step narrative style**, with commands isolated in single backticks and examples shown inline.  

---

# Cisco Packet Tracer (Lab 8) Notes

## 1. Cabling Rules

| Type                   | Use                                                          |
| ---------------------- | ------------------------------------------------------------ |
| Crossover Cable        | Connect like devices (`PC ↔ PC`, `Router ↔ Router`, `PC ↔ Router`) |
| Straight-Through Cable | Connect unlike devices (`PC ↔ Switch`, `Router ↔ Switch`)        |

**Note:**  
- `PC → Router` is considered **like devices**, so use a **crossover cable**.  
- `Router → Router` usually requires a **crossover cable** or a **DCE module** (to provide clocking).  

---

## 2. CLI Basics

| Command / Symbol | Description                                           |
| ---------------- | ----------------------------------------------------- |
| `?`              | Displays list of available commands                   |
| `>`              | User mode prompt                                      |
| `enable`         | Switches from User Mode (`>`) to Privilege Mode (`#`) |
| `#`              | Privilege mode prompt                                 |
| `ctrl+6`         | Abort current command                                 |

---

## 3. Configuration Modes

| Mode               | Command                                                | Description                                                    |
| ------------------ | ------------------------------------------------------ | -------------------------------------------------------------- |
| Privilege Mode     | `config t`                                             | Enter Global Configuration Mode                                |
| Global Config Mode | `interface <interface>`                                | Enter interface configuration mode to set IP, clock rate, etc. |
| Saving Config      | `write memory` or `copy running-config startup-config` | Saves configuration so it persists after reboot                |

---

## 4. IP and Network Configuration

* **Router Interfaces**  
  - Each interface must have an IP address assigned.  
  - Connected devices must be in the same subnet as that interface.  

* **PC Default Gateway**  
  - Should be the IP of the router interface the PC is connected to.  

Example:  
```
interface GigabitEthernet0/0/1
ip address 172.16.10.1 255.255.0.0
no shutdown
```

---

## 5. Routing – RIP

* To configure RIP (exam will provide subnet mask only):  
  - Go to Global Config Mode:  
    `enable`  
    `config t`  
  - Start RIP process:  
    `router rip`  
  - Set version:  
    `version 2`  
  - Add networks (use **network IDs**, not host IPs):  
    `network <172.16.0.0>`  
    `network <192.168.1.0>`  

* Example:  
  - If router interface IP is `172.16.10.1 255.255.0.0`, then network ID is `172.16.0.0`.  

* Ensure PCs connected directly or via switch have their **default gateway** set to the router interface IP.  

---

## 6. Routing – OSPF

* Go to Global Config Mode:  
  `enable`  
  `config t`  

* Start OSPF process (process ID usually `1`):  
  `router ospf 1`  

* Add networks using **network ID + wildcard mask + area number**:  
  `network <192.16.18.0> <0.0.0.255> area <0>`  

* Example:  
  - If router interface IP is `192.16.18.2 255.255.255.0`:  
    - Network ID = `192.16.18.0`  
    - Wildcard mask = `0.0.0.255`  
    - Area = `0`  

---

**Mixed Routing:**  
- Half RIP / Half OSPF → configure redistribution on intermediate router.  
-> See MS Teams
---

## 7. Typical Scenarios

| Scenario           | Notes                                                                                |
| ------------------ | ------------------------------------------------------------------------------------ |
| `PC ↔ PC via Switch` | All PCs must be in the same subnet                                                   |
| `PC ↔ Router ↔ PC`   | PCs are in different subnets; router interfaces configured accordingly                 |
| `Router ↔ Router`    | Each router interface configured with IP & RIP; DCE provides clocking if serial link |

---

## 8. Important CLI Commands

- `show interface` → queuing strategy  
- `show ip route` → routing protocol table  
- `show ip rip database` → RIP database  
- `show ip ospf 1` → OSPF details  
- On PC: `ipconfig`, `tracert`  



---

## 9. Configure DNS

* Select an end device and make it a **server**.  
* On the server:  
  - Go to **Services → DNS**  
  - Toggle DNS Service ON  
  - Add an **A record** (IPv4):  
    - Name (URL) → `<www.example.com>`  
    - IP address → `<192.168.1.10>` (IP of connected PC/server)  

* On each PC:  
  - Configure DNS server IP to the server’s IP.  
  - Example: If DNS server IP is `192.168.1.5`, set that in PC’s DNS field.  

* Test:  
  - From any PC, run `ping <www.example.com>`  
  - If DNS is working, it resolves the name to the IP and pings successfully.  

---

## 10. Custom PDU

* FTP Example:  
  - TOS = 1  
  - Starting Port = 50000  
  - Size = 1.5 KB  

---

## 11. Final Notes

* Save configuration before shutting down routers:  
  `write memory`  
  or  
  `copy running-config startup-config`  

* Use **Simulation Mode** to visualize packet flow.  

