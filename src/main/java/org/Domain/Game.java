package org.Domain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.Controllers.RunningModeController;

public class Game {

    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Chance chance;
    private Score score;
    private ArrayList<Barrier> barriers = new ArrayList<Barrier>(0); // Could maybe be a hashmap?
    private ArrayList<Debris> debris = new ArrayList<Debris>(0);
    private static Game instance;
    int numSimpleBarrier;
    int numFirmBarrier;
    int numExplosiveBarrier;
    int numrewardingBarrier;
    int numTotal;
    String[][] barrierBoard = new String[20][20];

    public Game(){
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
        // Think about how to initialize it, from constructor maybe?

        this.chance= new Chance();
        this.score= new Score();
        numSimpleBarrier=0;
        numFirmBarrier=0;
        numExplosiveBarrier=0;
        numrewardingBarrier=0;
        numTotal=0;
    }

    public Fireball getFireball() {
        return fireball;
    }

    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }

    public Chance getChance() { return chance;}
    public Score getScore(){return score;}

    public void addBarrier(Coordinate coordinates, BarrierType type) {
        Barrier newBarrier = new Barrier(coordinates, type);
        barriers.add(newBarrier);
        String s="";
        if (type == BarrierType.SIMPLE) { //Simple barrier
            numSimpleBarrier++;
            s="s";
        } else if (type == BarrierType.REINFORCED) { //Reinforced barrier
            numFirmBarrier++;
            s="f";
        } else if (type == BarrierType.EXPLOSIVE) { //Explosive barrier
            numExplosiveBarrier++;
            s="x";

        } else if (type == BarrierType.REWARDING) {
            numrewardingBarrier++;
            s="r";
        }
        numTotal++;
        int boardX= coordinates.getY()/20;
        int boardY= coordinates.getX()/50;
        barrierBoard[boardX][boardY]=s;
        printBoard();
    }

    public void removeBarrier(Coordinate coordinates){


        int initialSize=barriers.size();
        if(initialSize!=0){
            for (int i = 0; i < initialSize; i++) {
                Barrier barrier = barriers.get(i);
                if (barrier.getCoordinates().getX()==coordinates.getX() && barrier.getCoordinates().getY() == coordinates.getY()) {
                    System.out.println("Removed");
                    barriers.remove(barrier);
                    i--; // Decrease index because the size of ArrayList is reduced
                    return;
                }
            }
            int boardX= coordinates.getY()/20;
            int boardY= coordinates.getX()/50;
            //TODO removing from board is problematic
            barrierBoard[boardX][boardY]="!";
            printBoard();
            numTotal--;

        }
        System.out.println("No barriers in that coordinates");


    }
    public static Game getInstance(){
        if(instance==null){
            instance=new Game();
            return instance;
        }
        else{
            return instance;
        }
    }

    public void setFireball(Fireball fireball) {
        this.fireball = fireball;
    }

    public void setMagicalStaff(MagicalStaff magicalStaff) {
        this.magicalStaff = magicalStaff;
    }

    public void setChance(Chance chance) {
        this.chance = chance;
    }

    public void setScore(Score score) {
        this.score = score;
    }


    public ArrayList<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(ArrayList<Barrier> barriers) {
        this.barriers = barriers;
    }

    public static void setInstance(Game instance) {
        Game.instance = instance;
    }

    public ArrayList<Debris> getDebris() {
        return debris;
    }

    public boolean initialPopulation(int simpleNum, int firmNum, int exNum, int giftNum){
        int count=0;
        for (String[] row : barrierBoard) {
            for (String element : row) {
                if (element == null) {
                    count++;
                }
            }
        }
        if (count<simpleNum+firmNum+exNum+giftNum){
            return false;
        }

        Random random = new Random();
        // Choose a random element

        for (int k = 0; k < simpleNum; k++) {
            int randomRow = random.nextInt(20);
            int randomCol = random.nextInt(20);
            if (barrierBoard[randomRow][randomCol] == null){
                barrierBoard[randomRow][randomCol] += "s";
                Coordinate newCoord= new Coordinate(randomRow*50,randomCol*20);
                addBarrier(newCoord, BarrierType.SIMPLE);
            }
            else{
                k--;
            }

        }
        for (int k = 0; k < firmNum; k++) {
            int randomRow = random.nextInt(20);
            int randomCol = random.nextInt(20);
            if (barrierBoard[randomRow][randomCol] == null){
                barrierBoard[randomRow][randomCol] += "f";
                Coordinate newCoord= new Coordinate(randomRow*50,randomCol*20);
                addBarrier(newCoord, BarrierType.REINFORCED);
            }
            else{
                k--;
            }

        }
        for (int k = 0; k < exNum; k++) {
            int randomRow = random.nextInt(20);
            int randomCol = random.nextInt(20);
            if (barrierBoard[randomRow][randomCol] == null){
                barrierBoard[randomRow][randomCol] += "x";
                Coordinate newCoord= new Coordinate(randomRow*50,randomCol*20);
                addBarrier(newCoord, BarrierType.EXPLOSIVE);
            }
            else{
                k--;
            }

        }
        for (int k = 0; k < giftNum; k++) {
            int randomRow = random.nextInt(20);
            int randomCol = random.nextInt(20);
            if (barrierBoard[randomRow][randomCol] == null){
                barrierBoard[randomRow][randomCol] += "r";
                Coordinate newCoord= new Coordinate(randomRow*50,randomCol*20);
                addBarrier(newCoord, BarrierType.REWARDING);
            }
            else{
                k--;
            }

        }
        return true;
    }
    public void printBoard(){
        for (int i = 0; i < barrierBoard.length; i++) {
            for (int j = 0; j < barrierBoard[i].length; j++) {
                if(barrierBoard[i][j]==null){
                    System.out.print("0" + " ");
                }
                else{
                    System.out.print(barrierBoard[i][j] + " ");
                }
            }
            System.out.println();
        }

    }

}
