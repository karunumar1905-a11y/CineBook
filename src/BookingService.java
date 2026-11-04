
import java.util.ArrayList;
import java.util.List;

public class BookingService implements Bookable {

    private BookingManager bookingManager;
    private BookingDAO bookingDAO;
    private ShowSeatDAO showSeatDAO;

    public BookingService(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
        this.bookingDAO = new BookingDAO();
        this.showSeatDAO = new ShowSeatDAO();
    }

    public double calculateTotal(Show show, String[] seats) {
        double total = 0;

        for (String seat : seats) {
            total += show.getSeatPrice(seat);
        }

        return total;
    }

    public double calculateTotal(Show show, String[] seats, String coupon) {
        double total = calculateTotal(show, seats);

        if (coupon != null && coupon.equalsIgnoreCase("CINE10")) {
            total *= 0.90;
        }

        return total;
    }

    @Override
    public Booking bookSeats(Show show, String customerName,
                             String[] seats, String coupon) {

        if (show == null || customerName == null ||
                customerName.trim().isEmpty() ||
                seats == null || seats.length == 0) {
            System.out.println("Invalid booking details.");
            return null;
        }

        List<String> uniqueSeats = new ArrayList<>();

        for (String seat : seats) {
            if (seat == null) {
                System.out.println("Invalid seat.");
                return null;
            }

            seat = seat.trim().toUpperCase();

            if (!seat.matches("[A-E][1-8]") ||
                    uniqueSeats.contains(seat) ||
                    !show.isSeatAvailable(seat)) {
                System.out.println("Invalid, duplicate, or booked seat: " + seat);
                return null;
            }

            uniqueSeats.add(seat);
        }

        String[] bookedSeats = uniqueSeats.toArray(new String[0]);

        double total = calculateTotal(show, bookedSeats, coupon);

        if (!showSeatDAO.bookSeats(show, bookedSeats)) {
            System.out.println("Unable to book seats in database.");
            return null;
        }

        Booking booking = new Booking(
                customerName.trim(),
                show,
                bookedSeats,
                total
        );

        int databaseId = bookingDAO.saveBooking(booking);

        if (databaseId == -1) {
            showSeatDAO.releaseSeats(show, bookedSeats);
            System.out.println("Booking failed. Please try again.");
            return null;
        }

        booking.setBookingId(databaseId);

        for (String seat : bookedSeats) {
            show.bookSeat(seat);
        }

        bookingManager.addBooking(booking);

        System.out.println("Booking successful!");

        return booking;
    }

    @Override
    public double cancelBooking(int bookingId) {
        Booking booking = bookingManager.findBooking(bookingId);

        if (booking == null || booking.isCancelled()) {
            System.out.println("Booking not found or already cancelled.");
            return 0;
        }

        if (!bookingDAO.cancelBooking(bookingId)) {
            System.out.println("Could not update cancellation in database.");
            return 0;
        }

        booking.setCancelled(true);

        showSeatDAO.releaseSeats(
                booking.getShow(),
                booking.getSeats()
        );

        for (String seat : booking.getSeats()) {
            booking.getShow().releaseSeat(seat);
        }

        double refund = booking.getTotalAmount() * 0.80;

        System.out.printf(
                "Cancellation successful! Refund: Rs. %.2f%n",
                refund
        );

        return refund;
    }
}
