
public class Booking {
    private static int nextId = 1001;

    private int bookingId;
    private String customerName;
    private Show show;
    private String[] seats;
    private double totalAmount;
    private boolean cancelled;

    public Booking(String customerName, Show show,
                   String[] seats, double totalAmount) {
        this.bookingId = nextId++;
        this.customerName = customerName;
        this.show = show;
        this.seats = seats.clone();
        this.totalAmount = totalAmount;
        this.cancelled = false;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingCode() {
        return "BK" + bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Show getShow() {
        return show;
    }

    public String[] getSeats() {
        return seats.clone();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
