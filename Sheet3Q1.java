// package HWI;

import java.io.*;
import java.util.*;

public class Sheet3Q1 {

    static int n;
    static long[] seg, lazyA, lazyD;
    static boolean[] hasLazy;

    static final long MOD = 1_000_000_007;
    static final long INV2 = (MOD + 1) / 2; // modular inverse of 2

    // FAST SCANNER
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) {
            in = is;
        }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c, sign = 1, val = 0;
            while ((c = read()) <= ' ') ;
            if (c == '-') {
                sign = -1;
                c = read();
            }
            while (c > ' ') {
                val = val * 10 + c - '0';
                c = read();
            }
            return val * sign;
        }

        long nextLong() throws IOException {
            int c, sign = 1;
            long val = 0;
            while ((c = read()) <= ' ') ;
            if (c == '-') {
                sign = -1;
                c = read();
            }
            while (c > ' ') {
                val = val * 10 + c - '0';
                c = read();
            }
            return val * sign;
        }
    }

    // BUILD
    static void build(int node, int l, int r, long[] arr) {
        if (l == r) {
            seg[node] = arr[l] % MOD;
            return;
        }
        int mid = (l + r) >> 1;
        build(node * 2 + 1, l, mid, arr);
        build(node * 2 + 2, mid + 1, r, arr);
        seg[node] = (seg[node * 2 + 1] + seg[node * 2 + 2]) % MOD;
    }

    // AP SUM
    static long sum(long len, long a, long d) {
        long part = ( (2 * a % MOD + ((len - 1) % MOD) * d % MOD) % MOD );
        return (len % MOD) * part % MOD * INV2 % MOD;
    }

    // PUSH
    static void push(int node, int l, int r) {
        if (!hasLazy[node]) return;

        int mid = (l + r) >> 1;
        int left = node * 2 + 1;
        int right = node * 2 + 2;

        int leftLen = mid - l + 1;

        // LEFT
        seg[left] = sum(leftLen, lazyA[node], lazyD[node]);
        lazyA[left] = lazyA[node];
        lazyD[left] = lazyD[node];
        hasLazy[left] = true;

        // RIGHT
        long newA = (lazyA[node] + leftLen * lazyD[node] % MOD) % MOD;
        seg[right] = sum(r - mid, newA, lazyD[node]);
        lazyA[right] = newA;
        lazyD[right] = lazyD[node];
        hasLazy[right] = true;

        hasLazy[node] = false;
    }

    // UPDATE (assign AP)
    static void update(int node, int l, int r, int ql, int qr, long x, long y) {
        if (qr < l || r < ql) return;

        if (ql <= l && r <= qr) {
            long a = (x + (long)(l - ql) * y % MOD) % MOD;

            seg[node] = sum(r - l + 1, a, y);
            lazyA[node] = a;
            lazyD[node] = y % MOD;
            hasLazy[node] = true;
            return;
        }

        push(node, l, r);

        int mid = (l + r) >> 1;

        update(node * 2 + 1, l, mid, ql, qr, x, y);
        update(node * 2 + 2, mid + 1, r, ql, qr, x, y);

        seg[node] = (seg[node * 2 + 1] + seg[node * 2 + 2]) % MOD;
    }

    // QUERY
    static long query(int node, int l, int r, int ql, int qr) {
        if (qr < l || r < ql) return 0;

        if (ql <= l && r <= qr) return seg[node];

        push(node, l, r);

        int mid = (l + r) >> 1;

        return (query(node * 2 + 1, l, mid, ql, qr)
                + query(node * 2 + 2, mid + 1, r, ql, qr)) % MOD;
    }

    public static void main(String[] args) throws Exception {

        FastScanner fs = new FastScanner(System.in);

        n = fs.nextInt();
        long[] arr = new long[n];

        for (int i = 0; i < n; i++) {
            arr[i] = fs.nextLong();
        }

        int q = fs.nextInt();

        seg = new long[4 * n];
        lazyA = new long[4 * n];
        lazyD = new long[4 * n];
        hasLazy = new boolean[4 * n];

        build(0, 0, n - 1, arr);

        long answer = 0;

        while (q-- > 0) {
            int type = fs.nextInt();
            int l = fs.nextInt();
            int r = fs.nextInt();

            if (type == 1) {
                long val = query(0, 0, n - 1, l, l); // A[l]
                update(0, 0, n - 1, l, r, val, val);
            } else {
                long res = query(0, 0, n - 1, l, r);
                answer = (answer + res) % MOD;
            }
        }

        System.out.println(answer);
    }
}