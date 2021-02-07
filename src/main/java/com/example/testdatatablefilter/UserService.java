/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.testdatatablefilter;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sjbro
 */
@SessionScoped
public class UserService implements Serializable {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());
    private static final int OFFSET = 1;

    private static final String[] USER_NAMES = {
        "Sam", "Chris", "Jo", "Charlie"
        ,"Mike", "Judith", "John", "Alex", "Harry"
    };

    private final Map<Integer, User> mapOfUsers = new HashMap<>();

    public UserService() {
        for (int i = 0; i < USER_NAMES.length; i++) {
            mapOfUsers.put(i+OFFSET, new User(i+OFFSET, USER_NAMES[i]));
        }
        LOG.log(Level.INFO, "mapOfUsers now contains {0} elements", mapOfUsers.size());
    }

    public void create(@NotNull User entity) {
        if (entity.getId() == null) {
            entity.setId(getMaxId() + 1);
        } else {
            if (mapOfUsers.containsKey(entity.getId())) {
                throw new RuntimeException("Create User failed - User already exists with id " + entity.getId());
            }
        }
        mapOfUsers.put(entity.getId(), entity);
        LOG.log(Level.INFO, "mapOfUsers now contains {0} elements", mapOfUsers.size());
    }

    private Integer getMaxId() {
        Integer i = mapOfUsers.keySet().stream().max(Integer::compare).get();
        return (null == i) ? OFFSET - 1 : i;
    }

    public void edit(@NotNull User entity) {
        if ( !mapOfUsers.containsKey(entity.getId())) {
            throw new RuntimeException("Edit User failed - User does not exist with id " + entity.getId());
        }
        mapOfUsers.replace(entity.getId(), entity);
        LOG.log(Level.INFO, "mapOfUsers now contains {0} elements", mapOfUsers.size());
    }

    public void remove(@NotNull User entity) {
        if ( !mapOfUsers.containsKey(entity.getId())) {
            throw new RuntimeException("Remove User failed - User does not exist with id " + entity.getId());
        }
        mapOfUsers.remove(entity.getId());
        LOG.log(Level.INFO, "mapOfUsers now contains {0} elements", mapOfUsers.size());
    }

    public List<User> findAll() {
        return new ArrayList<>(mapOfUsers.values());
    }

    public User find(int id) {
        return mapOfUsers.get(id);
    }

    public User findByName(@NotNull String name) {
        return mapOfUsers.values().stream()
                .filter(user -> user.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public int count() {
        return mapOfUsers.size();
    }

}
