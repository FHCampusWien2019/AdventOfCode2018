package nl.tim.aoc.day14;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.ArrayList;
import java.util.List;

public class Day14Challenge1 extends Challenge
{
    private int in = 84601;
    private List<Integer> table;
    private int firstElf;
    private int secondElf;
    private int found;
    private int[] search;

    @Override
    public void prepare() {
        String read = Main.readFile("14.txt").get(0);
        in = Integer.valueOf(read);
        search = new int[read.length()];

        // Fill in sequence
        for (int i = 0; i < read.length(); i++)
        {
            search[i] = Integer.valueOf(read.substring(i, i + 1));
        }

        // Set start params
        firstElf = 0;
        secondElf = 1;
        found = 0;
    }

    @Override
    public Object run(String alternative) {
        return makeRecipe(false);
    }

    protected Object makeRecipe(boolean searchForSequence)
    {
        // Init list of appropiate initial capacity
        table = new ArrayList<>(in * (searchForSequence ? 750 : 1) + 10);

        // Add first recipes
        table.add(3);
        table.add(7);

        do {
            int[] newRecipes = getNew(table.get(firstElf), table.get(secondElf));

            // Add new recipes
            for (int i : newRecipes) {
                if (i != -1) {
                    table.add(i);

                    if (searchForSequence && search[found] == i) {
                        found += 1;

                        if (found == search.length) {
                            return table.size() - found;
                        }
                    } else {
                        found = 0;
                    }
                }
            }

            // Move indices
            firstElf = newIndex(firstElf, table.get(firstElf) + 1);
            secondElf = newIndex(secondElf, table.get(secondElf) + 1);

        } while (searchForSequence || table.size() < in + 10);

        StringBuilder builder = new StringBuilder();

        for (int i = table.size() - 10; i < table.size(); i++)
        {
            builder.append(table.get(i));
        }

        return builder.toString();
    }

    private int[] getNew(int firstRecipe, int secondRecipe)
    {
        int sum = firstRecipe + secondRecipe;

        return new int[] {sum >= 10 ? sum / 10 : -1, sum % 10};
    }

    private int newIndex(int start, int steps)
    {
        for (int i = 0; i < steps; i++)
        {
            start += 1;
            start %= table.size();
        }

        return start;
    }
}
