class Solution {
    public ArrayList<Integer> prefixSum2D(int[][] mat, int[][] queries) {
        // code here
       int n=mat.length;
       int m=mat[0].length;
       int [][] prefix=new int[n][m];
       // frist row 
       prefix[0][0]=mat[0][0];
       for(int j=1;j<m;j++)
       {
           prefix[0][j]=prefix[0][j-1]+mat[0][j];
       }
       //first col
       for(int i=1;i<n;i++)
       {
           prefix[i][0]=prefix[i-1][0]+mat[i][0];
       }
       for(int i=1;i<n;i++)
       {
           for(int j=1;j<m;j++)
           {
               prefix[i][j]=mat[i][j]+prefix[i-1][j]+prefix[i][j-1]-prefix[i-1][j-1];
           }
       }
       ArrayList<Integer> result=new ArrayList<>();
       int len=queries.length;
       for(int i=0;i<len;i++)
       {
           int r1=queries[i][0];
           int c1=queries[i][1];
           int r2=queries[i][2];
           int c2=queries[i][3];
           
           int ans=0;
           ans+=prefix[r2][c2];
           if( r1>0 )
           {
               ans-=prefix[r1-1][c2];
           }
           if( c1>0)
           {
               ans-=prefix[r2][c1-1];
           }
           if( r1>0 && c1>0)
           {
               ans+=prefix[r1-1][c1-1];
           }
           result.add( ans);
       }
       return result;
    }
}
