package com.snumap.snumapversion1;

import java.util.List;

/**
 * Created by rukeon01 on 2015-08-21.
 */
public class ListUserPref {
    public List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isExist(User user) { // 존재하면 true 반환
        for(User item: users)
        {
            if(item.getName().equals(user.getName()))
                return true;
        }
        return false;
    }

    public void deleteUser(User user) {
        int result = findByName(user);
        this.users.remove(result);
    }

    private int findByName(User user) {
        int pos = -1;
        for(User item: users)
        {
            pos++;
            if(item.getName().equals(user.getName()))
                return pos;
        }
        return -1;
    }
}
