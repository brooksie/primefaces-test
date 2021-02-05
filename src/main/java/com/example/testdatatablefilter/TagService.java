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
public class TagService implements Serializable {

    private static final Logger LOG = Logger.getLogger(TagService.class.getName());

    private static final String[] TAG_NAMES = {
        "Java", "PrimeFaces", "JSF", "JPA"
//        ,"Hibernate", "JMS", "CDI"
    };

    private Map<Integer, Tag> mapOfTags = new HashMap<>();

    public TagService() {
        for (int i = 0; i < TAG_NAMES.length; i++) {
            mapOfTags.put(i+1, new Tag(i+1, TAG_NAMES[i]));
        }
        LOG.info("mapOfTags now contains " + mapOfTags.size() + " elements");
    }

    public void create(@NotNull Tag entity) {
        if (entity.getId() == null) {
            entity.setId(getMaxId() + 1);
        } else {
            if (mapOfTags.containsKey(entity.getId())) {
                throw new RuntimeException("Create Tag failed - Tag already exists with id " + entity.getId());
            }
        }
        mapOfTags.put(entity.getId(), entity);
        LOG.info("mapOfTags now contains " + mapOfTags.size() + " elements");
    }

    private Integer getMaxId() {
        Integer i = mapOfTags.keySet().stream().max(Integer::compare).get();
        return (null == i) ? 0 : i;
    }

    public void edit(@NotNull Tag entity) {
        if ( !mapOfTags.containsKey(entity.getId())) {
            throw new RuntimeException("Edit Tag failed - Tag does not exist with id " + entity.getId());
        }
        mapOfTags.replace(entity.getId(), entity);
        LOG.info("mapOfTags now contains " + mapOfTags.size() + " elements");
    }

    public void remove(@NotNull Tag entity) {
        if ( !mapOfTags.containsKey(entity.getId())) {
            throw new RuntimeException("Remove Tag failed - Tag does not exist with id " + entity.getId());
        }
        mapOfTags.remove(entity.getId());
        LOG.info("mapOfTags now contains " + mapOfTags.size() + " elements");
    }

    public List<Tag> findAll() {
//        return List.copyOf(mapOfTags.values());
        return new ArrayList<>(mapOfTags.values());
    }

    public Tag find(int id) {
        return mapOfTags.get(id);
    }

    public Tag findByName(@NotNull String name) {
        return mapOfTags.values().stream()
                .filter(tag -> tag.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public int count() {
        return mapOfTags.size();
    }

}
