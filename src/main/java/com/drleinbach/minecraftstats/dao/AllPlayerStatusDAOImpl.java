package com.drleinbach.minecraftstats.dao;

import com.drleinbach.minecraftstats.beans.AllPlayerStatusImpl;
import com.drleinbach.minecraftstats.beans.ServerVisit;
import com.drleinbach.minecraftstats.beans.ServerVisitImpl;
import com.drleinbach.model.AllPlayerStatus;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * This is a data access object that handles queries to the 'all_player_status' and the
 * 'player_status' views.
 * <p/>
 * Created: 5/4/13
 *
 * @author Daniel
 */
@Transactional(propagation = Propagation.REQUIRED)
public class AllPlayerStatusDAOImpl extends JdbcDaoSupport implements Serializable, AllPlayerStatusDAO {

    /**
     * Logger, used to record diagnostic information
     */
    private static final Logger LOGGER = Logger.getLogger(AllPlayerStatusDAOImpl.class);

    /**
     * Query string for finding the logged in users.
     */
    private static final String GET_LOGGED_IN = "SELECT * FROM all_player_status where quit is null";

    /**
     * Query string for finding the number of visits to the server
     */
    private static final String GET_SERVER_VISITS =
            "select d.everyday as log_time, count(distinct p.screen_name) as count " +
                    "from one_day as d left join all_player_status as p " +
                    "on (p.login > d.everyday AND p.login < date_add(d.everyday,interval 1 hour)) " +
                    "OR (d.everyday BETWEEN p.login AND p.quit) " +
                    "OR (d.everyday > p.login AND p.quit IS NULL) group by d.everyday;";

    /**
     * Execution string for creating a temporary table used in determining the number of visits
     * to the server.
     */
    private static final String CREATE_TEMP_TABLE =
            "create temporary table IF NOT EXISTS `one_day` as " +
                    "select @rownum:=@rownum+1 as row, " +
                    "now() - interval (@rownum-1) hour - interval minute(now()) minute - interval second(now()) second everyday " +
                    "from (select 0 union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) t, " +
                    "(select 0 union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) t2, " +
                    "(select 0 union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) t3, " +
                    "(SELECT @rownum:=0) r WHERE @rownum < 24;";

    /**
     * Perform a query to find the players that are currently logged.
     *
     * @return List of the player that are currently logged in.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AllPlayerStatus> getLoggedIn() {

        LOGGER.debug("Get Logged In Users");
        PreparedStatementCreator psc =
                new PreparedStatementCreatorFactory(GET_LOGGED_IN).newPreparedStatementCreator(new Object[0]);

        List<AllPlayerStatus> playerStatuses;
        playerStatuses = getJdbcTemplate().query(psc, new DefaultRowMapper<AllPlayerStatus>(AllPlayerStatusImpl.class));
        return playerStatuses;
    }

    /**
     * Perform a query that determines the number of logged in users to the
     * server at regular time intervals.
     *
     * @return list of users logged into the server at time intervals.
     */
    @Override
    @Transactional(readOnly = false)
    public List<ServerVisit> getServerVistits() {
        LOGGER.debug("Find number of server visits.");

        PreparedStatementCreator psc =
                new PreparedStatementCreatorFactory(GET_SERVER_VISITS).newPreparedStatementCreator(new Object[0]);

        List<ServerVisit> serverVisits;

        getJdbcTemplate().execute(CREATE_TEMP_TABLE);
        serverVisits = getJdbcTemplate().query(psc, new DefaultRowMapper<ServerVisit>(ServerVisitImpl.class));
        return serverVisits;
    }


}
