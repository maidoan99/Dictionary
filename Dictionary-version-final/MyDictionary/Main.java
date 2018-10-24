package MyDictionary;

public class Main {
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        DictionaryCommandLine dictionaryCommandLine = new DictionaryCommandLine();

        dictionaryCommandLine.dictionaryAdvanced(dictionary);

    }
}