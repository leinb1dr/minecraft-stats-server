package com.drleinbach.minecraftstats.beans;

import java.sql.Timestamp;

/**
 * This interface represents the format
 * of the data gathered when determining
 * the number of players who have visited the
 * server.
 * <p/>
 * Created: 5/5/13
 *
 * @author Daniel
 */
public interface ServerVisit {

    /**
     * A time range for the record
     *
     * @return The time range for the record
     */
    Timestamp getLogTime();

    /**
     * Change the time range for the record
     *
     * @param logTime - The new time range for the record
     */
    void setLogTime(Timestamp logTime);

    /**
     * Gets the number of players who logged in during
     * the time range.
     *
     * @return Count of the players who have logged in during
     *         the set time range.
     */
    Long getCount();

    /**
     * Changes the count for the number of players for a given time range.
     *
     * @param count - New number of players who have logged into the server
     */
    void setCount(Long count);
}
