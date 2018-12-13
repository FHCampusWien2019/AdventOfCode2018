package nl.tim.aoc.day13;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.*;

public class Day13Challenge1 extends Challenge
{
    private int[] field;
    private int[] carts;

    private int xMax;
    private int yMax;

    @Override
    public void prepare()
    {
        List<String> lines = Main.readFile("13.txt");
        HashMap<Integer, Integer> carts = new HashMap<>();
        xMax = lines.stream().max(Comparator.comparingInt(String::length)).get().length();
        yMax = lines.size();

        field = new int[yMax * xMax];

        // Setup field
        int y = 0;
        for (String s : lines)
        {
            for (int x = 0; x < s.length(); x++)
            {
                int id = ((int) s.charAt(x)) - 32;

                if (id == 30 || id == 28 || id == 86 || id == 62)
                {
                    carts.put(x + y * xMax, id);
                    id += id == 30 ? -17 : id == 28 ? -15 : id == 86 ? 6 : 30;
                }

                field[x + y * xMax] = id;
            }

            y += 1;
        }

        this.carts = new int[5 * carts.size()];
        int i = 0;

        // Load carts
        for (Integer loc : carts.keySet())
        {
            int x = loc % xMax;
            y = loc / xMax;

            this.carts[i] = x;
            this.carts[i + 1] = y;
            this.carts[i + 2] = 0; // Next -> 0: left & 1: straight & 2: right
            this.carts[i + 3] = carts.get(loc);
            this.carts[i + 4] = i / 5;

            i += 5;
        }

        System.out.println((int) ' ' - 32); // 0
        System.out.println((int) '|' - 32); // 92 (+6 & +30)
        System.out.println((int) '/' - 32); // 15
        System.out.println((int) '\\' - 32); // 60
        System.out.println((int) '+' - 32); // 11
        System.out.println((int) '-' - 32); // 13 (-17 & -15)

        System.out.println((int) '>' - 32); // 30
        System.out.println((int) '<' - 32); // 28
        System.out.println((int) 'v' - 32); // 86
        System.out.println((int) '^' - 32); // 62
        System.out.println(Arrays.toString(field));
        System.out.println(Arrays.toString(this.carts));
    }

    @Override
    public Object run(String alternative)
    {
        List<Integer> done = new ArrayList<>();

        while (true)
        {
            for (int y = 0; y < yMax; y++)
            {
                for (int x = 0; x < xMax; x++)
                {
                    int i = getCart(x, y);

                    if (i != -1 && !done.contains(carts[i + 4]))
                    {
                        int spot = field[x + y * xMax];
                        int type = carts[i + 3];

                        // Crossing
                        if (spot == 11)
                        {
                            int move = getMove(i);

                            if (move != 1)
                            {
                                int newType;

                                switch (type)
                                {
                                    case 30:
                                        newType = move == 0 ? 62 : 86;
                                        break;
                                    case 28:
                                        newType = move == 0 ? 86 : 62;
                                        break;
                                    case 86:
                                        newType = move == 0 ? 30 : 28;
                                        break;
                                    default:
                                        newType = move == 0 ? 28 : 30;
                                        break;
                                }

                                carts[i + 3] = newType;
                            }
                        } else if (spot == 15)
                        {
                            carts[i + 3] = type == 62 ? 30 : type == 30 ? 62 : type == 28 ? 86 : 28;
                        } else if (spot == 60)
                        {
                            carts[i + 3] = type == 62 ? 28 : type == 30 ? 86 : type == 28 ? 62 : 30;
                        }

                        type = carts[i + 3];
                        int newY = y + (type == 86 ? 1 : type == 62 ? -1 : 0);
                        int newX = x + (type == 28 ? -1 : type == 30 ? 1 : 0);

                        // Check if a crash would happen
                        if (getCart(newX, newY) != -1)
                        {
                            return newX + "," + newY;
                        }

                        carts[i] = newX;
                        carts[i + 1] = newY;
                        done.add(carts[i + 4]);
                    }
                }
            }

            done.clear();
        }
    }

    public int getMove(int i)
    {
        int move = carts[i + 2];

        carts[i + 2] = (move + 1) % 3;

        return move;
    }

    public int getCart(int x, int y)
    {
        for (int i = 0; i < carts.length; i += 5)
        {
            if (carts[i] == x && carts[i + 1] == y)
            {
                return i;
            }
        }

        return -1;
    }
}
