
import java.util.List;
import java.util.Scanner;

public class CineApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        MovieDAO movieDAO = new MovieDAO();
        BookingManager manager = new BookingManager();
        BookingService service = new BookingService(manager);
        BookingDAO bookingDAO = new BookingDAO();
        ShowSeatDAO seatDAO = new ShowSeatDAO();

        List<Show> shows = movieDAO.getAllShows();

        if (shows.isEmpty()) {
            System.out.println("No movies found in database.");
            sc.close();
            return;
        }

        for (Show show : shows) {
            seatDAO.loadSeats(show);
        }

        manager.loadBookings(bookingDAO.getAllBookings(shows));

        Theatre theatre = new Theatre("CineBook Theatre");

        for (Show show : shows) {
            theatre.addShow(show);
        }

        boolean running = true;

        while (running) {
            System.out.println("\n===== CINEBOOK =====");
            System.out.println("1. View Movies");
            System.out.println("2. View Seats");
            System.out.println("3. Book Tickets");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View All Bookings");
            System.out.println("6. Delete Cancelled Booking");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            switch (choice) {

                case 1:
                    theatre.displayShows();
                    break;

                case 2: {
                    Show show = selectShow(sc, theatre);

                    if (show != null) {
                        show.displaySeats();
                    }
                    break;
                }

                case 3: {
                    Show show = selectShow(sc, theatre);

                    if (show == null) {
                        break;
                    }

                    show.displaySeats();

                    System.out.print("Enter seats (e.g. C3,C4): ");
                    String[] seats = sc.nextLine().split(",");

                    System.out.print("Customer name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Coupon code (Enter to skip): ");
                    String coupon = sc.nextLine().trim();

                    Booking booking = service.bookSeats(
                            show, name, seats, coupon);

                    if (booking != null) {
                        System.out.println("\nBooking ID : "
                                + booking.getBookingCode());

                        System.out.println("Seats      : "
                                + String.join(", ", booking.getSeats()));

                        System.out.printf("Total      : Rs. %.2f%n",
                                booking.getTotalAmount());
                    }
                    break;
                }

                case 4: {
                    System.out.print("Enter Booking ID (e.g. BK1001): ");
                    String code = sc.nextLine().trim().toUpperCase();

                    if (code.startsWith("BK")) {
                        code = code.substring(2);
                    }

                    try {
                        service.cancelBooking(Integer.parseInt(code));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid booking ID.");
                    }
                    break;
                }

                case 5:
                    manager.displayBookings();
                    break;

                case 6: {
                    System.out.print("Enter Booking ID to delete: ");
                    String code = sc.nextLine().trim().toUpperCase();

                    if (code.startsWith("BK")) {
                        code = code.substring(2);
                    }

                    try {
                        int deleteId = Integer.parseInt(code);

                        if (bookingDAO.deleteBooking(deleteId)) {
                            manager.removeBooking(deleteId);
                            System.out.println("Booking deleted successfully!");
                        } else {
                            System.out.println(
                                    "Delete failed. Only cancelled bookings can be deleted.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid booking ID.");
                    }
                    break;
                }

                case 0:
                    running = false;
                    System.out.println("Thank you for using CineBook!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    private static Show selectShow(Scanner sc, Theatre theatre) {
        theatre.displayShows();

        System.out.print("Enter Movie ID: ");
        Show show = theatre.findShow(readInt(sc));

        if (show == null) {
            System.out.println("Invalid Movie ID.");
        }

        return show;
    }

    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
