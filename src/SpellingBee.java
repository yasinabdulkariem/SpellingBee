import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate(){
        generateWord("", letters);
    }
    public void generateWord(String current, String remaining){
        if (remaining.isEmpty()){
            words.add(current);
            return;
        }

        for(int i = 0; i < remaining.length(); i++){
            words.add(current + remaining.charAt(i));
            generateWord(current + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i + 1));
        }
    }
    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergeSort(words, 0, words.size() - 1);
    }
    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high){
        if(high - low == 0){
            ArrayList<String> newArr = new ArrayList<>(1);
            newArr.add(arr.get(low));
            return newArr;
        }
        int med = (high + low) / 2;
        ArrayList<String> arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = mergeSort(arr, med + 1, high);
        return merge(arr1, arr2);
    }
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2){
        ArrayList<String> sol = new ArrayList<>(arr1.size() + arr2.size());
        int index1 = 0, index2 = 0;

        while(index1 < arr1.size() && index2 < arr2.size()){
            if(arr1.get(index1).compareTo(arr2.get(index2)) <= 0){
                sol.add(arr1.get(index1++));
            }
            else{
                sol.add(arr2.get(index2++));
            }
        }
        while(index1 < arr1.size()){
            sol.add(arr1.get(index1++));
        }
        while(index2 < arr2.size()){
            sol.add(arr2.get(index2++));
        }
        return sol;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE

        for(int i = 0; i < words.size(); i++){
            if(!found(words.get(i))){
                words.remove(i);
            }
        }
    }

    public boolean found(String word) {
        int low = 0;
        int high = DICTIONARY_SIZE - 1;

        while (low <= high) {
            int middle = (high + low) / 2;
            int comp = DICTIONARY[middle].compareTo(word);

            if(comp < 0){
                low = middle + 1;
            }
            else if(comp > 0){
                high = middle - 1;
            }
            else{
                return true;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
