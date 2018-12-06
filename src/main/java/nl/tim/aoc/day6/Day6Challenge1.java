package nl.tim.aoc.day6;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Day6Challenge1 extends Challenge
{
    private String[] field;
    private int offsetX;
    private int offsetY;
    private int w;
    private int h;

    private List<String> read;

    @Override
    public void prepare() {
        read = Main.readFile("6.txt");
        HashMap<Integer, Integer> coords = new HashMap<>();

        for (String s : read){
            coords.put(Integer.valueOf(s.split(",")[0]), Integer.valueOf(s.split(",")[1].trim()));
        }

        //int minX = Collections.min(coords.keySet()) - diff;
        int maxX = Collections.max(coords.keySet()) + 1;
        //int minY = Collections.min(coords.values()) - diff;
        int maxY = Collections.max(coords.values()) + 1;

        offsetX = 0;
        offsetY = 0;
        w = maxX;
        h = maxY;
        field =  new String[maxX * maxY];
        System.out.println(field.length);

        for (int x = 0; x < maxX; x++)
        {
            field[x] = "INF";
            field[x + (maxY - 1) * w] = "INF";
            //set(x, maxY - 1, "INF");
        }

        for (int y = 0; y < maxX; y++)
        {
            field[y * w] = "INF";
            field[maxX - 1 + y * w] = "INF";

            //set(0, y, "INF");
            //set(maxX - 1, y, "INF");
        }
    }

    public void set(int x, int y, String s)
    {
        field[(x + offsetX) + (y + offsetY) * w] = s;
    }

    public String get(int x, int y)
    {
        return field[(x + offsetX) + (y + offsetY) * w];
    }

    @Override
    public Object run() {
        HashMap<String, Integer> area = new HashMap<>();

        // Populate
        for (int ii = 0; ii < read.size(); ii++)
        {
            String s = read.get(ii);
            int x = Integer.valueOf(s.split(",")[0]);
            int y = Integer.valueOf(s.split(",")[1].trim());

            area.put(s, 0);

            for (int i = 0; i < w; i++)
            {
                for (int j = 0; j < h; j++)
                {
                    String loc = get(i, j);
                    //System.out.println(loc);
                    int dist = (x - i < 0 ? i - x : x - i) + (y - j < 0 ? j - y : y - j);

                    if (loc == null || loc.equals(""))
                    {
                        set(i, j, s + "&" + dist);
                        continue;
                    }

                    if (loc.equals("INF"))
                    {
                        set(i, j, s + "&" + dist + "&INF");
                        continue;
                    }

                    int otherDist = Integer.valueOf(loc.split("&")[1]);

                    if (otherDist == dist)
                    {
                        set(i, j, s + "&" + dist + "&TIE" + (loc.contains("INF") ? "&INF" : ""));
                    } else if (dist < otherDist)
                    {
                        set(i, j, s + "&" + dist + (loc.contains("INF") ? "&INF" : ""));
                    }
                }
            }
        }

        // Count
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                String loc = get(x, y);
                String point = loc.split("&")[0];

                if (loc.contains("INF"))
                {
                    area.put(point, -1);
                } else
                {
                    int currentArea = area.get(point);

                    if (currentArea != -1 && !loc.contains("TIE"))
                    {
                        area.put(point, currentArea + 1);
                    }
                }
            }
        }

        return Collections.max(area.values());
    }
}
