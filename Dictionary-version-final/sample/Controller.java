package sample;

import MyDictionary.Dictionary;
import MyDictionary.DictionaryCommandLine;
//import com.sun.deploy.util.FXLoader;
import gtranslate.Audio;
import gtranslate.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable{
    public Button delete;
    public Button edit;
    public Button like;
    public Button sound;
    public Button add;
    public TextField search;
    public ListView<String> listView;
    public ListView listFavourite;
    public WebView webView;
    public Button moveFavour;
    public WebEngine webEngine;
    public Tab favourite;


    protected Connection connection = SqliteConnection.getConnector();
    protected ObservableList <String> items = FXCollections.observableArrayList();
    protected ObservableList<String> itemsFavourite = FXCollections.observableArrayList();
    protected PreparedStatement preparedStatement = null;
    protected ResultSet resultSet = null;

    public Controller() throws SQLException, ClassNotFoundException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = webView.getEngine();
        loadDatabase();

        //Thuc hien do bong cho button khi di chuyen chuot den
        styleButton(add);
        styleButton(delete);
        styleButton(edit);
        styleButton(sound);
        styleButton(like);
        styleButton(moveFavour);

        /**
         * Thuc hien thao tac in ra nghia khi chon mot tu bat ki
         * button like chi thao tac duoc khi dang o listView
         * buton moveFavour chi
         * Chi co tu khong co trong listFaour moi duoc them vao listFavour
         */
        moveFavour.setDisable(true);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //handleSelectEvent(listView);
                playSelectItem(listView);
                moveFavour.setDisable(true);
            }
        });

        listFavourite.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //handleSelectEvent(listFavourite);
                playSelectItem(listFavourite);
                moveFavour.setDisable(false);
            }
        });
    }

    /**
     * Chuyen sang trang addWord khi nhan vao button add
     * @param e
     * @throws IOException
     */
    @FXML
    public void addWordView(ActionEvent e) throws IOException {
        Stage stage = (Stage)add.getScene().getWindow();
        Parent addWordView = FXMLLoader.load(getClass().getResource("/FXML/AddWord.fxml"));
        Scene scene = new Scene(addWordView);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Them tu vao database
     * @param target
     * @param explain
     */
    public void addWord(String target, String explain){
        String query = "INSERT INTO av (word, html) VALUES(?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, target);
            preparedStatement.setString(2, explain);
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Hien ra thong bao truoc va sau khi xoa
     * Xoa tu khi nhan vao button delete
     * @param event
     */
    @FXML
    public void deleteWord(ActionEvent event) {
        if(listView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure want to delete this word?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                try {
                    String word1 = listView.getSelectionModel().getSelectedItem();
                    if (word1 != null) {
                        String query = "delete from av  Where word=? ";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, word1);
                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        resultSet.close();

                        resetTable();
                        webEngine.loadContent("");
                        showAlertInfo("Deleted success!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else
            showAlertError("No select word!");
    }

    /**
     * Chuyen sang trang edit tu khi nhan vao button edit
     * Neu khong chon tu nao ma da edit se hien thong bao loi
     * @param e
     * @throws IOException
     */
    @FXML
    public void editWordView(ActionEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../FXML/EditWord.fxml"));
        Parent SceneChange = loader.load();
        Scene scene = new Scene(SceneChange);

        ControllerEditWord con = loader.getController();

        if(listView.getSelectionModel().getSelectedItem() != null){
            String html = "";
            con.initData(listView.getSelectionModel().getSelectedItem(), getHtml(html));
            stage.setScene(scene);
        }
        else{
            showAlertError("You haved not selected word");
        }
    }

    /**
     * Thuc hien viec xoa tu trong database
     * @param text1
     * @param text2
     */
    public void editWord(String text1, String text2){
        String query = "update av set word=? ,html=? where word='" + text1 + "' ";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text1);
            preparedStatement.setString(2, text2);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Lay nghia tieng viet cua tu can edit de set vao trang edit
     * @param html
     * @return
     */
    public String getHtml(String html){
        String query = "Select * from av WHERE word=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, listView.getSelectionModel().getSelectedItem());
            resultSet = preparedStatement.executeQuery();
            html = resultSet.getString("html");

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html;
    }

    /**
     * Them tu vao list tu yeu thich khi nhan vao button ike
     * @param event
     */
    @FXML
    public void favouriteWord(ActionEvent event) {
        if(listView.getSelectionModel().getSelectedItem() != null) {
            String query = "UPDATE av SET favorite = ? WHERE word = ?";
            try {
                String sWord = listView.getSelectionModel().getSelectedItem();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "yes");
                preparedStatement.setString(2, sWord);
                preparedStatement.executeUpdate();

                preparedStatement.close();
                resetTableFavourite();

                showAlertInfo("Inserted to favouriteList success!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
            showAlertError("No select word!");
    }

    /**
     * Chuc nang xoa mot tu khoi listFavourite (Khong xoa khoi database ban dau)
     * Hien ra thong bao sau khi da xoa xong
     * @param event
     * @throws SQLException
     */
    @FXML
    public void moveFavourite(ActionEvent event) throws SQLException {
        String word2 = (String) listFavourite.getSelectionModel().getSelectedItem();
        if(word2 != null){
            String query2 = "UPDATE av SET favorite = ? WHERE word = ?";
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.setString(1,null);
            preparedStatement.setString(2,word2);
            preparedStatement.executeUpdate();

            preparedStatement.close();

            resetTableFavourite();
            webEngine.loadContent("");

            showAlertInfo("Moved success!");
        }
        else
            showAlertError("No select word!");
    }

    /**
     * Reset lai table listView
     */
    public void resetTable(){
        try{
            String query = "select word   from  av";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            items.clear();

            while (resultSet.next()){
                items.add(resultSet.getString(1));
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset lai table listFavourite
     */
    public void resetTableFavourite(){
        try{
            String query2 = "SELECT word FROM av WHERE favorite = ?";
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.setString(1,"yes");
            resultSet = preparedStatement.executeQuery();

            itemsFavourite.clear();

            while(resultSet.next()){
                itemsFavourite.add(resultSet.getString(1));
                //listFavourite.setItems(itemsFavourite);
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thuc hien chuc nang in ra tat ca cac tu trong tu dien khi vua mo chuong trinh
     * Thuc hien tren ca listView va listFavourite
     */
    public void loadDatabase(){  // ban dau chay chuong trinh
        String query = "select word  from av";
        try{
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                items.add(resultSet.getString(1));
                listView.setItems(items);
            }
            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query1 = "SELECT word FROM av WHERE favorite = ?";
        try{
            itemsFavourite.clear();
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1,"yes");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                itemsFavourite.add(resultSet.getString(1));
                listFavourite.setItems(itemsFavourite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search tu
     * Go chu cai nao se hien ra tat ca cac tu bat dau bang nhung chu cai day
     */
    @FXML
    public void searchWord() {
        try{
            String query = "Select * from av Where word Like '" + search.getText() + "%' order by word";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            items.clear();
            while (resultSet.next()){
                items.add(resultSet.getString("word"));
            }

            listView.setItems(items);
            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * In ra Alert thong bao
     * @param info
     */
    public void showAlertInfo(String info){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

    /**
     * In ra alert loi
     * @param error
     */
    public void showAlertError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    /**
     * In ra alert canh bao
     * @param warning
     */
    public void showAlertWarning(String warning){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    /**
     * Thay doi tu scene nay sang scene khac
     * @param e
     * @param fxml
     * @throws IOException
     */
    public void changeScene(ActionEvent e, String fxml) throws IOException {
        Stage stage = (Stage)add.getScene().getWindow();
        Parent addWordView = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(addWordView);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Thuc hien chuc nang phat am cho listView
     * Neu chua chon tu nao ma da nhan button se hien ra thong bao loi
     * @param
     */
    @FXML
    public void say() {
        try {
            InputStream sound = null;
            Audio audio = Audio.getInstance();
            sound = audio.getAudio(listView.getSelectionModel().getSelectedItem(), Language.ENGLISH);
            audio.play(sound);//doc
        } catch (IOException ex) {
            Logger.getLogger(sample.Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(sample.Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Chuyen scene sang google translate
     * @param e
     * @throws IOException
     */
    @FXML
    public void ggtranslate (ActionEvent e) throws IOException{
        changeScene(e, "../FXML/googleTranslate.fxml");
    }

    /**
     * Thuc hien chuc nang nhan vao tu nao se hen ra nghia cua tu do
     * @param list
     */
    public void playSelectItem(ListView<String> list){
        String query = "Select * from av WHERE word=?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, list.getSelectionModel().getSelectedItem());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                webEngine.loadContent(resultSet.getString("html"));
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hieu ung bong do khi di chuot len button
     * @param button
     */
    public void styleButton(Button button) {
        DropShadow dropShadow = new DropShadow();
        //Hieu ung do bong khi di chuot den
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setEffect(dropShadow);
            }
        });

        //Button tro lai binh thuong khi chuot roi di
        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setEffect(null);
            }
        });
    }
}
