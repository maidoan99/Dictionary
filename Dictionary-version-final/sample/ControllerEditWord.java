package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerEditWord  extends Controller implements Initializable {
    @FXML
    public TextField target;
    @FXML
    public Button edit1;
    @FXML
    public HTMLEditor explain;
    @FXML
    public Button cancel;

    public ControllerEditWord() throws SQLException, ClassNotFoundException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Set tu tieng anh va nghia cua tu cho textField target va textField explain
     * @param word
     * @param html
     */
    public void initData(String word, String html){
        target.setText(word);
        explain.setHtmlText(html);
    }

    /**
     * Khi nhan vao button edit se goi ham edit o class Controller
     * Reset lai table listView va listFavourite sau khi da edit
     * Thong bao sau khi da edit
     * Chuyen lai scene chinh cua tu dien
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String text1 = target.getText();
        String text2 = explain.getHtmlText();

        editWord(text1, text2);
        resetTable();
        showAlertInfo("Update success!");

        Stage stage = (Stage)edit1.getScene().getWindow();
        Parent addWordView = FXMLLoader.load(getClass().getResource("/FXML/sample.fxml"));
        Scene scene = new Scene(addWordView);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Hien thong bao va thoat khoi tinh nang sua tu
     * @param e
     * @throws IOException
     */
    @FXML
    public void cancel(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure exit ?");

        Optional<ButtonType> option = alert.showAndWait();

        if(option.get() == ButtonType.OK){
            Stage stage =  (Stage)cancel.getScene().getWindow();
            Parent addWordView = FXMLLoader.load(getClass().getResource("/FXML/sample.fxml"));
            Scene scene = new Scene(addWordView);
            stage.setScene(scene);
            stage.show();
        }
    }

}
