
public enum SeatType {
    REGULAR(150),
    PREMIUM(250),
    VIP(400);

    private final double price;

    SeatType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public static SeatType getType(int row) {
        if (row <= 1) {
            return REGULAR;
        } else if (row <= 3) {
            return PREMIUM;
        } else {
            return VIP;
        }
    }
}
