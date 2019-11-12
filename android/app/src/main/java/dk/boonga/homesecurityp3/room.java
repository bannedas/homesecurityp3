package dk.boonga.homesecurityp3;

public class room {

    private String mTitle, mPhoto;

    public room(){

    } // needed for firebase

    public room(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public room(String mTitle, String mPhoto) {
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
