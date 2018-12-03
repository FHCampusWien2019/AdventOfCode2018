package nl.tim.aoc.day1;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1Challenge2 extends Challenge
{
    @Override
    public void prepare()
    {

    }

    @Override
    public Object run()
    {
        Set<Integer> seen = new HashSet<>();
        List<String> numbers = Main.readFile("1/1.txt");
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
