package nl.tim.aoc.day11;

public class CalcThread extends Thread {
    private int size;
    private Day11Challenge1 base;

    public CalcThread(int size, Day11Challenge1 base)
    {
        this.size = size;
        this.base = base;
    }

    @Override
    public void run()
    {
        int highestSum = Integer.MIN_VALUE;
        int xMax = 0;
        int yMax = 0;

        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                int sum = base.sum(x, y, size);

                if (sum > highestSum) {
                    highestSum = sum;
                    xMax = x + 1;
                    yMax = y + 1;
                }
            }
        }

        base.results.put(xMax + "," + yMax + "," + size, highestSum);
    }
}
