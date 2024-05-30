package org.Domain;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.Views.RunningModePage.COLLISION_COOLDOWN;

public class Game {
    private String gameName;
    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Chance chance;
    private Score score;
    private static Game instance;
    int numSimpleBarrier=0;
    int numFirmBarrier=0;
    int numExplosiveBarrier=0;
    int numRewardingBarrier=0;
    int numPurpleBarrier=0;
    int numTotal;
    public boolean started = false;
    public boolean ended = false;
    String[][] barrierBoard = new String[20][20];
    private Ymir ymir;
    private long lastCollisionTime = 0; // Time of the last collision in milliseconds
    private ArrayList<Barrier> barriers = new ArrayList<Barrier>(0); // Could maybe be a hashmap?
    private ArrayList<Debris> activeDebris;
    private ArrayList<Barrier> purpleBarriers = new ArrayList<Barrier>(0);;
    private ArrayList<Spell> droppingSpells;
    private ArrayList<Bullet> activeBullets;
    private Inventory inventory;
    private String date;
    private Game(){
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
        this.ymir = new Ymir(this);
        // Think about how to initialize it, from constructor maybe?
        this.chance= new Chance();
        this.score= new Score();
        this.gameName="none";
        this.date="";
        numSimpleBarrier=0;
        numFirmBarrier=0;
        numExplosiveBarrier=0;
        numRewardingBarrier=0;
        numPurpleBarrier=0;
        numTotal=0;
        activeDebris= new ArrayList<>();
        droppingSpells=new ArrayList<>();
        activeBullets=new ArrayList<>();
        inventory = new Inventory();
    }

