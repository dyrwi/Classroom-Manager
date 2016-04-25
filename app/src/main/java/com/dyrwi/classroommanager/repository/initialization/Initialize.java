package com.dyrwi.classroommanager.repository.initialization;

import com.dyrwi.classroommanager.model.BaseEntity;

import java.util.List;

/**
 * Created by Ben on 06-Oct-15.
 */
public interface Initialize<T extends BaseEntity> {
    void create();
    List<T> getAll();
}
