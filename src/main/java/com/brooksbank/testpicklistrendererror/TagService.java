/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brooksbank.testpicklistrendererror;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sjbro
 */
@Stateless
public class TagService {
    
    private static final String[] TAG_NAMES = {
        "Java", "PrimeFaces", "JSF", "JPA", "Hibernate", "JMS", "CDI"};
    
    private final Map<Integer, Tag> mapOfTags = new HashMap<>();

    public TagService() {
        for (int i = 0; i < TAG_NAMES.length; i++) {
            mapOfTags.put(i, new Tag(i, TAG_NAMES[i]));
        }
    }
    
    public List<Tag> findAll() {
        return List.copyOf(mapOfTags.values());
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
}
