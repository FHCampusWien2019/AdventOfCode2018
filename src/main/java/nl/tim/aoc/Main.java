package nl.tim.aoc;

import nl.tim.aoc.day1.Day1Challenge1;
import nl.tim.aoc.day1.Day1Challenge2;
import nl.tim.aoc.day10.Day10Challenge1;
import nl.tim.aoc.day11.Day11Challenge1;
import nl.tim.aoc.day11.Day11Challenge2;
import nl.tim.aoc.day12.Day12Challenge1;
import nl.tim.aoc.day12.Day12Challenge2;
import nl.tim.aoc.day13.Day13Challenge1;
import nl.tim.aoc.day13.Day13Challenge2;
import nl.tim.aoc.day14.Day14Challenge1;
import nl.tim.aoc.day14.Day14Challenge2;
import nl.tim.aoc.day2.Day2Challenge1;
import nl.tim.aoc.day2.Day2Challenge2;
import nl.tim.aoc.day3.Day3Challenge1;
import nl.tim.aoc.day3.Day3Challenge2;
import nl.tim.aoc.day4.Day4Challenge1;
import nl.tim.aoc.day4.Day4Challenge2;
import nl.tim.aoc.day5.Day5Challenge1;
import nl.tim.aoc.day5.Day5Challenge2;
import nl.tim.aoc.day6.Day6Challenge1;
import nl.tim.aoc.day6.Day6Challenge2;
import nl.tim.aoc.day7.Day7Challenge1;
import nl.tim.aoc.day7.Day7Challenge2;
import nl.tim.aoc.day8.Day8Challenge1;
import nl.tim.aoc.day8.Day8Challenge2;
import nl.tim.aoc.day9.Day9Challenge1;
import nl.tim.aoc.day9.Day9Challenge2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private static HashMap<String, Challenge> challenges;

    public static void main(String... args)
    {
        challenges = new HashMap<>();

        // Register challenges
        registerChallenges();

        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            System.out.print("Type challenge to run: ");

            String input = scanner.nextLine();

            Challenge challenge = challenges.get(input);

            if (challenge == null)
            {
                System.out.println("Challenge not found");
                continue;
            }

            String alternativeMethod = "default";
            Class<? extends Challenge> clazz = challenge.getClass();

            // Check if challenge has an alternative solution
            if (clazz.isAnnotationPresent(AlternativeMethod.class))
            {
                AlternativeMethod alternativeMethods = clazz.getAnnotation(AlternativeMethod.class);

                System.out.printf("This solution has alternative ways to handle this challenge.%n" +
                        "Type the name of the alternative method, or 'default' to use the default method:%n" +
                        " - default " + (alternativeMethods.recommended().equals("default") ? "(recommended)" : "")+ "%n");

                selectMethod:
                while (true)
                {
                    for (String alternative : alternativeMethods.alternatives())
                    {
                        System.out.printf(" - %s%n",
                                alternative +
                                        (alternative.equals(alternativeMethods.recommended()) ? " (recommended)" : "")
                        );
                    }

                    String in = scanner.nextLine();

                    for (String alt : alternativeMethods.alternatives())
                    {
                        if (alt.equals(in) || in.equals("default"))
                        {
                            alternativeMethod = in;
                            break selectMethod;
                        }
                    }

                    System.out.println("Invalid method");
                }
            }

            // Set timer
            long start = System.nanoTime();

            // Run preparations if any
            challenge.prepare();

            // Set timer after IO
            long io = System.nanoTime();

            // Run and print
            Object result = challenge.run(alternativeMethod);
            long end = System.nanoTime();

            System.out.printf("Challenge result (in %s ms or %s ms without IO+prep): %s\n",
                    (end - start) / 1000000,
                    (end - io) / 1000000,
                    result);
        }
    }

    private static void registerChallenges()
    {
        // This looked better in my mind when I thought of this, may need to refactor this
        challenges.put("1-1", new Day1Challenge1());
        challenges.put("1-2", new Day1Challenge2());
        challenges.put("2-1", new Day2Challenge1());
        challenges.put("2-2", new Day2Challenge2());
        challenges.put("3-1", new Day3Challenge1());
        challenges.put("3-2", new Day3Challenge2());
        challenges.put("4-1", new Day4Challenge1());
        challenges.put("4-2", new Day4Challenge2());
        challenges.put("5-1", new Day5Challenge1());
        challenges.put("5-2", new Day5Challenge2());
        challenges.put("6-1", new Day6Challenge1());
        challenges.put("6-2", new Day6Challenge2());
        challenges.put("7-1", new Day7Challenge1());
        challenges.put("7-2", new Day7Challenge2());
        challenges.put("8-1", new Day8Challenge1());
        challenges.put("8-2", new Day8Challenge2());
        challenges.put("9-1", new Day9Challenge1());
        challenges.put("9-2", new Day9Challenge2());
        challenges.put("10-1", new Day10Challenge1());
        challenges.put("10-2", new Day10Challenge1());
        challenges.put("11-1", new Day11Challenge1());
        challenges.put("11-2", new Day11Challenge2());
        challenges.put("12-1", new Day12Challenge1());
        challenges.put("12-2", new Day12Challenge2());
        challenges.put("13-1", new Day13Challenge1());
        challenges.put("13-2", new Day13Challenge2());
        challenges.put("14-1", new Day14Challenge1());
        challenges.put("14-2", new Day14Challenge2());
    }

    public static List<String> readFile(String fileName)
    {
        BufferedReader reader;
        List<String> res = new ArrayList<>();
        try
        {
            reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/" + fileName)));
            String line;

            while ((line = reader.readLine()) != null)
            {
                res.add(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return res;
    }
}
