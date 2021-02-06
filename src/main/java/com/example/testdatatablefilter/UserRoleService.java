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
import java.util.Objects;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sjbro
 */
@ApplicationScoped
public class UserRoleService implements Serializable {

    private static final Logger LOG = Logger.getLogger(UserRoleService.class.getName());
    private static final int OFFSET = 1001;

    private static final Integer[][] USERROLE_NAMES = {
        {1,1}
        , {2,2}
        , {3,3},{3,4},{3,5},{3,6}
        , {4,7},{4,8},{4,9},{4,10}
        , {5,3},{5,4},{5,5},{5,6},{5,7},{5,8},{5,9},{5,10},{5,11},{5,12},{5,13},{5,14}
        , {6,3},{6,7},{6,11}
    };

    private final Map<Integer, UserRole> mapOfUserRoles = new HashMap<>();

    public UserRoleService() {
        for (int i = 0; i < USERROLE_NAMES.length; i++) {
            mapOfUserRoles.put(i+OFFSET, 
                    new UserRole(i+OFFSET, USERROLE_NAMES[i][0], USERROLE_NAMES[i][1]));
        }
        LOG.log(Level.INFO, "Constructor: mapOfUserRoles now contains {0} elements", mapOfUserRoles.size());
    }

    public void create(@NotNull UserRole entity) {
        if (entity.getId() == null) {
            entity.setId(getMaxId() + 1);
        } else {
            if (mapOfUserRoles.containsKey(entity.getId())) {
                throw new RuntimeException("Create UserRole failed - UserRole already exists with id " + entity.getId());
            }
        }
        mapOfUserRoles.put(entity.getId(), entity);
        LOG.log(Level.INFO, "create: mapOfUserRoles now contains {0} elements", mapOfUserRoles.size());
    }

    private Integer getMaxId() {
        Integer i = mapOfUserRoles.keySet().stream().max(Integer::compare).get();
        return (null == i) ? OFFSET - 1 : i;
    }

    public void edit(@NotNull UserRole entity) {
        if ( !mapOfUserRoles.containsKey(entity.getId())) {
            throw new RuntimeException("Edit UserRole failed - UserRole does not exist with id " + entity.getId());
        }
        mapOfUserRoles.replace(entity.getId(), entity);
        LOG.log(Level.INFO, "edit: mapOfUserRoles now contains {0} elements", mapOfUserRoles.size());
    }

    public void remove(@NotNull UserRole entity) {
        if ( !mapOfUserRoles.containsKey(entity.getId())) {
            throw new RuntimeException("Remove UserRole failed - UserRole does not exist with id " + entity.getId());
        }
        mapOfUserRoles.remove(entity.getId());
        LOG.log(Level.INFO, "remove: mapOfUserRoles now contains {0} elements", mapOfUserRoles.size());
    }

    public List<UserRole> findAll() {
        return new ArrayList<>(mapOfUserRoles.values());
    }

    public List<UserRole> findByUser( @NotNull User u ) {  
        List<UserRole> filteredList = mapOfUserRoles.values().stream()
                .filter(ur -> Objects.equals(ur.getUserid(), u.getId()))
                .collect(Collectors.toList());
        return new ArrayList<>(filteredList);
    }
    
    public List<UserRole> findByRole( @NotNull Role r ) {  
        List<UserRole> filteredList = mapOfUserRoles.values().stream()
                .filter(ur -> Objects.equals(ur.getRoleid(), r.getId()))
                .collect(Collectors.toList());
        return new ArrayList<>(filteredList);
    }

    public UserRole findByUserAndRole( @NotNull User u, @NotNull Role r ) {  
        UserRole selected = mapOfUserRoles.values().stream()
                .filter(ur -> (Objects.equals(ur.getUserid(), u.getId()) && Objects.equals(ur.getRoleid(), r.getId())))
                .findAny()
                .orElse(null);
        return selected;
    }

    public UserRole find(int id) {
        return mapOfUserRoles.get(id);
    }

    public int count() {
        return mapOfUserRoles.size();
    }

}
