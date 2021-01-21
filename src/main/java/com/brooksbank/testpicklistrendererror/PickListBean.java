/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brooksbank.testpicklistrendererror;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;

/**
 *
 * @author sjbro
 */
@Named
@ViewScoped
public class PickListBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(PickListBean.class.getName());

    @Inject
    private TagService tagService;

    private DualListModel<Tag> tags;

    /**
     * Creates a new instance of PickListBean
     */
    public PickListBean() {
    }

    @PostConstruct
    void init() {
        List<Tag> source = new ArrayList<>(tagService.findAll());

        List<Tag> target = new ArrayList<>();

        tags = new DualListModel<>(source, target);
    }

    public DualListModel<Tag> getTags() {
        return tags;
    }

    public void setTags(DualListModel<Tag> tags) {
        this.tags = tags;
    }

    public void submitAction() {
        LOG.info("Selected values are:");

        List<Tag> selectedTags = tags.getTarget();
        LOG.log(Level.INFO, "selectedTags is {0}", selectedTags.getClass().getName());
        LOG.log(Level.INFO, "selectedTags has {0} elements", selectedTags.size());
        if (selectedTags.size() > 0) {
            Tag t0 = selectedTags.get(0);
            LOG.log(Level.INFO, "First element is {0}", t0.toString());
            for (Tag tag : selectedTags) {
                LOG.log(Level.INFO, "tag is {0}", tag.getClass().getName());
                LOG.info(tag.toString());
            }
        }
        LOG.info("<done>");
    }
}
