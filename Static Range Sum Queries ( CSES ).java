import java.io.*;
import java.util.*;

public class sg
{
    public static void main(String[] args)throws IOException
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in) );
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(System.out ));
        StringTokenizer st=new StringTokenizer(br.readLine() );
        int n=Integer.parseInt(st.nextToken());
        int q=Integer.parseInt(st.nextToken());

        long [] ar=new long[n];
        long[] pr=new long[n];
        StringTokenizer srr=new StringTokenizer(br.readLine());
        for(int i=0;i<n;i++)
        {
            ar[i]=Long.parseLong( srr.nextToken() );
        }
        pr[0]=ar[0];
        for(int i=1;i<n;i++)
        {
            pr[i]=pr[i-1]+ar[i];
        }
        
      
        while( q-->0 )
        {
            StringTokenizer s=new StringTokenizer(br.readLine());
            int a=Integer.parseInt(s.nextToken());
            int b=Integer.parseInt(s.nextToken());
            a=a-1;
            b=b-1;
            if( a==0)
            {
                bw.write( String.valueOf( pr[b] ) );
            }
            else 
            {
                bw.write( String.valueOf( pr[b]-pr[a-1] ) );
            }
            bw.write("\n");
  
        }
        bw.flush();
    
    }
    
}