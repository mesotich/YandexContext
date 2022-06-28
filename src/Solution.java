import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Solution {

    private static final int ELEMENTS_FOR_TWO_THREAD = 1000;
    private static final int ELEMENTS_FOR_THREE_THREAD = 10000;
    private static final int ELEMENTS_FOR_FOUR_THREAD = 50000;

    public static void main(String[] args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("input.txt"));
             BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            int tanksQuantity = Integer.parseInt(br.readLine());
            if (tanksQuantity == 1) {
                bw.write(String.valueOf(0));
                return;
            }
            String[] strings = br.readLine().split(" ");
            int len = strings.length;
            int n = len > ELEMENTS_FOR_TWO_THREAD
                    ? (len > ELEMENTS_FOR_THREE_THREAD ? (len > ELEMENTS_FOR_FOUR_THREAD ? 4 : 3) : 2) : 1;
            int result;
            if (n == 1) {
                FindingTread findingTread = new FindingTread(strings);
                result = findingTread.call();
            } else {
                result = multithreadingExecute(n, strings);
            }
            if (result == -1) {
                bw.write(String.valueOf(-1));
                return;
            }
            int minValue = Integer.parseInt(strings[0]);
            int maxValue = Integer.parseInt(strings[strings.length - 1]);
            bw.write(String.valueOf(maxValue - minValue));
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int multithreadingExecute(int threads, String[] strings) throws ExecutionException, InterruptedException {
        List<Future<Integer>> futures = new ArrayList<>();
        int len = strings.length;
        int start = 0;
        int end = len / threads;
        ExecutorService es = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            if (end < len
                    && Integer.parseInt(strings[end - 1]) > Integer.parseInt(strings[end])) {
                es.shutdownNow();
                return -1;
            }
            String[] arr = Arrays.copyOfRange(strings, start, end);
            Future<Integer> future = es.submit(new FindingTread(arr));
            futures.add(future);
            start = end;
            end = (i + 2) * len / threads;
        }
        while (true) {
            if (futures.isEmpty()) {
                es.shutdownNow();
                break;
            }
            for (Future<Integer> future : new ArrayList<>(futures)
            ) {
                if (future.isCancelled())
                    futures.remove(future);
                if (future.isDone()) {
                    int result = future.get();
                    if (result == -1) {
                        es.shutdownNow();
                        return -1;
                    }
                    futures.remove(future);
                }
            }
        }
        es.shutdownNow();
        return 0;
    }

    public static class FindingTread implements Callable<Integer> {

        private final String[] strings;

        public FindingTread(String[] strings) {
            this.strings = strings;
        }

        @Override
        public Integer call() {
            int current;
            int previous = 0;
            for (int i = 0; i < strings.length; i++) {
                current = Integer.parseInt(strings[i]);
                if (current < previous)
                    return -1;
                previous = current;
            }
            return 0;
        }
    }
}