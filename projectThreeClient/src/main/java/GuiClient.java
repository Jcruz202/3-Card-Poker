import java.util.HashMap;
import java.util.Stack;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class GuiClient extends Application{

    private Button playButton, foldButton, dealButton;
    private HashMap<String, Scene> sceneMap;
    private HBox buttonBox;

    private VBox clientBox, listBox;
    private Scene startScene;
    private BorderPane startPane;

    private Client clientConnection;
    private ListView<String> listItems;

    private StackPane listItemPane;
    private TextField portNum, ipAdrress;

    BorderPane gameBorderPane = new BorderPane();
    TextField playerPrize = new TextField();
    TextField dealerHand = new TextField();
    TextField playerHand = new TextField();

    PokerInfo pokerInfo = new PokerInfo();

    HBox handBox = new HBox();

    HBox backCards = new HBox();
    VBox handPane = new VBox();
    HBox buttonsHBox  = new HBox();
    Stage stage;
    PokerInfo data2;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        // TODO Auto-generated method stub
        stage.setTitle("The Networked Client GUI Example");

        Button clientChoice = new Button("Start");
        clientChoice.setStyle("-fx-font-size: 20px");

        clientChoice.setOnAction(e-> {
            String portNumInput = portNum.getText().trim();
            String ipAddressInput = ipAdrress.getText().trim();
            if (portNumInput.isEmpty() || ipAddressInput.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please fill out both Port Number and IP Address fields");
                alert.showAndWait();
            } else {
                int portNumber = Integer.valueOf(portNumInput);
                int ipAddressNumber = Integer.valueOf(ipAddressInput);

                pokerInfo.portNumber = portNumber;
                pokerInfo.ipAddressNumber = ipAddressNumber;

                stage.setScene(sceneMap.get("client"));
                stage.setTitle("This is a client");
            }

                clientConnection = new Client(data -> {
                    Platform.runLater(() -> {
                        data2 = (PokerInfo) data;
                        listItems.getItems().addAll(data2.message, data2.queenHighMessage, data2.pairPlusMessage);
                        playerHand.setText(data2.hand);
                        handBox.getChildren().clear();
                        handBox.getChildren().add(displayCards(data2.cardsPicked));
                        backCards.setTranslateY(-40);
                        handBox.setTranslateY(-20);
                        handPane.setTranslateX(-150);
                        handPane.setTranslateY(30);
                        gameBorderPane.setRight(handPane);
                        gameBorderPane.setBottom(buttonsHBox);
                        playerPrize.setText("Total Winnings: $" + 0);
                    });
                });
            clientConnection.start();
            clientConnection.send(pokerInfo);
        });
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.DIMGREY);

        Text welcomeText = new Text("3 Card\n Poker");
        welcomeText.setEffect(dropShadow);
        welcomeText.setStyle("-fx-font-size: 150px; -fx-font-weight: bold; -fx-fill: #FFD700");
        StackPane textBox = new StackPane();
        textBox.getChildren().add(welcomeText);
        textBox.setTranslateX(-90);
        textBox.setTranslateY(-80);

        portNum = new TextField();
        portNum.setPromptText("Enter Port Number");
        portNum.setMaxWidth(200);
        portNum.setMaxHeight(200);
        portNum.setTranslateX(850);
        portNum.setTranslateY(500);
        ipAdrress = new TextField();
        ipAdrress.setPromptText("Enter IP Address");
        ipAdrress.setMaxWidth(200);
        ipAdrress.setMaxHeight(200);
        ipAdrress.setTranslateX(850);
        ipAdrress.setTranslateY(-180);


        this.buttonBox = new HBox(400, clientChoice);
        buttonBox.setTranslateX(900);
        buttonBox.setTranslateY(580);
        startPane = new BorderPane();
        startPane.setRight(textBox);
        startPane.setBottom(ipAdrress);
        startPane.setTop(portNum);
        startPane.setCenter(buttonBox);
        startPane.setStyle("-fx-background-image: url('pokerBackground.png'); -fx-background-size: cover;");

        startScene = new Scene(startPane, 1250,750);

        listItems = new ListView<String>();
        listItems.setStyle("-fx-alignment: center; -fx-font-size: 20px");
        listBox = new VBox();
        listBox.getChildren().add(listItems);
        listItemPane = new StackPane(listBox);
        listBox.setPrefWidth(400);
        listBox.setPrefHeight(380);
        listItemPane.setTranslateX(80);
