
package com.example.testmissingnamespace;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.ejb.Stateless;


@Stateless
public class UserService implements java.io.Serializable {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

    private static final String[][] DATA = {
        { "sam", "pwdhash", "Sam", "Red"},
        { "chris", "pwdhash", "Chris", "White"},
        { "jo", "pwdhash", "Jo", "Yellow"},
        { "mike", "pwdhash", "Mike", "Grey"},
        { "alex", "pwdhash", "Alex", "Blue"}
    };

    private final List<Users> catalog = new ArrayList<>();

    public UserService() {
        for ( int i = 0; i < DATA.length; i++) {
            Users u = new Users(Long.valueOf(i), DATA[i][0], DATA[i][1], DATA[i][2], DATA[i][3]);
            catalog.add(u);
        }
        LOG.info(catalog.toString());
    }

    public List<Users> findAll() {
        return catalog;
    }
}
