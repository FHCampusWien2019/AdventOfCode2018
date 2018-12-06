package nl.tim.aoc.day2;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.List;

public class Day2Challenge2 extends Challenge
{
    private List<String> read;

    @Override
    public void prepare()
    {
        read = Main.readFile("2.txt");
    }

    @Override
    public Object run()
    {
        // Forgive me Robert, for I have sinned..
        for (String s : read)
        {
            for (String c : read)
            {
                if (s.equals(c))
                {
                    continue;
                }

                int diff = 0;
                int lastDiff = 0;

                for (int i = 0; i < s.length(); i++)
                {
                    char a = s.charAt(i);
                    char b = c.charAt(i);

                    if (a != b){
                        lastDiff = i;
                        diff += 1;
                    }

                    // If the diff is two we can stop, will not be our match
                    if (diff >= 2){
                        break;
                    }
                }

                if (diff == 1)
                {
                    return new StringBuilder(s).deleteCharAt(lastDiff).toString();
                }
            }
        }

        return null;
    }
}
