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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author sjbro
 */
@Named
@ViewScoped
public class FilterBean implements Serializable {

    @Inject
    private UserRoleService userRoleService;
    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;
    
    private boolean userRendered = true;
    private boolean roleRendered = true;
    private boolean userVisible = true;
    private boolean roleVisible = true;
    
    private List<UserRoleExt> list = new ArrayList<>();
    private List<UserRoleExt> filteredList = new ArrayList<>();
    
    private String rowsetSize = "1/2";   // true, false or null
    private static final String FIRST_HALF = "1/2";
//    private static final String SECOND_HALF = "2/2";
    private static final String ALL_ROWS = "ALL";
    
    /** Creates a new instance of FilterBean */
    public FilterBean() {
    }
    
    @PostConstruct
    public void updateList() {
        list = new ArrayList<>();
        filteredList = null;
        
        List<UserRole> userRoles = userRoleService.findAll();

        int minRow = (firstHalf() || allRows()) ? 0 : (int) (userRoles.size()/2) ;
        int maxRow = firstHalf() ? (int) (userRoles.size()/2) : userRoles.size();
        int counter = 0;
        
        for ( UserRole ur : userRoles) {
            if ( counter >= minRow && counter < maxRow) {
                UserRoleExt ext = new UserRoleExt(ur);
                ext.setUser(userService.find(ur.getUserid()));
                ext.setRole(roleService.find(ur.getRoleid()));
                list.add(ext);
            }
            counter++;
        }
    }

    
    public boolean isUserRendered() {
        return userRendered;
    }

    public void setUserRendered(boolean userRendered) {
        this.userRendered = userRendered;
    }

    public boolean isRoleRendered() {
        return roleRendered;
    }

    public void setRoleRendered(boolean roleRendered) {
        this.roleRendered = roleRendered;
    }

    public boolean isUserVisible() {
        return userVisible;
    }

    public void setUserVisible(boolean userVisible) {
        this.userVisible = userVisible;
    }

    public boolean isRoleVisible() {
        return roleVisible;
    }

    public void setRoleVisible(boolean roleVisible) {
        this.roleVisible = roleVisible;
    }

    public String getRowsetSize() {
        return rowsetSize;
    }

    public void setRowsetSize(String rowsetSize) {
        this.rowsetSize = rowsetSize;
    }

    public List<UserRoleExt> getList() {
        return list;
    }

    public List<UserRoleExt> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<UserRoleExt> filteredList) {
        this.filteredList = filteredList;
    }

    public void clearFilter() {
        filteredList = null;
    }
    
    public void rowsetSizeChanged() {
        updateList();
    }

    private boolean firstHalf() {
        return FIRST_HALF.equals(rowsetSize);
    }
    private boolean allRows() {
        return ALL_ROWS.equals(rowsetSize);
    }
}
