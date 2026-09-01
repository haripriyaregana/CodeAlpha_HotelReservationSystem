import java.util.*;
import java.io.*;

class Room {
    int roomNumber;
    String category;
    double price;
    boolean available;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.available = true;
    }
}

class Reservation {
    int id;
    String name;
    String phone;
    int roomNumber;
    String category;
    int nights;
    double total;

    Reservation(int id, String name, String phone, int roomNumber,
                String category, int nights, double total) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.category = category;
        this.nights = nights;
        this.total = total;
    }

    void display() {
        System.out.println("\n----- BOOKING DETAILS -----");
        System.out.println("Reservation ID : " + id);
        System.out.println("Customer Name  : " + name);
        System.out.println("Phone          : " + phone);
        System.out.println("Room Number    : " + roomNumber);
        System.out.println("Category       : " + category);
        System.out.println("Nights         : " + nights);
        System.out.println("Total Amount   : Rs." + total);
    }
}

public class HotelReservationSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    static int nextId = 1001;

    public static void main(String[] args) {

        addRooms();

        int choice;

        do {
            System.out.println("\n==============================");
            System.out.println("   HOTEL RESERVATION SYSTEM");
            System.out.println("==============================");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Booking Details");
            System.out.println("5. View All Rooms");
            System.out.println("6. Exit");
            System.out.println("==============================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    searchRooms();
                    break;

                case 2:
                    bookRoom();
                    break;

                case 3:
                    cancelReservation();
                    break;

                case 4:
                    viewBooking();
                    break;

                case 5:
                    viewAllRooms();
                    break;

                case 6:
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);
    }

    static void addRooms() {

        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1500));

        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(203, "Deluxe", 2500));

        rooms.add(new Room(301, "Suite", 4000));
        rooms.add(new Room(302, "Suite", 4000));
    }

    static void searchRooms() {

        System.out.println("\n1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");
        System.out.println("4. All");

        System.out.print("Choose category: ");
        int choice = sc.nextInt();

        String category = "";

        if (choice == 1) {
            category = "Standard";
        } else if (choice == 2) {
            category = "Deluxe";
        } else if (choice == 3) {
            category = "Suite";
        }

        boolean found = false;

        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {

            if (room.available &&
                (choice == 4 || room.category.equals(category))) {

                System.out.println(
                    "Room " + room.roomNumber +
                    " | " + room.category +
                    " | Rs." + room.price
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }

    static void bookRoom() {

        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {
            if (room.available) {
                System.out.println(
                    room.roomNumber + " | " +
                    room.category + " | Rs." +
                    room.price + " per night"
                );
            }
        }

        System.out.print("\nEnter room number: ");
        int roomNumber = sc.nextInt();
        sc.nextLine();

        Room selectedRoom = null;

        for (Room room : rooms) {
            if (room.roomNumber == roomNumber && room.available) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available!");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        System.out.print("Enter number of nights: ");
        int nights = sc.nextInt();

        double total = selectedRoom.price * nights;

        System.out.println("\n----- PAYMENT -----");
        System.out.println("Room Price : Rs." + selectedRoom.price);
        System.out.println("Nights     : " + nights);
        System.out.println("Total      : Rs." + total);

        System.out.print("Confirm payment? (yes/no): ");
        String payment = sc.next();

        if (payment.equalsIgnoreCase("yes")) {

            Reservation r = new Reservation(
                nextId,
                name,
                phone,
                roomNumber,
                selectedRoom.category,
                nights,
                total
            );

            reservations.add(r);
            selectedRoom.available = false;

            saveToFile(r);

            System.out.println("\nBooking successful!");
            System.out.println("Reservation ID: " + nextId);

            nextId++;

        } else {
            System.out.println("Payment cancelled.");
        }
    }

    static void cancelReservation() {

        System.out.print("Enter Reservation ID: ");
        int id = sc.nextInt();

        Reservation found = null;

        for (Reservation r : reservations) {
            if (r.id == id) {
                found = r;
                break;
            }
        }

        if (found == null) {
            System.out.println("Reservation not found!");
            return;
        }

        for (Room room : rooms) {
            if (room.roomNumber == found.roomNumber) {
                room.available = true;
                break;
            }
        }

        reservations.remove(found);

        System.out.println("Reservation cancelled successfully!");
    }

    static void viewBooking() {

        System.out.print("Enter Reservation ID: ");
        int id = sc.nextInt();

        boolean found = false;

        for (Reservation r : reservations) {

            if (r.id == id) {
                r.display();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Reservation not found!");
        }
    }

    static void viewAllRooms() {

        System.out.println("\n----- ALL ROOMS -----");

        for (Room room : rooms) {

            String status;

            if (room.available) {
                status = "Available";
            } else {
                status = "Booked";
            }

            System.out.println(
                room.roomNumber + " | " +
                room.category + " | Rs." +
                room.price + " | " +
                status
            );
        }
    }

    static void saveToFile(Reservation r) {

        try {

            FileWriter fw = new FileWriter(
                "bookings.txt",
                true
            );

            fw.write(
                r.id + "," +
                r.name + "," +
                r.phone + "," +
                r.roomNumber + "," +
                r.category + "," +
                r.nights + "," +
                r.total + "\n"
            );

            fw.close();

        } catch (IOException e) {

            System.out.println("Error saving booking.");
        }
    }
}