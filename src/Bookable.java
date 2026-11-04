public interface Bookable {
    Booking bookSeats(Show show, String customerName, String[] seats, String coupon);
    double cancelBooking(int bookingId);
}