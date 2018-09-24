public class DictionaryCommandlind {
    public static void showAllWords(){
        System.out.println("No  |English            |Vietnamese");

        int numOfWord = Dictionary.words.size();

        String word_target;
        String word_explain;

        for(int i = 0; i < numOfWord; i++){
            word_target = Dictionary.words.get(i).getWord_target();
            word_explain = Dictionary.words.get(i).getWord_explain();

            System.out.println((i+1) + "    " + word_target + "                 " + word_explain);
        }
    }

    public static void DictionaryBasic(){
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }
    public static void main(String[] args) {
        DictionaryBasic();
    }
}
