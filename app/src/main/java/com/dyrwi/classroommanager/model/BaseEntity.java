package com.dyrwi.classroommanager.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ben on 01-Oct-15.
 */
public abstract class BaseEntity {
    public final static String ID_FIELD_NAME = "id";
    public final static String DATE_CREATED_FIELD_NAME = "date_created";
    public final static String DATE_MODIFIED_FIELD_NAME = "date_modified";
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;
    @DatabaseField(columnName = DATE_CREATED_FIELD_NAME)
    private Date dateCreated;
    @DatabaseField(columnName = DATE_MODIFIED_FIELD_NAME)
    private Date dateModified;

    public BaseEntity() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setDateCreated(Date dateCreated) {
        if (dateCreated == null) {
            dateCreated = Calendar.getInstance().getTime();
        }
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        if(this.dateCreated == null)
            dateCreated = Calendar.getInstance().getTime();
        return this.dateCreated;
    }

    public void setDateModified(Date dateModified) {
        if (dateModified == null) {
            dateModified = Calendar.getInstance().getTime();
        }
        this.dateModified = dateModified;
    }

    public Date getDateModified() {
        if(this.dateModified == null)
            this.dateModified = Calendar.getInstance().getTime();
        return this.dateModified;
    }
}
