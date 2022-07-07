import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    private static final Stack<int[]> photos = new Stack<>();
    private static final TreeMap<Integer, Integer> first = new TreeMap<>(Comparator.reverseOrder());
    private static final TreeMap<Integer, Integer> second = new TreeMap<>(Comparator.reverseOrder());
    private static final TreeMap<Integer, Integer> third = new TreeMap<>(Comparator.reverseOrder());
    private static final TreeMap<Integer, Integer> fourth = new TreeMap<>(Comparator.reverseOrder());
    private static final List<Long> results = new ArrayList<>();

    public static void main(String[] args) {
        loadPhotos();
        execute();
        printResult();
    }

    private static void loadPhotos() {
        int[] day;
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"))) {
            int days = Integer.parseInt(br.readLine());
            for (int i = 0; i < days; i++) {
                day = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                photos.push(day);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execute() {
        first.put(0, 0);
        second.put(0, 0);
        third.put(0, 0);
        fourth.put(0, 0);
        while (!photos.empty()) {
            int[] photo = photos.pop();
            long firstSquare = getSquare(photo[2], photo[3]);
            long secondSquare = getSquare(photo[0], photo[3]);
            long thirdSquare = getSquare(photo[0], photo[1]);
            long fourthSquare = getSquare(photo[2], photo[1]);
            long sum = firstSquare + secondSquare + thirdSquare + fourthSquare;
            results.add(sum);
        }
    }

    private static void printResult() {
        String s;
        Collections.reverse(results);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            for (int i = 0; i < results.size(); i++) {
                s = i != results.size() - 1 ? "\n" : "";
                bw.write(results.get(i) + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long getSquare(int x, int y) {
        if (x == 0 || y == 0)
            return 0;
        long sum = 0;
        int delta;
        TreeMap<Integer, Integer> columns = x > 0 ? y > 0 ? first : fourth : y > 0 ? second : third;
        x = Math.abs(x);
        y = Math.abs(y);
        delta = y - columns.getOrDefault(x, columns.get(Optional.ofNullable(columns.lowerKey(x)).orElse(0)));
        if (delta <= 0)
            return 0;
        int x0 = x;
        columns.put(x, y);
        Optional<Integer> higherKey;
        int forRemove;
        while (delta > 0 && x != 0) {
            higherKey = Optional.ofNullable(columns.higherKey(x));
            sum += (long) delta * (x - higherKey.orElse(0));
            delta = y - columns.get(higherKey.orElse(0));
            forRemove = x;
            x = higherKey.orElse(0);
            if (forRemove != x0)
                columns.remove(forRemove);
        }
        return sum;
    }
}
