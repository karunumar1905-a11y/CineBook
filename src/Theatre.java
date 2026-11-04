
import java.util.ArrayList;
import java.util.List;

public class Theatre {
    private String theatreName;
    private List<Show> shows;

    public Theatre(String theatreName) {
        this.theatreName = theatreName;
        this.shows = new ArrayList<>();
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public List<Show> getShows() {
        return shows;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void displayShows() {
        System.out.println("\n===== " + theatreName + " =====");

        for (Show show : shows) {
            System.out.println(
                show.getMovieId() + ". " +
                show.getMovieName() + " - " +
                show.getShowTime()
            );
        }
    }

    public Show findShow(int movieId) {
        for (Show show : shows) {
            if (show.getMovieId() == movieId) {
                return show;
            }
        }

        return null;
    }
}
