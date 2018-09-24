import java.util.Scanner;

public class DictionaryManagement {
    public static void insertFromCommandline(){
        Scanner scan =  new Scanner(System.in);

        System.out.println("Nhap vao so luong tu: ");
        int numOfWord = scan.nextInt();
        scan.nextLine(); //nhap dau Enter thưa de chong troi lenh

        for(int i = 0; i < numOfWord; i++){

            System.out.println("Nhap tu tieng Anh: ");
            String word_target = scan.nextLine();

            System.out.println("Nhap giai thich sang tieng Viet: ");
            String word_explain = scan.nextLine();

            Word new_word = new Word (word_target, word_explain);

            Dictionary.words.add(new_word);
        }
    }
}
