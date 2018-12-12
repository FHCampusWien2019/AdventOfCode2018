package nl.tim.aoc.day9;

import nl.tim.aoc.Challenge;

public class Day9Challenge2 extends Challenge
{
    private Day9Challenge1 base;

    @Override
    public void prepare() {
        base = new Day9Challenge1();

        base.prepare();
        base.end *= 100; // rip my first array-based solution
    }

    @Override
    public Object run(String alternative) {
        return base.run("default");
    }
}
