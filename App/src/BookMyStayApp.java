/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 7:
 * Add-On Service Selection for Reservations
 *
 * Focus:
 * - Attach multiple services to a reservation
 * - Use Map<String, List<Service>>
 * - Keep booking & inventory untouched
 *
 * @author Ronith
 * @version 7.0
 */

import java.util.*;

public class BookMyStayApp {

    // -------------------- INVENTORY --------------------
    static class RoomInventory {
        private Map<String, Integer> availabilityMap = new HashMap<>();

        public RoomInventory() {
            availabilityMap.put("Single Room", 2);
            availabilityMap.put("Double Room", 1);
            availabilityMap.put("Suite Room", 1);
        }

        public int getAvailability(String type) {
            return availabilityMap.getOrDefault(type, 0);
        }

        public void decrement(String type) {
            availabilityMap.put(type, getAvailability(type) - 1);
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

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
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

    // -------------------- BOOKING SERVICE --------------------
    static class BookingService {

        private RoomInventory inventory;

        private Set<String> allocatedRoomIds = new HashSet<>();
        private Map<String, String> reservationToRoomId = new HashMap<>();

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void processBookings(BookingQueue queue) {

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();
                String type = r.getRoomType();

                if (inventory.getAvailability(type) > 0) {

                    String roomId = generateRoomId(type);

                    while (allocatedRoomIds.contains(roomId)) {
                        roomId = generateRoomId(type);
                    }

                    allocatedRoomIds.add(roomId);

                    // Map reservation (guest) → roomId
                    reservationToRoomId.put(r.getGuestName(), roomId);

                    inventory.decrement(type);

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

        public Map<String, String> getReservationMap() {
            return reservationToRoomId;
        }
    }

    // -------------------- SERVICE DOMAIN --------------------
    static class Service {
        private String serviceName;
        private double cost;

        public Service(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        public String getServiceName() {
            return serviceName;
        }

        public double getCost() {
            return cost;
        }
    }

    // -------------------- ADD-ON SERVICE MANAGER --------------------
    static class AddOnServiceManager {

        // reservationId (roomId) -> list of services
        private Map<String, List<Service>> serviceMap = new HashMap<>();

        // Add service
        public void addService(String reservationId, Service service) {
            serviceMap
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);

            System.out.println("Added service: " + service.getServiceName() +
                    " to Reservation: " + reservationId);
        }

        // Calculate total cost
        public double calculateTotalCost(String reservationId) {
            double total = 0;

            List<Service> services = serviceMap.get(reservationId);

            if (services != null) {
                for (Service s : services) {
                    total += s.getCost();
                }
            }

            return total;
        }

        // Display services
        public void displayServices(String reservationId) {
            System.out.println("\nServices for Reservation: " + reservationId);

            List<Service> services = serviceMap.get(reservationId);

            if (services == null || services.isEmpty()) {
                System.out.println("No services added.");
                return;
            }

            for (Service s : services) {
                System.out.println("- " + s.getServiceName() + " (₹" + s.getCost() + ")");
            }

            System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
        }
    }

    // -------------------- MAIN --------------------
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Add-On Services Module");
        System.out.println("   Version: 7.0");
        System.out.println("======================================");

        // Setup booking system
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));

        BookingService bookingService = new BookingService(inventory);
        bookingService.processBookings(queue);

        // Get confirmed reservations
        Map<String, String> reservations = bookingService.getReservationMap();

        // Add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Sample services
        Service breakfast = new Service("Breakfast", 500);
        Service wifi = new Service("WiFi", 200);
        Service spa = new Service("Spa", 1500);

        // Attach services
        for (String guest : reservations.keySet()) {
            String reservationId = reservations.get(guest);

            serviceManager.addService(reservationId, breakfast);
            serviceManager.addService(reservationId, wifi);

            // Add extra service to one guest
            if (guest.equals("Alice")) {
                serviceManager.addService(reservationId, spa);
            }

            // Display services
            serviceManager.displayServices(reservationId);
        }

        System.out.println("\nAdd-on services processed successfully.");
    }
}