/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.testdatatablefilter;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author sjbro
 */
@Named
@ViewScoped
public class DialogBean implements Serializable {

    @Inject
    private TagService tagService;
    
    @Inject
    private ExternalContext externalContext;

    @Named
    @Produces
    @ViewScoped
    private Tag tagEntityBean = new Tag();

    private DialogMode mode = null;

    /**
     * Creates a new instance of DialogBean
     */
    public DialogBean() {
    }

    @PostConstruct
    public void init() {
        // first get the dialog parameters
        Map<String, String[]> paramMap = externalContext.getRequestParameterValuesMap();
        if (paramMap != null && !paramMap.isEmpty()) {

            String[] modeList = (String[]) paramMap.get("mode");
            mode = (modeList == null || modeList.length == 0) ? null
                    : DialogMode.valueOf(modeList[0]);

            String[] tagList = (String[]) paramMap.get("tagid");
            if (tagList != null && tagList.length > 0) {
                tagEntityBean = tagService.find(Integer.valueOf(tagList[0]));
            }
        }
    }

    public String getMode() {
        return (null == mode) ? "" : mode.getDispayName();
    }

    public void addTag() {
        tagService.create(tagEntityBean);
        PrimeFaces.current().dialog().closeDynamic(tagEntityBean);
    }

    public void editTag() {
        tagService.edit(tagEntityBean);
        PrimeFaces.current().dialog().closeDynamic(tagEntityBean);
    }

    public void deleteTag() {
        tagService.remove(tagEntityBean);
        PrimeFaces.current().dialog().closeDynamic(tagEntityBean);
    }

    public void validateTagName(FacesContext context, UIComponent component, String value) {
        if (isNullOrBlank(value)) {
            return;     // ignore null values - required attribute should be set if necessary
        }

        Tag tag = tagService.findByName(value);
        if (null == mode) {
            doDefault(tag);
        } else {
            switch (mode) {
                case ADD:
                    if (tag != null) {
                        // tag already exists
                        throw new ValidatorException(
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Tag name already exists",
                                        "Tag name already exists (" + value + ")"));
                    }
                    break;
                case EDIT:
                    if (!(tag == null || tag.equals(tagEntityBean))) {
                        // new tag name already exists (and isn't the existing name either
                        throw new ValidatorException(
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Tag name already exists",
                                        "Tag name already exists (" + value + ")"));
                    }
                    break;
                default:
                    doDefault(tag);
                    break;
            }
        }
    }

    private void doDefault(Tag tag) {
        if (null == tag || !tag.equals(tagEntityBean)) {
            // for any other mode, tagname should not be changing!
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Tag name must not be changed in this mode",
                            "Tag name must not be changed in this mode"));
        }
    }

    private static boolean isNullOrBlank(String s) {
        return null == s || s.isBlank();
    }
}
