package hashim.org.clevermindpobict.model;

public class Jobs {

    private String position;
    private String experience;
    private String positionImage;
    private String id;

    public Jobs(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPositionImage() {
        return positionImage;
    }

    public void setPositionImage(String positionImage) {
        this.positionImage = positionImage;
    }
}
