import java.io.*;
import java.util.*;

public class PolynomialQueries {

    static int n;
    static long[] seg, lazyA, lazyD;

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

    // Build
    static void build(int node, int l, int r, long[] arr) {
        if (l == r) {
            seg[node] = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(node * 2 + 1, l, mid, arr);
        build(node * 2 + 2, mid + 1, r, arr);
        seg[node] = seg[node * 2 + 1] + seg[node * 2 + 2];
    }

    //  AP sum
    static long sum(long len, long a, long d) {
        return len * (2 * a + (len - 1) * d) / 2;
    }

    //  Push
    static void push(int node, int l, int r) {
        if (lazyA[node] == 0 && lazyD[node] == 0) return;

        int mid = (l + r) >> 1;
        int leftLen = mid - l + 1;

        int left = node * 2 + 1;
        int right = node * 2 + 2;

        // LEFT
        seg[left] += sum(leftLen, lazyA[node], lazyD[node]);
        lazyA[left] += lazyA[node];
        lazyD[left] += lazyD[node];

        // RIGHT
        long newA = lazyA[node] + leftLen * lazyD[node];
        seg[right] += sum(r - mid, newA, lazyD[node]);
        lazyA[right] += newA;
        lazyD[right] += lazyD[node];

        lazyA[node] = 0;
        lazyD[node] = 0;
    }

    //  Update
    static void update(int node, int l, int r, int ql, int qr) {

        if (qr < l || r < ql) return;

        if (ql <= l && r <= qr) {
            long a = l - ql + 1;
            seg[node] += sum(r - l + 1, a, 1);
            lazyA[node] += a;
            lazyD[node] += 1;
            return;
        }

        push(node, l, r);

        int mid = (l + r) >> 1;

        update(node * 2 + 1, l, mid, ql, qr);
        update(node * 2 + 2, mid + 1, r, ql, qr);

        seg[node] = seg[node * 2 + 1] + seg[node * 2 + 2];
    }

    // Query
    static long query(int node, int l, int r, int ql, int qr) {

        if (qr < l || r < ql) return 0;

        if (ql <= l && r <= qr) return seg[node];

        push(node, l, r);

        int mid = (l + r) >> 1;

        return query(node * 2 + 1, l, mid, ql, qr)
                + query(node * 2 + 2, mid + 1, r, ql, qr);
    }

    public static void main(String[] args) throws Exception {

        FastScanner fs = new FastScanner(System.in);

        n = fs.nextInt();
        int q = fs.nextInt();

        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = fs.nextLong();
        }

        seg = new long[4 * n];
        lazyA = new long[4 * n];
        lazyD = new long[4 * n];

        build(0, 0, n - 1, arr);

        StringBuilder sb = new StringBuilder();

        while (q-- > 0) {
            int type = fs.nextInt();
            int l = fs.nextInt() - 1;
            int r = fs.nextInt() - 1;

            if (type == 1) {
                update(0, 0, n - 1, l, r);
            } else {
                sb.append(query(0, 0, n - 1, l, r)).append('\n');
            }
        }

        System.out.print(sb);
    }
}