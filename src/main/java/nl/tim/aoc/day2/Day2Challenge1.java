package nl.tim.aoc.day2;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.HashMap;
import java.util.List;

public class Day2Challenge1 extends Challenge
{
    private List<String> read;

    @Override
    public void prepare() {
        read = Main.readFile("2.txt");
    }

    @Override
    public Object run(String alternative) {
        int two = 0;
        int three = 0;

        for (String s : read)
        {
            boolean firstSet = false;
            boolean secondSet = false;

            HashMap<Character, Integer> count = new HashMap<>();

            for (int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);

                if (count.containsKey(c)) {
                    count.put(c, count.get(c) + 1);
                } else {
                    count.put(c, 1);
                }
            }

            for (Character c : count.keySet())
            {
                if (!firstSet && count.get(c) == 2)
                {
                    two += 1;
                    firstSet = true;
                    continue;
                }

                if (!secondSet && count.get(c) == 3)
                {
                    three += 1;
                    secondSet = true;
                }
            }
        }

        return two * three;
    }
}
