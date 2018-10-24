package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerAddWord extends Controller implements Initializable {
    public TextField target;
    public HTMLEditor explain;
    public Button add1;
    public Button cancel;

    public ControllerAddWord() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }

    /**
     * Goi ham add o class Controller
     * Neu chua them tu tieng anh hoac chua them nghia cua tu se hien ra canh bao
     * Hien ra thong bao sau khi da them tu
     * Reset lai table ListView va listFavourite
     * Co the tiep tuc them tu ngay tai scene nay
     * @param event
     * @throws IOException
     */
    public void add(ActionEvent event) throws IOException {
        if(target.getText().isEmpty() && explain.getHtmlText().isEmpty()){
            showAlertWarning("You haved not enterd word and explain!");
        }
        else if(explain.getHtmlText().isEmpty()){
            showAlertWarning("You haved not enterd explain!");
        }
        else if(target.getText().isEmpty()){
            showAlertWarning("You haved not enterd word!");
        }
        else {
            String word = target.getText();
            String html = explain.getHtmlText();

            addWord(word, html);
            resetTable();
            showAlertInfo("Inserted success!");

            //Tiep tuc add cho den khi nhan cancel
            target.setText("");
            explain.setHtmlText("");
        }
    }

    /**
     * Hien ra thong bao va thoat khoi chuc nang them tu
     * Chuyen ve scene chinh
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
