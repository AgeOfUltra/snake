package com.example.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HelloApplication extends Application {
//    variables
    private static int speed=5;
    private static int foodColor = 0;
    private static final int width = 20;
    private static final int height = 20;
    private static int foodX=0;
    private static int foodY= 0;
    private static final int cornerSIze = 25;

    static  List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random random = new Random();
    public enum Dir{
    left,right,up,down;
    }

    static class Corner{
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }






    @Override
   public void start(Stage primaryStage) {
        try {
            newFood();
            VBox vbox = new VBox();
            Canvas canvas = new Canvas(width * cornerSIze, height * cornerSIze);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            vbox.getChildren().add(canvas);
            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }
                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(gc);
                    }

                }

            }.start();

            Scene scene = new Scene(vbox, width * cornerSIze, height * cornerSIze);

//            controls
            scene.addEventFilter(KeyEvent.KEY_PRESSED,keyEvent -> {
                if(keyEvent.getCode()== KeyCode.W){
                    direction=Dir.up;
                }
                if(keyEvent.getCode()== KeyCode.A){
                    direction=Dir.left;
                }
                if(keyEvent.getCode()== KeyCode.D){
                    direction=Dir.right;
                }
                if(keyEvent.getCode()== KeyCode.S){
                    direction=Dir.down;
                }
            });
//        snake parts
            snake.add(new Corner(width/2,height/2));
            snake.add(new Corner(width/2,height/2));
            snake.add(new Corner(width/2,height/2));


            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE GAME");
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
//    tick
    public static void tick(GraphicsContext gc){
        if(gameOver){
            gc.setFill(Color.RED);
            gc.setFont(new Font("",50));
            gc.fillText("GAME OVER",100,250);
            return;
        }
        for(int i=snake.size()-1;i>=1;i--){
            snake.get(i).x=snake.get(i-1).x;
            snake.get(i).y=snake.get(i-1).y;
        }
        switch (direction) {
            case up -> {
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
            }
            case down -> {
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
            }
            case left -> {
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
            }
            case right -> {
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
            }
        }
//        eat food
        if(foodX==snake.get(0).x && foodY==snake.get(0).y){
            snake.add(new Corner(-1,-1));
            newFood();
        }

//        self destroy
        for(int i=1;i<snake.size();i++){
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }
//        fill background
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width * cornerSIze, height * cornerSIze);

//        score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",30));
        gc.fillText("Score:"+(speed-6),10,30);

//        random color
        Color cc= Color.WHITE;
        switch (foodColor){
            case 0 ->{
                cc= Color.PURPLE;
            }
            case 1 ->{
                cc= Color.LIGHTBLUE;
            }
            case 2 ->{
                cc= Color.YELLOW;
            }
            case 3 ->{
                cc= Color.PINK;
            }
            case 4 ->{
                cc= Color.ORANGE;
            }
        }
        gc.setFill(cc);
        gc.fillOval(foodX*cornerSIze,foodY*cornerSIze,cornerSIze,cornerSIze);

//        snake coloring
        for(Corner c : snake){
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x*cornerSIze,c.y*cornerSIze,cornerSIze-1,cornerSIze-1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x*cornerSIze,c.y*cornerSIze,cornerSIze-2,cornerSIze-2);
        }
    }


//        food
    public static void newFood(){
       start:while (true){
            foodX= random.nextInt(width);
            foodY=random.nextInt(height);

            for(Corner c : snake){
                if(c.x==foodX && c.y==foodY){
                    continue start;
                }
            }

            foodColor = random.nextInt(5);
            speed++;
            break ;

        }

    }


    public static void main(String[] args) {
        launch();
    }
}