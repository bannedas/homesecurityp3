package dk.boonga.homesecurityp3;

public class Room {

    private String mTitle, mPhoto;

    public Room(){

    } // needed for firebase

    public Room(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public Room(String mTitle, String mPhoto) {
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
