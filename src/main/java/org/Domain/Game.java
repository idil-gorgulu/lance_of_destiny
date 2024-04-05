package org.Domain;
import java.util.ArrayList;
import org.Controllers.RunningModeController;

public class Game {

    private Fireball fireball;
    private MagicalStaff magicalStaff;

    // This will be a list

    
    private Chance chance;
    private Score score;

    private Barrier Barrier; //will be removed
    private ArrayList<Barrier> barriers = new ArrayList<Barrier>(); // Could maybe be a hashmap?
    private static Game instance;

    public Game(){
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
        // Think about how to initialize it, from constuctor maybe?
        this.Barrier = new Barrier(new Coordinate(450, 600), 0);


        this.chance= new Chance(3);
        this.score= new Score();

        barriers.add(new Barrier(new Coordinate(450, 600), 0));

    }


    public Fireball getFireball() {
        return fireball;
    }

    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }

    public Barrier getBarrier() { return Barrier;}


    public Chance getChance() { return chance;}
    public Score getScore(){return score;}

    public void addBarrier(Coordinate coordinates, int type){
        Barrier newBarrier = new Barrier(coordinates, type);
        barriers.add(newBarrier);
    }
    public void removeBarrier(Coordinate coordinates){
        int initialSize=barriers.size();
        for (int i = 0; i < initialSize; i++) {
            Barrier barrier = barriers.get(i);
            if (barrier.getCoordinates().getX()==coordinates.getX() && barrier.getCoordinates().getY() == coordinates.getY()) {
                barriers.remove(barrier);
                i--; // Decrease index because the size of ArrayList is reduced
            }
        }
        if (initialSize==barriers.size()){
            System.out.println("No barriers in that coordinates");
        }

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
}
