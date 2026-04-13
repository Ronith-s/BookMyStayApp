import java.util.HashMap;
import java.util.Map;

// Main Class
public class BookMyStayApp {

    // Custom Exception
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    // Room Inventory
    private static Map<String, Integer> rooms = new HashMap<>();

    // Initialize inventory
    static {
        rooms.put("STANDARD", 5);
        rooms.put("DELUXE", 3);
        rooms.put("SUITE", 2);
    }

    // Validate Room Type
    public static void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Validate Booking Count & Availability
    public static void validateAvailability(String roomType, int count) throws InvalidBookingException {
        if (count <= 0) {
            throw new InvalidBookingException("Booking count must be greater than 0");
        }

        int available = rooms.get(roomType);
        if (available < count) {
            throw new InvalidBookingException("Not enough rooms available. Requested: "
                    + count + ", Available: " + available);
        }
    }

    // Booking Method (Fail-Fast + Safe Update)
    public static void bookRoom(String roomType, int count) throws InvalidBookingException {
        validateRoomType(roomType);           // Fail-fast
        validateAvailability(roomType, count);

        int updated = rooms.get(roomType) - count;

        // Guard system state
        if (updated < 0) {
            throw new InvalidBookingException("Inventory cannot go negative!");
        }

        rooms.put(roomType, updated);
    }

    // Process Booking (Graceful Handling)
    public static void processBooking(String roomType, int count) {
        try {
            bookRoom(roomType, count);
            System.out.println("Booking successful for " + count + " " + roomType + " room(s).");
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    // Display Inventory
    public static void displayInventory() {
        System.out.println("\nCurrent Room Availability:");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Main Method
    public static void main(String[] args) {

        displayInventory();
        System.out.println("------------------------");

        // Test cases
        processBooking("STANDARD", 2);   // valid
        processBooking("PREMIUM", 1);    // invalid room
        processBooking("DELUXE", -1);    // invalid count
        processBooking("SUITE", 5);      // overbooking

        System.out.println("------------------------");
        displayInventory();
    }
}