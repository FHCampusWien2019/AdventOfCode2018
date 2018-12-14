package nl.tim.aoc.day13;

import nl.tim.aoc.Challenge;

public class Day13Challenge2 extends Challenge
{
    private Day13Challenge1 base;

    @Override
    public void prepare() {
        base = new Day13Challenge1();

        base.prepare();
    }

    @Override
    public Object run(String alternative) {
        return base.simulate(false);
    }
}
