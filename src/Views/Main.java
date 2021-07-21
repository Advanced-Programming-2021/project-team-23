package Views;

import Controllers.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main extends Application {

    public static Socket socket;
    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;

    public static Stage stage;
    public static Pane pane;

    public static void main(String[] args) throws IOException {
        initializeNetwork();
        launch(args);
    }

    public static void initializeNetwork(){
        try {
            socket = new Socket("8.tcp.ngrok.io", 10322);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        stage = mainStage;
        pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/registerMenu.fxml"));
        stage.setScene(new Scene(pane));
        stage.show();
        RegisterMenu registerMenu = new RegisterMenu();
        registerMenu.show();
    }

    public static String getResult(String string) throws IOException {
        dataOutputStream.writeUTF(string);
        dataOutputStream.flush();
        return dataInputStream.readUTF();
    }
}
