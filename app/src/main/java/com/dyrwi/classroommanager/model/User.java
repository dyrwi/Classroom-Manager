package com.dyrwi.classroommanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ben on 30-Sep-15.
 */
@DatabaseTable
public class User {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String emailAddress;

    @DatabaseField
    private String password;

}
