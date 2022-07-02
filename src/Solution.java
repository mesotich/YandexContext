import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solution {

    private static int quantityElem;
    private static int nodes;
    private static int[] array;
    private static int[] sortArray;
    private static final Map<Integer, Long> map = new HashMap<>();

    public static void main(String[] args) {
        init();
        sortArray = Arrays.copyOf(array, quantityElem);
        Arrays.sort(sortArray);
        findDist2();
        printResult();
    }

    private static void findDist2() {
        int head = nodes;
        int tail = 0;
        long leftShoulder = 0;
        long rightShoulder = 0;
        int nextLeft = 0;
        int nextRight = 0;
        for (int i = 1; i <= nodes; i++) {
            rightShoulder += sortArray[i] - sortArray[0];
        }
        map.put(sortArray[0], rightShoulder);
        for (int i = 1; i < sortArray.length; i++) {
            int delta = sortArray[i] - sortArray[i - 1];
            leftShoulder += (long) delta * (i - tail);
            rightShoulder -= (long) delta * (head - i + 1);
            while (head != sortArray.length - 1) {
                nextLeft = sortArray[i] - sortArray[tail];
                nextRight = sortArray[head + 1] - sortArray[i];
                if (nextLeft < nextRight)
                    break;
                head++;
                tail++;
                leftShoulder -= nextLeft;
                rightShoulder += nextRight;
            }
            map.put(sortArray[i], leftShoulder + rightShoulder);
        }
    }

    private static void init() {
        String[] firstLine = null;
        String secondLine = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"))
        ) {
            firstLine = br.readLine().split(" ");
            secondLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (firstLine == null || secondLine == null || firstLine.length != 2)
            return;
        quantityElem = Integer.parseInt(firstLine[0]);
        nodes = Integer.parseInt(firstLine[1]);
        array = Arrays.stream(secondLine.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    private static void printResult() {
        StringBuilder sb = new StringBuilder();
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            for (int i = 0; i < array.length; i++) {
                sb.append(map.get(array[i])).append(" ");
            }
            sb.delete(sb.length() - 1, sb.length());
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
