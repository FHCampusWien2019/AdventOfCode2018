package nl.tim.aoc;

import nl.tim.aoc.day1.Day1Challenge1;
import nl.tim.aoc.day1.Day1Challenge2;
import nl.tim.aoc.day2.Day2Challenge1;
import nl.tim.aoc.day2.Day2Challenge2;
import nl.tim.aoc.day3.Day3Challenge1;
import nl.tim.aoc.day3.Day3Challenge2;
import nl.tim.aoc.day4.Day4Challenge1;
import nl.tim.aoc.day4.Day4Challenge2;

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

            // Set timer
            long start = System.nanoTime();

            // Run preparations if any
            challenge.prepare();

            // Run and print
            Object result = challenge.run();
            long end = System.nanoTime();

            System.out.printf("Challenge result (in %s ms): %s\n", (end - start) / 1000000, result);
        }
    }

    private static void registerChallenges()
    {
        challenges.put("1-1", new Day1Challenge1());
        challenges.put("1-2", new Day1Challenge2());
        challenges.put("2-1", new Day2Challenge1());
        challenges.put("2-2", new Day2Challenge2());
        challenges.put("3-1", new Day3Challenge1());
        challenges.put("3-2", new Day3Challenge2());
        challenges.put("4-1", new Day4Challenge1());
        challenges.put("4-2", new Day4Challenge2());
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
