/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brooksbank.testpicklistrendererror;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author sjbro
 */

@FacesConverter(value = "myTagConverter", managed = true )
public class TagConverter implements Converter<Tag> {

    @Inject
    private TagService tagService;

    @Override
    public Tag getAsObject(FacesContext fc, UIComponent uic, String name) {
        if ( name == null || name.isBlank()) { return null; }
        Tag  tag = tagService.findByName(name);
        return tag;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Tag tag) {
        if ( tag == null ) return null;
        String name =  tag.getName();
        return name;
    }

}
