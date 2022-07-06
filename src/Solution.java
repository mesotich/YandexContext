import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    private static final Stack<Photo> photos = new Stack<>();
    private static int days;
    private static final TreeMap<Integer, Integer> first = new TreeMap<>(Comparator.reverseOrder());
    private static final TreeMap<Integer, Integer> second = new TreeMap<>(Comparator.reverseOrder());
    private static final TreeMap<Integer, Integer> third = new TreeMap<>(Comparator.reverseOrder());
    private static final TreeMap<Integer, Integer> fourth = new TreeMap<>(Comparator.reverseOrder());

    public static void main(String[] args) {
        loadPhotos();
        while (!photos.empty()) {
            Photo photo = photos.pop();
            int firstSquare = constructPolygon(photo.x2, photo.y2);
            int secondSquare = constructPolygon(photo.x1, photo.y2);
            int thirdSquare = constructPolygon(photo.x1, photo.y1);
            int fourthSquare = constructPolygon(photo.x2, photo.y1);
            System.out.println(firstSquare + secondSquare + thirdSquare + fourthSquare);
        }
    }

    private static void loadPhotos() {
        int[] day;
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"))) {
            days = Integer.parseInt(br.readLine());
            for (int i = 0; i < days; i++) {
                day = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                photos.push(new Photo(day[0], day[1], day[2], day[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int constructPolygon(int x0, int y0) {
        int sum = 0;
        int delta;
        TreeMap<Integer, Integer> columns = x0 > 0 ? y0 > 0 ? first : fourth : y0 > 0 ? second : third;
        x0 = Math.abs(x0);
        y0 = Math.abs(y0);
        Integer lowerKey = null;
        Integer higherKey =null;
        if (!columns.containsKey(x0) && x0 != 0 && y0 != 0) {
            lowerKey = columns.lowerKey(x0);
            higherKey = columns.higherKey(x0);
            if(higherKey==null)
                higherKey=0;
            if (lowerKey != null)
                sum = (y0 - columns.get(lowerKey))*(x0-higherKey);
            else sum = y0*(x0-higherKey);
            columns.put(x0, y0);
        }
        //TreeMap<Integer, Integer> tailMapColumn = columns.tailMap(x0, true);
        for (Map.Entry<Integer, Integer> entry : columns.entrySet()
        ) {
            higherKey = columns.higherKey(entry.getKey());
            if (higherKey == null) {
                higherKey = 0;
            }
            delta = y0 - entry.getValue();
            if (delta > 0) {
                entry.setValue(y0);
                sum += delta * (entry.getKey() - higherKey);
            }
        }
        return sum;
    }

    private static final class Photo {

        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        public Photo(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public int getX1() {
            return x1;
        }

        public int getY1() {
            return y1;
        }

        public int getX2() {
            return x2;
        }

        public int getY2() {
            return y2;
        }
    }
}
