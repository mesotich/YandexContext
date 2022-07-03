import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.math.BigInteger;

class SolutionTest {

    @org.junit.jupiter.api.Test
    void main() {
        getFactorial(5000);
    }
    public BigInteger getFactorial(int n){
        BigInteger bigInteger = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(i));
        }
        return bigInteger;
    }


}