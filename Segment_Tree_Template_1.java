//package Tree;

import java.io.*;
import java.util.StringTokenizer;

public class DynamicRangeSumQueries {
    static long[] segTree;
    static int n;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st  = new StringTokenizer(br.readLine());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        segTree = new long[4*n];
        long []arr = new long[n];
        st = new StringTokenizer(br.readLine());
        for(int i = 0 ; i < n ; i++)
        {
            arr[i] = Long.parseLong(st.nextToken());
        }

        buildSegTree(0,0,n-1,arr);//idx,l,r,arr

        for(int i = 0 ; i < q; i++){

            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            if(type == 1)
            {
                int k  = Integer.parseInt(st.nextToken())-1;
                long u = Long.parseLong(st.nextToken());
                updatesegTree(0,0,n-1,k,u);
            }
            else {
                int start  = Integer.parseInt(st.nextToken())-1;
                int end  = Integer.parseInt(st.nextToken())-1;
                long ans  = querysegTree(0,0,n-1,start,end);
                bw.write(ans  + "\n");
            }
        }
        bw.flush();
    }

    private static long querysegTree(int idx, int l, int r, int start, int end) {
        //out of range
        if(l > end || r < start)
            return 0;

        if(start<=l && r <= end)
        {
            return segTree[idx];
        }
        int mid = l + (r-l)/2;

        return querysegTree(2*idx+1,l,mid,start,end) + querysegTree(2*idx+2,mid+1,r,start,end);
    }

    private static void updatesegTree(int idx, int l, int r, int i, long val) {
        if(l == r)
        {
            segTree[idx] = val;
            return;
        }
        int mid = l + (r-l)/2;
        if(i<=mid)
        {
            updatesegTree(2*idx+1,l,mid,i,val);
        }
        else {
            updatesegTree(2*idx+2,mid+1,r,i,val);
        }
        segTree[idx] = segTree[2*idx+1] + segTree[2*idx+2];
    }

    private static void buildSegTree(int idx, int l, int r, long[] arr) {
        if(l == r)
        {
            segTree[idx] = arr[l];
            return;
        }
        int mid = l + (r-l)/2;
        buildSegTree(2*idx+1,l,mid,arr);
        buildSegTree(2*idx+2,mid+1,r,arr);
        segTree[idx] = segTree[2*idx+1] + segTree[2*idx+2];
    }
}