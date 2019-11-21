package dk.boonga.homesecurityp3;

public class Sensor {

    private String mTitle, mPhoto;

    public Sensor(){

    } // needed for firebase

    public Sensor(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public Sensor(String mTitle, String mPhoto) {
        this.mTitle = mTitle;
        this.mPhoto = mPhoto;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }
}
