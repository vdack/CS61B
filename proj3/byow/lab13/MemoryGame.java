package byow.lab13;

import byow.InputDemo.InputSource;
import byow.Core.RandomUtils;
import byow.InputDemo.KeyboardInputSource;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.time.Clock;
import java.util.Random;
import java.util.Scanner;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        String seedString;
        if (args.length < 1) {
            System.out.println("Please enter a seed:");
            Scanner scanner = new Scanner(System.in);
            seedString = scanner.nextLine();

        } else {
            seedString = args[0];
        }

        long seed = Long.parseLong(seedString);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
        System.out.println("finished.");
        return;
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        this.rand = new Random(seed);

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i += 1) {
            stringBuilder.append(CHARACTERS[rand.nextInt(25)]);
        }
        return stringBuilder.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.text(20, 20, s);
        StdDraw.show();

    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i += 1) {
            drawFrame(letters.substring(i,i + 1));
            StdDraw.pause(500);
        }
        drawFrame("");
        //TODO: Display each character in letters, making sure to blank the screen between letters
    }

    public String solicitNCharsInput(int n) {
        StringBuilder sb = new StringBuilder();
        InputSource inputSource = new KeyboardInputSource();
        for(int i = 0; i < n; i += 1) {
            if (inputSource.possibleNextInput()) {
                sb.append(inputSource.getNextKey());
            }
            drawFrame(sb.toString().toLowerCase());
        }
        return sb.toString().toLowerCase();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Engine loop

        int level = 3;
        while (level < 6) {
            int length = rand.nextInt(3) + level;
            String target = generateRandomString(length);
            flashSequence(target);
            System.out.println("current target: " + target);
            String input = solicitNCharsInput(length);
            System.out.println("input : " + input);
            StdDraw.pause(500);
            if (!input.equals(target)) {
                drawFrame("lose !");
                System.out.println("fail at level " + level);
                break;
            }
            drawFrame("congratulations!");
            StdDraw.pause(500);
            System.out.println("pass level " + level);
            level += 1;
        }

        StdDraw.clear();
    }

}
