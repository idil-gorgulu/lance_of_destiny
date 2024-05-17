package org.Domain;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Chance chance;
    private Score score;
    private ArrayList<Barrier> barriers = new ArrayList<Barrier>(0); // Could maybe be a hashmap?
    private ArrayList<Debris> debris = new ArrayList<Debris>(0);
    private static Game instance;
    int numSimpleBarrier=0;
    int numFirmBarrier=0;
    int numExplosiveBarrier=0;
    int numrewardingBarrier=0;
    int numTotal;
    public boolean started = false;
    public boolean ended = false;
    String[][] barrierBoard = new String[20][20];

    private Game(){
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
        if (Math.random()<0.2)    {
            newBarrier.setMoving(true);
            if (Math.random()<0.5)  newBarrier.setVelocity(3);
            else                    newBarrier.setVelocity(-3);
        }

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
        int boardX = coordinates.getX() / 50; // Adjust the indexing here
        int boardY = coordinates.getY() / 20; // Adjust the indexing here
        barrierBoard[boardY][boardX] = s; // Adjusted the indexing here
        printBoard();
    }

    public void removeBarrier(Coordinate coordinates, BarrierType type){
        int initialSize=barriers.size();
        if(initialSize!=0){
            for (int i = 0; i < initialSize; i++) {
                Barrier barrier = barriers.get(i);
                if (barrier.getCoordinate().getX()==coordinates.getX() && barrier.getCoordinate().getY() == coordinates.getY()) {
                    System.out.println("Removed");
                    barriers.remove(barrier);
                    i--; // Decrease index because the size of ArrayList is reduced
                    System.out.println("After removal, board:");
                    int boardX= coordinates.getY()/20;
                    int boardY= coordinates.getX()/50;
                    barrierBoard[boardX][boardY]=null;
                    printBoard();
                    numTotal--;
                    if (type == BarrierType.SIMPLE) { //Simple barrier
                        numSimpleBarrier--;
                    } else if (type == BarrierType.REINFORCED) { //Reinforced barrier
                        numFirmBarrier--;
                    } else if (type == BarrierType.EXPLOSIVE) { //Explosive barrier
                        numExplosiveBarrier--;
                    } else if (type == BarrierType.REWARDING) {
                        numrewardingBarrier--;
                    }
                    return;
                }
            }
        }
        System.out.println("No barriers in that coordinates");
    }
    public static Game getInstance(){
        if (instance==null) {
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
        int tot = numSimpleBarrier + numExplosiveBarrier + numFirmBarrier + numrewardingBarrier;
        if (400 - tot < simpleNum + firmNum + exNum + giftNum){
            return false;
        }
        Random random = new Random();
        int simpleCount = 0;
        int firmCount = 0;
        int exCount = 0;
        int giftCount = 0;

        while (simpleCount < simpleNum || firmCount < firmNum || exCount < exNum || giftCount < giftNum) {
            int randomRow = random.nextInt(20);
            int randomCol = random.nextInt(20);

            if (barrierBoard[randomRow][randomCol] == null) {
                Coordinate newCoord = new Coordinate( randomCol * 50,randomRow * 20);
                if (simpleCount < simpleNum) {
                    addBarrier(newCoord, BarrierType.SIMPLE);
                    simpleCount++;
                } else if (firmCount < firmNum) {
                    addBarrier(newCoord, BarrierType.REINFORCED);
                    firmCount++;
                } else if (exCount < exNum) {
                    addBarrier(newCoord, BarrierType.EXPLOSIVE);
                    exCount++;
                } else if (giftCount < giftNum) {
                    addBarrier(newCoord, BarrierType.REWARDING);
                    giftCount++;
                }
            }
        }
        System.out.println("Adding Completed");
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

    public int getNumSimpleBarrier() {
        return numSimpleBarrier;
    }

    public int getNumFirmBarrier() {
        return numFirmBarrier;
    }

    public int getNumExplosiveBarrier() {
        return numExplosiveBarrier;
    }

    public int getNumrewardingBarrier() {
        return numrewardingBarrier;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public String[][] getBarrierBoard() {
        return barrierBoard;
    }

    //TODO: Update this according to new database saving
    public void addDetailedBarrier(Coordinate coordinates, BarrierType type, int numHits, boolean isMoving, int velocity) {
        Barrier newBarrier = new Barrier(coordinates, type);
        newBarrier.setnHits(numHits);
        newBarrier.setMoving(isMoving);
        newBarrier.setVelocity(velocity);
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
        int boardX = coordinates.getX() / 50; // Adjust the indexing here
        int boardY = coordinates.getY() / 20; // Adjust the indexing here
        barrierBoard[boardY][boardX] = s; // Adjusted the indexing here
        printBoard();
    }

    public void reset() {
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
        this.chance = new Chance();
        this.score = new Score();
        this.barriers.clear();
        this.debris.clear();
        this.numSimpleBarrier = 0;
        this.numFirmBarrier = 0;
        this.numExplosiveBarrier = 0;
        this.numrewardingBarrier = 0;
        this.numTotal = 0;
        this.started = true;
        this.ended = false;
        this.barrierBoard = new String[20][20];
    }

    public static Game createNewGame() {
        instance = new Game();
        instance.started = false;
        return instance;
    }
}
