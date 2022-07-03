import java.math.BigInteger;

public class Solution {

    private static final int N = 500;
    private static final int COMMUNICATIONS = getCommunicationQuantity(N - 1);
    private static final int BAD_COMMUNICATIONS = N - 2;
    private static BigInteger nFact;
    private static BigInteger kFact;
    private static BigInteger nkFact;
    private static long res =0;

    public static void main(String[] args) {
        nFact = factorial(COMMUNICATIONS);
        kFact = factorial(1);
        nkFact = factorial(COMMUNICATIONS - 1);
        long start = System.currentTimeMillis();
        System.out.println(divide(calculate(N)));
        long finish = System.currentTimeMillis();
        System.out.println("Время вычисления заняло: " + (finish - start));
        System.out.println("Время метода: " + res);


    }

    private static BigInteger calculate(int n) {
        BigInteger result = BigInteger.ONE;
        BigInteger badCombinations;
        BigInteger combinations = BigInteger.ZERO;
        for (int i = 1; i <= COMMUNICATIONS; i++) {
            long start = System.currentTimeMillis();
            combinations = getCombinations(0, 0);
            long finish = System.currentTimeMillis();
            res += (finish - start);
            if (i >= N - 2) {
                badCombinations = getBadCombinations(i - (N - 2));
                if (badCombinations.compareTo(combinations) >= 0)
                    return result.multiply(BigInteger.valueOf(n));
                combinations = combinations.subtract(badCombinations);
            }
            result = result.add(combinations);
            nextFactorial(i + 1);
        }
        return result.multiply(BigInteger.valueOf(n));
    }

    private static BigInteger getBadCombinations(int k) {
        BigInteger plusBad = BigInteger.ONE;
        if (k > 0)
            plusBad = getCombinations(COMMUNICATIONS - BAD_COMMUNICATIONS, k);
        return plusBad.multiply(BigInteger.valueOf(N - 1));
    }

    private static BigInteger divide(BigInteger bigInteger) {
        BigInteger[] arr = bigInteger.divideAndRemainder(BigInteger.valueOf(1_000_000_000 + 7));
        return arr[1];
    }

    private static int getCommunicationQuantity(int n) {
        return n * (n - 1) / 2;
    }

    private static BigInteger getCombinations(int n, int k) {
        BigInteger nf;
        BigInteger kf;
        BigInteger nkf;
        if (n == 0 & k == 0) {
            nf = nFact;
            kf = kFact;
            nkf = nkFact;
        } else {
            nf = factorial(n);
            kf = factorial(k);
            nkf = factorial(n - k);
        }
        return nf.divide(kf.multiply(nkf));
    }

    public static BigInteger factorial(int n) {
        BigInteger bigInteger = new BigInteger(String.valueOf(1));
        for (int i = 1; i <= n; i++) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(i));
        }
        return bigInteger;
    }

    public static void nextFactorial(int nextInt) {
        kFact = kFact.multiply(BigInteger.valueOf(nextInt));
        nkFact = nkFact.divide(BigInteger.valueOf(nextInt));
    }
}




