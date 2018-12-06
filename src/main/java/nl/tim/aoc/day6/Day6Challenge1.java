package nl.tim.aoc.day6;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Day6Challenge1 extends Challenge
{
    private String[] field;

    private int w;
    private int h;

    private List<String> read;

    @Override
    public void prepare() {
        read = Main.readFile("6.txt");
    }

    private void set(int x, int y, String s)
    {
        field[x + y * w] = s;
    }

    private String get(int x, int y)
    {
        return field[x + y * w];
    }

    @Override
    public Object run() {
        HashMap<String, Integer> area = new HashMap<>();
        HashMap<Integer, Integer> coords = new HashMap<>();

        // Setting up field
        for (String s : read){
            coords.put(Integer.valueOf(s.split(",")[0]), Integer.valueOf(s.split(",")[1].trim()));
        }

        int minX = Collections.min(coords.keySet());
        int maxX = Collections.max(coords.keySet());
        int minY = Collections.min(coords.values());
        int maxY = Collections.max(coords.values());

        w = maxX - minX + 2;
        h = maxY - minY + 2;

        field =  new String[(maxX - minX + 2) * (maxY - minY + 2)];

        for (int x = 0; x < w; x++)
        {
            field[x] = "INF";
            field[x + (maxY - minY) * w] = "INF";
        }

        for (int y = 0; y < h; y++)
        {
            field[y * w] = "INF";
            field[maxX - minX + y * w] = "INF";
        }

        // Populate
        for (String s : read) {
            int x = Integer.valueOf(s.split(",")[0]);
            int y = Integer.valueOf(s.split(",")[1].trim());

            area.put(s, 0);

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    String loc = get(i, j);

                    int dist = (x - i < 0 ? i - x : x - i) + (y - j < 0 ? j - y : y - j);

                    if (loc == null || loc.equals("")) {
                        set(i, j, s + "&" + dist);
                        continue;
                    }

                    if (loc.equals("INF")) {
                        set(i, j, s + "&" + dist + "&INF");
                        continue;
                    }

                    int otherDist = Integer.valueOf(loc.split("&")[1]);

                    if (otherDist == dist) {
                        set(i, j, s + "&" + dist + "&TIE" + (loc.contains("INF") ? "&INF" : ""));
                    } else if (dist < otherDist) {
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
                String loc = field[x + y * w];
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
