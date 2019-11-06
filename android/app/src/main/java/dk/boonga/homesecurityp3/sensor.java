package dk.boonga.homesecurityp3;

public class sensor {

    private String mTitle, mLocation, mSensorType, mPhoto;

    public sensor(){

    } // needed for firebase

    public sensor(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public sensor(String mTitle, String mLocation, String mDistance, String mPhoto) {
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
