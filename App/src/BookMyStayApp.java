import java.util.*;

class BookMyStayApp {

    // Data Structures
    private List<Integer> rooms = new ArrayList<>();
    private Queue<String[]> bookingQueue = new LinkedList<>();
    private Set<Integer> bookedRooms = new HashSet<>();
    private Map<Integer, String> bookingMap = new HashMap<>();

    // Add rooms
    public void addRoom(int roomId) {
        rooms.add(roomId);
    }

    // Request booking (FIFO)
    public void requestBooking(String customerName, int roomId) {
        bookingQueue.add(new String[]{customerName, String.valueOf(roomId)});
        System.out.println("Request added: " + customerName + " -> Room " + roomId);
    }

    // Process bookings
    public void processBookings() {
        while (!bookingQueue.isEmpty()) {
            String[] request = bookingQueue.poll();
            String name = request[0];
            int roomId = Integer.parseInt(request[1]);

            if (!rooms.contains(roomId)) {
                System.out.println("Room " + roomId + " does not exist.");
                continue;
            }

            if (bookedRooms.contains(roomId)) {
                System.out.println("Room " + roomId + " already booked. Skipping " + name);
                continue;
            }

            // Confirm booking
            bookedRooms.add(roomId);
            bookingMap.put(roomId, name);

            System.out.println("Booking confirmed: " + name + " -> Room " + roomId);
        }
    }

    // Cancel booking
    public void cancelBooking(int roomId) {
        if (bookingMap.containsKey(roomId)) {
            bookingMap.remove(roomId);
            bookedRooms.remove(roomId);
            System.out.println("Booking cancelled for Room " + roomId);
        } else {
            System.out.println("No booking found for Room " + roomId);
        }
    }

    // Display rooms
    public void displayStatus() {
        System.out.println("\nRoom Status:");
        for (int roomId : rooms) {
            if (bookedRooms.contains(roomId)) {
                System.out.println("Room " + roomId + " - Booked by " + bookingMap.get(roomId));
            } else {
                System.out.println("Room " + roomId + " - Available");
            }
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        BookMyStayApp system = new BookMyStayApp();

        // Add rooms
        system.addRoom(101);
        system.addRoom(102);
        system.addRoom(103);

        // Booking requests
        system.requestBooking("Alice", 101);
        system.requestBooking("Bob", 101);   // Duplicate
        system.requestBooking("Charlie", 102);
        system.requestBooking("David", 104); // Invalid room

        // Process bookings
        system.processBookings();

        // Display status
        system.displayStatus();

        // Cancel booking
        system.cancelBooking(101);

        // Final status
        system.displayStatus();
    }
}