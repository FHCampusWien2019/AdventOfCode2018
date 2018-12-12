package nl.tim.aoc.day6;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6Challenge2 extends Challenge
{
    private Set<Point> points;
    private int startX = Integer.MIN_VALUE;
    private int stopX = Integer.MAX_VALUE;
    private int startY = Integer.MIN_VALUE;
    private int stopY = Integer.MAX_VALUE;

    @Override
    public void prepare() {
        List<String> read = Main.readFile("6.txt");
        points = new HashSet<>();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (String s : read)
        {
            int x = Integer.valueOf(s.split(",")[0]);
            int y = Integer.valueOf(s.split(",")[1].trim());

            minX = x < minX ? x : minX;
            maxX = x > maxX ? x : maxX;
            minY = y < minY ? y : minY;
            maxY = y > maxY ? y : maxY;

            points.add(new Point(x, y));
        }

        startX = minX - (10000 - (maxX - minX)) / (read.size() - 1);
        stopX = maxX + (10000 - (maxX - minX)) / (read.size() - 1);
        startY = minY - (10000 - (maxY - minY)) / (read.size() - 1);
        stopY = maxY + (10000 - (maxY - minY)) / (read.size() - 1);
    }

    @Override
    public Object run(String alternative) {
        int count = 0;

        for (int x = startX; x <= stopX; x++)
        {
            for (int y = startY; y <= stopY; y++)
            {
                int dist = 0;

                for (Point p : points)
                {
                    int i = (int) p.getX();
                    int j = (int) p.getY();
                    dist += Math.abs(x - i) + Math.abs(y - j);

                    if (dist > 10000)
                    {
                        break;
                    }
                }

                if (dist <= 10000)
                {
                    count += 1;
                }
            }
        }

        return count;
    }
}
