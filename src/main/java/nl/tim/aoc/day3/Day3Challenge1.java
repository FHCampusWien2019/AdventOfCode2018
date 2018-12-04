package nl.tim.aoc.day3;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.List;

public class Day3Challenge1 extends Challenge
{
    private int[] field;
    private List<String> read;


    @Override
    public void prepare() {
        field = new int[1000*1000];
        read = Main.readFile("3/1.txt");
    }

    @Override
    public Object run() {
        for (String s : read)
        {
            String[] input = s.split("\\s");
            int x = Integer.valueOf(input[2].split(",")[0]);
            int y = Integer.valueOf(input[2].split(",")[1].replace(":", ""));
            int width = Integer.valueOf(input[3].split("x")[0]);
            int heigth = Integer.valueOf(input[3].split("x")[1]);

            for (int i = x; i < width + x; i++)
            {
                for (int j = y; j < heigth + y; j++)
                {
                    field[i + j * 1000] = field[i + j * 1000] + 1;
                }
            }
        }

        int count = 0;

        for (int x = 0; x < 1000; x++)
        {
            for (int y = 0; y < 1000; y++)
            {
                if (field[x + y * 1000] > 1){
                    count += 1;
                }
            }
        }

        return count;
    }
}
