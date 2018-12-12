package nl.tim.aoc.day11;

import nl.tim.aoc.AlternativeMethod;
import nl.tim.aoc.Challenge;

@AlternativeMethod(alternatives = {"opencl"})
public class Day11Challenge2 extends Challenge {
    private Day11Challenge1 base;

    @Override
    public void prepare() {
        base = new Day11Challenge1();

        base.prepare();
    }

    @Override
    public Object run(String alternative) {
        if (alternative.equals("opencl"))
        {
            base.populateFieldOpenCL();
            return base.calcLargestAreaOpenCL(1, 300);
        } else
        {
            base.populateField();
            return base.calcLargestArea(1, 300);
        }
    }
}
