package dk.boonga.homesecurityp3;

public class room {

    private String mTitle, mLocation, mSensorType, mPhoto;

    public room(){

    } // needed for firebase

    public room(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public room(String mTitle, String mLocation, String mDistance, String mPhoto) {
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mSensorType = mSensorType;
        this.mPhoto = mPhoto;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmSensorType() {
        return mSensorType;
    }

    public void setmSensorType(String mDistance) {
        this.mSensorType = mSensorType;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }
}
