import java.util.Scanner;

public class Solution {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int tanksQuantity = getValueFromConsole(100_000);
        int[] volumes = new int[tanksQuantity];
        for (int i = 0; i < volumes.length; i++) {
            volumes[i] = getValueFromConsole(1_000_000_000);
        }
        System.out.println(calculateOperationQuantity(volumes));
    }

    private static int calculateOperationQuantity(int[] volumes) {
        if (volumes.length == 1)
            return 0;
        int minValue = Integer.MAX_VALUE;
        int maxValue = 0;
        for (int i = 0; i < volumes.length; i++) {
            if (i + 1 >= volumes.length)
                return maxValue - minValue;
            if (volumes[i + 1] < volumes[i])
                return -1;
            if (volumes[i] < minValue)
                minValue = volumes[i];
            if (volumes[i + 1] > maxValue)
                maxValue = volumes[i + 1];
        }
        return maxValue - minValue;
    }

    private static int getValueFromConsole(int max) {
        int value;
        while (true) {
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value < 1 || value > max) {
                    System.out.println("Введите число от 1 до " + max);
                    scanner.next();
                } else {
                    return value;
                }
            } else {
                System.out.println("Вы ввели не число");
                scanner.next();
            }
        }
    }
}
