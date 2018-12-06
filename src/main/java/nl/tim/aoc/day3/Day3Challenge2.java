package nl.tim.aoc.day3;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day3Challenge2 extends Challenge
{
    private int[] field;
    private List<String> read;
    private Set<Integer> invalid;

    @Override
    public void prepare() {
        field = new int[1000*1000];
        invalid = new HashSet<>();
        read = Main.readFile("3.txt");
    }

    @Override
    public Object run() {
        for (String s : read)
        {
            String[] input = s.split("\\s");
            int id = Integer.valueOf(input[0].substring(1));
            int x = Integer.valueOf(input[2].split(",")[0]);
            int y = Integer.valueOf(input[2].split(",")[1].replace(":", ""));
            int width = Integer.valueOf(input[3].split("x")[0]);
            int heigth = Integer.valueOf(input[3].split("x")[1]);

            for (int i = x; i < width + x; i++)
            {
                for (int j = y; j < heigth + y; j++)
                {
                    if (field[i + j * 1000] != 0)
                    {
                        invalid.add(id);
                        invalid.add(field[i + j * 1000]);
                    }

                    field[i + j * 1000] = id;
                }
            }
        }

        for (int i = 1; i < read.size(); i++)
        {
            if (!invalid.contains(i))
            {
                return i;
            }
        }

        return null;
    }
}
