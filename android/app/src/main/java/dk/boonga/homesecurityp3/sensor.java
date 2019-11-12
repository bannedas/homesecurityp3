package dk.boonga.homesecurityp3;

public class sensor {

    private String mTitle, mPhoto;

    public sensor(){

    } // needed for firebase

    public sensor(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public sensor(String mTitle, String mPhoto) {
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
