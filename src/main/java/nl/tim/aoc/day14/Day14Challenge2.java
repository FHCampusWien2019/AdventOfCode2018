package nl.tim.aoc.day14;

import nl.tim.aoc.Challenge;

public class Day14Challenge2 extends Challenge
{
    private Day14Challenge1 base;

    @Override
    public void prepare() {
        base = new Day14Challenge1();

        base.prepare();
    }

    @Override
    public Object run(String alternative) {
        return base.makeRecipe(true);
    }
}
