
package com.example.testmissingnamespace;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named(value="passwordMaintController")
@ViewScoped
public class PasswordMaintController implements Serializable {

    @Inject
    private UserService userService;

    private List<Users> users = null;

    private String selectedUsername;

    /** Creates a new instance of PasswordMaintController */
    public PasswordMaintController() {
    }

    public String getSelectedUsername() {
        return selectedUsername;
    }

    public void setSelectedUsername(String selectedUsername) {
        this.selectedUsername = selectedUsername;
    }

    public List<Users> getUsers() {
        if (this.users == null ) {
            this.users = userService.findAll();
        }
        return this.users;
    }

    public void selectedUserChanged() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "User changed",
                        "User changed (" + selectedUsername + ")"));
    }

}
