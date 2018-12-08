package nl.tim.aoc.day8;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.ArrayList;
import java.util.List;

public class Day8Challenge1 extends Challenge
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

        return calc(in);
    }

    private int calc(List<Integer> input)
    {
        if (input.isEmpty())
        {
            return 0;
        }

        int children = input.remove(0);
        int data = input.remove(0);
        int sum = 0;

        for (int i = 0; i < children; i++)
        {
            sum += calc(input);
        }

        for (int i = 0; i < data; i++)
        {
            sum += input.remove(0);
        }

        return sum;
    }
}
