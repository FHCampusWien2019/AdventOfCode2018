package nl.tim.aoc;

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
            long start = System.currentTimeMillis();

            // Run preparations if any
            challenge.prepare();

            // Run and print
            String result = challenge.run();
            long end = System.currentTimeMillis();

            System.out.printf("Challenge result (in %s ms): %s\n", end - start, result);
        }
    }

    private static void registerChallenges()
    {

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
