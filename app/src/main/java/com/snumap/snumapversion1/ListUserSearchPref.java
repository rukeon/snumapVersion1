package com.snumap.snumapversion1;

import java.util.List;

/**
 * Created by rukeon01 on 2015-08-28.
 */
public class ListUserSearchPref {
    public List<UserSearch> search;

    public List<UserSearch> getUserSearch() {
        return search;
    }

    public void setUserSearch(List<UserSearch> search) {
        this.search = search;
    }

    public boolean isExist(UserSearch otherSearch) { // 존재하면 true 반환
        if (search == null)
            return false;

        for(UserSearch item: search)
        {
            if(item.getFrom().equals(otherSearch.getFrom()) && item.getTo().equals(otherSearch.getTo()))
                return true;
        }
        return false;
    }

    public void deleteUserSearch(UserSearch otherSearch) {
        int result = find(otherSearch);
        if(result != -1)
            this.search.remove(result);
    }

    private int find(UserSearch otherSearch) {
        int pos = -1;
        for(UserSearch item: search)
        {
            pos++;
            if(item.getFrom().equals(otherSearch.getFrom()) && item.getTo().equals(otherSearch.getTo()))
                return pos;
        }
        return -1;
    }
}
