import java.util.*;
import java.io.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            long n = in.nextLong();
            long result = solve(n);
            System.out.println("Case #" + i + ": " + result);
        }
    }

    private static long solve(long n) {
        while (true) {
            if (isOK(++n)) break;
        }
        return n;
    }

    private static boolean isOK(long n) {
        String str = "" + n;
        for (int i = 1; i < str.length(); i++) {
            long x = Long.parseLong(str.substring(0, i));
            StringBuilder builder = new StringBuilder("" + x);
            while (true) {
                builder.append("" + (++x));
                String bs = builder.toString();
                if (!str.startsWith(bs)) break;
                if (str.equals(bs)) return true;
                if (bs.length() > str.length()) break;
            }
        }
        return false;
    }

}