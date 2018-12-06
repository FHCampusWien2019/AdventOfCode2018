package nl.tim.aoc.day5;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

public class Day5Challenge2 extends Challenge
{
    private StringBuilder input;

    @Override
    public void prepare()
    {
        input = Day5Challenge1.reactPolymers(new StringBuilder(Main.readFile("5.txt").get(0)));
    }

    @Override
    public Object run() {
        int lowest = Day5Challenge1.reactPolymers(removeChar(input, 'a')).length();
        char[] charsToTest = {'b', 'c', 'd', 'e', 'f', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r','s',
        't', 'u', 'v', 'w', 'x', 'y', 'z'};

        for (char c : charsToTest)
        {
            int len = Day5Challenge1.reactPolymers(removeChar(input, c)).length();

            if (len < lowest)
            {
                lowest = len;
            }
        }

        return lowest;
    }

    public StringBuilder removeChar(StringBuilder input, char exclude)
    {
        input = new StringBuilder(input);

        int i = input.length() - 1;

        while (i >= 0)
        {
            char at = input.charAt(i);

            if (Character.toLowerCase(at) == Character.toLowerCase(exclude))
            {
                input.deleteCharAt(i);
            }

            i--;
        }

        return input;
    }
}
