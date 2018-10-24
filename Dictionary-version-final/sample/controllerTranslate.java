package sample;

import gtranslate.Audio;
import gtranslate.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import javax.security.auth.callback.LanguageCallback;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class controllerTranslate implements Initializable {
    @FXML
    private TextArea TextIn;

    @FXML
    private TextArea TextOut;
    @FXML
    private ComboBox LanguageIn;
    @FXML
    private ComboBox LanguageOut;

    /**
     * Tao list de chua tat ca cac gia tri cua ComboBox
     */
    ObservableList<String> OptionIn = FXCollections.observableArrayList(
            "English", "Vietnamese", "Japanese", "Chinese"
    );
    ObservableList<String> OptionOut = FXCollections.observableArrayList(
            "English", "Vietnamese", "Japanese", "Chinese"
    );

    /**
     *Chuyen doi tu ngon ngu can dich sang ngon ngu cho ggtranslte hieu
     * VD chuyen tu English sang en
     */
    HashMap<String, String > LanguageCode = new HashMap<String, String>();
    String SourceLanguage = "English";
    String TargetLanguage = "Vietnamese";

    /**
     * Lay nghia sau khi da dich cho vao textField nghia cua tu
     * @throws IOException
     */
    public void insert() throws IOException {  // hàm này là lúc nhập áya ạ
        SourceLanguage = (String) LanguageIn.getValue();
        TargetLanguage = (String) LanguageOut.getValue();
        try{
            TextOut.setText(GoogleTranslate.translate( LanguageCode.get(TargetLanguage),TextIn.getText()));
        }catch(IOException ex){
            ex.getStackTrace();
        }
    }

    /**
     * Doi ngon ngu o textField tu can dich  khi cho ngon ngu khac trong ComboBox
     * @throws IOException
     */
    public void handleLanguageIn() throws IOException {
        SourceLanguage = (String) LanguageIn.getValue();
        TextIn.setText(GoogleTranslate.translate(LanguageCode.get(SourceLanguage), TextIn.getText())); // tóm lại là hàm này được chưa ạ, xùy
    }

    /**
     * Doi ngon ngu o textField nghia cua tu khi cho ngon ngu khac trong ComboBox
     * @throws IOException
     */
    public void handleLanguageOut() throws IOException {
        TargetLanguage = (String) LanguageOut.getValue();
        TextOut.setText(GoogleTranslate.translate(LanguageCode.get(TargetLanguage), TextOut.getText()));
    }

    /**
     * Chuyen doi tu can dich va nghia nghia cua tu khi nhan button change
     * @throws IOException
     */
    public void changeLanguage() throws IOException {// thực ra có khác 1 vài chỗ cơ mà co bản là giống :v ừa hú
        String temp = SourceLanguage;
        SourceLanguage = TargetLanguage;
        TargetLanguage = temp;
        LanguageIn.setValue(SourceLanguage);
        LanguageOut.setValue(TargetLanguage);
        TextIn.setText(GoogleTranslate.translate(LanguageCode.get(SourceLanguage),TextIn.getText())); // thực ra cái chỗ bên trên kia cx ko cần đâu
        TextOut.setText(GoogleTranslate.translate(LanguageCode.get(TargetLanguage),TextOut.getText()));
    }

    /**
     * Tro ve scene chinh
     * @param e
     * @throws IOException
     */
    public void goDashBoard(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent addWordView = FXMLLoader.load(getClass().getResource("/FXML/sample.fxml"));
        Scene scene = new Scene(addWordView);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * KHoi tao cac loai ngon ngu co the dich
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) { //
        LanguageCode.put("English", "en");
        LanguageCode.put("Vietnamese", "vi");
        LanguageCode.put("Japanese", "ja");
        LanguageCode.put("Chinese", "zh");

        LanguageIn.setValue("English");
        LanguageOut.setValue("Vietnamese");
        LanguageIn.setItems(OptionIn);
        LanguageOut.setItems(OptionOut);
    }

    /**
     * Phat am o cot tu can dich nghia
     */
    public void sayIn() {
        if(TextIn.getText() != "") {
            try {
                InputStream sound = null;
                Audio audio = Audio.getInstance();
                sound = audio.getAudio(TextIn.getText(), LanguageCode.get(SourceLanguage));

                audio.play(sound);//doc
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Phat a o cot nghia cua tu da dich
     */
    public void sayOut() {
        try {
            InputStream sound = null;
            Audio audio = Audio.getInstance();
            sound = audio.getAudio(TextOut.getText(), LanguageCode.get(TargetLanguage));
            audio.play(sound);//doc
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
