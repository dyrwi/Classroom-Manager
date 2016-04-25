package com.dyrwi.classroommanager.model;

import android.net.Uri;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ben on 01-Oct-15.
 */
public class Person extends BaseEntity {

    private static final String TAG = "Person";

    @DatabaseField
    private String englishName;
    @DatabaseField
    private String phoneNumber;
    @DatabaseField
    private String message;
    @DatabaseField
    private Date dateOfBirth;
    @DatabaseField
    private String pictureUri;

    public Person() {
        Uri uri = Uri.parse("android.resource://com.dyrwi.classroommanger/drawable/avatar_blank");
        setPictureUri(uri.toString());
    }


    public String getEnglishName() {
        if(this.englishName == null)
            this.englishName = "English Name";
        return englishName;
    }

    public void setEnglishName(String englishName) {
        if(englishName == null) {
            englishName = "English Name";
        }
        this.englishName = englishName;
    }

    public String getPhoneNumber() {
        if(this.phoneNumber == null)
            this.phoneNumber = "012-345-678";
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            phoneNumber = "012-345-678";
        }
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        if(this.message == null)
            this.message = "Message";
        return message;
    }

    public void setMessage(String message) {
        if(message == null) {
            message = "message";
        }
        this.message = message;
    }

    public Date getDateOfBirth() {
        if(this.dateOfBirth == null) {
            this.dateOfBirth = Calendar.getInstance().getTime();
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        if(dateOfBirth == null) {
            dateOfBirth = Calendar.getInstance().getTime();
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }
}
