import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MovieTicketBookingSystem {

    // Represents a movie
    static class Movie {
        private String title;
        private String genre;
        private List<Seat> seats;

        public Movie(String title, String genre, int numberOfSeats) {
            this.title = title;
            this.genre = genre;
            this.seats = new ArrayList<>();
            for (int i = 0; i < numberOfSeats; i++) {
                seats.add(new Seat(i + 1));
            }
        }

        public String getTitle() {
            return title;
        }

        public String getGenre() {
            return genre;
        }

        public List<Seat> getSeats() {
            return seats;
        }

        public boolean isSeatAvailable(int seatNumber) {
            return seats.get(seatNumber - 1).isAvailable();
        }

        public void bookSeat(int seatNumber) {
            seats.get(seatNumber - 1).book();
        }

        @Override
        public String toString() {
            return title + " (" + genre + ")";
        }
    }

    // Represents a seat in the movie theater
    static class Seat {
        private int seatNumber;
        private boolean available;

        public Seat(int seatNumber) {
            this.seatNumber = seatNumber;
            this.available = true;
        }

        public int getSeatNumber() {
            return seatNumber;
        }

        public boolean isAvailable() {
            return available;
        }

        public void book() {
            this.available = false;
        }
    }

    // Handles the booking process
    static class Booking {
        private List<Movie> movies;

        public Booking() {
            movies = new ArrayList<>();
            // Sample data
            movies.add(new Movie("Kalki 2898ad", "Sci-Fi", 10));
            movies.add(new Movie("Bahubali : The Conclusion", "Action", 10));
            movies.add(new Movie("RRR", "Action", 10));
            movies.add(new Movie("Pushpa", "Drama", 10));
        }

        public List<Movie> getMovies() {
            return movies;
        }

        public boolean bookSeat(Movie movie, int seatNumber) {
            if (movie.isSeatAvailable(seatNumber)) {
                movie.bookSeat(seatNumber);
                return true;
            }
            return false;
        }
    }

    // Main class to create the GUI
    public static class MovieTicketBookingGUI {
        private JFrame frame;
        private JComboBox<String> movieComboBox;
        private JPanel seatPanel;
        private Booking booking;

        public MovieTicketBookingGUI() {
            booking = new Booking();
            initialize();
        }

        private void initialize() {
            frame = new JFrame("Movie Ticket Booking System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new BorderLayout());

            movieComboBox = new JComboBox<>();
            for (Movie movie : booking.getMovies()) {
                movieComboBox.addItem(movie.toString());
            }
            movieComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateSeatPanel();
                }
            });

            seatPanel = new JPanel();
            seatPanel.setLayout(new GridLayout(2, 5, 5, 5)); // 2 rows, 5 columns

            JPanel controlPanel = new JPanel(new BorderLayout());
            controlPanel.add(new JLabel("Select Movie:"), BorderLayout.WEST);
            controlPanel.add(movieComboBox, BorderLayout.CENTER);

            frame.add(controlPanel, BorderLayout.NORTH);
            frame.add(seatPanel, BorderLayout.CENTER);

            updateSeatPanel();
            frame.setVisible(true);
        }

        private void updateSeatPanel() {
            seatPanel.removeAll();
            int movieIndex = movieComboBox.getSelectedIndex();
            Movie selectedMovie = booking.getMovies().get(movieIndex);

            for (Seat seat : selectedMovie.getSeats()) {
                JButton seatButton = new JButton(String.valueOf(seat.getSeatNumber()));
                if (!seat.isAvailable()) {
                    seatButton.setEnabled(false); // Disable button if the seat is already booked
                } else {
                    seatButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            bookSelectedSeat(selectedMovie, seat.getSeatNumber());
                        }
                    });
                }
                seatPanel.add(seatButton);
            }

            seatPanel.revalidate();
            seatPanel.repaint();
        }

        private void bookSelectedSeat(Movie movie, int seatNumber) {
            if (booking.bookSeat(movie, seatNumber)) {
                JOptionPane.showMessageDialog(frame, "Seat " + seatNumber + " booked successfully for " + movie.getTitle() + ".");
            } else {
                JOptionPane.showMessageDialog(frame, "Seat " + seatNumber + " is not available.");
            }
            updateSeatPanel();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MovieTicketBookingGUI window = new MovieTicketBookingGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}