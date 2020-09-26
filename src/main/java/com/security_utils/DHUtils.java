package com.security_utils;

import com.models.ConversationTO;
import com.models.DHRequestTO;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;

public class DHUtils {
    private static final Random random = new Random();
    private static Long myA = null;
    private static Long publicA = null;
    private static Long currentP = null;
    private static Long currentG = null;
    private static Long currentSecret = null;

    public static String decryptKey(String secret, String key) {

//        byte[] iv = {'1','0','1','0','1','0','1','0', '1','0','1','0','1','0','1','0'};
//        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            secret = secret + "0000000000000000";
            secret = secret.substring(0, 16);
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(key));
            return new String(decryptedBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e); //TODO different
        }
    }

    private static Long generatePrimaryNumberLong(long from, long to) {

        long p = 0L;
        boolean bol = true;
        while (bol) {
            if (p > from && p < to && checkIfPrimary(p)) {
                bol = false;
            } else {
                p = random.nextInt() % to + from;
            }
        }
        return p;
    }

    private static boolean checkIfPrimary(Long number) {
        if (number == 1) {
            return true;
        }
        double root = Math.sqrt(number);
        double i = 2.0;
        while (i < root) {
            if (number % i == 0) return false;
            i += 1;
        }
        return true;
    }

    // Iterative Function to calculate (x^y)%p in O(logy)
    private static long power(long x, long y, long p) {
        long res = 1;     // Initialize result

        x = x % p; // Update x if it is more than or
        // equal to p

        while (y > 0) {
            // If y is odd, multiply x with result
            if (y % 2 == 1) {
                res = (res * x) % p;
            }

            // y must be even now
            y = y >> 1; // y = y/2
            x = (x * x) % p;
        }
        return res;
    }

    // Function to find smallest primitive root of n
    private static long findPrimitive(long n) {
        HashSet<Long> s = new HashSet<>();

        // Check if n is prime or not
        if (!checkIfPrimary(n)) {
            return -1;
        }

        // Find value of Euler Totient function of n
        // Since n is a prime number, the value of Euler
        // Totient function is n-1 as there are n-1
        // relatively prime numbers.
        long phi = n - 1;

        // Find prime factors of phi and store in a set
        findPrimeFactors(s, phi);

        // Check for every number from 2 to phi
        for (int r = 2; r <= phi; r++) {
            // Iterate through all prime factors of phi.
            // and check if we found a power with value 1
            boolean flag = false;
            for (Long a : s) {

                // Check if r^((phi)/primefactors) mod n
                // is 1 or not
                if (power(r, phi / (a), n) == 1) {
                    flag = true;
                    break;
                }
            }

            // If there was no power with value 1.
            if (!flag) {
                return r;
            }
        }

        // If no primitive root found
        return -1;
    }

    // Utility function to store prime factors of a number
    private static void findPrimeFactors(HashSet<Long> s, long n) {
        // Print the number of 2s that divide n
        while (n % 2 == 0) {
            s.add(2L);
            n = n / 2;
        }

        // n must be odd at this point. So we can skip
        // one element (Note i = i +2)
        for (long i = 3; i <= Math.sqrt(n); i = i + 2) {
            // While i divides n, print i and divide n
            while (n % i == 0) {
                s.add(i);
                n = n / i;
            }
        }

        // This condition is to handle the case when
        // n is a prime number greater than 2
        if (n > 2) {
            s.add(n);
        }
    }

    public static DHRequestTO generateDHRequest() {
        currentP = generatePrimaryNumberLong(10000, 99999);
        currentG = findPrimitive(currentP);
        myA = generatePrimaryNumberLong(10, 100);
        publicA = power(currentG, myA, currentP);

        return DHRequestTO.builder()
                .A(publicA)
                .p(currentP)
                .g(currentG)
                .build();
    }

    public static DHRequestTO generateDHRequest(String conversationID) {
        currentP = generatePrimaryNumberLong(10000, 99999);
        currentG = findPrimitive(currentP);
        myA = generatePrimaryNumberLong(10, 100);
        publicA = power(currentG, myA, currentP);

        return DHRequestTO.builder()
                .ID(conversationID)
                .A(publicA)
                .p(currentP)
                .g(currentG)
                .build();
    }

    public static String getKeyFromDHResponse(ConversationTO conversationTO) {
        Long S = conversationTO.getS();
        currentSecret = power(S, myA, currentP);

        return decryptKey(currentSecret.toString(), conversationTO.getKey());
    }
}