//        listItemPane.setTranslateY(30);

        sceneMap = new HashMap<String, Scene>();

        sceneMap.put("client",  createClientGui());

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        startScene.getRoot().requestFocus();
        stage.setScene(startScene);
        stage.show();
    }

    private MenuBar menuBar(){
        final int[] themeClick = {0};
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> exit());
        MenuItem freshStartItem = new MenuItem("Fresh Start");
        freshStartItem.setOnAction(e -> freshStart());
        MenuItem newLookItem = new MenuItem("New Look");
        newLookItem.setOnAction(e -> newLook(themeClick));
        Menu gameMenu = new Menu("Options");
        gameMenu.setStyle("-fx-font-size: 20px");
        gameMenu.getItems().addAll(exitItem, freshStartItem, newLookItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }



    public Scene createClientGui() {

        TextField anteField = new TextField();
        anteField.setPromptText("Enter Ante Bet (5-25)");
        anteField.setStyle("-fx-font-size: 20px");
        anteField.setMaxWidth(250);
        anteField.setTranslateX(150);
        TextField pairPlusField = new TextField();
        pairPlusField.setPromptText("Enter Pair Plus Bet (5-25)");
        pairPlusField.setStyle("-fx-font-size: 20px");
        pairPlusField.setMaxWidth(250);
        pairPlusField.setTranslateX(150);
        TextField playWagerField = new TextField();
        playWagerField.setPromptText("Play Wager Bet (5-25)");
        playWagerField.setStyle("-fx-font-size: 20px");
        playWagerField.setMaxWidth(250);
        playWagerField.setTranslateX(150);

        playButton = new Button("Play");
        playButton.setTranslateX(750);
        playButton.setTranslateY(-30);
        playButton.setStyle("-fx-font-size: 20px");

        foldButton = new Button("Fold");
        foldButton.setTranslateX(800);
        foldButton.setTranslateY(-30);
        foldButton.setStyle("-fx-font-size: 20px");

        dealButton = new Button("Deal");
        dealButton.setTranslateX(250);
        dealButton.setTranslateY(-30);
        dealButton.setStyle("-fx-font-size: 20px");

        buttonsHBox.getChildren().clear();
        buttonsHBox.getChildren().addAll(dealButton);

        playerHand.setPrefWidth(200);
        playerHand.setPrefHeight(60);
        playerHand.setEditable(false);
        playerHand.setStyle("-fx-font-size: 30px; -fx-alignment: Center");

        dealerHand.setPrefWidth(200);
        dealerHand.setPrefHeight(60);
        dealerHand.setEditable(false);
        dealerHand.setStyle("-fx-font-size: 30px; -fx-alignment: Center");

        
        playerPrize.setPrefWidth(100);
        playerPrize.setPrefHeight(60);
//        playerPrize.setTranslateY(10);
        playerPrize.setTranslateX(80);
        playerPrize.setEditable(false);
        playerPrize.setStyle("-fx-font-size: 30px; -fx-alignment: Center");

        clientBox = new VBox(playerPrize, listItemPane, anteField, pairPlusField, playWagerField);
        clientBox.setTranslateY(10);
        clientBox.setSpacing(15);
        gameBorderPane.setLeft(clientBox);
        gameBorderPane.setTop(menuBar());
        gameBorderPane.setStyle("-fx-background-image: url('pokerGreenBakcground.jpeg'); -fx-background-size: cover;");

        Scene clientScene = new Scene(gameBorderPane, 1250, 750);

        String[] tempCards = new String[3];
        for(int i=0; i<3; i++){
            tempCards[i] = "\uD83C\uDCA0";
        }
        Text playerText = new Text("Player");
        playerText.setStyle("-fx-font-size: 50px; -fx-fill: #ffffff");
        Text dealerText = new Text("Dealer");
        dealerText.setStyle("-fx-font-size: 50px; -fx-fill: #ffffff");


        HBox dealerHbox = new HBox();
        dealerHbox.getChildren().addAll(dealerText, dealerHand);
        dealerHbox.setAlignment(Pos.CENTER);
        dealerHbox.setSpacing(20);

        HBox playerHbox = new HBox();
        playerHbox.getChildren().addAll(playerText, playerHand);
        playerHbox.setAlignment(Pos.CENTER);
        playerHbox.setTranslateY(20);
        playerHbox.setSpacing(20);

        Line line = new Line(50, 50, 500, 50);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(3.0);

        foldButton.setOnAction(e->{
            stage.setScene(foldScene());
        });

        dealButton.setOnAction(e -> {
            String anteInput = anteField.getText().trim();
            String pairPlusInput = pairPlusField.getText().trim();
            String playWagerInput = playWagerField.getText().trim();
            if (anteInput.isEmpty() || playWagerInput.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please fill out both Ante fields and Play wager");
                alert.showAndWait();
            } else {
                int anteBet = Integer.valueOf(anteInput);
                int pairPlusBet = Integer.valueOf(pairPlusInput);
                int playWager = Integer.valueOf(playWagerInput);
                pokerInfo.anteBet = anteBet;
                pokerInfo.pairPlus = pairPlusBet;
                pokerInfo.playBet = playWager;
                backCards.getChildren().add(displayBackCard(tempCards));
                handPane.getChildren().addAll(dealerHbox, backCards, line, playerHbox, handBox);
                buttonsHBox.getChildren().addAll(playButton, foldButton);
                dealButton.setDisable(true);
                clientConnection.send(pokerInfo);
            }

            playButton.setOnAction(event -> {
                playerPrize.clear();
                playerPrize.setText("Total Winnings: $" + data2.total);
                PauseTransition pause = new PauseTransition(Duration.seconds(.001)); // Add a 1 second pause
                RotateTransition flip = new RotateTransition(Duration.seconds(1), backCards); // Add a flip transition
                flip.setAxis(Rotate.Y_AXIS);
                flip.setFromAngle(0);
                flip.setToAngle(90);
                flip.setOnFinished(event1 -> {
                    backCards.getChildren().clear();
                    backCards.getChildren().add(displayCards(data2.dealerHand));
                    dealerHand.setText(data2.tempDealerHand);
                    playButton.setDisable(true);
                    foldButton.setDisable(true);
                    clientScene.getRoot().requestFocus();
                    RotateTransition flipBack = new RotateTransition(Duration.seconds(1), backCards); // Add a flip back transition
                    flipBack.setAxis(Rotate.Y_AXIS);
                    flipBack.setFromAngle(90);
                    flipBack.setToAngle(0);
                    flipBack.play();
                    if(data2.queenHigh == true) {
                        if (data2.winner.equals("player")) {
                            PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
                            pause2.setOnFinished(ee -> {
                                Scene winScene = createWinScreen();
//                                Stage stage = (Stage) playButton.getScene().getWindow();
                                stage.setScene(winScene);
                            });
                            pause2.play();
                        } else if (data2.winner.equals("dealer")) {
                            PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
                            pause2.setOnFinished(ee -> {
                                Scene loseScene = createLoseScreen();
//                                Stage stage = (Stage) playButton.getScene().getWindow();
                                stage.setScene(loseScene);
                            });
                            pause2.play();
                        }
                    }else if(data2.queenHigh == false){
                        PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
                        pause2.setOnFinished(ee -> {
                            Scene notQualifyScene = createNotQualify();
//                            Stage stage = (Stage) playButton.getScene().getWindow();
                            stage.setScene(notQualifyScene);
                        });
                        pause2.play();
                    }
                });
                pause.setOnFinished(actionEvent -> flip.play());
                pause.play();
            });
        });

        clientScene.getRoot().requestFocus();
        return clientScene;
    }

    private HBox displayCards(String[] playerHand) {
        Label card1 = new Label(playerHand[0]);
        card1.setFont(Font.font("Arial", 200));
        card1.setTextFill(Color.WHITE);

        Label card2 = new Label(playerHand[1]);
        card2.setFont(Font.font("Arial", 200));
        card2.setTextFill(Color.WHITE);

        Label card3 = new Label(playerHand[2]);
        card3.setFont(Font.font("Arial", 200));
        card3.setTextFill(Color.WHITE);

//        for(int i=0; i<3; i++){
//            if((data2.playerID[i].charAt(0) == 's') || (data2.playerID[i].charAt(0) == 'c')){
//                card1.setTextFill(Color.WHITE);
//                card2.setTextFill(Color.WHITE);
//                card3.setTextFill(Color.WHITE);
//            }
//            else{
//                card1.setTextFill(Color.RED);
//                card2.setTextFill(Color.RED);
//                card3.setTextFill(Color.RED);
//            }
//        }

        HBox handBox = new HBox(10, card1, card2, card3);
        handBox.setAlignment(Pos.CENTER);

        return handBox;
    }

    public Scene foldScene(){
        VBox winBox = new VBox();
        winBox.setAlignment(Pos.CENTER);
        winBox.setSpacing(20);
        winBox.setStyle("-fx-background-color: #1E90FF;"); // set blue background

        Text winText = new Text("You folded!");
        winText.setStyle("-fx-font-size: 70px; -fx-fill: #000000; -fx-font-weight: bold;"); // set black and bold text

        Text winInfo = new Text("Money Won: $" + data2.pairPlus + "\nMoney Lost: $" + data2.lost);
        winInfo.setStyle("-fx-font-size: 30px;");

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;"); // set black and bold button text

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;");

        HBox buttonsHbox = new HBox();
        buttonsHbox.getChildren().addAll(playAgainButton, exitGameButton);
        buttonsHbox.setAlignment(Pos.CENTER);
        buttonsHbox.setSpacing(30);
        winBox.getChildren().addAll(winText, winInfo, buttonsHbox);

        exitGameButton.setOnAction(e-> exit());
        playAgainButton.setOnAction(e-> {
            playAgain();
            stage.setScene(createClientGui());
            stage.show();
        });

        Scene foldScene = new Scene(winBox, 1250, 750);
        return foldScene;
    }

    private HBox displayBackCard(String[] dealerHand) {

        Label card1 = new Label(dealerHand[0]);
        card1.setFont(Font.font("Arial", 200));
        card1.setTextFill(Color.DARKBLUE); // set text color to white

        Label card2 = new Label(dealerHand[0]);
        card2.setFont(Font.font("Arial", 200));
        card2.setTextFill(Color.DARKBLUE); // set text color to white

        Label card3 = new Label(dealerHand[0]);
        card3.setFont(Font.font("Arial", 200));
        card3.setTextFill(Color.DARKBLUE); // set text color to white

        HBox handBox = new HBox(10, card1, card2, card3);
        handBox.setAlignment(Pos.CENTER);

        return handBox;
    }

    private void freshStart(){
        pokerInfo.playBet =0;
        pokerInfo.total =0;
        pokerInfo.pairPlus=0;
        pokerInfo.anteBet=0;
        pokerInfo.dHand=0;
        pokerInfo.hand = "";
        pokerInfo.tempDealerHand ="";
        playerPrize.setText("Total Winnings: $" + 0);
        reset();
    }

    private void playAgain(){
        pokerInfo.dHand=0;
        pokerInfo.hand = "";
        pokerInfo.tempDealerHand ="";
        stage.setScene(createClientGui());
        reset();

    }

    private void reset() {
        for(int i=0;i<3;i++){
            pokerInfo.playerID[i] = "";
            pokerInfo.cardsPicked[i] = "";
            pokerInfo.dealerHand[i] ="";
            pokerInfo.dealerID[i] ="";
        }
        clientConnection.send(pokerInfo);

        handPane.getChildren().clear();
        dealButton.setDisable(false);
        playButton.setDisable(false);
        foldButton.setDisable(false);
    }

    private void exit() {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit");
        exitAlert.setHeaderText("Are you sure you want to exit?");
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        exitAlert.getButtonTypes().setAll(yes, no);
        if (exitAlert.showAndWait().orElse(no) == yes) {
            Platform.exit();
        }
    }

    private Scene createNotQualify() {
        VBox winBox = new VBox();
        winBox.setAlignment(Pos.CENTER);
        winBox.setSpacing(20);
        winBox.setStyle("-fx-background-color: #1E90FF;"); // set blue background

        Text winText = new Text("Dealer Didn't Qualify!");
        winText.setStyle("-fx-font-size: 70px; -fx-fill: #000000; -fx-font-weight: bold;"); // set black and bold text

        Text winInfo = new Text("Money Won: $" + data2.pairPlus + "\nMoney Lost: $" + data2.lost);
        winInfo.setStyle("-fx-font-size: 30px;");

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;"); // set black and bold button text

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;");

        HBox buttonsHbox = new HBox();
        buttonsHbox.getChildren().addAll(playAgainButton, exitGameButton);
        buttonsHbox.setAlignment(Pos.CENTER);
        buttonsHbox.setSpacing(30);
        winBox.getChildren().addAll(winText, winInfo, buttonsHbox);

        exitGameButton.setOnAction(e-> exit());
        playAgainButton.setOnAction(e-> {
            playAgain();
            stage.setScene(createClientGui());
            stage.show();
        });

        Scene winScene = new Scene(winBox, 1250, 750);

        return winScene;
    }

    private Scene createWinScreen() {
        VBox winBox = new VBox();
        winBox.setAlignment(Pos.CENTER);
        winBox.setSpacing(20);
        winBox.setStyle("-fx-background-color: #1E90FF;");

        Text winText = new Text("Player Wins!");
        winText.setStyle("-fx-font-size: 70px; -fx-fill: #000000; -fx-font-weight: bold;");

        Text winInfo = new Text("Money Won: $" + data2.total + "\nMoney Lost: $" + data2.lost);
        winInfo.setStyle("-fx-font-size: 30px");

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;");

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;");

        HBox buttons1Hbox = new HBox();
        buttons1Hbox.getChildren().addAll(playAgainButton, exitGameButton);
        buttons1Hbox.setAlignment(Pos.CENTER);
        buttons1Hbox.setSpacing(30);
        winBox.getChildren().addAll(winText, winInfo, buttons1Hbox);

        exitGameButton.setOnAction(e-> exit());
        playAgainButton.setOnAction(e-> {
//            Scene newScene = createClientGui();
//            Stage stage = (Stage) playAgainButton.getScene().getWindow();
//            stage.setScene(newScene);
//            stage.show();
            playAgain();
            stage.setScene(createClientGui());
            stage.show();
        });

        Scene winScene = new Scene(winBox, 1250, 750);

        return winScene;
    }

    private Scene createLoseScreen() {
        VBox winBox = new VBox();
        winBox.setAlignment(Pos.CENTER);
        winBox.setSpacing(20);
        winBox.setStyle("-fx-background-color: #1E90FF;"); // set blue background

        Text winText = new Text("Dealer Wins!");
        winText.setStyle("-fx-font-size: 70px; -fx-fill: #000000; -fx-font-weight: bold;"); // set black and bold text

        Text winInfo = new Text("Money Won: $" + data2.pairPlus + "\nMoney Lost: $" + data2.lost);
        winInfo.setStyle("-fx-font-size: 30px;");

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;"); // set black and bold button text

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;");

        HBox buttonsHbox = new HBox();
        buttonsHbox.getChildren().addAll(playAgainButton, exitGameButton);
        buttonsHbox.setAlignment(Pos.CENTER);
        buttonsHbox.setSpacing(30);
        winBox.getChildren().addAll(winText, winInfo, buttonsHbox);

        exitGameButton.setOnAction(e-> exit());
        playAgainButton.setOnAction(e-> {
            playAgain();
            stage.setScene(createClientGui());
            stage.show();
        });

        Scene winScene = new Scene(winBox, 1250, 750);

        return winScene;
    }

    private void newLook(int[] themeClick){

        if (themeClick[0] == 0) {
            themeClick[0]++;
            gameBorderPane.setStyle("-fx-background-image: url('pokerBlueBackground.jpeg'); -fx-background-size: cover;");
        }
        else if (themeClick[0] == 1) {
            themeClick[0] = 0;
            gameBorderPane.setStyle("-fx-background-image: url('pokerGreenBakcground.jpeg'); -fx-background-size: cover;");

        }
    }
}