/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balloonpop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import java.lang.Runnable;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import static javafx.application.Application.launch;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import static javafx.scene.media.AudioClip.INDEFINITE;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.*;

/**
 *
 * @author anorryce
 */
public class BalloonPop implements ActionListener{
    JFrame jF;
    JLabel l1,l2,l3,l4;
    JTextField t1,t2,t3,t4;
    JButton b1,b2;
    JPanel p1,p2;
    Stage stTrue;
    
    private static final AudioClip CLICK_AUDIOCLIP = new AudioClip(BalloonPop.class.getResource("/balloonPopSound.mp3").toString());
    
    Image balloonBlue,balloonGre,balloonBla;
    Button blnBluBtn,blnGreBtn,blnBlaBtn,muteBtn,exitBtn;
    boolean isGold;
    int ranNum1,ranNum2,ranNum3;
    double r1, r2, r3;
    int redSets = 0;
    int level = 0;
    int levelScore = 0; //this is to tell if the user has reached 1000 points (ie, the next level)
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        new BalloonPop();
        launch(args);
    }
    public BalloonPop(){
        jF = new JFrame("Calculator");
        jF.setSize(300,300);
        jF.setLocation(500,200);
        jF.setLayout(new BorderLayout());
        jF.setVisible(true);
        
        l1 = new JLabel("Num1");
        l2 = new JLabel("Num2");
        l3 = new JLabel("Num3");
        l4 = new JLabel("Num4");
        
        t1 = new JTextField();
        t2 = new JTextField();
        t3 = new JTextField();
        t4 = new JTextField();
        
        p1 = new JPanel();
        p1.setLayout(new GridLayout(4,2));
        p1.add(l1);
        p1.add(t1);
        p1.add(l2);
        p1.add(t2);
        p1.add(l3);
        p1.add(t3);
        p1.add(l4);
        p1.add(t4); 
        jF.add(p1,BorderLayout.CENTER);
        jF.setVisible(true);
        
        p2 = new JPanel();
        p2.setLayout(new GridLayout(1,2));
        b1 = new JButton("Add");
        b1.addActionListener(this);
        
        b2 = new JButton("Average");
        b2.addActionListener(this);
        
        p2.add(b1);
        p2.add(b2);
        jF.add(p2,BorderLayout.SOUTH);
        
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==b1){
            Game fx = new Game();
            try {
                fx.start(stTrue);
            } catch (Exception ex) {
                System.out.println("Dun goofed");
                Logger.getLogger(BalloonPop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            int x1 = Integer.parseInt(t1.getText());
            int x2 = Integer.parseInt(t2.getText());
            int x3 = Integer.parseInt(t3.getText());
            
            int avg = (x1+x2+x3)/3;
            t4.setText(avg+"");
        }
    }
    public class Game extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {
            Label paused = new Label("PAUSED");
            paused.setStyle("-fx-font: 50 arial; -fx-text-fill: white;");

            URL resource = getClass().getResource("/backgroundMusic.mp3");
            MediaPlayer a =new MediaPlayer(new Media(resource.toString()));
            a.setOnEndOfMedia(new Runnable() {
                  public void run() {
                    a.seek(Duration.ZERO);
                  }
            });
            a.setCycleCount(MediaPlayer.INDEFINITE);
            a.setStopTime(Duration.INDEFINITE);
            a.play();

            Label score = new Label("0");
            score.setStyle("-fx-font: 30 arial; -fx-text-fill: black;");

            PathTransition pt1 = new PathTransition();
            PathTransition pt2 = new PathTransition();
            PathTransition pt3 = new PathTransition();

            for(int i = 0; i<3;i++){
                if(i==0){
                    r1 = (int) ((Math.random() * 90) + 10) / 100.0;
                }
                else if(i==1){
                    r2 = (int) ((Math.random() * 90) + 10) / 100.0;
                }
                else if(i==2){
                    r3 = (int) ((Math.random() * 90) + 10) / 100.0;
                }
            }
            ranNum1 = ThreadLocalRandom.current().nextInt(1,7);
            ranNum2 = ThreadLocalRandom.current().nextInt(1,7);
            ranNum3 = ThreadLocalRandom.current().nextInt(1,7);

            Image cloudIMG = new Image("cloud.png");
            Image balloonBlueIMG = new Image("balloonBlue.png");
            Image balloonGreenIMG = new Image("balloonGreen.png");
            Image balloonBlackIMG = new Image("balloonBlack.png");
            Image balloonYellowIMG = new Image("balloonYellow.png");
            Image balloonRedIMG = new Image("balloonRed.png");
            Image balloonSilverIMG = new Image("balloonSilver.png");
            Image balloonOrangeIMG = new Image("balloonOrange.png");
            Image muteIMG = new Image("mute.png");
            Image exitIMG = new Image("exit.png");
            Image pauseIMG = new Image("pause.png");
            Image playIMG = new Image("play.png");

            ImageView muteBtnIV = new ImageView();
            muteBtnIV.setImage(muteIMG);
            muteBtnIV.setFitWidth(20);
            muteBtnIV.setPreserveRatio(true);
            muteBtnIV.setSmooth(true);
            muteBtnIV.setCache(true);
            Button muteBtn = new Button();
            muteBtn.setGraphic(muteBtnIV);
            muteBtn.setOnAction(e->{
                if(a.isMute()){
                    a.muteProperty().set(false);
                }
                else {
                    a.muteProperty().set(true);
                }
            });
            muteBtn.setTooltip(new Tooltip("mute"));
            muteBtn.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");

            ImageView balloonIV1 = new ImageView();
            if(r1 >= 0 && r1 < .3){//30% chance of getting a blue balloon
                balloonIV1.setImage(balloonBlueIMG);
            }
            else if(r1 < .49){//19% chance of getting a green balloon
                balloonIV1.setImage(balloonGreenIMG);
            }
            else if(r1 < .64){//15% chance of getting a black balloon
                balloonIV1.setImage(balloonBlackIMG);
            }
            else if(r1 < .64){//15% chance of getting a black balloon
                balloonIV1.setImage(balloonBlackIMG);
            }
            else if(r1 < .69){//5% chance of getting a yellow balloon
                balloonIV1.setImage(balloonYellowIMG);
            }
            else if(r1 < .82){//%13 chance of getting a red balloon
                balloonIV1.setImage(balloonRedIMG);
            }
            else if(r1 < .9){//8% chance of getting a silver balloon
                balloonIV1.setImage(balloonSilverIMG);
            }
            else if(r1 < 1){//10% chance of getting a orange balloon
                balloonIV1.setImage(balloonOrangeIMG);
            }
            balloonIV1.setFitWidth(50);
            balloonIV1.setPreserveRatio(true);
            balloonIV1.setSmooth(true);
            balloonIV1.setCache(true);

            ImageView balloonIV2 = new ImageView();
            if(r2 >= 0 && r2 < .3){//25% chance of getting a blue balloon
                balloonIV2.setImage(balloonBlueIMG);
            }
            else if(r2 < .49){//15% chance of getting a green balloon
                balloonIV2.setImage(balloonGreenIMG);
            }
            else if(r2 < .64){//25% chance of getting a black balloon
                balloonIV2.setImage(balloonBlackIMG);
            }
            else if(r2 < .69){//2% chance of getting a yellow balloon
                balloonIV2.setImage(balloonYellowIMG);
            }
            else if(r2 < .82){//%10 chance of getting a red balloon
                balloonIV2.setImage(balloonRedIMG);
            }
            else if(r2 < .9){//5% chance of getting a silver balloon
                balloonIV2.setImage(balloonSilverIMG);
            }
            else if(r2 < 1){//18% chance of getting an orange balloon
                balloonIV2.setImage(balloonOrangeIMG);
            }
            balloonIV2.setFitWidth(50);
            balloonIV2.setPreserveRatio(true);
            balloonIV2.setSmooth(true);
            balloonIV2.setCache(true);

            ImageView balloonIV3 = new ImageView();
            if(r3 >= 0 && r3 < .3){//25% chance of getting a blue balloon
                balloonIV3.setImage(balloonBlueIMG);
            }
            else if(r3 < .49){//15% chance of getting a green balloon
                balloonIV3.setImage(balloonGreenIMG);
            }
            else if(r3 < .64){//25% chance of getting a black balloon
                balloonIV3.setImage(balloonBlackIMG);
            }
            else if(r3 < .69){//2% chance of getting a yellow balloon
                balloonIV3.setImage(balloonYellowIMG);
            }
            else if(r3 < .82){//%10 chance of getting a red balloon
                balloonIV3.setImage(balloonRedIMG);
            }
            else if(r3 < .9){//5% chance of getting a silver balloon
                balloonIV3.setImage(balloonSilverIMG);
            }
            else if(r3 < 1){
                balloonIV3.setImage(balloonOrangeIMG);
            }
            balloonIV3.setFitWidth(50);
            balloonIV3.setPreserveRatio(true);
            balloonIV3.setSmooth(true);
            balloonIV3.setCache(true);

            Button balloonBtn1 = new Button();
            balloonBtn1.setGraphic(balloonIV1);
            balloonBtn1.setText("pt1");
            if(r1 >= 0 && r1 < .3){//25% chance of getting a blue balloon
                balloonBtn1.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=10;
                    score.setText(numScore+"");
                    levelScore+=10;
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r1 < .49){//15% chance of getting a green balloon
                balloonBtn1.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=50;
                    levelScore+=50;
                    score.setText(numScore+"");
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r1 < .64 && redSets >= 3){//25% chance of getting a black balloon
                balloonBtn1.setOnAction(e->{
                    StackPane sp = new StackPane();
                    sp.getChildren().add(balloonBtn1);
                    //sp.getChildren().add(cloudIMG);
                    int numScore = Integer.parseInt(score.getText());
                    numScore-=100;
                    levelScore+=100;
                    score.setText(numScore+"");
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);});
            }
            else if(r1 < .64){//25% chance of getting a black balloon
                balloonBtn1.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore-=100;
                    levelScore-=100;
                    score.setText(numScore+"");
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r1 < .69){//2% chance of getting a yellow balloon
                balloonBtn1.setOnAction(e->{
                    //next set of balloons are all silver.
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=5;
                    levelScore+=5;
                    score.setText(numScore+"");
                    isGold = true;
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r1 < .82){//%10 chance of getting a red balloon
                balloonBtn1.setOnAction(e->{
                    //sets negative balloons transparent for next 3 loops
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r1 < .9){//5% chance of getting a silver balloon
                balloonBtn1.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=200;
                    levelScore+=200;
                    score.setText(numScore+"");
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r1 < 1){
                balloonBtn1.setOnAction(e->{
                    //level increases (makes things harder)
                    balloonBtn1.setGraphic(null);
                    balloonBtn1.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            balloonBtn1.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");

            Button balloonBtn2 = new Button();
            balloonBtn2.setGraphic(balloonIV2);
            balloonBtn2.setText("pt2");
            if(r2 >= 0 && r2 < .3){//25% chance of getting a blue balloon
                balloonBtn2.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=10;
                    levelScore+=10;
                    score.setText(numScore+"");
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r2 < .49){//15% chance of getting a green balloon
                balloonBtn2.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=50;
                    levelScore+=50;
                    score.setText(numScore+"");
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r2 < .64){//25% chance of getting a black balloon
                balloonBtn2.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore-=100;
                    levelScore-=100;
                    score.setText(numScore+"");
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r2 < .69){//2% chance of getting a yellow balloon
                balloonBtn2.setOnAction(e->{
                    //next set of balloons are all silver.
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=5;
                    levelScore+=5;
                    score.setText(numScore+"");
                    isGold = true;
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r2 < .82){//%10 chance of getting a red balloon
                balloonBtn2.setOnAction(e->{
                    //sets negative balloons transparent for next 3 loops
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r2 < .9){//5% chance of getting a silver balloon
                balloonBtn2.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=200;
                    levelScore+=200;
                    score.setText(numScore+"");
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r2 < 1){
                balloonBtn2.setOnAction(e->{
                    //level increases (makes things harder)
                    balloonBtn2.setGraphic(null);
                    balloonBtn2.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            balloonBtn2.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");

            Button balloonBtn3 = new Button();
            balloonBtn3.setGraphic(balloonIV3);
            balloonBtn3.setText("pt3");
            if(r3 >= 0 && r3 < .3){//25% chance of getting a blue balloon
                balloonBtn3.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=10;
                    levelScore+=10;
                    score.setText(numScore+"");
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r3 < .49){//15% chance of getting a green balloon
                balloonBtn3.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=50;
                    levelScore+=50;
                    score.setText(numScore+"");
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r3 < .64){//25% chance of getting a black balloon
                balloonBtn3.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore-=100;
                    levelScore-=100;
                    score.setText(numScore+"");
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r3 < .69){//2% chance of getting a yellow balloon
                balloonBtn3.setOnAction(e->{
                    //next set of balloons are all silver.
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=5;
                    levelScore+=5;
                    score.setText(numScore+"");
                    isGold = true;
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r3 < .82){//%10 chance of getting a red balloon
                balloonBtn3.setOnAction(e->{
                    //sets negative balloons transparent for next 3 loops
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r3 < .9){//5% chance of getting a silver balloon
                balloonBtn3.setOnAction(e->{
                    int numScore = Integer.parseInt(score.getText());
                    numScore+=200;
                    levelScore+=200;
                    score.setText(numScore+"");
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            else if(r3 < 1){
                balloonBtn3.setOnAction(e->{
                    //level increases (makes things harder)
                    balloonBtn3.setGraphic(null);
                    balloonBtn3.setDisable(true);
                    BalloonPop.CLICK_AUDIOCLIP.play(1);});
            }
            balloonBtn3.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");

            pt1.setRate(.15);
            pt1.setDuration(Duration.millis(2000));
            pt1.setPath(new Line(0.0,500,-700,-450));
            pt1.setNode(balloonBtn1);
            pt1.setOnFinished(e->{/*
                //checks to see if level need to increase.
                if(levelScore<=1000){
                    level++;
                    levelScore-=1000;
                }
                //checks to see if speed needs to increase based upon the current level
                if(level <= 0 && level >= 10){
                    pt1.setRate((.06*level)+.15);
                }
                else{
                    pt1.setRate(.75);
                }*/
                balloonBtn1.setDisable(false);
                r1 = (int) ((Math.random() * 90) + 10) / 100.0;

                if(isGold){
                    balloonIV1.setImage(balloonSilverIMG);
                }
                else if(r1 >= 0 && r1 < .3){//25% chance of getting a blue balloon
                    balloonIV1.setImage(balloonBlueIMG);
                }
                else if(r1 < .49){//15% chance of getting a green balloon
                    balloonIV1.setImage(balloonGreenIMG);
                }
                else if(r1 < .64){//25% chance of getting a black balloon
                    balloonIV1.setImage(balloonBlackIMG);
                }
                else if(r1 < .69){//2% chance of getting a yellow balloon
                    balloonIV1.setImage(balloonYellowIMG);
                }
                else if(r1 < .82){//%10 chance of getting a red balloon
                    balloonIV1.setImage(balloonRedIMG);
                }
                else if(r1 < .9){//5% chance of getting a silver balloon
                    balloonIV1.setImage(balloonSilverIMG);
                }
                else if(r1 < 1){
                    balloonIV1.setImage(balloonOrangeIMG);
                }

                balloonBtn1.setGraphic(balloonIV1);

                if(isGold){
                    balloonBtn1.setOnAction(e2->{
                        //next set of balloons will all be silver
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=200;
                        levelScore+=200;
                        score.setText(numScore+"");
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);
                    isGold = false;});
                }
                else if(r1 >= 0 && r1 < .3){//25% chance of getting a blue balloon
                    balloonBtn1.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=10;
                        levelScore+=10;
                        score.setText(numScore+"");
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r1 < .49){//15% chance of getting a green balloon
                    balloonBtn1.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=50;
                        levelScore+=50;
                        score.setText(numScore+"");
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r1 < .64){//25% chance of getting a black balloon
                    balloonBtn1.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore-=100;
                        levelScore-=100;
                        score.setText(numScore+"");
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r1 < .69){//2% chance of getting a yellow balloon
                    balloonBtn1.setOnAction(e2->{
                        //next set of balloons are all silver.
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=5;
                        levelScore+=5;
                        score.setText(numScore+"");
                        isGold = true;
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r1 < .82){//%10 chance of getting a red balloon
                    balloonBtn1.setOnAction(e2->{
                        //sets negative balloons transparent for next 3 loops
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r1 < .9){//5% chance of getting a silver balloon
                    balloonBtn1.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=200;
                        levelScore+=200;
                        score.setText(numScore+"");
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r1 < 1){
                    balloonBtn1.setOnAction(e2->{
                        //level increases (makes things harder)
                        balloonBtn1.setGraphic(null);
                        balloonBtn1.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
            });

            pt2.setRate(.15);
            pt2.setDuration(Duration.millis(2000));
            pt2.setPath(new Line(0.0,500,0,-450));
            pt2.setNode(balloonBtn2);
            pt2.setOnFinished(e->{/*
                //checks to see if speed should increase based on level
                if(level <= 0 && level >= 10){
                    pt2.setRate((.06*level)+.15);
                }
                else{
                    pt2.setRate(.75);
                }*/
                balloonBtn2.setDisable(false);
                r2 = (int) ((Math.random() * 90) + 10) / 100.0;

                if(isGold){
                    balloonIV2.setImage(balloonSilverIMG);
                }
                else if(r2 >= 0 && r2 < .3){//25% chance of getting a blue balloon
                    balloonIV2.setImage(balloonBlueIMG);
                }
                else if(r2 < .49){//15% chance of getting a green balloon
                    balloonIV2.setImage(balloonGreenIMG);
                }
                else if(r2 < .64){//25% chance of getting a black balloon
                    balloonIV2.setImage(balloonBlackIMG);
                }
                else if(r2 < .69){//2% chance of getting a yellow balloon
                    balloonIV2.setImage(balloonYellowIMG);
                }
                else if(r2 < .82){//%10 chance of getting a red balloon
                    balloonIV2.setImage(balloonRedIMG);
                }
                else if(r2 < .9){//5% chance of getting a silver balloon
                    balloonIV2.setImage(balloonSilverIMG);
                }
                else if(r2 < 1){//18% chance of getting an orange balloon
                    balloonIV2.setImage(balloonOrangeIMG);
                }

                balloonBtn2.setGraphic(balloonIV2);

                if(isGold){
                    balloonBtn2.setOnAction(e2->{
                        //next set of balloons will all be silver
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=200;
                        levelScore+=200;
                        score.setText(numScore+"");
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);
                    isGold=false;});
                }
                else if(r2 >= 0 && r2 < .3){//25% chance of getting a blue balloon
                    balloonBtn2.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=10;
                        levelScore+=10;
                        score.setText(numScore+"");
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r2 < .49){//15% chance of getting a green balloon
                    balloonBtn2.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=50;
                        levelScore+=50;
                        score.setText(numScore+"");
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r2 < .64){//25% chance of getting a black balloon
                    balloonBtn2.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore-=100;
                        levelScore-=100;
                        score.setText(numScore+"");
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r2 < .69){//2% chance of getting a yellow balloon
                    balloonBtn2.setOnAction(e2->{
                        //next set of balloons are all silver.
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=5;
                        levelScore+=5;
                        score.setText(numScore+"");
                        isGold = true;
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r2 < .82){//%10 chance of getting a red balloon
                    balloonBtn2.setOnAction(e2->{
                        //sets negative balloons transparent for next 3 loops
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r2 < .9){//5% chance of getting a silver balloon
                    balloonBtn2.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=200;
                        levelScore+=200;
                        score.setText(numScore+"");
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r2 < 1){
                    balloonBtn2.setOnAction(e2->{
                        //level increases (makes things harder)
                        balloonBtn2.setGraphic(null);
                        balloonBtn2.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
            });

            pt3.setRate(.15);
            pt3.setDuration(Duration.millis(2000));
            pt3.setPath(new Line(0.0,500,800,-450));
            pt3.setNode(balloonBtn3);
            pt3.setOnFinished(e->{/*
                //checks to see if speed should increase based on level
                if(level <= 0 && level >= 10){
                    pt3.setRate((.06*level)+.15);
                }
                else{
                    pt3.setRate(.75);
                }*/
                balloonBtn3.setDisable(false);
                r3 = (int) ((Math.random() * 90) + 10) / 100.0;

                if(isGold){
                    balloonIV3.setImage(balloonSilverIMG);
                }
                else if(r3 >= 0 && r3 < .3){//25% chance of getting a blue balloon
                    balloonIV3.setImage(balloonBlueIMG);
                }
                else if(r3 < .49){//15% chance of getting a green balloon
                    balloonIV3.setImage(balloonGreenIMG);
                }
                else if(r3 < .64){//25% chance of getting a black balloon
                    balloonIV3.setImage(balloonBlackIMG);
                }
                else if(r3 < .69){//2% chance of getting a yellow balloon
                    balloonIV3.setImage(balloonYellowIMG);
                }
                else if(r3 < .82){//%10 chance of getting a red balloon
                    balloonIV3.setImage(balloonRedIMG);
                }
                else if(r3 < .9){//5% chance of getting a silver balloon
                    balloonIV3.setImage(balloonSilverIMG);
                }
                else if(r3 < 1){
                    balloonIV3.setImage(balloonOrangeIMG);
                }

                balloonBtn3.setGraphic(balloonIV3);

                if(isGold){
                    balloonBtn3.setOnAction(e2->{
                        //next set of balloons will all be silver
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=200;
                        levelScore+=200;
                        score.setText(numScore+"");
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);
                    isGold = false;});
                }
                else if(r3 >= 0 && r3 < .3){//25% chance of getting a blue balloon
                    balloonBtn3.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=10;
                        levelScore+=10;
                        score.setText(numScore+"");
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r3 < .49){//15% chance of getting a green balloon
                    balloonBtn3.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=50;
                        levelScore+=50;
                        score.setText(numScore+"");
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r3 < .64){//25% chance of getting a black balloon
                    balloonBtn3.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore-=100;
                        levelScore-=100;
                        score.setText(numScore+"");
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r3 < .69){//2% chance of getting a yellow balloon
                    balloonBtn3.setOnAction(e2->{
                        //next set of balloons are all silver.
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=5;
                        levelScore+=5;
                        score.setText(numScore+"");
                        isGold = true;
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r3 < .82){//%10 chance of getting a red balloon
                    balloonBtn3.setOnAction(e2->{
                        //sets negative balloons transparent for next 3 loops
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r3 < .9){//5% chance of getting a silver balloon
                    balloonBtn3.setOnAction(e2->{
                        int numScore = Integer.parseInt(score.getText());
                        numScore+=200;
                        levelScore+=200;
                        score.setText(numScore+"");
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
                else if(r3 < 1){
                    balloonBtn3.setOnAction(e2->{
                        //level increases (makes things harder)
                        balloonBtn3.setGraphic(null);
                        balloonBtn3.setDisable(true);
                        BalloonPop.CLICK_AUDIOCLIP.play(1);});
                }
            });

            ParallelTransition prlt= new ParallelTransition(pt1,pt2,pt3);
            prlt.setCycleCount(ParallelTransition.INDEFINITE);
            prlt.play();

            ImageView exitBtnIV = new ImageView();
            exitBtnIV.setImage(exitIMG);
            exitBtnIV.setFitWidth(30);
            exitBtnIV.setPreserveRatio(true);
            exitBtnIV.setSmooth(true);
            exitBtnIV.setCache(true);
            exitBtn = new Button();
            exitBtn.setGraphic(exitBtnIV);
            exitBtn.setOnAction(e->{
                if(prlt.getStatus().equals(Animation.Status.PAUSED)){
                    prlt.play();
                }
                else{
                    prlt.pause();
                }
            });
            exitBtn.setTooltip(new Tooltip("exit"));
            exitBtn.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");

            StackPane sp = new StackPane();
            sp.getChildren().addAll(balloonBtn1,balloonBtn2,balloonBtn3);
            sp.setStyle(
                "-fx-background-image: url(" + "background.jpg" +
                "); " +
                "-fx-background-size: cover;"
            );

            ImageView pauseBtnIV = new ImageView();
            pauseBtnIV.setImage(pauseIMG);
            pauseBtnIV.setFitWidth(28);
            pauseBtnIV.setPreserveRatio(true);
            pauseBtnIV.setSmooth(true);
            pauseBtnIV.setCache(true);
            Button pauseBtn = new Button();
            pauseBtn.setGraphic(pauseBtnIV);
            pauseBtn.setOnAction(e->{
                if(prlt.getStatus().equals(Animation.Status.PAUSED)){
                    prlt.play();
                    sp.getChildren().remove(paused);
                    muteBtn.setDisable(false);
                    muteBtn.fire();
                    if(balloonBtn1.isVisible()){
                        balloonBtn1.setDisable(false);
                    }
                    if(balloonBtn2.isVisible()){
                        balloonBtn2.setDisable(false);
                    }
                    if(balloonBtn3.isVisible()){
                        balloonBtn3.setDisable(false);
                    }
                    pauseBtnIV.setImage(pauseIMG);
                }
                else{
                    prlt.pause();
                    sp.getChildren().add(paused);
                    if(!a.isMute()){
                        muteBtn.fire();
                    }
                    muteBtn.setDisable(true);
                    balloonBtn1.setDisable(true);
                    balloonBtn2.setDisable(true);
                    balloonBtn3.setDisable(true);
                    pauseBtnIV.setImage(playIMG);
                }
            });
            pauseBtn.setTooltip(new Tooltip("pause"));
            pauseBtn.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");

            Image blueBar = new Image("blueBar.png",1420,75,false,true);

            Background hub = new Background((new BackgroundImage(new Image("blueBar.png",1420,75,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

            HBox hb = new HBox();
            hb.setBackground(hub);
            hb.getChildren().addAll(score,muteBtn,pauseBtn,exitBtn);

            BorderPane bp = new BorderPane();
            bp.setBottom(hb);
            bp.setCenter(sp);

            Scene scene = new Scene(bp, 300, 250);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            primaryStage.setTitle("Balloon Pop");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

            BackgroundImage myBI= new BackgroundImage(new Image("background.jpg",100,100,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
            bp.setBackground(new Background(myBI));
            
            stTrue = primaryStage;
        }
        public void main(String args[]){
            launch(args);
        }
    }
}
