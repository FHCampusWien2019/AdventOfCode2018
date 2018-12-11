package nl.tim.aoc.day11;

import nl.tim.aoc.Challenge;

public class Day11Challenge2 extends Challenge {
    private Day11Challenge1 base;

    @Override
    public void prepare() {
        base = new Day11Challenge1();

        base.prepare();
    }

    @Override
    public Object run() {
        base.populateField();

        return base.calcLargestArea(1, 300);
    }
}
