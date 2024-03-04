package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int c = 1; c <=128; c *= 2){
            int counter = c*1000;
            Ns.addLast(counter);
            opCounts.addLast(counter);
            AList<Integer> tempList = new AList<>();
            Stopwatch stopwatch = new Stopwatch();
            for (int k = 0; k < counter; k++){
                tempList.addLast(1);
            }
            times.addLast(stopwatch.elapsedTime());
        }
        printTimingTable(Ns, times, opCounts);
    }
}
