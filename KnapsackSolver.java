import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
public class KnapsackSolver{
    static boolean debug = false;

    static List<Integer> sortedV = new ArrayList<>();
    static List<Integer> m = new ArrayList<>();
    static List<Integer> sortedW = new ArrayList<>();
    static List<Integer> N = new ArrayList<Integer>(); 
    static List<Integer> Xtop = new ArrayList<Integer>(); 
    static List<Integer> X = new ArrayList<Integer>(); 
    static int i = 0;
    static int Ztop = 0;
    static int V_N = 0;
    static int W_up = 0;
    static double U = 0;
    static int W = 0;
    static int[][] M;

    public static void main(String args[]){


        // String filePath = "data_100.txt";
        // String filePath = "data_1000.txt";
        String filePath = "data_10000.txt";


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
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

            for (int i = 0 ; i < v.length ; i++){
                N.add(i);
            }

            java.lang.management.MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
			MemoryUsage beforeMemoryUsage = memoryMXBean.getHeapMemoryUsage();
			long beforeUsedMem = beforeMemoryUsage.getUsed();
            long begin = System.currentTimeMillis();

            eliminateDominated(w, v);
            step_1_initialize();
            
            MemoryUsage afterMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        	long afterUsedMem = afterMemoryUsage.getUsed();
			long end = System.currentTimeMillis();
            long time = end-begin;
            long actualMemUsed=(afterUsedMem-beforeUsedMem);

            System.out.println("Elapsed Time: "+time +" milli seconds");
            System.out.println("Memory Usage: "+actualMemUsed+ " byte");

            System.out.println("HASIL AKHIR BnB UNTUK DATASET " +  w.length);
            System.out.println(Ztop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void step_1_initialize(){
        // STEP 1 INITIALIZE
        M = new int[N.size()][W];
        X.add(0, W / sortedW.get(0));
        V_N = sortedV.get(0) * X.get(0);
        W_up = W - sortedW.get(0)*X.get(0);
        U = calculateU(sortedW, sortedV, W_up, V_N, i, N.size());
        Xtop.addAll(X);

        for (int j = 0 ; j < N.size() ; j++){
            int min = Integer.MAX_VALUE;
            for (int k = 0 ; k < sortedV.size() ; k++){ 
                if (k > j && sortedW.get(k) < min){
                    min = sortedW.get(k);
                }
            }
            m.add(min);
        }

        if (debug) {
            System.out.println("AFTER INITIALIZE");
            System.out.println(X);
            System.out.println(i);
            System.out.println(V_N);
            System.out.println(W_up);
            System.out.println(U);
            System.out.println(m);
        }

        step_2_develop();
    }

    public static void step_2_develop(){
        //STEP 2 DEVELOP  
        boolean param = true;  
        while (param) {
            if (W_up < m.get(i)){
                if (Ztop < V_N){
                    Ztop = V_N;
                    Xtop = X;
                    if (Ztop == U){
                        // INI HARUSNYA FINISH
                        param = false;
                        if (debug) {
                            System.out.println("AFTER 2.1");
                            System.out.println(X);
                            System.out.println(i);
                            System.out.println(V_N);
                            System.out.println(W_up);
                            System.out.println(U);
                            System.out.println(m);
                            }
                        step_3_backtrack();
                        break;
                    }
                }
                param = false;
                if (debug) {
                    System.out.println("AFTER 2.2");
                    System.out.println(X);
                    System.out.println(i);
                    System.out.println(V_N);
                    System.out.println(W_up);
                    System.out.println(U);
                    System.out.println(m);
                }
                step_3_backtrack();
                break;
                // backtrack
            }
            else {
                Integer min_j = findMinJ(i, W_up, sortedW, V_N);
                if ((min_j == null) || (V_N + calculateU(sortedW, sortedV, W_up, V_N, min_j, N.size() ) < Ztop)){
                    param = false;
                    if (debug) {
                        System.out.println("AFTER 2.3");
                        System.out.println(X);
                        System.out.println(i);
                        System.out.println(V_N);
                        System.out.println(W_up);
                        System.out.println(U);
                        System.out.println(m);
                    }    
                    step_3_backtrack();
                    break;
                    // backtrack
                }

                if (M[i][W_up] >= V_N){
                    param = false;
                    if (debug) {
                        System.out.println("AFTER 2.4");
                        System.out.println(X);
                        System.out.println(i);
                        System.out.println(V_N);
                        System.out.println(W_up);
                        System.out.println(U);
                        System.out.println(m);
                    } 
                    step_3_backtrack();
                    break;
                    // backtrack
                }
                X.add(min_j, W_up / sortedW.get(min_j));
                V_N += sortedV.get(min_j) * X.get(min_j);
                W_up -= sortedW.get(min_j) * X.get(min_j);
                M[i][W_up] = V_N;
                i = min_j;
            }

        }
    }

    public static void step_3_backtrack(){
        boolean param = true;
        while (param) { 
            Integer max_j = findMaxJ(i, sortedW, V_N);
            if (max_j == null){
                param = false;
                if (debug) {
                    System.out.println("AFTER 3.1");
                    System.out.println(X);
                    System.out.println(i);
                    System.out.println(V_N);
                    System.out.println(W_up);
                    System.out.println(U);
                    System.out.println(m);
                }
                break;
            }

            if (max_j < 0){
                param = false;
                if (debug) {
                    System.out.println("AFTER 3.2");
                    System.out.println(X);
                    System.out.println(i);
                    System.out.println(V_N);
                    System.out.println(W_up);
                    System.out.println(U);
                    System.out.println(m);
                } 
                break;
            }

            i = max_j;
            X.set(i, X.get(i) - 1);
            V_N -= sortedV.get(i);
            W_up += sortedW.get(i);
            if (W_up < m.get(i)){
                if (debug) {
                        System.out.println("AFTER 3.3");
                        System.out.println(X);
                        System.out.println(i);
                        System.out.println(V_N);
                        System.out.println(W_up);
                        System.out.println(U);
                        System.out.println(m);
                    }
                continue;
            }

            if ((V_N + (Math.floor( (W_up * sortedV.get(i+1) / sortedW.get(i+1)) ) ) <= Ztop)){
                V_N -= sortedV.get(i) * X.get(i);
                W_up +=  sortedW.get(i) * X.get(i);
                X.set(i, 0);
                if (debug) {
                    System.out.println("AFTER 3.4");
                    System.out.println(X);
                    System.out.println(i);
                    System.out.println(V_N);
                    System.out.println(W_up);
                    System.out.println(U);
                    System.out.println(m);
                }
                continue;
            }

            if (W_up >= m.get(i)){
                param = false;
                if (debug) {
                    System.out.println("AFTER 3.5");
                    System.out.println(X);
                    System.out.println(i);
                    System.out.println(V_N);
                    System.out.println(W_up);
                    System.out.println(U);
                    System.out.println(m);
                }
                step_2_develop();
                break;
            }
        }
    }

    public static void eliminateDominated(int[] w, int[]v){
        // Elminate dominated items
        int j = 0;
        while (j < N.size() - 1) {
            int k = j + 1;
            while (k < N.size()) {
                if ((w[N.get(k)]/w[N.get(j)]) * v[N.get(j)] >= v[N.get(k)]) {
                    N.remove(k);
                    k++;
                }
                else if ((w[N.get(j)]/w[N.get(k)]) * v[N.get(k)] >= v[N.get(j)]) {
                    N.remove(j);
                    k = N.size();
                }
                else {
                    k++;
                }
            }
            j++;
        }

        // sort based on v/w
        N.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                double ratio1 = (double) v[i1] / w[i1];
                double ratio2 = (double) v[i2] / w[i2];

                if (ratio1 < ratio2) {
                    return 1;
                } else if (ratio1  > ratio2) {
                    return -1;
                }
                return 0;
            }
        });

