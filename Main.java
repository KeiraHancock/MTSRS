import java.util.*;

public class Main {
    // Can change these if want a larger/smaller theater
    private static final int ROWS = 5;   // A..E
    private static final int COLS = 10;  // 1..10

    // 'O' = Open seat, 'X' = Reserved
    private static final char[][] seats = new char[ROWS][COLS];

    public static void main(String[] args) {
        initSeating();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Movie Theater Seat Reservation System ===");
        printSeating();

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1) Display seating chart");
            System.out.println("2) Reserve a seat (e.g., B7)");
            System.out.println("3) Cancel a reservation (e.g., B7)");
            System.out.println("4) Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1":
                    printSeating();
                    break;
                case "2":
                    System.out.print("Enter seat to reserve (e.g., B7): ");
                    String reserveCode = sc.nextLine().trim();
                    reserveSeat(reserveCode, sc);
                    // Show updated chart after operation
                    printSeating();
                    break;
                case "3":
                    System.out.print("Enter seat to cancel (e.g., B7): ");
                    String cancelCode = sc.nextLine().trim();
                    cancelReservation(cancelCode);
                    // Show updated chart after operation
                    printSeating();
                    break;
                case "4":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        sc.close();
    }

    //  Core features

    private static void initSeating() {
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(seats[r], 'O');
        }
    }

    private static void printSeating() {
        System.out.println("\nCurrent Seating Chart");
        // header numbers
        System.out.print("    ");
        for (int c = 1; c <= COLS; c++) {
            System.out.printf("%-3d", c);
        }
        System.out.println();

        // rows
        for (int r = 0; r < ROWS; r++) {
            System.out.printf("%-3s ", rowLabel(r));
            for (int c = 0; c < COLS; c++) {
                System.out.print(seats[r][c] + "  ");
            }
            System.out.println();
        }
        System.out.println("Legend: O = available, X = reserved");
    }

    private static void reserveSeat(String seatCode, Scanner sc) {
        int[] pos = parseSeatCode(seatCode);
        if (pos == null) {
            System.out.println("Invalid seat code. Use format like B7.");
            return;
        }
        int r = pos[0], c = pos[1];

        if (seats[r][c] == 'X') {
            System.out.println("Sorry, " + formatSeat(r, c) + " is already taken.");

            int[] suggestion = findNearestAvailable(r, c);
            if (suggestion == null) {
                System.out.println("Unfortunately, there are no seats available.");
                return;
            }

            String suggestedSeat = formatSeat(suggestion[0], suggestion[1]);
            System.out.print("Suggested available seat: " + suggestedSeat + ". Reserve it? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y") || ans.equals("yes")) {
                seats[suggestion[0]][suggestion[1]] = 'X';
                System.out.println("Reserved " + suggestedSeat + ".");
            } else {
                System.out.println("No seat reserved.");
            }
        } else {
            seats[r][c] = 'X';
            System.out.println("Reserved " + formatSeat(r, c) + ".");
        }
    }

    private static void cancelReservation(String seatCode) {
        int[] pos = parseSeatCode(seatCode);
        if (pos == null) {
            System.out.println("Invalid seat code. Use format like B7.");
            return;
        }
        int r = pos[0], c = pos[1];

        if (seats[r][c] == 'X') {
            seats[r][c] = 'O';
            System.out.println("Canceled reservation for " + formatSeat(r, c) + ".");
        } else {
            System.out.println(formatSeat(r, c) + " is not reserved.");
        }
    }

    //  Helpers 

    // Parse seat like "B7" -> [rowIndex, colIndex]
    private static int[] parseSeatCode(String code) {
        if (code == null || code.length() < 2) return null;

        code = code.toUpperCase().replaceAll("\\s+", "");

        // Row letter(s): first char must be letter A..Z, only use A.. (ROWS)
        char rowChar = code.charAt(0);
        if (rowChar < 'A' || rowChar >= 'A' + ROWS) return null;

        String colPart = code.substring(1);
        if (!colPart.matches("\\d+")) return null;

        int colNum;
        try {
            colNum = Integer.parseInt(colPart);
        } catch (NumberFormatException e) {
            return null;
        }

        if (colNum < 1 || colNum > COLS) return null;

        int r = rowChar - 'A';
        int c = colNum - 1;
        return new int[]{r, c};
    }

    private static String formatSeat(int r, int c) {
        return rowLabel(r) + (c + 1);
    }

    private static String rowLabel(int r) {
        return String.valueOf((char) ('A' + r));
    }

    /**
     * Find the nearest available seat to a target (r,c) using Manhattan distance.
     * If multiple are tied, favor lower row letter, then lower column number.
     */
    private static int[] findNearestAvailable(int r, int c) {
        if (anyAvailable() == false) return null;

        int maxD = Math.max(Math.max(r, ROWS - 1 - r), Math.max(c, COLS - 1 - c));
        for (int d = 0; d <= maxD; d++) {
            List<int[]> candidates = new ArrayList<>();
            // Collect all cells with |dr|+|dc| == d
            for (int dr = -d; dr <= d; dr++) {
                int dc = d - Math.abs(dr);
                int[][] deltas = new int[][]{{dr, dc}, {dr, -dc}};
                for (int[] delta : deltas) {
                    int rr = r + delta[0];
                    int cc = c + delta[1];
                    if (dc == 0 && delta == deltas[1]) continue; // avoid duplicates when dc == 0
                    if (inBounds(rr, cc) && seats[rr][cc] == 'O') {
                        candidates.add(new int[]{rr, cc});
                    }
                }
            }
            if (!candidates.isEmpty()) {
                // Tie-break: smallest row, then smallest col
                candidates.sort((a, b) -> {
                    if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
                    return Integer.compare(a[1], b[1]);
                });
                return candidates.get(0);
            }
        }
        return null;
    }

    private static boolean inBounds(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }

    private static boolean anyAvailable() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seats[r][c] == 'O') return true;
            }
        }
        return false;
    }
}
