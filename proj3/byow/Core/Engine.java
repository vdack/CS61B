package byow.Core;

import antlr.StringUtils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.InputDemo.KeyboardInputSource;
import edu.princeton.cs.introcs.In;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class
Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 120;
    public static final int HEIGHT = 90;
    public static final int WIDTH_OFFSET = 10;
    public static final int HEIGHT_OFFSET = 5;

    private static final int MENU_MODE = 0;
    private static final int GAME_MODE = 1;
    private static final int SEED_MODE = 2;
    private static final int QUIT_MODE = 3;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        interactWithSource(inputSource);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        InputSource inputSource = new StringInputDevice(input.toUpperCase());
        return interactWithSource(inputSource);
    }
    private TETile[][] interactWithSource(InputSource inputSource) {
        int controlMode = MENU_MODE;
        World world = new World(WIDTH, HEIGHT, 12345678, WIDTH_OFFSET, HEIGHT_OFFSET);
        world.initialize();
        world.menu();
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        ter.renderFrame(world.getWorld());

        while (inputSource.possibleNextInput()) {
            char opKey = inputSource.getNextKey();
            if (controlMode == MENU_MODE) {
                if (opKey == 'N') {
                    controlMode = SEED_MODE;
                } else if (opKey == 'L') {
                    //TODO load game.
                    try {
                        world.loadWorld("savings.dat");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    controlMode = GAME_MODE;
                } else if (opKey == 'Q') {
                    try {
                        world.saveWorld("savings.dat");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    world.menu();
                    controlMode = QUIT_MODE;
                } else{
                    //Nothing
                }
            } else if (controlMode == GAME_MODE) {
                if (opKey == 'W') {
                    world.avatorMoveUp();
                } else if (opKey == 'S') {
                    world.avatorMoveDown();
                } else if (opKey == 'A') {
                    world.avatorMoveLeft();
                } else if (opKey == 'D') {
                    world.avatorMoveRight();
                } else if (opKey == ':'){
                    controlMode = MENU_MODE;
                } else {
                    //nothing
                }

            } else if (controlMode == SEED_MODE) {
                //get seed number
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(opKey);
                while (inputSource.possibleNextInput()) {
                    char possibleNumber = inputSource.getNextKey();
                    if (possibleNumber == 'S') {
                        break;
                    }
                    stringBuilder.append(possibleNumber);
                }
                Long seedNumber = Long.parseLong(stringBuilder.toString(),10);
                System.out.println("Status: world seed is :" + seedNumber);
                world = new World(WIDTH, HEIGHT, seedNumber, WIDTH_OFFSET, HEIGHT_OFFSET);
                world.initialize();
                world.worldGenerate();
                controlMode = GAME_MODE;
            } else if (controlMode == QUIT_MODE) {
                break;
            } else {
                continue;
            }
//            ter.renderFrame(world.getWorld());
        }

        return world.getWorld();
    }
}
