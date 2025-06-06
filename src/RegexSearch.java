import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexSearch {
    static List<String> remainingWordleWords;
    public static void main(String[] args) {
        boolean playGame = true;
        setUpWordleWords();
        int turnCounter = 0;

        while (playGame == true){
        ArrayList<WordleResponse> responses = new ArrayList<>();
            turnCounter++;

            for (int i = 0; i < 5; i++) {
                String letter = JOptionPane.showInputDialog("Enter letter #" + (i + 1));
                String responseType = JOptionPane.showInputDialog("Enter the # indicated for the letter result\n1 - CORRECT LOCATION (Green)\n2 - WRONG LOCATION (Yellow)\n3 - WRONG LETTER (Gray)");

                LetterResponse letterResponse = null;

                if (responseType.equals("1")) {
                    letterResponse = LetterResponse.CORRECT_LOCATION;
                }
                else if (responseType.equals("2")) {
                    letterResponse = LetterResponse.WRONG_LOCATION;
                }
                else {
                    letterResponse = LetterResponse.WRONG_LETTER;
                }
                responses.add(new WordleResponse(Character.toLowerCase(letter.charAt(0)), i, letterResponse));
            }

        wordleMatches(responses);
        System.out.println(remainingWordleWords.size() + " remaining wordle words:");
        System.out.println(remainingWordleWords);

        if (remainingWordleWords.size() == 1 && turnCounter != 6){
            playGame = false;
            System.out.println("The word is " + remainingWordleWords.get(0) + "!");
        }
        else if (turnCounter == 6){
            playGame = false;
            System.out.println("You ran out of turns!");
        }
        }
    }

    public static boolean properName(String s) {
        String text = s;

        Pattern pattern = Pattern.compile("[A-Z][a-z]+");

        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean integer(String s) {
        String text = s;

        Pattern pattern = Pattern.compile("(?x) ^ [+-]? (?!0\\d) \\d+ \\.? \\d*");

        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean ancestor(String s) {
        String text = s;

        Pattern pattern = Pattern.compile("(?ix) ((?: great-)* (?:grand)? (?<!great-)(?:mother|father))");

        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean palindrome(String s) {
        String text = s;

        Pattern pattern = Pattern.compile("(?ix) ([a-z])([a-z])([a-z])([a-z])([a-z])\\5\\4\\3\\2\\1");

        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean wordMatcher(String word, String pat) {
        String text = word;

        Pattern pattern = Pattern.compile("(?ix)" + pat);

        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean rightSpotFinder(String word, Character c, int index) {
        //create array list of 4 periods
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            chars.add('.');
        }
        //add correct character to array list at its index in the word
        chars.add(index, c);
        //now we have something like "..A.." which we can use to find words which have the character at that index
        String pat = chars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        Pattern pattern = Pattern.compile("(?ix)" + pat);

        Matcher matcher = pattern.matcher(word);

        return matcher.matches();
    }

    public static boolean wrongSpotFinder(String word, Character c, int index) {

        Pattern pattern = Pattern.compile("(?ix)^(?=.{" + index + "}[^" + c + "]).{5}$");

        Matcher matcher = pattern.matcher(word);

        boolean isTrue = matcher.matches();
        return matcher.matches();
    }

    public static int getDuplicatesGreenYellowCount(List<WordleResponse> responses, WordleResponse response) {
        int counter = 0;
        char letter = response.getChar();

        for (WordleResponse r : responses) {
            if (letter == r.getChar()) {
                counter++;
            }
        }

        if (counter > 1){
            int greenYellowCount = 0;
            for (WordleResponse r : responses) {
                if (letter == r.getChar()) {
                    if (r.getResp() == LetterResponse.WRONG_LOCATION || r.getResp() == LetterResponse.CORRECT_LOCATION){
                        greenYellowCount++;
                    }
                }
            }
        return greenYellowCount;
        }

        return 0;
    }

    public static int letterCount(String word, char letter) {
        int counter = 0;
        for (char c : word.toCharArray()) {
            if (c == letter) {
                counter++;
            }
        }
        return counter;
    }

    public static List<String> wordleMatches(List<WordleResponse> responses){

        for (WordleResponse response : responses){

            int greensAndYellows = getDuplicatesGreenYellowCount(responses, response);

            if (response.getResp() == LetterResponse.CORRECT_LOCATION){
                remainingWordleWords = remainingWordleWords.stream()
                        .filter(word -> rightSpotFinder(word, response.getChar(), response.getIndex()))
                        .collect(Collectors.toList());
            }
            else if (response.getResp() == LetterResponse.WRONG_LOCATION){
                remainingWordleWords = remainingWordleWords.stream()
                        .filter(word -> wrongSpotFinder(word, response.getChar(), response.getIndex()))
                        .filter(word -> wordMatcher(word, ".*" + response.getChar() + ".*"))
                        .collect(Collectors.toList());
            }
             else {
                 if (greensAndYellows == 0) {
                     remainingWordleWords = remainingWordleWords.stream()
                             .filter(word -> wordMatcher(word, "[^" + response.getChar() + "][^" + response.getChar() + "][^" + response.getChar() + "][^" + response.getChar() + "][^" + response.getChar() + "]"))
                             .collect(Collectors.toList());
                 }
                 else {
                     remainingWordleWords = remainingWordleWords.stream()
                             .filter(word -> letterCount(word, response.getChar()) == greensAndYellows)
                             .filter(word -> wrongSpotFinder(word, response.getChar(), response.getIndex()))
                             .collect(Collectors.toList());
                 }
            }
        }

        return remainingWordleWords;
    }

    public static void setUpWordleWords(){
        remainingWordleWords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("valid-wordle-words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                remainingWordleWords.add(line);
            }
        }
        catch (IOException e) {
        }
    }

    public static List<String> getWordleWords(){
        return remainingWordleWords;
    }

}