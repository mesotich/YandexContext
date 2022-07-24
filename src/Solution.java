import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    private static int n;
    private static List<Square> list = new ArrayList<>();

    public static void main(String[] args) {
        load();
        printResult();
    }

    private static void load() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"))) {
            n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                int[] squares = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                list.add(new Square(squares[0], squares[1], squares[2], squares[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printResult() {
        int sum = 0;
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (i != j && (intersect(list.get(i), list.get(j)) || intersect(list.get(j), list.get(i)))) {
                        sum++;
                    }
                }
                bw.write(sum + "\n");
                sum = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean intersect(Square a, Square b) {
        return (a.x1 > b.x1 && a.x1 < b.x2 && a.y1 > b.y1 && b.y1 < b.y2) ||
                (a.x1 > b.x1 && a.x1 < b.x2 && a.y2 > b.y1 && a.y2 < b.y2) ||
                (a.x2 > b.x1 && a.x2 < b.x2 && a.y1 > b.y1 && a.y1 < b.y2) ||
                (a.x2 > b.x1 && a.x2 < b.x2 && a.y2 > b.y1 && a.y2 < b.y2) ||
                (a.x1 == b.x1 && a.x2 == b.x2 && a.y1 == b.y1 && a.y2 == b.y2) ||
                (a.x1 == b.x1 && a.x2 == b.x2 && a.y1 > b.y1 && a.y1 < b.y2) ||
                (a.x1 == b.x1 && a.x2 == b.x2 && a.y2 > b.y1 && a.y2 < b.y2) ||
                (a.y1 == b.y1 && a.y2 == b.y2 && a.x1 > b.x1 && a.x1 < b.x2) ||
                (a.y1 == b.y1 && a.y2 == b.y2 && a.x2 > b.x1 && a.x2 < b.x2);
    }

    private static class Square {

        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        public Square(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}