/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 6:
 * Reservation Confirmation & Room Allocation
 *
 * Focus:
 * - FIFO request processing
 * - Unique room ID assignment
 * - Prevent double booking using Set
 * - Inventory synchronization
 *
 * @author Ronith
 * @version 6.0
 */

import java.util.*;

public class BookMyStayApp {

    // -------------------- ROOM DOMAIN --------------------
    static abstract class Room {
        private String roomType;

        public Room(String roomType) {
            this.roomType = roomType;
        }

        public String getRoomType() {
            return roomType;
        }
    }

    static class SingleRoom extends Room {
        public SingleRoom() { super("Single Room"); }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double Room"); }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite Room"); }
    }

    // -------------------- INVENTORY --------------------
    static class RoomInventory {
        private Map<String, Integer> availabilityMap;

        public RoomInventory() {
            availabilityMap = new HashMap<>();
            availabilityMap.put("Single Room", 2);
            availabilityMap.put("Double Room", 1);
            availabilityMap.put("Suite Room", 1);
        }

        public int getAvailability(String roomType) {
            return availabilityMap.getOrDefault(roomType, 0);
        }

        public void decrement(String roomType) {
            availabilityMap.put(roomType, getAvailability(roomType) - 1);
        }

        public void displayInventory() {
            System.out.println("\n---- Current Inventory ----");
            for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    // -------------------- RESERVATION --------------------
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }
    }

    // -------------------- BOOKING QUEUE --------------------
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNext() {
            return queue.poll(); // FIFO removal
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // -------------------- BOOKING SERVICE --------------------
    static class BookingService {

        private RoomInventory inventory;

        // Track allocated room IDs
        private Set<String> allocatedRoomIds = new HashSet<>();

        // Map roomType -> allocated IDs
        private Map<String, Set<String>> allocationMap = new HashMap<>();

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void processBookings(BookingQueue queue) {

            System.out.println("\n---- Processing Booking Requests ----");

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();
                String type = r.getRoomType();

                System.out.println("\nProcessing: " + r.getGuestName() + " -> " + type);

                // Check availability
                if (inventory.getAvailability(type) > 0) {

                    // Generate unique room ID
                    String roomId = generateRoomId(type);

                    // Ensure uniqueness (extra safety)
                    while (allocatedRoomIds.contains(roomId)) {
                        roomId = generateRoomId(type);
                    }

                    // Record allocation
                    allocatedRoomIds.add(roomId);

                    allocationMap
                            .computeIfAbsent(type, k -> new HashSet<>())
                            .add(roomId);

                    // Update inventory (IMPORTANT: immediate)
                    inventory.decrement(type);

                    // Confirm booking
                    System.out.println("Booking CONFIRMED for " + r.getGuestName());
                    System.out.println("Assigned Room ID: " + roomId);

                } else {
                    // Reject booking
                    System.out.println("Booking FAILED for " + r.getGuestName() + " (No availability)");
                }
            }
        }

        // Generate room ID (simple unique format)
        private String generateRoomId(String roomType) {
            String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
            int random = new Random().nextInt(1000);
            return prefix + "-" + random;
        }

        public void displayAllocations() {
            System.out.println("\n---- Allocated Rooms ----");

            for (Map.Entry<String, Set<String>> entry : allocationMap.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    // -------------------- MAIN --------------------
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Booking Allocation Module");
        System.out.println("   Version: 6.0");
        System.out.println("======================================");

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Add requests (more than capacity to test rejection)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));
        queue.addRequest(new Reservation("Eve", "Suite Room"));
        queue.addRequest(new Reservation("Frank", "Suite Room")); // should fail

        // Process bookings
        BookingService service = new BookingService(inventory);
        service.processBookings(queue);

        // Show results
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll requests processed.");
    }
}