    public Fireball getFireball() {
        return fireball;
    }
    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }
    public Ymir getYmir() {return ymir;}
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
            numRewardingBarrier++;
            s = "r";
        }
        numTotal++;
        int boardX = coordinates.getX() / 50; // Adjust the indexing here
        int boardY = coordinates.getY() / 20; // Adjust the indexing here
        barrierBoard[boardY][boardX] = s; // Adjusted the indexing here
        printBoard();
    }

    public void addPurpleBarrier(Coordinate coordinates) {
        Barrier newBarrier = new Barrier(coordinates, BarrierType.HOLLOW_PURPLE);
        if (Math.random()<0.2)    {
            newBarrier.setMoving(true);
            if (Math.random()<0.5)  newBarrier.setVelocity(3);
            else                    newBarrier.setVelocity(-3);
        }

        barriers.add(newBarrier);
        purpleBarriers.add(newBarrier);
        numPurpleBarrier++;
        numTotal++;
        int boardX = coordinates.getX() / 50; // Adjust the indexing here
        int boardY = coordinates.getY() / 20; // Adjust the indexing here
        barrierBoard[boardY][boardX] = "p"; // Adjusted the indexing here
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
                if (barrier.isFrozen()) return;
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
                        numRewardingBarrier--;
                    }
                    else if (type == BarrierType.HOLLOW_PURPLE) {
                        numPurpleBarrier--;
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

    public void setChance(int chance) {

        this.chance.setRemainingChance( chance);
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
        int tot = numSimpleBarrier + numExplosiveBarrier + numFirmBarrier + numRewardingBarrier;
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
        return numRewardingBarrier;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public String[][] getBarrierBoard() {
        return barrierBoard;
    }

    public Inventory getInventory() {
        return inventory;
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
            numRewardingBarrier++;
            s="r";
        }
        numTotal++;
        int boardX = coordinates.getX() / 50; // Adjust the indexing here
        int boardY = coordinates.getY() / 20; // Adjust the indexing here
        barrierBoard[boardY][boardX] = s; // Adjusted the indexing here
        printBoard();
    }

    public void addDetailedBarrierFromDb(Coordinate coordinates, BarrierType type, int numHits, boolean isMoving, int velocity) {
        Barrier newBarrier = new Barrier(coordinates, type);
        newBarrier.setnHits(numHits);
        newBarrier.setMoving(isMoving);
        newBarrier.setVelocity(velocity);
        barriers.add(newBarrier);
        if (type == BarrierType.SIMPLE) { //Simple barrier
            numSimpleBarrier++;
        } else if (type == BarrierType.REINFORCED) { //Reinforced barrier
            numFirmBarrier++;
        } else if (type == BarrierType.EXPLOSIVE) { //Explosive barrier
            numExplosiveBarrier++;
        } else if (type == BarrierType.REWARDING) {
            numRewardingBarrier++;
        }
        numTotal++;
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
        this.numRewardingBarrier = 0;
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

    // TODO Fix the logic
    public void moveBarriers(){
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
                        if ((!br2.equals(br)) && (br.getCoordinate().getY()==br2.getCoordinate().getY())
                                &&(width*4.5>Math.abs(br2.getCoordinate().getX() - newpos)) ){
                            isAvailable = false;
                            br.setVelocity(-1*br.getVelocity());
                            break;
                        }
                    }
                    if (isAvailable)   br.moveBarrier();
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

    /**
     * Checks if the fireball and magical staff collide, and if they do, updates the fireball's velocity accordingly.
     *
     * <p></p>REQUIRES:
     * - The game must have previously set fireball and staff objects.
     * - The fields of fireball and staff must be set correctly.
     * - Both fireball and staff must be within the screen boundaries.
     *
     * <p>MODIFIES: fireball.xVelocity, fireball.yVelocity
     *
     * <p>EFFECTS:
     * - Creates two Rectangle2D objects representing the bounding boxes around the fireball and the magical staff, considering their coordinates and orientation.
     * - Checks if these rectangles intersect.
     * - If they intersect, calculates the new velocity values for the fireball based on the collision angle and updates the fireball's velocity.
     * - Plays a sound effect upon collision.
     * - Ensures a cooldown period between consecutive collision checks to avoid multiple detections of the same collision.
     */
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
            //runningModePage.playSoundEffect(1); TODO fix
            lastCollisionTime = currentTime;

            double energy=xVelocity*xVelocity+yVelocity*yVelocity;
            //System.out.println("\nold: " + fireball.getxVelocity() + " " + fireball.getyVelocity()+" "+energy);

            double u=xVelocity*Math.cos(angleRadians)+yVelocity*Math.sin(angleRadians);
            double v=xVelocity*Math.sin(angleRadians)-yVelocity*Math.cos(angleRadians);

            double reflectionX=u*Math.cos(angleRadians)-v*Math.sin(angleRadians);
            double reflectionY=u*Math.sin(angleRadians)+v*Math.cos(angleRadians);

            if (reflectionX*magicalStaffVelocity>0){ //staff & ball same direction
                reflectionX= reflectionX+  Math.signum(xVelocity) * 0.5;
            }
            else if (reflectionX*magicalStaffVelocity<0){ //opposite direction
                reflectionX=-reflectionX;
            }

            fireball.setxVelocity(reflectionX);
            fireball.setyVelocity(reflectionY);
            energy = reflectionX*reflectionX+reflectionY*reflectionY;
            //System.out.println("new: " + fireball.getxVelocity() + " " + fireball.getyVelocity()+" "+energy);

        }


    }

    /**
     * Checks for collisions between the fireball and barriers, updates velocities accordingly,
     * removes destroyed barriers, plays sound effects, and updates the score.
     *
     * @requires game != null && game.getFireball() != null && game.getBarriers() != null && runningModePage != null && this.getGameSession() != null
     * @modifies game.getBarriers(), fireball, this.getGameSession().getScore()
     * @effects
     *      - Detects and processes collisions between the fireball and barriers.
     *      - Updates the fireball's velocity based on the collision direction.
     *      - Removes barriers that are hit by the fireball.
     *      - Plays a sound effect when a collision occurs.
     *      - Updates the score based on the number of barriers removed.
     */
    public void checkBarrierFireballCollision(int timeInSeconds){
        ArrayList<Barrier> barriers = getBarriers();
        ArrayList<Barrier> toRemove = new ArrayList<>();
        ArrayList<Barrier> purplesToRemove = new ArrayList<>();

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
               // runningModePage.playSoundEffect(1);

                if (br==fireball.getLastCollided()) return;

                fireball.setLastCollided(br);
                if (!fireball.isOverwhelming()){ // no collision if it is
                    Rectangle sideLRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY() + 5, 1, 5);
                    Rectangle sideRRect = new Rectangle(br.getCoordinate().getX() + 50, br.getCoordinate().getY() + 5, 1, 5);

                    if ((sideLRect.intersects(fireballRectangle)) || (sideRRect.intersects(fireballRectangle))) {
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
                        if(br.getType()==BarrierType.HOLLOW_PURPLE){
                            purplesToRemove.add(br);
                        }
                        toRemove.add(br);
                    }
                }
            }
        }
        barriers.removeAll(toRemove);
        purpleBarriers.removeAll(purplesToRemove);
        // Updating the score.
        getScore().incrementScore(toRemove.size(), timeInSeconds);
    }
    private boolean hitBarrier(Barrier barrier, int hitTimes) {
        if (barrier.isFrozen()) return false;
        barrier.setnHits(barrier.getnHits() - hitTimes);
        if (barrier.getnHits() <= 0) {
            barrier.destroy();
            if(barrier.getType()==BarrierType.EXPLOSIVE){
                explodeBarrier(barrier);
            }
            else if(barrier.getType()==BarrierType.REWARDING){
                dropSpell(barrier);
            }
            else if(barrier.getType()==BarrierType.HOLLOW_PURPLE){
                purpleBarriers.remove(barrier);
            }
            return true;
        }
        return false;
    }


    private void explodeBarrier(Barrier barrier) {
        Debris debris = new Debris(barrier.getCoordinate());
        debris.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        activeDebris.add(debris);
    }

    private void dropSpell(Barrier barrier){
        Spell spell = new Spell(barrier.getCoordinate());
        spell.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        droppingSpells.add(spell);
    }

    public void useSpell(SpellType spellType){
        if(inventory.checkSpellCount(spellType)){
            if(spellType == SpellType.FELIX_FELICIS){
                chance.incrementChance();
                inventory.updateInventory(spellType, -1);
            }
            if(spellType == SpellType.STAFF_EXPANSION) {
                magicalStaff.setStaffWidth(200);
                magicalStaff.setExpansionTime(System.currentTimeMillis());
                inventory.updateInventory(spellType, -1);
            }
            if(spellType == SpellType.HEX && !getMagicalStaff().isShooting()){
                getMagicalStaff().setShooting(true);
                getMagicalStaff().setCannonTime(System.currentTimeMillis());
                inventory.updateInventory(spellType, -1);
            }
            if(spellType == SpellType.OVERWHELMING_FIREBALL && getFireball().isOverwhelming()){
                getFireball().setOverwhelming(true);
                getFireball().setOverwhelmTime(System.currentTimeMillis());
                inventory.updateInventory(spellType, -1);
            }
        }
    }

    public Bullet[] createHexBullet(){

        MagicalStaff magicalStaff=getMagicalStaff();
        int leftMostX=magicalStaff.getTopLeftCornerOfMagicalStaff().getX();
        int leftMostY=magicalStaff.getTopLeftCornerOfMagicalStaff().getY();
        int length=magicalStaff.getStaffWidth();
        double angleRadian=Math.toRadians(magicalStaff.getAngle());
        double lengthY= (double) length /2 * Math.sin(angleRadian);
        double lengthXLeft= (double) length/2*(1-Math.cos(angleRadian));
        double lengthXRight= (double) length/2 * (1+Math.cos(angleRadian));

        Coordinate leftCoordinate= new Coordinate((int) (leftMostX+lengthXLeft),(int) (leftMostY-lengthY));
        Coordinate rightCoordinate=new Coordinate((int) (leftMostX+lengthXRight),(int) (leftMostY+lengthY));
        int bulletX=3*(int)Math.round(Math.sin(Math.toRadians(magicalStaff.getAngle())));
        int bulletY=3*(int)-Math.round( Math.cos(Math.toRadians(magicalStaff.getAngle())));

        Bullet bullet=new Bullet(leftCoordinate,bulletX,bulletY);
        bullet.setBackground(new Color(0, 0, 0, 0));
        Bullet bullet2=new Bullet(rightCoordinate,bulletX,bulletY);
        bullet2.setBackground(new Color(0, 0, 0, 0));

        activeBullets.add(bullet);
        activeBullets.add(bullet2);

        Bullet[] bullets= {bullet,bullet2};
        return bullets;
    }

    public Shape getStaffOrientation(){
        MagicalStaff magicalStaff = getMagicalStaff();
        double msAngle = magicalStaff.getAngle();
        double angleRadians = Math.toRadians(msAngle);
        Rectangle2D.Double magicalStaffRectangle = new Rectangle2D.Double(
                magicalStaff.getTopLeftCornerOfMagicalStaff().getX(),
                magicalStaff.getTopLeftCornerOfMagicalStaff().getY(),
                magicalStaff.getStaffWidth(),
                20
        );

        AffineTransform transform = new AffineTransform();
        double centerX = magicalStaffRectangle.getCenterX();
        double centerY = magicalStaffRectangle.getCenterY();
        transform.rotate(angleRadians, centerX, centerY);
        Shape transformedRectangle = transform.createTransformedShape(magicalStaffRectangle);
        return transformedRectangle;
    }

    public boolean checkHexBulletCollision(Bullet bullet, int timeInSeconds){
        boolean collides=false;
        ArrayList<Barrier> toRemove = new ArrayList<>();
        Rectangle2D.Double bulletRectangle = new Rectangle2D.Double( // Barrier collision
                bullet.getCoordinate().getX()  ,  bullet.getCoordinate().getY(),20,20);

        for (Barrier br : barriers) {
            Rectangle brRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY(),
                    (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());
            if (brRect.intersects(bulletRectangle)) {
                if (hitBarrier(br,1))  toRemove.add(br);
                collides=true;
            }
        }
        barriers.removeAll(toRemove);
        // Updating the score.
        getScore().incrementScore(toRemove.size(), timeInSeconds);
        return collides;
    }

    public ArrayList<Barrier> getPurpleBarriers() {
        return purpleBarriers;
    }

    public void setPurpleBarriers(ArrayList<Barrier> purpleBarriers) {
        this.purpleBarriers = purpleBarriers;
    }

    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setYmir(Ymir ymir) {
        this.ymir = ymir;
    }

    public void getYmir(Ymir ymir) {
        this.ymir = ymir;
    }

}
