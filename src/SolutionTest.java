import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class SolutionTest {

    @Test
    public void main() {
        fillRandomData(300000);
        long start = System.currentTimeMillis();
        Solution.main(new String[0]);
        long finish = System.currentTimeMillis();
        System.out.println("Выполнение метода Main заняло: "+(finish-start)+" ms");
    }

    private void fillRandomData(int n) {
        StringBuilder sb = new StringBuilder(n + "\n");
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int x1 = random.nextInt(-1_000_000_000, 1);
            int y1 = random.nextInt(-1_000_000_000, 1);
            int x2 = random.nextInt(0, 1_000_000_000);
            int y2 = random.nextInt(0, 1_000_000_000);
            sb.append(x1).append(" ").append(y1).append(" ").append(x2).append(" ").append(y2).append("\n");
        }
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("input.txt"))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}