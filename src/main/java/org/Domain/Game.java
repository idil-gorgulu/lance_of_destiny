package org.Domain;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.Views.RunningModePage.COLLISION_COOLDOWN;

public class Game {
    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Chance chance;
    private Score score;
    private static Game instance;
    int numSimpleBarrier=0;
    int numFirmBarrier=0;
    int numExplosiveBarrier=0;
    int numrewardingBarrier=0;
    int numTotal;
    public boolean started = false;
    public boolean ended = false;
    String[][] barrierBoard = new String[20][20];
    private Ymir ymir;
    private long lastCollisionTime = 0; // Time of the last collision in milliseconds
    private ArrayList<Barrier> barriers = new ArrayList<Barrier>(0); // Could maybe be a hashmap?
    private ArrayList<Debris> activeDebris;
    private ArrayList<Spell> droppingSpells;
    private ArrayList<Bullet> activeBullets;
    private HashMap<SpellType,Integer> inventory;


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
        ymir = new Ymir(this);
        activeDebris= new ArrayList<>();
        droppingSpells=new ArrayList<>();
        activeBullets=new ArrayList<>();
        inventory=new HashMap<>();
        for (SpellType type : SpellType.values()) {
            inventory.put(type, 0);
        }
    }

    public Fireball getFireball() {
        return fireball;
    }

    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }

    public Chance getChance() { return chance;}
    public Score getScore(){return score;}

    public HashMap<SpellType, Integer> getInventory(){ return inventory;}

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

    /* Attempts to remove a barrier of a specified type at the given coordinates.
     REQUIRES:
        - the input coordinate axes should not be null and must be of a valid position within
          the grid boundaries.
        - the barrier type should be from the BarrierType enum class.
     MODIFIES:
        - The visibility/existence of a barrier by potentially removing one.
        - The barrier list, also by potentially removing a barrier.
        - The barrier grid (i.e. the game board) by potentially changing the character at the corresponding
          coordinate.
        - Barrier count variables including numTotal, numSimpleBarrier, numFirmBarrier, numRewardingBarrier and
         numExplosiveBarrier.
     EFFECTS:
        - If a barrier of the inputted barrier type is present at the input coordinates, it is removed from the
          barrier list and the index corresponding to the coordinates in the barrierBoard array is set to null.
        - The barrier counts numTotal and num(BarrierType) get decremented
        - Prints messages which indicate the removal state of a barrier and the current barrier board.
        - If no barrier or a barrier of the wrong type is found at the inputted coordinates, a message gets
          printed. */
    public void removeBarrier(Coordinate coordinates, BarrierType type){
        int initialSize=barriers.size();
        if(initialSize!=0){
            for (int i = 0; i < initialSize; i++) {
                Barrier barrier = barriers.get(i);
                if (barrier.getCoordinate().getX()==coordinates.getX() && barrier.getCoordinate().getY() == coordinates.getY()
                   && barrier.getType() == type) {
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

    public ArrayList<Debris> getActiveDebris() {
        return activeDebris;
    }
    public ArrayList<Spell> getSpells(){
        return droppingSpells;
    }
    public ArrayList<Bullet>getActiveBullets(){
        return activeBullets;
    }

    /**
     * public boolean initialPopulation(int simpleNum, int firmNum, int exNum, int giftNum)
     * Initializes the population of barriers on the board with inputs.
     *
     * //REQUIRES:
     * - simpleNum, firmNum, exNum, and giftNum are non-negative integers.
     * - numSimpleBarrier, numExplosiveBarrier, numFirmBarrier, and numrewardingBarrier are non-negative integers.
     * - barrierBoard is a 2D array of dimensions 20x20.
     *
     * //MODIFIES: barrierBoard, numSimpleBarrier, numExplosiveBarrier, numFirmBarrier, numrewardingBarrier
     *
     * //EFFECTS:
     * - Places the specified numbers of simple, firm, explosive, and gift barriers
     *   on the board if there is sufficient space.
     * -  if there is not enough space: Returns false to place all the specified barriers.
     * -  if there is sufficient space: Returns true and places barriers.
     */
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
        this.activeDebris.clear();
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


    // BELOW ARE THE CODES BROUGHT FROM RUNNINGMCONTROLLER -Melih

    public void moveBarriers(){ // TODO Fix the logic
        int newpos;
        boolean isAvailable;
        int width= 10;
        for (Barrier br: getBarriers()){
            if (br.isMoving()) {
                if (br.getType() == BarrierType.EXPLOSIVE) {
                    if (br.getVelocity() != 0) {
                        br.moveBarrier();                   }               }
                else {
                    isAvailable = true;
                    newpos = br.getCoordinate().getX() +  br.getVelocity();
                    for (Barrier br2 : getBarriers()) {
                        if ((!br2.equals(br)) && (br.getCoordinate().getY()==br2.getCoordinate().getY())){
                            if (width*4.5>Math.abs(br2.getCoordinate().getX() - newpos)) {
                                isAvailable = false;
                                br.setVelocity(-1*br.getVelocity());
                                break;
                            }

                        }
                    }
                    if (isAvailable) {
                        br.moveBarrier();
                    }
                }
            }
        }
    }

    public void checkScreenBordersFireballCollision(){

        Fireball fireball = getFireball();
        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();
        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        int containerWidth = 1000;
        int containerHeight = 600;

        // Check collision with left and right boundaries
        if ((fireballX - fireballRadius <= 0) || (fireballX + fireballRadius > containerWidth - 10)) {
            //runningModePage.playSoundEffect(1); TODO fix
            fireball.setLastCollided(null);
            fireball.setxVelocity(-xVelocity);// Reverse X velocity
            fireball.setCoordinate(new Coordinate((int) (fireballX-xVelocity),fireballY));
        }
        // Check collision with top and bottom boundaries
        if (fireballY - fireballRadius <= -10)  {
            //runningModePage.playSoundEffect(1); TODO fix
            fireball.setyVelocity(-yVelocity);// TOP
            fireball.setLastCollided(null);
            fireball.setCoordinate(new Coordinate(fireballX,(int)(fireballY-yVelocity)));
        }

        else if (fireballY + fireballRadius >= containerHeight) {
            // BOTTOM
            fireball.setLastCollided(null);
            getChance().decrementChance();
            //playSoundEffect(2); TODO fix
            if (getChance().getRemainingChance() == 0) {
                this.started = false;
                System.out.println("Not active");
                // runningModePage.stopMusic(); TODO fix
                return;
            }
            int fireballWidth = fireball.getPreferredSize().width;
            int fireballPositionX = (1000 - fireballWidth) / 2; // make these dynamic
            int fireballHeight = fireball.getPreferredSize().height;
            int fireballPositionY = (500 - fireballHeight - 200); // make these dynamic
            fireball.setxVelocity(3);
            fireball.setyVelocity(-3);
            fireball.getCoordinate().setX(fireballPositionX);
            fireball.getCoordinate().setY(fireballPositionY);
            fireball.setBounds(fireballPositionX, fireballPositionY, fireballWidth, fireballHeight);
            fireball.setOverwhelming(false);
            //fireball.setBackground(Color.red);
            fireball.setBackground(new Color(0, 0, 0, 0)); // Transparent background
            fireball.setOpaque(true);

        }
    }
    //Put for Testing
    public void setLastCollisionTime(long time){
        this.lastCollisionTime=time;
    }
    public void checkMagicalStaffFireballCollision(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCollisionTime < COLLISION_COOLDOWN) {
            return; // Skip collision check if we are within the cooldown period
        }

        Fireball fireball = getFireball();
        MagicalStaff magicalStaff = getMagicalStaff();
        int magicalStaffVelocity = magicalStaff.getVelocity();

        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        double msAngle = magicalStaff.getAngle();
        double angleRadians = Math.toRadians(msAngle);

        Rectangle2D.Double fireballRectangle = new Rectangle2D.Double(
                fireball.getCoordinate().getX() - fireball.getFireballRadius(),
                fireball.getCoordinate().getY() - fireball.getFireballRadius(),
                2 * fireball.getFireballRadius(),
                2 * fireball.getFireballRadius()
        );

        Rectangle2D.Double magicalStaffRectangle = new Rectangle2D.Double(
                magicalStaff.getTopLeftCornerOfMagicalStaff().getX(),
                magicalStaff.getTopLeftCornerOfMagicalStaff().getY(),
                magicalStaff.getStaffWidth(),
                magicalStaff.getStaffHeight()
        );

        AffineTransform transform = new AffineTransform();
        double centerX = magicalStaffRectangle.getCenterX();
        double centerY = magicalStaffRectangle.getCenterY();
        transform.rotate(angleRadians, centerX, centerY);
        Shape transformedRectangle = transform.createTransformedShape(magicalStaffRectangle);

        if (transformedRectangle.intersects(fireballRectangle)) {
            fireball.setLastCollided(null);
            //System.out.println("\nCollision detected");
            //runningModePage.playSoundEffect(1); TODO fix
            lastCollisionTime = currentTime;

            double energy=xVelocity*xVelocity+yVelocity*yVelocity;
            //System.out.println("\nold: " + fireball.getxVelocity() + " " + fireball.getyVelocity()+" "+energy);

            double u=xVelocity*Math.cos(angleRadians)+yVelocity*Math.sin(angleRadians);
            double v=xVelocity*Math.sin(angleRadians)-yVelocity*Math.cos(angleRadians);

            double reflectionX=u*Math.cos(angleRadians)-v*Math.sin(angleRadians);
            double reflectionY=u*Math.sin(angleRadians)+v*Math.cos(angleRadians);
            fireball.setxVelocity(reflectionX);
            fireball.setyVelocity(reflectionY);
            energy=reflectionX*reflectionX+reflectionY*reflectionY;
            //System.out.println("new: " + fireball.getxVelocity() + " " + fireball.getyVelocity()+" "+energy);

            /*
            if (Math.abs(msAngle)<1e-5){
                if (xVelocity*magicalStaffVelocity>0){ //staff & ball same direction
                   // System.out.println("same direction");
                    fireball.setxVelocity( xVelocity+  Math.signum(xVelocity) * 0.5);
                }
                else if (xVelocity*magicalStaffVelocity<0){ //opposite direction
                    //System.out.println("opp direction");
                    fireball.setxVelocity(-xVelocity);
                }
                fireball.setyVelocity(-fireball.getyVelocity());

            }
            else {
                System.out.println("Magical Staff angle: " + -msAngle);
                double normalAngle = Math.toRadians((-msAngle + 90));
                System.out.println(normalAngle);
                Vector normal = new Vector(Math.cos(normalAngle), Math.sin(normalAngle));
                System.out.println("Cos and sin " + Math.cos(normalAngle) + " " + Math.sin(normalAngle));
                Vector velocity = new Vector(xVelocity, yVelocity);
                double dProd = normal.dot(velocity);
                double reflectionX = velocity.getX() - 2 * dProd * normal.getX();
                double reflectionY = velocity.getY() - 2 * dProd * normal.getY();
                //Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal)));
                System.out.println("old: " + fireball.getxVelocity() + " " + fireball.getyVelocity());
                fireball.setxVelocity(-reflectionX);
                fireball.setyVelocity(reflectionY);
                System.out.println("new: " + fireball.getxVelocity() + " " + fireball.getyVelocity());
            }

            */
        }


    }



    // move back to controller
    public void checkBarrierFireballCollision(){
        ArrayList<Barrier> barriers = getBarriers();
        ArrayList<Barrier> toRemove = new ArrayList<>();

        Fireball fireball = getFireball();
        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        Rectangle2D.Double fireballRectangle = new Rectangle2D.Double(
                fireball.getCoordinate().getX() - fireball.getFireballRadius(),
                fireball.getCoordinate().getY() - fireball.getFireballRadius(),
                2 * fireball.getFireballRadius(),
                2 * fireball.getFireballRadius()
        );

        for (Barrier br : barriers) {
            Rectangle brRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY(), (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());

            if (brRect.intersects(fireballRectangle)) {
                //runningModePage.playSoundEffect(1); TODO fix

                if (br==fireball.getLastCollided()) return;

                fireball.setLastCollided(br);
                if (!fireball.isOverwhelming()){ // no collision if it is
                    Rectangle sideLRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY() + 5, 1, 5);
                    Rectangle sideRRect = new Rectangle(br.getCoordinate().getX() + 50, br.getCoordinate().getY() + 5, 1, 5);

                    if ((sideLRect.intersects(fireballRectangle)) || (sideRRect.intersects(fireballRectangle))) {
                        // System.out.println("side collision");
                        fireball.setxVelocity(-xVelocity);
                    } else {
                        if (xVelocity*br.getVelocity()>0){ //barrier & ball same direction
                            fireball.setxVelocity( xVelocity+  Math.signum(xVelocity) * 0.5);
                        }
                        else if (xVelocity*br.getVelocity()<0){ //opposite direction
                            fireball.setxVelocity(-xVelocity);
                        }
                        fireball.setyVelocity(-yVelocity);
                    }
                    if (hitBarrier(br,1)) {
                        toRemove.add(br);
                    }}
                else {
                    if (hitBarrier(br,10)){ //This is always true
                        toRemove.add(br);
                    }
                }
            }
        }
        barriers.removeAll(toRemove);
        // Updating the score.

        //getScore().incrementScore(toRemove.size(), this.runningModePage.timeInSeconds); // TODO time is not in game now


    }

    // move back to controller
    public boolean hitBarrier(Barrier barrier, int hitTimes) { // Could have been private method, since only called in collision method
        barrier.setnHits(barrier.getnHits() - hitTimes);
        //barrier.revalidate();
        //barrier.repaint();
        if (barrier.getnHits() <= 0) {
            barrier.destroy();
            if(barrier.getType()==BarrierType.EXPLOSIVE){
                System.out.println("Explosive is broken");
                explodeBarrier(barrier);
            }
            else if(barrier.getType()==BarrierType.REWARDING){
                System.out.println("Rewarding is broken");
                dropSpell(barrier);
            }
            return true;
        }

        return false;
    }


    // move back to controller
    private void explodeBarrier(Barrier barrier) {
        Debris debris = new Debris(barrier.getCoordinate());
        debris.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        activeDebris.add(debris); // Add debris to the list

        //runningModePage.repaint();
    }

    // move back to controller
    private void dropSpell(Barrier barrier){
        Spell spell = new Spell(barrier.getCoordinate());
        spell.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        droppingSpells.add(spell); // Add spells to the list
    }

    public void useFelixFelicis(){
        int remaining=inventory.get(SpellType.FELIX_FELICIS);
        if (remaining>0) {
            getChance().incrementChance();
            inventory.put(SpellType.FELIX_FELICIS,remaining -1 );
        }
    }
    public void useStaffExpansion(){
        int remaining=inventory.get(SpellType.STAFF_EXPANSION);
        if (remaining>0) {
            getMagicalStaff().setStaffWidth(200);
            //runningModePage.playSoundEffect(3); TODO fix
            inventory.put(SpellType.STAFF_EXPANSION,  remaining -1 );

            MagicalStaff magicalStaff= getMagicalStaff();
            magicalStaff.setExpansionTime(System.currentTimeMillis());
        }
    }
}
