package nl.tim.aoc.day12;

import nl.tim.aoc.Challenge;

public class Day12Challenge2 extends Challenge
{
    private Day12Challenge1 base;

    @Override
    public void prepare() {
        base = new Day12Challenge1();

        base.prepare();
    }

    @Override
    public Object run() {
        return base.tickState(50000000000L);
    }
}
