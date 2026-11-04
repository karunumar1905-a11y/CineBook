
import java.util.Arrays;

public class Show {

    private int movieId;
    private String movieName;
    private String showTime;
    private char[][] seats;

    private static final int ROWS = 5;
    private static final int COLS = 8;

    public Show(int movieId, String movieName, String showTime) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.showTime = showTime;

        seats = new char[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            Arrays.fill(seats[i], 'O');
        }
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setSeatStatus(String seatCode, char status) {
        if (seatCode != null && seatCode.matches("[A-E][1-8]")) {
            int row = seatCode.charAt(0) - 'A';
            int col = Integer.parseInt(seatCode.substring(1)) - 1;
            seats[row][col] = status;
        }
    }

    public void displaySeats() {
        System.out.println("\nSeat Map: O = Available, X = Booked");
        System.out.println("    1 2 3 4 5 6 7 8");

        for (int i = 0; i < ROWS; i++) {
            System.out.print((char) ('A' + i) + "   ");

            for (int j = 0; j < COLS; j++) {
                System.out.print(seats[i][j] + " ");
            }

            System.out.println();
        }
    }

    public boolean isSeatAvailable(String seatCode) {
        if (seatCode == null || !seatCode.matches("[A-E][1-8]")) {
            return false;
        }

        int row = seatCode.charAt(0) - 'A';
        int col = Integer.parseInt(seatCode.substring(1)) - 1;

        return seats[row][col] == 'O';
    }

    public boolean bookSeat(String seatCode) {
        if (!isSeatAvailable(seatCode)) {
            return false;
        }

        int row = seatCode.charAt(0) - 'A';
        int col = Integer.parseInt(seatCode.substring(1)) - 1;

        seats[row][col] = 'X';

        return true;
    }

    public void releaseSeat(String seatCode) {
        if (seatCode != null && seatCode.matches("[A-E][1-8]")) {
            int row = seatCode.charAt(0) - 'A';
            int col = Integer.parseInt(seatCode.substring(1)) - 1;

            seats[row][col] = 'O';
        }
    }

    public double getSeatPrice(String seatCode) {
        if (seatCode == null || !seatCode.matches("[A-E][1-8]")) {
            throw new IllegalArgumentException("Invalid seat code");
        }

        int row = seatCode.charAt(0) - 'A';

        return SeatType.getType(row).getPrice();
    }
}
