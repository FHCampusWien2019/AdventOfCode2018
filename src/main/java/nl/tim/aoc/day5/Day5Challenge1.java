package nl.tim.aoc.day5;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

public class Day5Challenge1 extends Challenge
{
    private StringBuilder input;

    @Override
    public void prepare()
    {
        input = new StringBuilder(Main.readFile("5.txt").get(0));
    }

    @Override
    public Object run(String alternative)
    {
        return reactPolymers(input).toString().length();
    }

    public static StringBuilder reactPolymers(StringBuilder input)
    {
        int i = 0;
        while (true)
        {
            if (i >= input.length() - 1)
            {
                return input;
            }

            char a = input.charAt(i);
            char b = input.charAt(i + 1);

            if (a != b && Character.toLowerCase(a) == Character.toLowerCase(b))
            {
                input.deleteCharAt(i + 1);
                input.deleteCharAt(i);
                i = 0 > i - 1 ? 0 : i - 1;

                continue;
            }

            i++;
        }
    }

}
