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

    /* For your reading pleasure */
    private final int SLASH = 15;
    private final int BACKSLASH = 60;
    private final int PLUS = 11;
    private final int RIGHT = 30;
    private final int LEFT = 28;
    private final int DOWN = 86;
    private final int UP = 62;

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

                if (id == RIGHT || id == LEFT || id == DOWN || id == UP)
                {
                    carts.put(x + y * xMax, id);
                    id += id == RIGHT ? -17 : id == LEFT ? -15 : id == DOWN ? 6 : RIGHT;
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
    }

    @Override
    public Object run(String alternative)
    {
        return simulate(true);
    }

    protected String simulate(boolean firstCrash)
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
                        if (cartsLeft() == 1)
                        {
                            return x + "," + y;
                        }

                        int spot = field[x + y * xMax];
                        int type = carts[i + 3];

                        // Crossing
                        if (spot == PLUS)
                        {
                            int move = getMove(i);

                            if (move != 1)
                            {
                                int newType;

                                switch (type)
                                {
                                    case RIGHT:
                                        newType = move == 0 ? UP : DOWN;
                                        break;
                                    case LEFT:
                                        newType = move == 0 ? DOWN : UP;
                                        break;
                                    case DOWN:
                                        newType = move == 0 ? RIGHT : LEFT;
                                        break;
                                    default:
                                        newType = move == 0 ? LEFT : RIGHT;
                                        break;
                                }

                                carts[i + 3] = newType;
                            }
                        } else if (spot == SLASH)
                        {
                            carts[i + 3] = type == UP ? RIGHT : type == RIGHT ? UP : type == LEFT ? DOWN : LEFT;
                        } else if (spot == BACKSLASH)
                        {
                            carts[i + 3] = type == UP ? LEFT : type == RIGHT ? DOWN : type == LEFT ? UP : RIGHT;
                        }

                        type = carts[i + 3];
                        int newY = y + (type == DOWN ? 1 : type == UP ? -1 : 0);
                        int newX = x + (type == LEFT ? -1 : type == RIGHT ? 1 : 0);

                        // Check if a crash would happen
                        int indexNewLoc = getCart(newX, newY);
                        if (indexNewLoc != -1)
                        {
                            if (firstCrash)
                            {
                                // Found first crash
                                return newX + "," + newY;
                            } else
                            {
                                // Remove carts
                                carts[i] = -1;
                                carts[indexNewLoc] = -1;

                                continue;
                            }
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

    private int cartsLeft()
    {
        int count = 0;

        for (int i = 0; i < carts.length; i += 5)
        {
            count += carts[i] != -1 ? 1 : 0;
        }

        return count;
    }

    private int getMove(int i)
    {
        int move = carts[i + 2];

        carts[i + 2] = (move + 1) % 3;

        return move;
    }

    private int getCart(int x, int y)
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
