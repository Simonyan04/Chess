package Chess.puzzles;
import Chess.exceptions.MalformedPuzzleException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PuzzleDatabase {
    private static final String PATH = "Chess/puzzles/database.txt";
    private ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
    public PuzzleDatabase(){
        load();
    }
    public void load(){
        Scanner input = null;
        try {
            input = new Scanner(new FileInputStream(PATH));
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
        int n = input.nextInt();
        if (n == 0){
            return;
        }
        input.nextLine();
        Puzzle current = null;
        for (int i = 0; i < n; i++) {
            try {
                current = new Puzzle(input.nextLine(),input.nextLine());
            }
            catch (MalformedPuzzleException e){
                System.out.println(e.getMessage());
                System.exit(0);
            }
            if (!puzzles.contains(current)){
                puzzles.add(current);
            }
        }
        input.close();
        Collections.sort(puzzles);
    }
    public void save(){
        PrintWriter output;
        try {
            output = new PrintWriter(new FileOutputStream(PATH));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (puzzles != null){
            output.println(puzzles.size());
            for (int i = 0; i < puzzles.size(); i++) {
                output.println(puzzles.get(i));
            }
            output.close();
        }
    }
    public void addPuzzlesFromFile(String file){
        Scanner input = null;
        try {
            input = new Scanner(new FileInputStream(file));
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
        Puzzle current = null;
        input.nextLine();
        while (input.hasNextLine()){
            try {
                current = new Puzzle(input.nextLine(),input.nextLine());
            }
            catch (MalformedPuzzleException e){
                System.out.println(e.getMessage());
                System.exit(0);
            }
            if (puzzles.isEmpty()){
                puzzles.add(current);
            }
            else {
                if (!puzzles.contains(current)){
                    puzzles.add(current);
                }
            }
        }
        input.close();
    }

    public int getSize() {
        return puzzles.size();
    }
    public Puzzle getPuzzle(int index){
        return puzzles.get(index);
    }
}
