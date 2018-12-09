package nl.tim.aoc.day9;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.*;

public class Day9Challenge1 extends Challenge
{
    private int players;
    protected int end;
    private LinkedList<Integer> game;
    private HashMap<Integer, Long> score;

    @Override
    public void prepare() {
        List<String> read = Main.readFile("9.txt");
        game = new LinkedList<>();
        score = new HashMap<>();

        players = Integer.valueOf(read.get(0).split("\\s")[0]);
        end = Integer.valueOf(read.get(0).split("\\s")[6]);
    }

    @Override
    public Object run() {
        int currentPoint = 1;
        int player = 0;

        game.addFirst(0);

        while (currentPoint <= end)
        {
            if (currentPoint % 23 == 0)
            {
                rot(-7);
                score.put(player, score.getOrDefault(player, 0L) + currentPoint + game.removeFirst());
            } else
            {
                rot(2);
                game.addLast(currentPoint);
            }

            currentPoint += 1;
            player += 1;
            player %= players;
        }

        // 3443939356
        // 408679
        return Collections.max(score.values());
    }

    private void rot(int rotation)
    {
        if (rotation >= 0)
        {
            for (int i = 0; i < rotation; i++)
            {
                game.addFirst(game.removeLast());
            }
        } else
        {
            for (int i = 0; i < 0 - rotation - 1; i++)
            {
                game.addLast(game.removeFirst());
            }
        }
    }
}
