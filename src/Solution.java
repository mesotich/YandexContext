import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tanksQuantity = scanner.nextInt();
        if (tanksQuantity == 1) {
            System.out.println(0);
            return;
        }
        int[] volumes = new int[tanksQuantity];
        for (int i = 0; i < volumes.length; i++) {
            volumes[i] = scanner.nextInt();
        }
        System.out.println(calculateOperationQuantity(volumes));
    }

    private static int calculateOperationQuantity(int[] volumes) {
        int minValue = Integer.MAX_VALUE;
        int maxValue = 0;
        for (int i = 0; i < volumes.length; i++) {
            if (volumes[i + 1] < volumes[i])
                return -1;
            if (i + 1 >= volumes.length)
                return maxValue - minValue;
            if (volumes[i] < minValue)
                minValue = volumes[i];
            if (volumes[i + 1] > maxValue)
                maxValue = volumes[i + 1];
        }
        return maxValue - minValue;
    }
}