/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 5:
 * Booking Request Handling using Queue (FIFO)
 *
 * Focus:
 * - Accept booking requests
 * - Store them in arrival order
 * - No allocation or inventory updates
 *
 * @author Ronith
 * @version 5.0
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
            System.out.println("Single Room | Beds: 1 | Price: ₹2000");
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 3500.0);
        }

        public void displayDetails() {
            System.out.println("Double Room | Beds: 2 | Price: ₹3500");
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 6000.0);
        }

        public void displayDetails() {
            System.out.println("Suite Room | Beds: 3 | Price: ₹6000");
        }
    }

    // -------------------- INVENTORY --------------------
    static class RoomInventory {
        private Map<String, Integer> availabilityMap;

        public RoomInventory() {
            availabilityMap = new HashMap<>();
            availabilityMap.put("Single Room", 5);
            availabilityMap.put("Double Room", 3);
            availabilityMap.put("Suite Room", 2);
        }

        public int getAvailability(String roomType) {
            return availabilityMap.getOrDefault(roomType, 0);
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

        public void display() {
            System.out.println("Guest: " + guestName + " | Requested: " + roomType);
        }
    }

    // -------------------- BOOKING QUEUE --------------------
    static class BookingQueue {
        private Queue<Reservation> requestQueue;

        public BookingQueue() {
            requestQueue = new LinkedList<>();
        }

        // Add request (enqueue)
        public void addRequest(Reservation reservation) {
            requestQueue.offer(reservation);
            System.out.println("Request added for " + reservation.getGuestName());
        }

        // Display queue
        public void displayQueue() {
            System.out.println("\n---- Booking Request Queue (FIFO) ----");

            if (requestQueue.isEmpty()) {
                System.out.println("No pending requests.");
                return;
            }

            for (Reservation r : requestQueue) {
                r.display();
            }
        }

        // Peek next request (no removal)
        public Reservation peekNext() {
            return requestQueue.peek();
        }
    }

    // -------------------- MAIN --------------------
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Booking Request Module");
        System.out.println("   Version: 5.0");
        System.out.println("======================================\n");

        // Initialize inventory (read-only here)
        RoomInventory inventory = new RoomInventory();

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("David", "Single Room"));

        // Display queue
        bookingQueue.displayQueue();

        // Show next request (FIFO check)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNext();
        if (next != null) {
            next.display();
        }

        System.out.println("\nNOTE: No rooms allocated yet. Inventory remains unchanged.");
    }
}