package nl.tim.aoc.day4;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Day4Challenge1 extends Challenge
{
    private HashMap<Integer, HashMap<Integer, Integer>> data;
    private List<String> read;

    @Override
    public void prepare() {
        data = new HashMap<>();
        read = Main.readFile("4/1.txt");

        read.sort(new Comp());
    }

    @Override
    public Object run() {
        populateHashMap(read, data);

        int currentGuard = -1;
        int maxTime = -1;
        int maxCount = -1;

        for (Integer guard : data.keySet())
        {
            HashMap<Integer, Integer> d = data.get(guard);

            int cnt = 0;
            int max = -1;
            int maxT = -1;

            for (Integer time : d.keySet())
            {
                Integer count = d.get(time);
                cnt += count;

                if (count > max)
                {
                    maxT = time;
                    max = count;
                }
            }

            if (cnt > maxCount) {
                currentGuard = guard;
                maxCount = cnt;
                maxTime = maxT;
            }
        }

        return currentGuard * maxTime;
    }

    public static void populateHashMap(List<String> r, HashMap<Integer, HashMap<Integer, Integer>> dIn)
    {
        int currentGuard = 0;
        String sleepingSince = "";

        for (String s : r)
        {
            String time = s.substring(11, 17);
            s = s.substring(18).trim();

            if (s.startsWith("Guard")) {
                currentGuard = Integer.valueOf(s.split("\\s")[1].substring(1));

                if (!dIn.containsKey(currentGuard))
                {
                    dIn.put(currentGuard, new HashMap<>());
                }
            }

            if (s.startsWith("falls")) {
                sleepingSince = time;
            }

            if (s.startsWith("wakes")) {
                for (int i = Integer.valueOf(sleepingSince.split(":")[1]); i < Integer.valueOf(time.split(":")[1]); i++)
                {
                    //System.out.println(data.get(currentGuard));
                    HashMap<Integer, Integer> dw = dIn.get(currentGuard);
                    int count = dw.getOrDefault(i, 0);

                    count += 1;

                    dIn.get(currentGuard).put(i, count);
                }
            }
        }
    }

    public static class Comp implements Comparator<String>
    {
        @Override
        public int compare(String o1, String o2) {
            return o1.substring(1, 17).compareTo(o2.substring(1, 17));
        }
    }
}
