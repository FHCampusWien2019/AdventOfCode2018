package nl.tim.aoc.day10;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.*;

public class Day10Challenge1 extends Challenge
{
    private List<String> read;
    private List<Velocity> points;
    private HashMap<Integer, Long> averageArea;

    @Override
    public void prepare() {
        read = Main.readFile("10.txt");
        points = new ArrayList<>();
        averageArea = new HashMap<>();
    }

    @Override
    public Object run() {
        int i = 0;
        int maxWait = 125000;
        Scanner scanner = new Scanner(System.in);

        for (String s : read)
        {
            int x = Integer.valueOf(s.split(",")[0].split("<")[1].trim());
            int y = Integer.valueOf(s.split(",")[1].split(">")[0].trim());
            int dx = Integer.valueOf(s.split(",")[1].split("<")[1].trim());
            int dy = Integer.valueOf(s.split(",")[2].split(">")[0].trim());

            points.add(new Velocity(x, y, dx, dy));
        }

        while (true)
        {
            long start = System.nanoTime();

            while (i < maxWait)
            {
                if (draw(i, false))
                {
                    break;
                }
                i++;
            }

            int lowestCount = 0;
            long lowestArea = Integer.MAX_VALUE;

            for (Integer count : averageArea.keySet()) {
                if (averageArea.get(count) < lowestArea) {
                    lowestArea = averageArea.get(count);
                    lowestCount = count;
                }
            }

            draw(lowestCount, true);

            System.out.print("That run took " + ((System.nanoTime() - start) / 1000000) + " ms." +
                    "\nDoes that answer look ok? (y/n): ");
            if (!scanner.nextLine().equals("n"))
            {
                // It's a bit overkill to write an entire class for just this part
                // so today will just have one class instead of two.
                return lowestCount;
            }

            // Didn't work, try again with bigger boundaries
            maxWait += 10000;
        }
    }

    /*
    Small disclaimer:
    This solution depends on the fact that all points are part of the characters and therefore get closer all the time
    If you would add a point that is not related to the characters in any way (e.g. floats away into space) the solution would probably break.
     */
    private boolean increasing(int count, long area)
    {
        return !averageArea.isEmpty() && averageArea.get(count - 1) < area;
    }

    private boolean draw(int i, boolean draw)
    {
        long xmin = Integer.MAX_VALUE;
        long xmax = Integer.MIN_VALUE;
        long ymin = Integer.MAX_VALUE;
        long ymax = Integer.MIN_VALUE;

        for (Velocity v : points)
        {
            int x = v.x + i * v.dx;
            int y = v.y + i * v.dy;

            // Swapped x & y to make it print fancier
            ymin = x < ymin ? x : ymin;
            ymax = x > ymax ? x : ymax;
            xmin = y < xmin ? y : xmin;
            xmax = y > xmax ? y : xmax;
        }

        long area = Math.abs((xmax - xmin) * (ymax - ymin));

        if ((area < 0 || increasing(i, area)) && !draw)
        {
            return true;
        }

        averageArea.put(i, area);

        if (draw)
        {
            for (long x = xmin; x <= xmax; x++) {
                for (long y = ymin; y <= ymax; y++) {
                    boolean found = false;

                    for (Velocity v : points)
                    {
                        // Swapped x and y to make it print fancier
                        if (y == v.x + i * v.dx && x == v.y + i * v.dy)
                        {
                            found = true;
                            break;
                        }
                    }

                    if (found)
                    {
                        System.out.print("#");
                    } else
                    {
                        System.out.print(".");
                    }
                }

                System.out.print("\n");
            }

            System.out.print("\n\n");
        }

        return false;
    }

    public class Velocity
    {
        int x;
        int y;
        int dx;
        int dy;

        public Velocity(int x, int y, int dx, int dy)
        {
            this.x = x;
            this.y = y;

            this.dx = dx;
            this.dy = dy;
        }
    }
}
