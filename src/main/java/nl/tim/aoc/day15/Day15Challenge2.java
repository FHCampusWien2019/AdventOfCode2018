package nl.tim.aoc.day15;

import nl.tim.aoc.Challenge;

public class Day15Challenge2 extends Challenge
{
    private Day15Challenge1 base;

    @Override
    public void prepare()
    {
        base = new Day15Challenge1();

        base.prepare();
    }

    @Override
    public Object run(String alternative)
    {
        return base.run(true, 15);
    }
}
