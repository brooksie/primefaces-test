/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.testdatatablefilter;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sjbro
 */
public class UserRole implements Serializable {
    
    @NotNull
    private Integer id;
    
    @NotBlank
    private Integer userid;
    @NotBlank
    private Integer roleid;

    public UserRole() {
    }

    public UserRole(Integer userid, Integer roleid) {
        this.userid = userid;
        this.roleid = roleid;
    }

    public UserRole(Integer id, Integer userid, Integer roleid) {
        this.id = id;
        this.userid = userid;
        this.roleid = roleid;
    }

    
    public Integer getId() {
        return id;
    }

    // *** note change of scope ***
    // ID should not be changed once set
    protected void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserRole other = (UserRole) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "UserRole{" + "id=" + id + ", userid=" + userid + ", roleid=" + roleid + '}';
    }


    
}
