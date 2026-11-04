
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public List<Show> getAllShows() {
        List<Show> shows = new ArrayList<>();

        String sql = "SELECT movie_id, movie_name, show_time FROM movies";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Show show = new Show(
                    rs.getInt("movie_id"),
                    rs.getString("movie_name"),
                    rs.getString("show_time")
                );

                shows.add(show);
            }

        } catch (SQLException e) {
            System.out.println("Unable to load movies: " + e.getMessage());
        }

        return shows;
    }
}
