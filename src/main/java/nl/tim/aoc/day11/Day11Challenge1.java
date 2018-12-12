package nl.tim.aoc.day11;

import nl.tim.aoc.AlternativeMethod;
import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;
import nl.tim.aoc.day11.opencl.OpenCLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
Yet another disclaimer: I'm not that happy with this code, since it's basically an O(n^5) (it takes part two well over
1 minute) solution that is a bit camouflaged by threading. Will try to make this better after midterms.
 */
@AlternativeMethod(alternatives = {"opencl"})
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
    public Object run(String alternative) {
        if (alternative.equals("opencl"))
        {
            OpenCLHelper.setUpOpenCL();
            populateFieldOpenCL();

            String res = calcLargestAreaOpenCL(3, 3);

            OpenCLHelper.tearDownOpenCL();
            return res;
        } else
        {
            populateField();
            return calcLargestArea(3, 3);
        }
    }

    protected void populateFieldOpenCL()
    {
        this.field = OpenCLHelper.getPowerLevels(this.gridSerial);
    }

    protected String calcLargestAreaOpenCL(int min, int max)
    {
        int[] maxArea = OpenCLHelper.getBestArea(this.field, min, max);

        return maxArea[0] + "," + maxArea[1] + "," + maxArea[3];
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

        return ((((rackID * y + gridSerial) * rackID) / 100) % 10) - 5;
    }
}
