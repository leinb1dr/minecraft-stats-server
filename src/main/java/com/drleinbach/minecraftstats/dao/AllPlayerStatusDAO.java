package com.drleinbach.minecraftstats.dao;

import com.drleinbach.minecraftstats.beans.ServerVisit;
import com.drleinbach.model.AllPlayerStatus;

import java.util.List;

/**
 * Data access object interface for the 'all_player_status'
 * database view. This outlines the required database actions.
 * <p/>
 * Created: 5/5/13
 *
 * @author Daniel
 */
public interface AllPlayerStatusDAO {

    /**
     * Find a list of all of the currently logged in users.
     *
     * @return List of users currently logged into the server.
     */
    List<AllPlayerStatus> getLoggedIn();

    /**
     * For a time range, count the number of unique users logged
     * into the server.
     *
     * @return List of users logged into the server by time range.
     */
    List<ServerVisit> getServerVistits();
}
