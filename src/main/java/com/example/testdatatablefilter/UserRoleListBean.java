/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.testdatatablefilter;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
public class UserRoleListBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(UserRoleListBean.class.getName());
    
    private String byOption = null; // either U or R
    private User selectedUser;
    private Role selectedRole;
    
    private boolean usernameColumnRendered = false;
    private boolean groupnameColumnRendered = true;
    private String dataTableTitle = null;
    
    private List<UserRoleExt> userRoleList;
    private UserRoleExt selectedUserRole;
    private List<UserRoleExt> userRoleFilteredList;
    
    private List<User> userCatalog;
    private List<Role> roleCatalog;

    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;
    @Inject 
    private UserRoleService userRoleService;
    
    private final int fixed = (int) (Math.random()*999 - 0.5);

    /** Creates a new instance of UserRoleListBean */
    public UserRoleListBean() {
    }

    @PostConstruct
    protected void init() {
        LOG.log(Level.INFO, "{0}: creating {1}", 
                new Object[] {fixed, this.getClass().getName()});
        
        userCatalog = userService.findAll();
        roleCatalog = roleService.findAll();
    }

    public String getByOption() {
        return byOption;
    }

    public void setByOption(String byOption) {
        this.byOption = byOption;
    }
    
    public void byOptionChanged() {
        LOG.log(Level.INFO,"{0}: byOptionChanged() called...", new Object[]{fixed});
        dataTableTitle = null;
        usernameColumnRendered = false;
        groupnameColumnRendered = false;
        if ("U".equals(byOption)) {
            dataTableTitle = "Assigned Roles for this User";
            groupnameColumnRendered = true;
            selectedUserChanged();
        } else if ("R".equals(byOption)) {
            dataTableTitle = "Assigned Users for this Role";
            usernameColumnRendered = true;
            selectedRoleChanged();
        }
    }

    public List<User> getUserCatalog() {
        return userCatalog;
    }

    public List<Role> getRoleCatalog() {
        return roleCatalog;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    public List<UserRoleExt> getUserRoleList() {
        return userRoleList;
    }

    public UserRoleExt getSelectedUserRole() {
        return selectedUserRole;
    }

    public void setSelectedUserRole(UserRoleExt selectedUserRole) {
        this.selectedUserRole = selectedUserRole;
    }

    public List<UserRoleExt> getUserRoleFilteredList() {
        return userRoleFilteredList;
    }

    public void setUserRoleFilteredList(List<UserRoleExt> userRoleFilteredList) {
        this.userRoleFilteredList = userRoleFilteredList;
    }

    public String getDataTableTitle() {
        return dataTableTitle;
    }
    
    public boolean isUsernameColumnRendered() {
        return usernameColumnRendered;
    }

    public boolean isRolenameColumnRendered() {
        return groupnameColumnRendered;
    }

    public void selectedUserChanged() {
        // The selected user has [potentially] changed.
        // This method populates the full list of UserRoles for this user, including role names
        LOG.log(Level.INFO, "{0}: selectedUserChanged() called... SelectedUser is {1}", new Object[] {fixed, selectedUser});

        if ( null != selectedUser ) {
            // get the list of UserRoles
            List<UserRole> urList = userRoleService.findByUser(selectedUser);
            buildExtendedList(urList);
        } else {
            LOG.log(Level.INFO, "{0}: skipped", new Object[]{fixed});
        }
    }
    
    public void selectedRoleChanged() {
        // The selected role has [potentially] changed.
        // This method populates the full list of UserRoles for this role, including user names
        LOG.log(Level.INFO, "{0}: selectedRoleChanged() called... SelectedRole is {1}", new Object[] {fixed, selectedRole});

        if ( null != selectedRole ) {
            // get the list of UserRoles
            List<UserRole> urList = userRoleService.findByRole(selectedRole);
            buildExtendedList(urList);
        } else {
            LOG.log(Level.INFO, "{0}: skipped", new Object[]{fixed});
        }
    }
    
    private void buildExtendedList(List<UserRole> urList) {
        // create the extended list
        List<UserRoleExt> urExtList = new ArrayList<>();
        urList.stream().map(ur -> {
            UserRoleExt urExt = new UserRoleExt(ur);
            urExt.setRole(roleService.find(ur.getRoleid()));
            urExt.setUser(userService.find(ur.getUserid()));
            return urExt;
        }).forEachOrdered(urExt -> {
            urExtList.add(urExt);
        });
        userRoleList = urExtList;

        LOG.log(Level.INFO, "{0}: userRoleList.size is {1}", new Object[] {fixed, userRoleList.size()});
        userRoleList.forEach(ur -> {
            LOG.log(Level.INFO, "{0}: User: {1}, Role: {2}",
                    new Object[]{fixed, ur.getUser().getName(), ur.getRole().getName()});
        });
    }
    
    public void deleteUserRoleAction() {
        LOG.log(Level.INFO, "request to delete {0}", selectedUserRole.toString());
        invokeDialog(DialogMode.DELETE, ("U".equals(this.byOption)?selectedUser : null), 
                ("R".equals(this.byOption)?selectedRole : null),
                selectedUserRole);
    }
    
    public void addUserRoleAction() {
        LOG.log(Level.INFO, "request to add ");
        invokeDialog(DialogMode.ADD, ("U".equals(this.byOption)?selectedUser : null), 
                ("R".equals(this.byOption)?selectedRole : null), null);
    }
    
    private void invokeDialog(DialogMode mode, User user, Role role, UserRole userRole) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("modal", true);
        options.put("contentHeight", "500");

        Map<String, List<String>> params = new HashMap<>();
        params.put("mode", Arrays.asList(mode.name()));
        params.put("byOption", Arrays.asList(this.byOption));
        if (user != null) {
            params.put("userid", Arrays.asList(String.valueOf(user.getId())));
        }
        if (role != null) {
            params.put("roleid", Arrays.asList(String.valueOf(role.getId())));
        }
        if (userRole != null) {
            params.put("userroleid", Arrays.asList(String.valueOf(userRole.getId())));
        }
        PrimeFaces.current().dialog().openDynamic("userRoleDialog", options, params);
    }
    
    public void onAddReturn(SelectEvent event) {
        LOG.log(Level.INFO,"{0}: onAddReturn() called...", new Object[]{fixed});
        // return from Add dialog - refresh the list
        if ("U".equals(this.byOption)) {
            selectedUserChanged();
        } else if ("R".equals(this.byOption)) {
            selectedRoleChanged();
        }
    }

    public void onDeleteReturn(SelectEvent event) {
        LOG.log(Level.INFO,"{0}: onDeleteReturn() called...", new Object[]{fixed});
        // return from Remove dialog - refresh the list
        if ("U".equals(this.byOption)) {
            selectedUserChanged();
        } else if ("R".equals(this.byOption)) {
            selectedRoleChanged();
        }
    }

}
