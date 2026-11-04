import java.util.ArrayList;
import java.util.List;

public class BookingManager {

    private List<Booking> bookings;

    public BookingManager() {
        bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void loadBookings(List<Booking> oldBookings) {
        bookings.addAll(oldBookings);
    }

    public Booking findBooking(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public boolean removeBooking(int bookingId) {
        Booking selectedBooking = findBooking(bookingId);

        if (selectedBooking == null || !selectedBooking.isCancelled()) {
            return false;
        }

        bookings.remove(selectedBooking);
        return true;
    }

    public void displayBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Customer: " + booking.getCustomerName());
            System.out.println("Movie: " + booking.getShow().getMovieName());
            System.out.println("Seats: " +
                    String.join(", ", booking.getSeats()));
            System.out.println("Amount: Rs. " + booking.getTotalAmount());
            System.out.println("Status: " +
                    (booking.isCancelled() ? "Cancelled" : "Confirmed"));
            System.out.println("--------------------");
        }
    }
}