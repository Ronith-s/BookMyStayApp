/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 3:
 * Centralized Room Inventory Management using HashMap.
 *
 * Focus:
 * - Replace scattered variables with a single source of truth
 * - Encapsulate inventory logic
 * - Enable scalable room management
 *
 * @author Ronith
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    // -------------------- ROOM DOMAIN --------------------
    static abstract class Room {
        private String roomType;
        private int numberOfBeds;
        private double pricePerNight;

        public Room(String roomType, int numberOfBeds, double pricePerNight) {
            this.roomType = roomType;
            this.numberOfBeds = numberOfBeds;
            this.pricePerNight = pricePerNight;
        }

        public String getRoomType() {
            return roomType;
        }

        public int getNumberOfBeds() {
            return numberOfBeds;
        }

        public double getPricePerNight() {
            return pricePerNight;
        }

        public abstract void displayDetails();
    }

    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 2000.0);
        }

        public void displayDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getNumberOfBeds());
            System.out.println("Price: ₹" + getPricePerNight());
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 3500.0);
        }

        public void displayDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getNumberOfBeds());
            System.out.println("Price: ₹" + getPricePerNight());
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 6000.0);
        }

        public void displayDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getNumberOfBeds());
            System.out.println("Price: ₹" + getPricePerNight());
        }
    }

    // -------------------- INVENTORY MANAGEMENT --------------------
    static class RoomInventory {

        private Map<String, Integer> availabilityMap;

        // Constructor initializes inventory
        public RoomInventory() {
            availabilityMap = new HashMap<>();

            availabilityMap.put("Single Room", 5);
            availabilityMap.put("Double Room", 3);
            availabilityMap.put("Suite Room", 2);
        }

        // Get availability
        public int getAvailability(String roomType) {
            return availabilityMap.getOrDefault(roomType, 0);
        }

        // Update availability (controlled)
        public void updateAvailability(String roomType, int newCount) {
            if (availabilityMap.containsKey(roomType)) {
                availabilityMap.put(roomType, newCount);
            } else {
                System.out.println("Room type not found!");
            }
        }

        // Display full inventory
        public void displayInventory() {
            System.out.println("---- Current Room Inventory ----");
            for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
                System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
            }
        }
    }

    // -------------------- MAIN METHOD --------------------
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Inventory Management Module");
        System.out.println("   Version: 3.1");
        System.out.println("======================================\n");

        // Room objects (domain)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Inventory object (state manager)
        RoomInventory inventory = new RoomInventory();

        // Display room details + availability
        System.out.println("---- Room Details ----\n");

        single.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getRoomType()) + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getRoomType()) + "\n");

        suite.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getRoomType()) + "\n");

        // Show full inventory
        inventory.displayInventory();

        // Simulate update
        System.out.println("\nUpdating Single Room availability to 4...\n");
        inventory.updateAvailability("Single Room", 4);

        inventory.displayInventory();

        System.out.println("\nApplication executed successfully.");
    }
}