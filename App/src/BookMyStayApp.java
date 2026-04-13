/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 8:
 * Booking History & Reporting
 *
 * Focus:
 * - Store confirmed bookings
 * - Maintain order using List
 * - Generate reports without modifying data
 *
 * @author Ronith
 * @version 8.0
 */

import java.util.*;

public class BookMyStayApp {

    // -------------------- INVENTORY --------------------
    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 2);
            availability.put("Double Room", 1);
            availability.put("Suite Room", 1);
        }

        public int getAvailability(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void decrement(String type) {
            availability.put(type, getAvailability(type) - 1);
        }
    }

    // -------------------- RESERVATION --------------------
    static class Reservation {
        private String guestName;
        private String roomType;
        private String roomId;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public String getRoomId() { return roomId; }

        public void display() {
            System.out.println("Guest: " + guestName +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomId);
        }
    }

    // -------------------- QUEUE --------------------
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNext() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // -------------------- BOOKING HISTORY --------------------
    static class BookingHistory {
        private List<Reservation> history = new ArrayList<>();

        public void addReservation(Reservation r) {
            history.add(r); // preserves order
        }

        public List<Reservation> getAllReservations() {
            return history;
        }
    }

    // -------------------- BOOKING SERVICE --------------------
    static class BookingService {

        private RoomInventory inventory;
        private BookingHistory history;

        private Set<String> allocatedIds = new HashSet<>();

        public BookingService(RoomInventory inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
        }

        public void processBookings(BookingQueue queue) {

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();
                String type = r.getRoomType();

                if (inventory.getAvailability(type) > 0) {

                    String roomId = generateRoomId(type);

                    while (allocatedIds.contains(roomId)) {
                        roomId = generateRoomId(type);
                    }

                    allocatedIds.add(roomId);

                    r.setRoomId(roomId);

                    inventory.decrement(type);

                    // Store in history
                    history.addReservation(r);

                    System.out.println("CONFIRMED: " + r.getGuestName() +
                            " | Room ID: " + roomId);

                } else {
                    System.out.println("FAILED: " + r.getGuestName());
                }
            }
        }

        private String generateRoomId(String type) {
            String prefix = type.replace(" ", "").substring(0, 2).toUpperCase();
            return prefix + "-" + new Random().nextInt(1000);
        }
    }

    // -------------------- REPORT SERVICE --------------------
    static class BookingReportService {

        public void generateReport(BookingHistory history) {

            System.out.println("\n==== Booking History Report ====\n");

            List<Reservation> list = history.getAllReservations();

            if (list.isEmpty()) {
                System.out.println("No bookings found.");
                return;
            }

            Map<String, Integer> summary = new HashMap<>();

            for (Reservation r : list) {
                r.display();

                // Count per room type
                summary.put(
                        r.getRoomType(),
                        summary.getOrDefault(r.getRoomType(), 0) + 1
                );
            }

            System.out.println("\n---- Summary ----");
            for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                System.out.println(entry.getKey() + " Booked: " + entry.getValue());
            }
        }
    }

    // -------------------- MAIN --------------------
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Booking History Module");
        System.out.println("   Version: 8.0");
        System.out.println("======================================");

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingQueue queue = new BookingQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));

        // Process bookings
        BookingService bookingService = new BookingService(inventory, history);
        bookingService.processBookings(queue);

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);

        System.out.println("\nReporting completed successfully.");
    }
}