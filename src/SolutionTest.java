import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

class SolutionTest {

    @Test
    void main() {
        generateInput(300000,200000);
        long start = System.currentTimeMillis();
        Solution.main(new String[0]);
        long finish = System.currentTimeMillis();
        System.out.println("Метод main занял: "+(finish-start)+"ms");
    }

    private void generateInput(int n, int m) {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(" ").append(m).append("\n");
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            sb.append(random.nextInt(1, 1_000_000_000)).append(" ");
        }
        sb.delete(sb.length() - 1, sb.length());
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("input.txt"))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}