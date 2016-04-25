package com.dyrwi.classroommanager.repository.initialization;

import android.content.Context;

import com.dyrwi.classroommanager.repository.Repo;

/**
 * Created by Ben on 06-Oct-15.
 */
public class ClassroomManagerInitialization {
    private Repo repo;
    private Context context;
    public ClassroomManagerInitialization(Context context) {
        //Provide it with the context so it can work with the repo
        //also work with activities.
        this.context  = context;
        this.repo = new Repo(context);
    }

    public void clearAll() {

    }
}
