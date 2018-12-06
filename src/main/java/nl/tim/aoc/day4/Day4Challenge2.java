package nl.tim.aoc.day4;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.HashMap;
import java.util.List;

public class Day4Challenge2 extends Challenge
{
    private HashMap<Integer, HashMap<Integer, Integer>> data;
    private List<String> read;

    @Override
    public void prepare() {
        data = new HashMap<>();
        read = Main.readFile("4.txt");

        read.sort(new Day4Challenge1.Comp());
    }

    @Override
    public Object run() {
        Day4Challenge1.populateHashMap(read, data);

        int currentGuard = -1;
        int maxTime = -1;
        int maxCount = -1;

        for (Integer guard : data.keySet())
        {
            HashMap<Integer, Integer> d = data.get(guard);

            for (Integer time : d.keySet())
            {
                Integer count = d.get(time);

                if (count > maxCount)
                {
                    currentGuard = guard;
                    maxTime = time;
                    maxCount = count;
                }
            }

        }

        return currentGuard * maxTime;
    }
}
