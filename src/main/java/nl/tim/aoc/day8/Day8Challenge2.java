package nl.tim.aoc.day8;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.ArrayList;
import java.util.List;

public class Day8Challenge2 extends Challenge
{
    private String read;

    @Override
    public void prepare() {
        read = Main.readFile("8.txt").get(0);
    }

    @Override
    public Object run() {
        List<Integer> in = new ArrayList<>();

        for (String s : read.split("\\s"))
        {
            in.add(Integer.valueOf(s));
        }

        return sum(in, 0);
    }

    private int getIndex(List<Integer> in, int startNode, int target)
    {
        return skip(in, startNode, target);
    }

    private int skip(List<Integer> in, int start, int stopAt)
    {
        int i = start;
        int children = in.get(i);
        int data = in.get(i + 1);

        i += 1;

        if (children < stopAt && stopAt > 0)
        {
            return -1;
        }

        for (int j = 0; j < children; j++)
        {
            if (j + 1 == stopAt)
            {
                return i + 1;
            }

            i = skip(in, i + 1, -2);
        }

        i += data;

        return i;
    }

    private int getDataIndex(List<Integer> in, int startNode)
    {
        return skip(in, startNode, -2) - in.get(startNode + 1) + 1;
    }

    private int sum(List<Integer> in, int start)
    {
        int children = in.get(start);
        int data = in.get(start + 1);
        int sum = 0;
        int dataIndex = getDataIndex(in, start);

        if (children == 0)
        {
            // Sum elements
            for (int i = 0; i < data; i++)
            {
                sum += in.get(dataIndex + i);
            }
        } else {
            // Use data as index
            for (int i = 0; i < data; i++) {
                int childIndex = getIndex(in, start, in.get(dataIndex + i));

                if (childIndex != -1) {
                    // Only add if child exists
                    sum += sum(in, childIndex);
                }
            }
        }

        return sum;
    }
}
