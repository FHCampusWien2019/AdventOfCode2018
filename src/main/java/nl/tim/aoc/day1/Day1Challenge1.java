package nl.tim.aoc.day1;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.List;

public class Day1Challenge1 extends Challenge
{
    @Override
    public void prepare()
    {

    }

    @Override
    public Object run()
    {
        int res = 0;
        List<String> numbers = Main.readFile("1/1.txt");

        for (String s : numbers) {
            res += Integer.valueOf(s);
        }

        return res;
    }
}
