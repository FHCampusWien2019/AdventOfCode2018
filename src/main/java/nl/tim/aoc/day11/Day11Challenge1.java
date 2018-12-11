package nl.tim.aoc.day11;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
Yet another disclaimer: I'm not that happy with this code, since it's basically an O(n^5) (it takes part two well over
1 minute) solution that is a bit camouflaged by threading.
After my midterms are over I will try to use OpenCL for the first time to maybe hide even more the fact that it's O(n^5).
But before that I'll probably implement a summed area table
 */
public class Day11Challenge1 extends Challenge
{
    private int gridSerial;
    private int[] field;
    protected ConcurrentHashMap<String, Integer> results;

    @Override
    public void prepare() {
        gridSerial = Integer.valueOf(Main.readFile("11.txt").get(0));
        field = new int[300*300];
        results = new ConcurrentHashMap<>();
    }

    @Override
    public Object run() {
        populateField();

        return calcLargestArea(3, 3);
    }


    protected String calcLargestArea(int min, int max)
    {
        int highestSum = Integer.MIN_VALUE;
        String result = "";

        List<CalcThread>  threads = new ArrayList<>();

        for (int i = min; i <= max; i++) {
            CalcThread thread = new CalcThread(i, this);

            threads.add(thread);
            thread.start();
        }

        try
        {
            for (CalcThread thread : threads)
            {
                thread.join();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        for (String res : results.keySet())
        {
            int sum = results.get(res);

            if (sum > highestSum)
            {
                highestSum = sum;
                result = res;
            }
        }

        return result;
    }

    protected int sum(int x, int y, int size)
    {
        int sum = 0;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (x+i >= 300 && y+j >= 300)
                {
                    return sum;
                }

                if (x+i >= 300 || y+j >= 300)
                {
                    continue;
                }

                sum += field[x + i + (y + j) * 300];
            }
        }

        return sum;
    }

    protected void populateField()
    {
        for (int x = 0; x < 300; x++)
        {
            for (int y = 0; y < 300; y++)
            {
                field[x + y * 300] = getPowerLevel(x, y);
            }
        }
    }

    // Input normal coords
    private int getPowerLevel(int x, int y)
    {
        int rackID = x + 11;
        String powerLevel = "" + ((rackID * (y + 1) + gridSerial) * rackID) / 100;

        return Integer.valueOf(powerLevel.substring(powerLevel.length() - 1)) - 5;
    }
}
