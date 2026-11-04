
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowSeatDAO {

    public void loadSeats(Show show) {
        String sql = "SELECT seat_code, seat_status FROM show_seats WHERE movie_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, show.getMovieId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String seatCode = rs.getString("seat_code");
                    String status = rs.getString("seat_status");

                    char symbol = status.equalsIgnoreCase("BOOKED") ? 'X' : 'O';

                    show.setSeatStatus(seatCode, symbol);
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to load seats: " + e.getMessage());
        }
    }

    public boolean bookSeats(Show show, String[] seats) {
        String sql = "UPDATE show_seats SET seat_status = 'BOOKED' " +
                "WHERE movie_id = ? AND seat_code = ? " +
                "AND seat_status = 'AVAILABLE'";

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (String seat : seats) {
                    ps.setInt(1, show.getMovieId());
                    ps.setString(2, seat.trim().toUpperCase());
                    ps.addBatch();
                }

                int[] results = ps.executeBatch();

                for (int result : results) {
                    if (result == 0) {
                        con.rollback();
                        return false;
                    }
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Seat booking failed: " + e.getMessage());
            return false;
        }
    }

    public void releaseSeats(Show show, String[] seats) {
        String sql = "UPDATE show_seats SET seat_status = 'AVAILABLE' " +
                "WHERE movie_id = ? AND seat_code = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (String seat : seats) {
                ps.setInt(1, show.getMovieId());
                ps.setString(2, seat.trim().toUpperCase());
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            System.out.println("Seat release failed: " + e.getMessage());
        }
    }
}
