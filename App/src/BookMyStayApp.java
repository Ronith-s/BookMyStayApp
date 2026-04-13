/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 4:
 * Room Search & Availability Check (Read-Only Operation)
 *
 * Focus:
 * - Read-only access to inventory
 * - Filtering available rooms
 * - Separation of search logic from inventory updates
 *
 * @author Ronith
 * @version 4.0
 */

import java.util.*;

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

    // -------------------- INVENTORY --------------------
    static class RoomInventory {

        private Map<String, Integer> availabilityMap;

        public RoomInventory() {
            availabilityMap = new HashMap<>();
            availabilityMap.put("Single Room", 5);
            availabilityMap.put("Double Room", 3);
            availabilityMap.put("Suite Room", 0); // intentionally 0 to test filtering
        }

        // Read-only access
        public int getAvailability(String roomType) {
            return availabilityMap.getOrDefault(roomType, 0);
        }

        public Map<String, Integer> getAllAvailability() {
            return availabilityMap;
        }
    }

    // -------------------- SEARCH SERVICE --------------------
    static class RoomSearchService {

        public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {

            System.out.println("---- Available Rooms ----\n");

            boolean found = false;

            for (Room room : rooms) {
                int available = inventory.getAvailability(room.getRoomType());

                // Validation: only show available rooms
                if (available > 0) {
                    room.displayDetails();
                    System.out.println("Available: " + available + "\n");
                    found = true;
                }
            }

            // Defensive check
            if (!found) {
                System.out.println("No rooms available at the moment.");
            }
        }
    }

    // -------------------- MAIN --------------------
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Room Search Module");
        System.out.println("   Version: 4.0");
        System.out.println("======================================\n");

        // Initialize rooms
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Search service (read-only)
        RoomSearchService searchService = new RoomSearchService();

        // Perform search
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println("\nSearch completed. No changes made to inventory.");
    }
}