package nl.tim.aoc.day7;

import nl.tim.aoc.Challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day7Challenge2 extends Challenge
{
    private Day7Challenge1 base;
    private String res;
    private StringBuilder done;
    private HashMap<Integer, Task> workers;

    @Override
    public void prepare() {
        base = new Day7Challenge1();
        workers = new HashMap<>();

        base.prepare();
        res = (String) base.run();
        done = new StringBuilder();
    }

    @Override
    public Object run() {
        int count = 0;

        while (done.length() != res.length())
        {
            add();
            elfTick();
            count += 1;
        }

        return count;
    }

    private void add()
    {
        for (int i = 0; i < base.alphabet.length(); i++)
        {
            String c = base.alphabet.substring(i, i + 1);

            if (res.contains(c) &&
                    !done.toString().contains(c) &&
                    workers.keySet().size() < 5 &&
                    !elfWorking(c) &&
                    dependenciesDone(c))
            {
                workElf(c);
            }
        }
    }

    private boolean elfWorking(String c)
    {
        for (Integer elf : workers.keySet())
        {
            Task task = workers.get(elf);

            if (task.ch.equals(c))
            {
                return true;
            }
        }

        return false;
    }

    private int nextElf()
    {
        for (int i = 0; i < 5; i++)
        {
            if (!workers.containsKey(i))
            {
                return i;
            }
        }

        return -1;
    }

    private void elfTick()
    {
        List<Integer> rem = new ArrayList<>();

        for (Integer elf : workers.keySet())
        {
            Task task = workers.get(elf);

            task.time -= 1;

            if (task.time <= 0)
            {
                rem.add(elf);
                done.append(task.ch);
            }
        }

        for (Integer e : rem)
        {
            workers.remove(e);
        }
    }

    private void workElf(String c)
    {
        int nextElf = nextElf();
        int time = 61 + base.alphabet.indexOf(c);

        workers.put(nextElf, new Task(c, time));
    }

    private boolean dependenciesDone(String c)
    {
        Day7Challenge1.Instruction ins = base.get(c);

        if (ins == null)
        {
            return true;
        }

        for (String s : ins.before)
        {
            if (!done.toString().contains(s))
            {
                return false;
            }
        }

        return true;
    }

    public class Task
    {
        String ch;
        int time;

        public Task(String ch, int time)
        {
            this.ch = ch;
            this.time = time;
        }
    }
}
