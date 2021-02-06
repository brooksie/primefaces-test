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
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sjbro
 */
@ApplicationScoped
public class RoleService implements Serializable {

    private static final Logger LOG = Logger.getLogger(RoleService.class.getName());
    private static final int OFFSET = 1;

    private static final String[] ROLE_NAMES = {
        "*", "*.view"
        , "User.view", "User.create", "User.edit", "User.delete"
        , "Role.view", "Role.create", "Role.edit", "Role.delete"
        , "UserRole.view", "UserRole.create", "UserRole.edit", "UserRole.delete"
    };

    private final Map<Integer, Role> mapOfRoles = new HashMap<>();

    public RoleService() {
        for (int i = 0; i < ROLE_NAMES.length; i++) {
            mapOfRoles.put(i+OFFSET, new Role(i+OFFSET, ROLE_NAMES[i]));
        }
        LOG.log(Level.INFO, "mapOfRoles now contains {0} elements", mapOfRoles.size());
    }

    public void create(@NotNull Role entity) {
        if (entity.getId() == null) {
            entity.setId(getMaxId() + 1);
        } else {
            if (mapOfRoles.containsKey(entity.getId())) {
                throw new RuntimeException("Create Role failed - Role already exists with id " + entity.getId());
            }
        }
        mapOfRoles.put(entity.getId(), entity);
        LOG.log(Level.INFO, "mapOfRoles now contains {0} elements", mapOfRoles.size());
    }

    private Integer getMaxId() {
        Integer i = mapOfRoles.keySet().stream().max(Integer::compare).get();
        return (null == i) ? OFFSET - 1 : i;
    }

    public void edit(@NotNull Role entity) {
        if ( !mapOfRoles.containsKey(entity.getId())) {
            throw new RuntimeException("Edit Role failed - Role does not exist with id " + entity.getId());
        }
        mapOfRoles.replace(entity.getId(), entity);
        LOG.log(Level.INFO, "mapOfRoles now contains {0} elements", mapOfRoles.size());
    }

    public void remove(@NotNull Role entity) {
        if ( !mapOfRoles.containsKey(entity.getId())) {
            throw new RuntimeException("Remove Role failed - Role does not exist with id " + entity.getId());
        }
        mapOfRoles.remove(entity.getId());
        LOG.log(Level.INFO, "mapOfRoles now contains {0} elements", mapOfRoles.size());
    }

    public List<Role> findAll() {
        return new ArrayList<>(mapOfRoles.values());
    }

    public Role find(int id) {
        return mapOfRoles.get(id);
    }

    public Role findByName(@NotNull String name) {
        return mapOfRoles.values().stream()
                .filter(role -> role.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public int count() {
        return mapOfRoles.size();
    }

}
