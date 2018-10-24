package MyDictionary;

import java.io.IOException;
import java.util.ArrayList;


public class Dictionary {
    static ArrayList<Word> words = new ArrayList();

    public static void setDictionary(String word_target, String word_explain) {
        Word word = new Word(word_target, word_explain);
        words.add(word);
    }

    public static ArrayList<Word> getDictionary(){
        return words;
    }
    // set them
}