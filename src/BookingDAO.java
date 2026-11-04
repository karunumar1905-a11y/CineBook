
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public int saveBooking(Booking booking) {
        String sql = "INSERT INTO bookings " +
                "(customer_name, movie_id, show_time, seats, total_amount, booking_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, booking.getCustomerName());
            ps.setInt(2, booking.getShow().getMovieId());
            ps.setString(3, booking.getShow().getShowTime());
            ps.setString(4, String.join(",", booking.getSeats()));
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, "CONFIRMED");

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Booking save failed: " + e.getMessage());
        }

        return -1;
    }

    public List<Booking> getAllBookings(List<Show> shows) {
        List<Booking> list = new ArrayList<>();

        String sql = "SELECT * FROM bookings ORDER BY booking_id DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                Show selectedShow = null;

                for (Show show : shows) {
                    if (show.getMovieId() == movieId) {
                        selectedShow = show;
                        break;
                    }
                }

                if (selectedShow == null) {
                    continue;
                }

                String[] seats = rs.getString("seats").split(",");

                Booking booking = new Booking(
                        rs.getString("customer_name"),
                        selectedShow,
                        seats,
                        rs.getDouble("total_amount")
                );

                booking.setBookingId(rs.getInt("booking_id"));

                booking.setCancelled(
                        "CANCELLED".equals(rs.getString("booking_status"))
                );

                list.add(booking);
            }

        } catch (SQLException e) {
            System.out.println("Unable to load bookings: " + e.getMessage());
        }

        return list;
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET booking_status = 'CANCELLED' " +
                "WHERE booking_id = ? AND booking_status = 'CONFIRMED'";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Cancellation update failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings " +
                "WHERE booking_id = ? AND booking_status = 'CANCELLED'";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
