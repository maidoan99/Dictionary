package MyDictionary;
import javax.swing.plaf.DesktopIconUI;
import java.io.*;
import java.util.*;
import java.lang.*;

public class DictionaryManagement {

    static Scanner scan =  new Scanner(System.in);
    static String word_target;
    static String word_explain;

    public void insertFromCommandline(Dictionary dictionary){

        System.out.println("Nhap vao so luong tu: ");
        int numOfWord = scan.nextInt();
        scan.nextLine(); //chong troi lenh

        int index = 0;// count

        do {
            System.out.println("Nhap tu tieng Anh: ");
            word_target = scan.nextLine();

            System.out.println("Nhap giai thich sang tieng Viet: ");
            word_explain = scan.nextLine();

            dictionary.setDictionary(word_target, word_explain);
            index++;
        }
        while (index < numOfWord);
    }

    public void insertFromFile(Dictionary dictionary) {
        try {
            dictionary.getDictionary().clear();
            File file = new File("C:\\Users\\MAI\\Desktop\\OOP\\Old\\src\\MyDictionary\\Dictionaries.txt");
            String line;

            FileInputStream fileInputStream = new FileInputStream(file);//mo file
            Scanner input = new Scanner(file); //doc tu tep su dung scanner

            while (input.hasNextLine()) {//cho den khi het file
                line = input.nextLine(); //doc tung dong

                if (line.trim() != "") {  //neu dong khong rong
                    //cat cac thong tin bang dau tap
                    String item[] = line.split("\t");
                    word_target = item[0];
                    word_explain = item[1];

                    dictionary.setDictionary(word_target, word_explain);
                }
            }
        }
        catch (Exception ie){
            ie.printStackTrace();
        }
    }

    public void dictionaryLookup(Dictionary dictionary){
        try {
            boolean lookup = false;
            System.out.println("Search word : ");
            String findWord = scan.nextLine();

            ArrayList<Word> words = dictionary.getDictionary();

            for (Word word : words) {
                if (findWord.equals(word.getWord_target())) {
                    System.out.println("=> Explain : " + word.getWord_explain());
                    lookup = true;
                }
            }
            if(!lookup) System.out.println("Can't look up this word!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dictionaryDelete(Dictionary dictionary){

        try {
            boolean delete = false;
            System.out.println("Delete word: ");
            String delete_word  = scan.nextLine();
            Iterator<Word> it = dictionary.getDictionary().iterator();

            while(it.hasNext()){
                if(delete_word.equals(it.next().getWord_target())){
                    it.remove();
                    delete = true;
                    System.out.println("Deleted!");
                    return;
                }
            }
            if(delete == false)
                System.out.println("Can't find this word to delete!");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dictionaryChange(Dictionary dictionary){
        try {
            boolean result = false;
            System.out.print("Change word: ");
            String change = scan.nextLine();
            String change_target;
            String change_explain;

            for (Word word : dictionary.words) {
                if (change.equals(word.getWord_target())) {
                    System.out.print("Change target to: ");
                    change_target = scan.nextLine();
                    word.setWord_target(change_target);

                    System.out.println("Change explain to: ");
                    change_explain = scan.nextLine();
                    word.setWord_target(change_explain);

                    System.out.println("Change success!");
                    result = true;
                }
            }
            if(!result) System.out.println("Can't find this word tp change!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sortDictionary(Dictionary dictionary){
        //Sap xep lai tu dien theo thu tu bang chu cai
        Collections.sort(dictionary.words, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getWord_target().compareTo(o2.getWord_target());
            }
        });
    }

    public void dictionaryExportToFile(Dictionary dictionary){
        try {
            File file = new File("dictionaries_out.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintWriter output = new PrintWriter(fileOutputStream);

            ArrayList<Word> words = dictionary.getDictionary();

            //Sap xep lai words
            sortDictionary(dictionary);

            for (Word word : words) {
                word_target = word.getWord_target();
                word_explain = word.getWord_explain();

                output.println(word_target + "\t" + word_explain);//ghi ra file
            }

            System.out.println("Export success!");
            output.close();// dong file
        }
        catch (IOException ie){
            ie.printStackTrace();
        }
    }
}