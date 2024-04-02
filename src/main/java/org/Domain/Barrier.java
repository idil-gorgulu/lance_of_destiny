package org.Domain;


public class Barrier {
    private Coordinate coordinates;
    private int width;
    private int height;
    private int type; //will indicate the type of the barrier
    private String imageDirectory;

    public Barrier(Coordinate coordinates, int type) {
        this.coordinates = coordinates;
        this.width = 10;
        this.height = 10;
        this.type = type;
        this.imageDirectory = setImageDirectory();
    }

    private String setImageDirectory() {
        if(type == 0) { //Simple barrier
            return "./lance_of_destiny/assets/BlueGem.png";
        }else if(type == 1) { //Reinforced barrier
            return "./lance_of_destiny/assets/Firm.png";
        }else if(type == 2) { //Explosive barrier
            return "./lance_of_destiny/assets/RedGem.png";
        }else if (type == 3) {
            return "./lance_of_destiny/assets/GreenGem.png";
        }
        // In here return Exception
        return null;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
}
