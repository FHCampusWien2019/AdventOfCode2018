package nl.tim.aoc.day1;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1Challenge2 extends Challenge
{
    private List<String> numbers;

    @Override
    public void prepare()
    {
        numbers = Main.readFile("1.txt");
    }

    @Override
    public Object run(String alternative)
    {
        Set<Integer> seen = new HashSet<>();
        int current = 0;

        while (true)
        {
            for (String s : numbers)
            {
                seen.add(current);

                int n = Integer.valueOf(s);
                current += n;

                if (seen.contains(current))
                {
                    return current;
                }
            }
        }
    }
}
