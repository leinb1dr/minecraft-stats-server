package com.drleinbach.minecraftstats.beans;

import org.apache.log4j.Logger;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Implementation of the interface that represents the format of
 * data when determining the number of users that have logged into
 * the server.
 * <p/>
 * Created: 5/5/13
 *
 * @author Daniel
 */
public class ServerVisitImpl implements ServerVisit, Serializable {

    /**
     * Version of the object
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger, used to record diagnostic information
     */
    private static final Logger LOGGER = Logger.getLogger(ServerVisitImpl.class);

    /**
     * The time range for the database record
     */
    @Column(name = "log_time")
    private Timestamp logTime;

    /**
     * Count of unique users logged in for a given time range
     */
    @Column
    private Long count;

    /**
     * Provides access to a private member that holds the records
     * time range.
     *
     * @return The time range for the record.
     * @see com.drleinbach.minecraftstats.beans.ServerVisit#getLogTime()
     */
    @Override
    public Timestamp getLogTime() {
        return logTime;
    }

    /**
     * Provides change access to a private member that holds the records
     * time range so it can be updated.
     *
     * @param logTime - The new time range for the record
     * @see com.drleinbach.minecraftstats.beans.ServerVisit#setLogTime(java.sql.Timestamp)
     */
    @Override
    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    /**
     * Provides access to the private member that holds the
     * count of the number of unique users logged into the server.
     *
     * @return The count of users logged into the server
     * @see com.drleinbach.minecraftstats.beans.ServerVisit#getCount()
     */
    @Override
    public Long getCount() {
        return count;
    }

    /**
     * Provides change access to the private member that holds the
     * count of the number of unique users logged into the server.
     *
     * @param count - The new count of users logged into the server
     * @see com.drleinbach.minecraftstats.beans.ServerVisit#setCount(Long)
     */
    @Override
    public void setCount(Long count) {
        this.count = count;
    }
}
