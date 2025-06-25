package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService = null;
        Train trainSelectedForBooking = null;

        while (option != 7) {
            System.out.println("\nChoose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignup = new User(
                            nameToSignUp,
                            passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );
                    try {
                        userBookingService = new UserBookingService(userToSignup);
                        userBookingService.signUp(userToSignup);
                        System.out.println("Signup successful!");
                    } catch (IOException ex) {
                        System.out.println("Signup failed: " + ex.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Enter the username to login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(
                            nameToLogin,
                            passwordToLogin,
                            UserServiceUtil.hashPassword(passwordToLogin),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                        System.out.println("Login successful!");
                    } catch (IOException ex) {
                        System.out.println("Login failed: " + ex.getMessage());
                    }
                    break;

                case 3:
                    if (userBookingService == null) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBookings();
                    break;

                case 4:
                    if (userBookingService == null) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.println("Type your source station");
                    String source = scanner.next();
                    System.out.println("Type your destination station");
                    String dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source, dest);
                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + ". Train id: " + t.getTrainId());
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("Station: " + entry.getKey() + " Time: " + entry.getValue());
                        }
                        index++;
                    }
                    System.out.println("Select a train by typing 1, 2, 3...");
                    int trainIndex = scanner.nextInt();
                    if (trainIndex > 0 && trainIndex <= trains.size()) {
                        trainSelectedForBooking = trains.get(trainIndex - 1); // FIXED
                    } else {
                        System.out.println("Invalid train selection");
                    }
                    break;

                case 5:
                    if (userBookingService == null || trainSelectedForBooking == null) {
                        System.out.println("Login and select a train first.");
                        break;
                    }
                    System.out.println("Select a seat out of these seats");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row : seats) {
                        for (Integer val : row) {
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Enter the row:");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column:");
                    int col = scanner.nextInt();
                    System.out.println("Booking your seat...");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                    if (booked.equals(Boolean.TRUE)) {
                        System.out.println("Booked! Enjoy your journey 🚆");
                    } else {
                        System.out.println("Can't book this seat 😢");
                    }
                    break;

                case 6:
                    System.out.println("Cancel booking feature not implemented yet.");
                    break;

                case 7:
                    System.out.println("Exiting the App. Thank you!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}