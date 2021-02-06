/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.testdatatablefilter;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author sjbro
 */
@Named
@ViewScoped
public class ListBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(ListBean.class.getName());
    
    @Inject
    private TagService tagService;

    private List<Tag> tagCatalog;
    private List<Tag> filteredTagCatalog = null;
    private Tag selectedTag = null;
    
    /** Creates a new instance of ListBean */
    public ListBean() {
    }
    
    @PostConstruct
    protected void init() {
        updateTagCatalog();
    }

    protected void updateTagCatalog() {
        tagCatalog = tagService.findAll();
        LOG.log(Level.INFO, "Catalog contains " + tagCatalog);
    }
    
    // getters and setters

    public List<Tag> getTagCatalog() {
        return tagCatalog;
    }

    public List<Tag> getFilteredTagCatalog() {
        return filteredTagCatalog;
    }

    public void setFilteredTagCatalog(List<Tag> filteredTagCatalog) {
        this.filteredTagCatalog = filteredTagCatalog;
    }

    public Tag getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(Tag selectedTag) {
        this.selectedTag = selectedTag;
    }


    public void addAction() {
        invokeDialog(DialogMode.ADD, null);
    }

    public void editAction() {
        invokeDialog(DialogMode.EDIT, selectedTag);
    }

    public void deleteAction() {
        invokeDialog(DialogMode.DELETE, selectedTag);
    }

    public void viewAction() {
        invokeDialog(DialogMode.VIEW, selectedTag);
    }

    protected void invokeDialog(DialogMode mode, Tag tag) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("modal", true);
        options.put("contentHeight", "500");

        Map<String, List<String>> params = new HashMap<>();
        params.put("mode", Arrays.asList(mode.name()));
        if (tag != null) {
            params.put("tagid", Arrays.asList(String.valueOf(tag.getId())));
        }
        PrimeFaces.current().dialog().openDynamic("tagDialog", options, params);
    }
    
    public void addDialogReturned(SelectEvent event) {
        updateTagCatalog();
    }
    public void editDialogReturned(SelectEvent event) {
        updateTagCatalog();
    }
    public void deleteDialogReturned(SelectEvent event) {
        updateTagCatalog();
    }

}
