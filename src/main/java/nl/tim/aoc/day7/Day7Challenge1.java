package nl.tim.aoc.day7;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.util.*;

public class Day7Challenge1 extends Challenge {
    protected final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private List<String> read;
    protected Set<Instruction> is;
    private StringBuilder res = new StringBuilder();

    @Override
    public void prepare() {
        is = new HashSet<>();
        read = Main.readFile("7.txt");
    }

    @Override
    public Object run(String alternative) {
        for (String s : read)
        {
            String before = s.split("\\s")[1];
            String next = s.split("\\s")[7];
            Instruction i = get(next);

            if (i == null)
            {
                is.add(new Instruction(before, next));
            } else
            {
                i.before.add(before);
            }
        }

        while (true)
        {
            String winner = null;

            for (int i = 0; i < alphabet.length(); i++)
            {
                String a = alphabet.substring(i, i + 1);

                if (!res.toString().contains(a) && valid(a)) {
                    winner = a;
                    break;
                }
            }

            if (winner == null)
            {
                break;
            }

            res.append(winner);
        }

        return res.toString();
    }

    protected Instruction get(String s)
    {
        for (Instruction i : is)
        {
            if (i.next.equals(s))
            {
                return i;
            }
        }

        return null;
    }

    private boolean valid(String s)
    {
        Instruction i = get(s);
        boolean valid = true;

        if (i == null)
        {
            return true;
        }

        for (String req : i.before){
            if (!res.toString().contains(req))
            {
                valid = false;
                break;
            }
        }

        return valid;
    }

    public class Instruction
    {
        List<String> before;
        String next;

        public Instruction(String before, String next)
        {
            this.before = new ArrayList<>();
            this.next = next;

            this.before.add(before);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Instruction instruction = (Instruction) o;
            return Objects.equals(before, instruction.before) &&
                    Objects.equals(next, instruction.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(before, next);
        }
    }
}
