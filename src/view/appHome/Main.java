package view.appHome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import service.impl.EmployeeFileWriterServiceImpl;
import service.impl.UserFileWriterServiceImpl;
import service.impl.DepartmentFileWriterServiceImpl;
import service.impl.DesignationFileWriterServiceImpl;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        UserFileWriterServiceImpl.startFileWriterService();
        EmployeeFileWriterServiceImpl.startFileWriterService();
        DepartmentFileWriterServiceImpl.startFileWriterService();
        DesignationFileWriterServiceImpl.startFileWriterService();

        Parent root = FXMLLoader.load(getClass().getResource("/view/appHome/appLogin.fxml"));
        primaryStage.setTitle("HR Management System");
        primaryStage.getIcons().add(new Image("/lib/images/favicon.png"));
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
