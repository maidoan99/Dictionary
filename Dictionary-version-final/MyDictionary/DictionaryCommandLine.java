package MyDictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class DictionaryCommandLine {

    Scanner scan = new Scanner(System.in);
    public static void showAllWords(Dictionary dictionary) {

        System.out.println("No\t|English\t\t\t|Vietnamese");

        int numOfWord = Dictionary.words.size();

        String word_target;
        String word_explain;

        int index = 1;
        for (Word word : dictionary.getDictionary()) {
            word_target = word.getWord_target();
            word_explain = word.getWord_explain();

            System.out.print(index + "\t|");
            System.out.print(word_target);
            for(int i = 0; i < 19- word_target.length(); i++)
                System.out.print(" ");
            System.out.println("| " + word_explain);
            index++;
        }
    }

    public void dictionaryBasic(Dictionary dictionary){
        DictionaryManagement dictionaryManagement = new DictionaryManagement();

        System.out.println("choose 1: If you want to insert word " +
                "\nChoose 2: If you want to show all word" +
                "\nChoose 3: Exit");
        int request = scan.nextInt();

        switch (request){
            case 1:
                dictionaryManagement.insertFromCommandline(dictionary);
                dictionaryBasic(dictionary);
                break;
            case 2:
                showAllWords(dictionary);
                dictionaryBasic(dictionary);
                break;
            case 3:
                System.exit(0);  //thoat khoi chuong trinh
                break;
            default:
                dictionaryBasic(dictionary);
                break;
        }
    }

    public void dictionaryAdvanced(Dictionary dictionary){
        DictionaryManagement dictionaryManagement = new DictionaryManagement();


        System.out.println("Choose 1: If you want to insert from file" +
                "\nChoose 2: If you want to show all words" +
                "\nChoose 3: If you want to look up word" +
                "\nChoose 4: If you want to search word" +
                "\nChoose 5: If you want to delete word" +
                "\nChoose 6: If you want to change word" +
                "\nChoose 7: If you want to export to file" +
                "\nChoose 8: If you want to insert from CommandLine" +
                "\nChoose 9: Exit");
        // them showAllWord
        int request = scan.nextInt();

        switch (request){
            case 1:
                dictionaryManagement.insertFromFile(dictionary);
                dictionaryAdvanced(dictionary); // xuong day goi no lại them vao
                break;
            case 2:
                showAllWords(dictionary);
                dictionaryAdvanced(dictionary);
            case 3:
                dictionaryManagement.dictionaryLookup(dictionary);
                dictionaryAdvanced(dictionary);
                break;
            case 4:
                dictionarySearcher(dictionary);
                dictionaryAdvanced(dictionary);
                break;
            case 5:
                dictionaryManagement.dictionaryDelete(dictionary);
                dictionaryAdvanced(dictionary);
                break;
            case 6:
                dictionaryManagement.dictionaryChange(dictionary);
                dictionaryAdvanced(dictionary);
                break;
            case 7:
                dictionaryManagement.dictionaryExportToFile(dictionary);
                dictionaryAdvanced(dictionary);
                break;
            case 8:
                dictionaryManagement.insertFromCommandline(dictionary);
                dictionaryAdvanced(dictionary);
                break;
            case 9:
                System.exit(0);
                break;
            default:
                dictionaryAdvanced(dictionary);
                break;

        }

    }

    public void dictionarySearcher(Dictionary dictionary){
        try {
            Scanner input = new Scanner(System.in);

            System.out.println("Search word: ");
            String find_word = input.nextLine();

            ArrayList<Word> words = dictionary.getDictionary();

            for (Word word : words) {
                String word_target = word.getWord_target();
                if (word_target.substring(0, find_word.length()).equals(find_word))
                    System.out.println(word.getWord_target() + "\t\t: " + word.getWord_explain());
            }
        }catch (Exception e) {
            //khong tim thay tu can tra
            System.out.println("Can't find this word!");
        }
    }
}
