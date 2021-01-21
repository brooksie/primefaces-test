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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
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
        String msg = buildSelectedItems();
        LOG.log(Level.INFO, "selected tags in the page were {0}", msg);
        messageSelectedItems("Page");
    }

    public void openDialogAction() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("modal", true);
        PrimeFaces.current().dialog().openDynamic("/picklistDialog", options, null);
    }

    public void dialogSubmitAction() {
        String msg = buildSelectedItems();
        LOG.log(Level.INFO, "selected tags in the dialog were {0}", msg);
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    private void messageSelectedItems(String src) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,
                src + " tags selected:", buildSelectedItems());
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    private String buildSelectedItems() {
        StringBuilder buff = new StringBuilder("{");
        boolean first = true;
        for (Tag t : tags.getTarget()) {
            buff.append(!first ? ", " : "").append(t.getName());
            first = false;
        }
        buff.append("}");
        return buff.toString();
    }
}
