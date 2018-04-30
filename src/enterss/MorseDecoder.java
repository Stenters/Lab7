/*
 * CS2852 – 031
 * Spring 2017
 * Lab 7 - Morse Code Decoder
 * Name: Stuart Enters
 * Created: 4/30/2018
 */

package enterss;


import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Class for decoding a Morse code file using a tree
 */
public class MorseDecoder {

    private static MorseTree<Character> tree = new MorseTree<>();

    public static void main(String[] args) {
        loadDecoder(new File("code.txt"));
        ArrayList<String> content;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter an input filename");
        String filename = scanner.next();
        File file = new File(filename);


        try{
            content = read(file);

            System.out.println("Please enter an output filename");
            filename = scanner.next();
            file = new File(filename);
            write(file, content);
        } catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }


        scanner.close();

    }

    private static void write(File file, ArrayList<String> content) {
        try (FileWriter writer = new FileWriter(file)){
            for (String s: content) {
                writer.write(s);
            }
        } catch (IOException e){
            System.out.println("Err");
        }
    }

    private static ArrayList<String> read(File file) throws FileNotFoundException {
        if (file == null){
            throw new FileNotFoundException("Err: No file found");
        }
        Scanner scanner = new Scanner(file);
        String line;
        String[] words;
        String[] codes;
        ArrayList<String> decoded = new ArrayList<>();

        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            words = line.split("\\|");
            for (String s: words) {
                codes = s.split(" ");
                for (String st: codes) {
                    String plaintext = tree.decode(st) + "";
                    if (!plaintext.equals("null")){
                        decoded.add(plaintext);
                    }
                }
                decoded.add(" ");
            }
            decoded.add("\n");
        }

        return decoded;

    }

    private static void loadDecoder(File file){
        try (Scanner input = new Scanner(file)){
            while (input.hasNextLine()){
                String line = input.nextLine();
                String[] params = line.split("\t");
                tree.add(params[0].charAt(0), params[1]);
            }

        } catch (IOException e){
            Scanner scanner = new Scanner(System.in);
            System.err.println(
                    "Error, dictionary file has been moved, please enter dictionary filepath");
            String filepath = scanner.nextLine();
            File newFile = new File(filepath);
            loadDecoder(newFile);
        }

    }
}
