import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private static final List<Integer> rows = new ArrayList<>();
    private static final int leftMask = Integer.parseInt("1110000", 2);
    private static final int rightMask = Integer.parseInt("0000111", 2);
    private static final int windowMask = Integer.parseInt("1000001", 2);
    private static final int aisleMask = Integer.parseInt("0010100", 2);
    private static final int middleLeftSeats = Integer.parseInt("0100000", 2);
    private static final int middleRightSeats = Integer.parseInt("0000010", 2);
    private static int totalFreeSeats = 0;
    private static int groupsLeft = 0;

    public static void main(String[] args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"));
             BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            int rowQuantity = Integer.parseInt(br.readLine());
            for (int i = 0; i < rowQuantity; i++) {
                String line = br.readLine();
                int row = convertRowLineToInt(line);
                totalFreeSeats += getFreeSeats(row);
                rows.add(row);
            }
            int passengersGroupQuantity = Integer.parseInt(br.readLine());
            groupsLeft = passengersGroupQuantity;
            for (int i = 0; i < passengersGroupQuantity; i++) {
                if (totalFreeSeats == 0)
                    break;
                String[] group = br.readLine().split(" ");
                int tickets = -1;
                for (int j = 0; j < rows.size(); j++) {
                    int rowCode = rows.get(j);
                    int passengers = Integer.parseInt(group[0]);
                    int groupCode = convertGroupToInt(passengers, group[1], group[2]);
                    tickets = findTickets(rowCode, groupCode);
                    if (tickets != -1) {
                        int newRow = sellTickets(rowCode, groupCode);
                        rows.set(j, newRow);
                        totalFreeSeats -= passengers;
                        printResult(bw, false, j, tickets);
                        break;
                    }
                }
                if (tickets == -1) {
                    printResult(bw, true, -1, -1);
                }
                groupsLeft--;
            }
            for (int i = 0; i < groupsLeft; i++) {
                printResult(bw, true, -1, -1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getFreeSeats(int rowCode) {
        if (rowCode == 0)
            return 0;
        int count = 0;
        while (rowCode != 0) {
            count++;
            rowCode = rowCode & rowCode - 1;
        }
        return count;
    }

    private static int convertGroupToInt(int passengers, String side, String preference) {
        boolean isLeft = side.equals("left");
        int groupCode = isLeft ? leftMask : rightMask;
        if (passengers == 3)
            return groupCode;
        int intPreference = preference.equals("window") ? windowMask : aisleMask;
        groupCode = groupCode & intPreference;
        if (passengers == 1)
            return groupCode;
        return isLeft ? (groupCode | middleLeftSeats) : (groupCode | middleRightSeats);
    }

    private static void printResult(BufferedWriter bw, boolean isEmptyResult, int changeRow, int tickets) throws IOException {
        if (isEmptyResult) {
            bw.write("Cannot fulfill passengers requirements\n");
            return;
        }
        bw.write("Passengers can take seats:" + "\n");
        for (int i = 0; i < rows.size(); i++) {
            int row = rows.get(i);
            String line = convertRowIntToLine(row);
            if (tickets != -1 && changeRow == i) {
                line = replaceNew(new StringBuilder(line), tickets);
            }
            bw.write(line + "\n");
        }
    }

    private static int sellTickets(int rowCode, int groupCode) {
        return rowCode ^ groupCode;
    }

    private static int findTickets(int rowCode, int groupCode) {
        if (rowCode == 0)
            return -1;
        if ((rowCode & groupCode) == groupCode)
            return groupCode;
        return -1;
    }

    private static int convertRowLineToInt(String line) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '.')
                sb.append('1');
            else sb.append('0');
        }
        return Integer.parseInt(sb.toString(), 2);
    }

    private static String convertRowIntToLine(int row) {
        String binaryRow = Integer.toBinaryString(row);
        StringBuilder sb = new StringBuilder(binaryRow);
        StringBuilder result = new StringBuilder();
        int len = sb.length();
        if (len < 7) {
            for (int i = 0; i < 7 - len; i++) {
                sb.insert(i, 0);
            }
        }
        len = sb.length();
        char ch;
        for (int i = 0; i < len; i++) {
            ch = i == 3 ? '_' : sb.charAt(i) == '1' ? '.' : '#';
            result.append(ch);
        }
        return result.toString();
    }

    private static String replaceNew(StringBuilder sb, int tickets) {
        String binaryString = Integer.toBinaryString(tickets);
        int del = sb.length() - binaryString.length();
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1')
                sb.replace(i + del, i + del + 1, "X");
        }
        return sb.toString();
    }
}


