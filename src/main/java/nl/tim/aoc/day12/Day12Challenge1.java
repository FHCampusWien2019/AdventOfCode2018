package nl.tim.aoc.day12;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.ArrayList;
import java.util.List;

public class Day12Challenge1 extends Challenge
{
    private List<Integer> state;
    private List<Integer> newState;
    private List<Rule> rules;
    private int offset;
    private int newOffset;

    @Override
    public void prepare() {
        state = new ArrayList<>();
        newState = new ArrayList<>();
        rules = new ArrayList<>();
        offset = 0;
        newOffset = 0;

        List<String> read = Main.readFile("12.txt");
        String initialState = read.get(0).split("\\s")[2].trim();

        for (int i = 0; i < initialState.length(); i++)
        {
            String val = initialState.substring(i, i + 1);

            if (val.equals("#"))
            {
                state.add(1);
            } else
            {
                state.add(0);
            }
        }

        read.remove(0);
        for (String rule : read)
        {
            int res = rule.split("\\s=>\\s")[1].equals("#") ? 1 : 0;
            int[] state = new int[5];

            for (int i = 0; i < 5; i++)
            {
                state[i] = rule.split("\\s=>\\s")[0].substring(i, i + 1).equals("#") ? 1 : 0;
            }

            rules.add(new Rule(state, res));
        }
    }

    @Override
    public Object run(String alternative) {
        return tickState(20);
    }

    private long calcScore()
    {
        long sum = 0;

        for (int i = 0; i < state.size(); i++)
        {
            if (state.get(i) == 1)
            {
                sum += i - offset;
            }
        }

        return sum;
    }

    protected long tickState(long times)
    {
        long lastScore = 0;
        long scoreDif = 0;
        int sameScoreCount = 0;

        for (long i = 0; i < times; i++)
        {
            tickState();

            long newScore = calcScore();
            sameScoreCount += scoreDif == newScore - lastScore ? 1 : -sameScoreCount;
            scoreDif = newScore - lastScore;
            lastScore = newScore;

            if (sameScoreCount == 10)
            {
                System.out.println("Pattern found: adding " + scoreDif + " repeatedly");
                return lastScore + scoreDif * (times - i - 1);
            }
        }

        return lastScore;
    }

    private void tickState()
    {
        int end = state.size() - offset + 5;

        for (int i = -offset - 5; i < end; i++)
        {
            int[] state = new int[5];

            for (int j = -2; j < 3; j++)
            {
                state[j + 2] = getState(i + j);
            }

            for (Rule rule : rules)
            {
                if (rule.applies(state))
                {
                    state[2] = rule.apply();
                    break;
                }
            }

            setState(i, state[2]);
        }

        pushState();
    }

    private void pushState()
    {
        state.clear();
        state.addAll(newState);
        offset = newOffset;

        newState.clear();
        newOffset = 0;
    }

    private void setState(int index, int value)
    {
        for (; index < -newOffset; newOffset += 1)
        {
            newState.add(0, index == -(newOffset + 1) ? value : 0);
        }

        for (; index >= newState.size() - newOffset;)
        {
            newState.add(index == newState.size() - newOffset ? value : 0);
        }

        if (index >= -newOffset && index < newState.size() - newOffset)
        {
            newState.set(index + newOffset, value);
        }
    }

    private int getState(int index)
    {
        return (index < -offset || index >= state.size() - offset) ? 0 : state.get(index + offset);
    }

    public class Rule
    {
        private int[] rule;
        private int result;

        public Rule(int[] rule, int result)
        {
            this.rule = rule;
            this.result = result;
        }

        protected int apply()
        {
            return this.result;
        }

        protected boolean applies(int[] state)
        {
            for (int i = 0; i < 5; i++)
            {
                if (this.rule[i] != state[i])
                {
                    return false;
                }
            }

            return true;
        }
    }
}
