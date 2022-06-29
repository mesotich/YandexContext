import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private static final String FULL_FILL = "Cannot fulfill passengers requirements";
    private static final String CAN_TAKE = "Passengers can take seats: ";
    private static int totalFreeSeats = 0;
    private static final PlaneInformation leftHole = new PlaneInformation();
    private static final PlaneInformation rightHole = new PlaneInformation();

    public static void main(String[] args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"));
             BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            int rowsQuantity = Integer.parseInt(br.readLine());
            for (int i = 0; i < rowsQuantity; i++) {
                String[] rows = br.readLine().split("_");
                Row leftRow = Row.createRow(rows[0], Side.LEFT);
                Row rightRow = Row.createRow(rows[1], Side.RIGHT);
                leftHole.addRow(leftRow);
                rightHole.addRow(rightRow);
            }
            int passengersGroupQuantity = Integer.parseInt(br.readLine());
            for (int i = 0; i < passengersGroupQuantity; i++) {
                totalFreeSeats = leftHole.getPlaneInformationFreeSeats() + rightHole.getPlaneInformationFreeSeats();
                if (totalFreeSeats == 0)
                    break;
                String[] stringGroup = br.readLine().split(" ");
                GroupTicket group = new GroupTicket(Integer.parseInt(stringGroup[0]), Side.valueOf(stringGroup[1].toUpperCase()), Preference.valueOf(stringGroup[2].toUpperCase()));
                int rowForSelling = ticketsForSelling(group);
                if (rowForSelling != -1) {
                    Side side = group.getSide();
                    PlaneInformation planeInformation = side.equals(Side.LEFT) ? leftHole : rightHole;
                    planeInformation.sellTickets(group, rowForSelling);
                    printResult(rowForSelling, side, bw);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printResult(int newRow, GroupTicket group, BufferedWriter bw) throws IOException {
        StringBuilder sb = new StringBuilder(CAN_TAKE);
        sb.append("\n");
        for (int i = 0; i < leftHole.getRows().size(); i++) {
            String leftPart = leftHole.getRows().get(i).toString();
            String rightPart = rightHole.getRows().get(i).toString();
            if (i + 1 == newRow) {
                if (side.equals(Side.LEFT)) {
                    leftPart = leftPart.replace('#', 'X');
                } else {
                    rightPart = rightPart.replace('#', 'X');
                }
            }
            sb.append(leftPart)
                    .append("_")
                    .append(rightPart);
            sb.append("\n");
        }
        bw.write(sb.toString());
    }

    private static GroupTicket ticketsForSelling(GroupTicket group) {
        GroupTicket ticket = null;
        PlaneInformation planeInformation = group.getSide().equals(Side.LEFT) ? leftHole : rightHole;
        int passengers = group.getPassengers();
        if (planeInformation.getPlaneInformationFreeSeats() < passengers)
            return null;
        Preference preference = group.getPreference();
        if ((preference.equals(Preference.WINDOW) && planeInformation.getWindowsFreeSeats() == 0) ||
                preference.equals(Preference.AISLE) && planeInformation.getAisleFreeSeats() == 0)
            return null;
        List<Row> rows = planeInformation.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            int rowFreeSeats = row.getFreeSeats();
            boolean rowIsFreeWindow = row.isFreeWindow();
            boolean rowIsFreeAisle = row.isFreeAisle();
            if (passengers <= rowFreeSeats &&
                    ((preference.equals(Preference.WINDOW) && rowIsFreeWindow) ||
                            (preference.equals(Preference.AISLE) && rowIsFreeAisle)) &&
                    !(passengers == 2 && rowFreeSeats == 2 && rowIsFreeWindow && rowIsFreeAisle)) {
                return i + 1;
            }
        }
        return null;
    }

    private static class GroupTicket {

        private Side side;
        private int row;
        private String places;

        public GroupTicket(Side side, int row, String places) {

            this.side = side;
            this.row = row;
            this.places = places;
        }
    }

    private static class PlaneInformation {

        private List<Row> rows = new ArrayList<>();
        private int planeInformationFreeSeats = 0;
        private int windowsFreeSeats = 0;
        private int aisleFreeSeats = 0;

        public void addRow(Row row) {
            planeInformationFreeSeats += row.freeSeats;
            if (row.isFreeWindow())
                windowsFreeSeats++;
            if (row.isFreeAisle())
                aisleFreeSeats++;
            rows.add(row);
        }

        public void sellTickets(GroupTicket group, int rowNumber) {
            Row row = rows.get(rowNumber - 1);
            int passengers = group.getPassengers();
            Preference preference = group.getPreference();
            row.setFreeSeats(row.getFreeSeats() - passengers);
            planeInformationFreeSeats -= passengers;
            if (passengers == 3) {
                row.setFreeWindow(false);
                windowsFreeSeats--;
                row.setFreeAisle(false);
                aisleFreeSeats--;
            } else {
                if (preference.equals(Preference.WINDOW)) {
                    row.setFreeWindow(false);
                    windowsFreeSeats--;
                } else {
                    row.setFreeAisle(false);
                    aisleFreeSeats--;
                }
            }
        }

        public int getPlaneInformationFreeSeats() {
            return planeInformationFreeSeats;
        }

        public int getWindowsFreeSeats() {
            return windowsFreeSeats;
        }

        public int getAisleFreeSeats() {
            return aisleFreeSeats;
        }

        public List<Row> getRows() {
            return rows;
        }
    }

    private static class Row {

        private int freeSeats;
        private boolean freeWindow;
        private boolean freeAisle;
        private final Side side;

        public static Row createRow(String row, Side side) {
            char ch;
            int freesSeats = 0;
            boolean freeWindow = false;
            boolean freeAisle = false;
            boolean first = false;
            boolean third = false;
            for (int i = 0; i < 3; i++) {
                ch = row.charAt(i);
                if (ch == '.') {
                    switch (i) {
                        case 0:
                            first = true;
                            break;
                        case 1:
                            break;
                        case 2:
                            third = true;
                            break;
                    }
                    freesSeats++;
                }
                if (side.equals(Side.LEFT)) {
                    freeWindow = first;
                    freeAisle = third;
                } else {
                    freeWindow = third;
                    freeAisle = first;
                }
            }
            totalFreeSeats += freesSeats;
            return new Row(freesSeats, freeWindow, freeAisle, side);
        }

        private Row(int freeSeats, boolean freeWindow, boolean freeAisle, Side side) {
            this.freeSeats = freeSeats;
            this.freeWindow = freeWindow;
            this.freeAisle = freeAisle;
            this.side = side;
        }

        public int getFreeSeats() {
            return freeSeats;
        }

        public void setFreeSeats(int freeSeats) {
            this.freeSeats = freeSeats;
        }

        public boolean isFreeWindow() {
            return freeWindow;
        }

        public void setFreeWindow(boolean freeWindow) {
            this.freeWindow = freeWindow;
        }

        public boolean isFreeAisle() {
            return freeAisle;
        }

        public void setFreeAisle(boolean freeAisle) {
            this.freeAisle = freeAisle;
        }

        public Side getSide() {
            return side;
        }

        private void swapSides(Row row) {
            boolean buffer;
            buffer = row.freeWindow;
            row.freeWindow = row.freeAisle;
            row.freeAisle = buffer;
        }

        @Override
        public String toString() {
            if (side.equals(Side.RIGHT))
                swapSides(this);
            int occupiedPlace = 0;
            StringBuilder sb = new StringBuilder("...");
            if (!freeWindow) {
                sb.replace(0, 1, "#");
                occupiedPlace++;
            }
            if (!freeAisle) {
                sb.replace(2, sb.length(), "#");
                occupiedPlace++;
            }
            if (3 - occupiedPlace > freeSeats)
                sb.replace(1, 2, "#");
            if (side.equals(Side.RIGHT))
                swapSides(this);
            return sb.toString();
        }
    }

    public enum Side {
        LEFT, RIGHT
    }

    public enum Preference {
        WINDOW, AISLE
    }
}
