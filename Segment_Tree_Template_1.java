import java.io.*;
import java.util.StringTokenizer;

public class DynamicRangeSumQueries {
    static long[] segTree;
    static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        segTree = new long[4 * n];
        long[] arr = new long[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        // idx, l, r: define the current node. arr: the input array
        // To start building the tree for the whole array, you call:
        // build(0, 0, n-1);
        buildSegTree(0, 0, n - 1, arr);

        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            if (type == 1) {
                int k = Integer.parseInt(st.nextToken()) - 1;
                long u = Long.parseLong(st.nextToken());
                // To update, you call:
                // update(0, 0, n-1, pos, new_val);
                updatesegTree(0, 0, n - 1, k, u);
            } else {
                int start = Integer.parseInt(st.nextToken()) - 1;
                int end = Integer.parseInt(st.nextToken()) - 1;
                // To query the whole array, you call:
                // int ans = query(0, 0, n-1, ql, qr);
                long ans = querysegTree(0, 0, n - 1, start, end);
                bw.write(ans + "\n");
            }
        }
        bw.flush();
    }

    // idx, l, r: same as build function - they define the current node.
    // ql, qr: the query range we are interested in.
    private static long querysegTree(int idx, int l, int r, int start, int end) {
        // Case 1: No overlap 
        if (l > end || r < start)
            return 0;
        // Case 2: Complete overlap -> return the node's value
        if (start <= l && r <= end) {
            return segTree[idx];
        }
        // Case 3: Partial overlap -> ask the children and combine
        int mid = l + (r - l) / 2;
        return querysegTree(2 * idx + 1, l, mid, start, end)
             + querysegTree(2 * idx + 2, mid + 1, r, start, end);
    }

    // idx, l, r: same as before.
    // pos: the position in the original array to update
    // val: the new value
    private static void updatesegTree(int idx, int l, int r, int i, long val) {
        // Base case: found the leaf node to update
        if (l == r) {
            // Update the original array (optional but good practice)
            // arr[l] = val;
            // Update the segment tree
            segTree[idx] = val;
            return;
        }
        // Decide which child the `pos` belongs to
        int mid = l + (r - l) / 2;
        if (i <= mid) {
            // Go to the left child
            updatesegTree(2 * idx + 1, l, mid, i, val);
        } else {
            // Go to the right child
            updatesegTree(2 * idx + 2, mid + 1, r, i, val);
        }
        // After updating a child, update the current node's value.
        segTree[idx] = segTree[2 * idx + 1] + segTree[2 * idx + 2];
    }

    // idx: current node index in segment tree
    // l, r: range that this node covers
    private static void buildSegTree(int idx, int l, int r, long[] arr) {
        // Base case: it's a leaf node (l == r)
        if (l == r) {
            segTree[idx] = arr[l];
            return;
        }
        // If it's not a leaf, it has two children.
        int mid = l + (r - l) / 2;
        // build(2*idx+1, l, mid)
        buildSegTree(2 * idx + 1, l, mid, arr);
        // build(2*idx+2, mid+1, r)
        buildSegTree(2 * idx + 2, mid + 1, r, arr);
        // After building children, update the current node's value.
        segTree[idx] = segTree[2 * idx + 1] + segTree[2 * idx + 2];
    }
}
