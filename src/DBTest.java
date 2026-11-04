
import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        try (Connection con = Database.getConnection()) {
            System.out.println("MySQL Connected Successfully!");

            if (con.isValid(2)) {
                System.out.println("Database connection is active.");
            }

            MovieDAO dao = new MovieDAO();

            System.out.println("\nAvailable Movies:");

            for (Show show : dao.getAllShows()) {
                System.out.println(
                    show.getMovieId() + ". " +
                    show.getMovieName() + " - " +
                    show.getShowTime()
                );
            }

        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            System.out.println(e.getMessage());
        }
    }
}

