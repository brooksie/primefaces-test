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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;


/**
 *
 * @author sjbro
 */
@Named
@ViewScoped
public class UserRoleDialogBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(UserRoleDialogBean.class.getName());
    
    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;
    @Inject
    private UserRoleService userRoleService;
    
    @Inject
    private ExternalContext externalContext;

//    @Named
//    @Produces
//    @ViewScoped
//    private UserRole userRoleEntityBean = new UserRole();

    private DialogMode mode = null;
    private String byOption = null; // U = byUser; R = byRole;
    private Integer providedUserRoleid = null;
    private Integer providedUserid = null;
    private Integer providedRoleid = null;
    
    // used by Delete/View mode
    private UserRoleExt userRole;
    
    // used by Add
    private String choiceLabel;
    private String optionName = "?";
    
    private final List<User> userChoices = new ArrayList<>();
    private User selectedUserChoice;
    private final List<Role> roleChoices = new ArrayList<>();
    private Role selectedRoleChoice;
    
    private final int fixed = (int) (Math.random()*999 - 0.5);

    /** Creates a new instance of UserRoleDialogBean */
    public UserRoleDialogBean() {
    }

    @PostConstruct
    protected void init() {
        LOG.log(Level.INFO, "{0}: creating {1}", 
                new Object[] {fixed, this.getClass().getName()});
        // first get the dialog parameters
        Map<String, String[]> paramMap = externalContext.getRequestParameterValuesMap();
        if (paramMap != null && !paramMap.isEmpty()) {

            mode = (null == getStringParm("mode", paramMap)) ? null
                    : DialogMode.valueOf(getStringParm("mode", paramMap).toUpperCase());
            byOption = getStringParm("byOption", paramMap);
            providedUserRoleid = getIntegerParm("userroleid", paramMap);
            providedUserid     = getIntegerParm("userid",     paramMap);
            providedRoleid     = getIntegerParm("roleid",     paramMap);
        }
        
        if (DialogMode.EDIT.equals(mode)) {
            throw new IllegalArgumentException("DialogMode.EDIT is not supported by UserRoleDialogController");
        }

        if ( (DialogMode.DELETE.equals(mode) || DialogMode.VIEW.equals(mode)) && providedUserRoleid != null ) {
            // for Delete/View, the role should be present. Obtain the entity, and get both parents
            userRole = getUserRoleExt(providedUserRoleid);
        }
        
        if ( DialogMode.ADD.equals(mode) ) {
            if ( "U".equals(byOption) ) {
                User user = userService.find(providedUserid);
                optionName = user.getName();
                choiceLabel="Assigned Role";
                roleChoices.addAll(prepareRoleSelection(user));
            } else if ( "R".equals(byOption) ) {
                Role role = roleService.find(providedRoleid);
                optionName = role.getName();
                choiceLabel="Assigned User";
                userChoices.addAll(prepareUserSelection(role));
            }
        }
    }

    private UserRoleExt getUserRoleExt(Integer id) {
        UserRole ur = userRoleService.find(id);
        UserRoleExt urExt = new UserRoleExt(ur);
        urExt.setUser(userService.find(ur.getUserid()));
        urExt.setRole(roleService.find(ur.getRoleid()));
        return urExt;
    }
    
    protected List<Role> prepareRoleSelection(User user) {
        Set<Role> rolesSet = new HashSet<>(roleService.findAll());
        List<UserRole> urCollection = userRoleService.findByUser(user);
        urCollection.forEach(ur -> {
            rolesSet.remove(roleService.find(ur.getRoleid()));
        });
        return new ArrayList<>(rolesSet);
    }

    protected List<User> prepareUserSelection(Role role) {
        Set<User> usersSet = new HashSet<>(userService.findAll());
        List<UserRole> urCollection = userRoleService.findByRole(role);
        urCollection.forEach(ur -> {
            usersSet.remove(userService.find(ur.getUserid()));
        });
        return new ArrayList<>(usersSet);
    }



    public String getMode() {
        return (null == mode) ? "" : mode.getDispayName();
    }

    public String getByOption() {
        return byOption;
    }

    public UserRoleExt getUserRole() {
        return userRole;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getChoiceLabel() {
        return choiceLabel;
    }

    public List<User> getUserChoices() {
        return userChoices;
    }

    public List<Role> getRoleChoices() {
        return roleChoices;
    }

    public User getSelectedUserChoice() {
        return selectedUserChoice;
    }

    public void setSelectedUserChoice(User selectedUserChoice) {
        this.selectedUserChoice = selectedUserChoice;
    }

    public Role getSelectedRoleChoice() {
        return selectedRoleChoice;
    }

    public void setSelectedRoleChoice(Role selectedRoleChoice) {
        this.selectedRoleChoice = selectedRoleChoice;
    }


    public void addUserRole() {
        //userRoleService.create(userRoleEntityBean);
        //PrimeFaces.current().dialog().closeDynamic(userRoleEntityBean);
        
        if ( "U".equals(byOption) ) {
            addUserRoleToUser();
        }
        if ( "R".equals(byOption) ) {
            addUserRoleToRole();
        }
        LOG.log(Level.INFO, "{0}: returning after add", new Object[]{fixed});
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    protected void addUserRoleToUser() {
        //User u = userService.find(providedUserid);
        UserRole ur = new UserRole();
        ur.setUserid(providedUserid);
        
        // the role is "selectedRoleChoice"
        Role r = roleService.find(selectedRoleChoice.getId());
        ur.setRoleid(r.getId());

        userRoleService.create(ur);
    }

    protected void addUserRoleToRole() {
        //Role r = roleService.findByIdWithUserRole(providedRoleid);
        UserRole ur = new UserRole();
        ur.setRoleid(providedRoleid);
        
        // the user is "selectedUserChoice"
        User u = userService.find(selectedUserChoice.getId());
        ur.setUserid(u.getId());
        
        userRoleService.create(ur);
    }

    public void deleteUserRole() {
        
        // Delete the userRole and disconnect from associated User and Role.
        // Use the capability in User or in Role.
        // The object to delete is "userRole"
        
        userRoleService.remove(userRole);

        LOG.log(Level.INFO, "{0}: returning after delete", new Object[]{fixed});
        PrimeFaces.current().dialog().closeDynamic(userRole);
    }

//    private static boolean isNullOrBlank(String s) {
//        return null == s || s.isBlank();
//    }

    public static String getStringParm(String name, Map<String, String[]> parmMap) {
        String[] parmList = (String[]) parmMap.get(name);
        return ( parmList == null || parmList.length == 0 )? null : parmList[0];
    }
    public static Integer getIntegerParm(String name, Map<String, String[]> parmMap) {
        String[] parmList = (String[]) parmMap.get(name);
        return ( parmList == null || parmList.length == 0 )? null : Integer.valueOf(parmList[0]);
    }

}
