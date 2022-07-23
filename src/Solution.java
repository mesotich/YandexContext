import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Solution {

    private static int numbers;
    private static int[] letters;

    public static void main(String[] args) {
        load();
        printResult();
    }

    private static void load() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"))) {
            numbers = Integer.parseInt(br.readLine());
            letters = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int findBit(int number) {
        int dev;
        for (int i = 0; i < 27; i++) {
            dev = number % 2;
            if (dev == 1)
                return i;
            number /= 2;
        }
        return -1;
    }

    private static void printResult() {
        int a = 0;
        int b;
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            for (int i = 0; i < numbers; i++) {
                b = letters[i];
                int ind = findBit(a ^ b);
                char ch = (ind == 26) ? ' ' : (char) (ind + 97);
                bw.write(ch);
                a = b;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}