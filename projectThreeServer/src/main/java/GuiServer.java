
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{


    TextField s1,s2,s3,s4, c1;
    Button serverChoice,clientChoice,b1;
    HashMap<String, Scene> sceneMap;
    GridPane grid;
    HBox buttonBox;
    VBox clientBox;
    Scene startScene;
    BorderPane startPane;
    Server serverConnection;
//    Client clientConnection;
    HBox clientsPane;
    ListView<String> listItems1;
    ListView<String> listItems2;
    ListView<String> listItems3;
    ListView<String> listItems4;
    Button onButton = new Button("On");
    Button offButton = new Button("Off");

    ComboBox<Integer> portNumbers;
    TextArea clientJoin, numClients, clientLeft;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("The Networked Server GUI Example");

        this.serverChoice = new Button("Start Server");
        serverChoice.setStyle("-fx-font-size: 20px");

        this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
            primaryStage.setTitle("This is the Server");
            serverConnection = new Server(data -> {
                Platform.runLater(()->{
                    PokerInfo data2 = (PokerInfo)data;
                    listItems1.getItems().addAll(data2.message, data2.queenHighMessage, data2.pairPlusMessage);
                    numClients.setText("Number\nof clients\nconnected\n" + serverConnection.count);
                    clientJoin.setText("Client " + serverConnection.count + "\nJOINS\nthe\nserver");
                    clientLeft.setText("Client " + serverConnection.count +  "\nLEFT\nthe\nserver");
                });
            });
            onButton.setDisable(true);
        });

        Text serverText = new Text("SERVER");
        serverText.setStyle("-fx-font-size: 250px");
        serverText.setTranslateY(20);

        serverChoice.setTranslateX(330);
        serverChoice.setTranslateY(70);

        TextField portNum = new TextField();
        portNum.setPromptText("Enter Port Number");
        portNum.setMaxWidth(200);
        portNum.setPrefHeight(40);
        portNum.setStyle("-fx-font-size: 20px");
        portNum.setTranslateX(300);
        portNum.setTranslateY(50);

        VBox myVbox = new VBox();
        myVbox.getChildren().addAll(serverText, portNum, serverChoice);
        myVbox.setTranslateX(200);
        startPane = new BorderPane();
        startPane.setStyle("-fx-background-color: #5f9bb7");
        startPane.setCenter(myVbox);

        startScene = new Scene(startPane, 1250,750);



        sceneMap = new HashMap<String, Scene>();
        sceneMap.put("server",  createServerGui());

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        startScene.getRoot().requestFocus();
        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    public HBox items(){
        listItems1 = new ListView<String>();
        TitledPane listBox1 = new TitledPane();
        listBox1.setText("Client 1");
        listBox1.setStyle("-fx-alignment: center; -fx-font-size: 15px");
        listBox1.setCollapsible(false);
        listBox1.setPrefHeight(400);
        listBox1.setPrefWidth(200);
        TextField listBox1Bet = new TextField("Total Bet: $");
        listBox1Bet.setStyle("-fx-font-size: 20px");
        listBox1Bet.setEditable(false);
        VBox temp1 = new VBox();
        temp1.getChildren().addAll(listBox1Bet, listItems1);
        listBox1.setContent(temp1);

        listItems2 = new ListView<String>();
        TitledPane listBox2 = new TitledPane();
        listBox2.setText("Client 2");
        listBox2.setStyle("-fx-alignment: center; -fx-font-size: 15px");
        listBox2.setCollapsible(false);
        listBox2.setPrefHeight(400);
        listBox2.setPrefWidth(200);
        TextField listBox2Bet = new TextField("Total Bet: $");
        listBox2Bet.setStyle("-fx-font-size: 20px");
        listBox2Bet.setEditable(false);
        VBox temp2 = new VBox();
        temp2.getChildren().addAll(listBox2Bet, listItems2);
        listBox2.setContent(temp2);

        listItems3 = new ListView<String>();
        TitledPane listBox3 = new TitledPane();
        listBox3.setText("Client 3");
        listBox3.setStyle("-fx-alignment: center; -fx-font-size: 15px");
        listBox3.setCollapsible(false);
        listBox3.setPrefHeight(400);
        listBox3.setPrefWidth(200);
        TextField listBox3Bet = new TextField("Total Bet: $");
        listBox3Bet.setStyle("-fx-font-size: 20px");
        listBox3Bet.setEditable(false);
        VBox temp3 = new VBox();
        temp3.getChildren().addAll(listBox3Bet, listItems3);
        listBox3.setContent(temp3);

        listItems4 = new ListView<String>();
        TitledPane listBox4 = new TitledPane();
        listBox4.setText("Client 4");
        listBox4.setStyle("-fx-alignment: center; -fx-font-size: 15px");
        listBox4.setCollapsible(false);
        listBox4.setPrefHeight(400);
        listBox4.setPrefWidth(200);
        TextField listBox4Bet = new TextField("Total Bet: $");
        listBox4Bet.setStyle("-fx-font-size: 20px");
        listBox4Bet.setEditable(false);
        VBox temp4 = new VBox();
        temp4.getChildren().addAll(listBox4Bet, listItems4);
        listBox4.setContent(temp4);

        clientsPane = new HBox();
        clientsPane.setSpacing(20);
        clientsPane.getChildren().addAll(listBox1, listBox2, listBox3, listBox4);
        clientsPane.setAlignment(Pos.CENTER);
        return clientsPane;
    }

    public VBox information(){
        VBox information = new VBox();

        numClients = new TextArea();
        numClients.setText("Number\nof\nclients\nconnected");
        numClients.setStyle("-fx-font-size: 25px; -fx-alignment: center");
        numClients.setPrefHeight(200);
        numClients.setPrefWidth(200);

        clientJoin = new TextArea();
        clientJoin.setText("Client\nJOINS\nthe\nserver");
        clientJoin.setStyle("-fx-font-size: 25px; -fx-text-alignment: center");
        clientJoin.setPrefHeight(200);
        clientJoin.setPrefWidth(100);

        clientLeft = new TextArea();
        clientLeft.setText("Client\nLEFT\nthe\nserver");
        clientLeft.setStyle("-fx-font-size: 25px; -fx-alignment: center");
        clientLeft.setPrefHeight(200);
        clientLeft.setPrefWidth(100);




        onButton.setStyle("-fx-font-size: 30px; -fx-background-color: #328a17; -fx-border-color: black; -fx-text-fill: white");
        offButton.setStyle("-fx-font-size: 30px; -fx-background-color: #a11d1d; -fx-border-color: black; -fx-text-fill: white");

        onButton.setOnAction(e->{
            serverConnection.start();
            onButton.setDisable(true);
            offButton.setDisable(false);
        });
        offButton.setOnAction(e->{
            offButton.setDisable(true);
            onButton.setDisable(false);
            serverConnection.stop();
        });

        HBox buttonsHbox = new HBox();
        buttonsHbox.getChildren().addAll(onButton, offButton);
        buttonsHbox.setSpacing(20);
        buttonsHbox.setAlignment(Pos.CENTER);

        information.getChildren().addAll(buttonsHbox, numClients, clientJoin, clientLeft);
        information.setSpacing(20);

        return information;
    }

    public Scene createServerGui() {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(70));
        pane.setStyle("-fx-background-color: coral");

        pane.setRight(items());
        pane.setLeft(information());
        Scene serverScene = new Scene(pane, 1250,750);
        serverScene.getRoot().requestFocus();
        return serverScene;
    }
}
