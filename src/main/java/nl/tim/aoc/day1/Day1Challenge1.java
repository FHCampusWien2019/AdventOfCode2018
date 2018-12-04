package nl.tim.aoc.day1;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.List;

public class Day1Challenge1 extends Challenge
{
    private List<String> numbers;

    @Override
    public void prepare()
    {
        numbers = Main.readFile("1/1.txt");
    }

    @Override
    public Object run()
    {
        int res = 0;

        for (String s : numbers) {
            res += Integer.valueOf(s);
        }

        return res;
    }
}
