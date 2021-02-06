/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.testdatatablefilter;

/**
 *
 * @author sjbro
 */
public class UserRoleExt extends UserRole implements java.io.Serializable {
    
    private Role role;      // copy of parent object
    private User user;      // copy of parent object
    
    public UserRoleExt() {
        super();
    }

    public UserRoleExt(Integer userid, Integer roleid) {
        super(userid, roleid);
    }

    public UserRoleExt(Integer id, Integer userid, Integer roleid) {
        super(id, userid, roleid);
    }
    
    public UserRoleExt(UserRole ur) {
        super(ur.getId(), ur.getUserid(), ur.getRoleid());
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserRoleExt{" + "id=" + getId() 
                + ", userid=" + getUserid() 
                + ", roleid=" + getRoleid() 
                + ", user=" + (user == null ? "<null>" : user.toString()) 
                + ", role=" + (role == null ? "<null>" : role.toString()) + '}';
    }
}
