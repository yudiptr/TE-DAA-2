// Java program to find maximum achievable 
// value with a knapsack of weight W and 
// multiple instances allowed. 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public class Knapsack 
{ 
	
	private static int max(int i, int j) 
	{ 
			return (i > j) ? i : j; 
	} 
	
	// Returns the maximum value with knapsack 
	// of W capacity 
	private static int unboundedKnapsack(int W, int n, 
								int[] val, int[] wt) 
	{ 
		
		// dp[i] is going to store maximum value 
		// with knapsack capacity i. 
		int dp[] = new int[W + 1]; 
		
		// Fill dp[] using above recursive formula 
		for(int i = 0; i <= W; i++){ 
			for(int j = 0; j < n; j++){ 
				if(wt[j] <= i){ 
					dp[i] = max(dp[i], dp[i - wt[j]] + 
								val[j]); 
				} 
			} 
		} 
		return dp[W]; 
	} 

	// Driver program 
	public static void main(String[] args) 
	{ 

		// String filePath = "data_100.txt";
        // String filePath = "data_1000.txt";
        String filePath = "data_10000.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {	
			int W;
            int[] v;
            int[] w;
            // Read W
            String line = br.readLine();
            W = Integer.parseInt(line);

            // Read val (v)
            line = br.readLine();
            String[] valStr = line.split(", ");
            v = Arrays.stream(valStr).mapToInt(Integer::parseInt).toArray();

            // Read wt (w)
            line = br.readLine();
            String[] wtStr = line.split(", ");
            w = Arrays.stream(wtStr).mapToInt(Integer::parseInt).toArray();

            // for (int i = 0 ; i < v.length ; i++){
            //     N.add(i);
            // }

            int n = v.length; 

			java.lang.management.MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
			MemoryUsage beforeMemoryUsage = memoryMXBean.getHeapMemoryUsage();
			long beforeUsedMem = beforeMemoryUsage.getUsed();
            long begin = System.currentTimeMillis();

			System.out.println("HASIL AKHIR DP UNTUK DATASET " + w.length);
            System.out.println(unboundedKnapsack(W, n, v, w));

			MemoryUsage afterMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        	long afterUsedMem = afterMemoryUsage.getUsed();
			long end = System.currentTimeMillis();
            long time = end-begin;
            long actualMemUsed=(afterUsedMem-beforeUsedMem);

            System.out.println("Elapsed Time: "+time +" milli seconds");
            System.out.println("Memory Usage: "+actualMemUsed+ " byte");
        } catch (IOException e) {
            e.printStackTrace();
        }
	} 
} 
// This code is contributed by Aditya Kumar 