        for (int index : N) {
            sortedV.add(v[index]);
            sortedW.add(w[index]);
        }
    }


    public static double calculateU(List<Integer> w, List<Integer> v, int W_up, int V_N, int i, int n){
        if (i+2 < n){

            int z_up = V_N + (W_up / w.get(i+1)) * v.get(i+1);
            int w_up_up = W_up - (W_up / w.get(i+1)) * w.get(i+1);

            int u_up = z_up + (w_up_up * v.get(i+2) ) / w.get(i+2);

            double w_up_up_optimized = w_up_up + Math.ceil((1/w.get(i) * (w.get(i+1) - w_up_up) )) * w.get(1);
            double u_up_up = z_up + Math.floor((w_up_up_optimized * v.get(i+1) / w.get(i+1)) - Math.ceil( (1/w.get(i)) * (w.get(i+1) - w_up_up)) * v.get(i));

            double U = Math.max(u_up, u_up_up);
            return U;

        }   
        else return V_N;
    }

    public static Integer findMinJ(int i, int w_up, List<Integer> w, int n) {
        Integer min_j = null;
        for (int j = i+1 ; j<w.size() ; j++){
            if (w.get(j) <= w_up ){
                if (min_j == null || min_j > w.get(j)) min_j = j;
            }
        }
        return min_j;
    }

    public static Integer findMaxJ(int i, List<Integer> w, int n) {
        Integer max_j = null;
        
        for (int j = 0 ; j <= i ; j++){
            if (X.get(j) > 0){
                if (max_j == null) max_j = j;
                else if (max_j < j) max_j = j;
            }
        }
        return max_j;
    }

    
}



