package com.drleinbach.minecraftstats.dao;

import com.drleinbach.minecraftstats.beans.ServerVisit;
import com.drleinbach.model.AllPlayerStatus;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test to be performed on a local database that ensures the
 * AllPlayerStatusDAOImpl works.
 *
 * Created: 5/5/13
 *
 * @author Daniel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AllPlayerStatusDAOImplTest {

    private static final Logger LOGGER = Logger.getLogger(AllPlayerStatusDAOImplTest.class);

    @Autowired
    private AllPlayerStatusDAO playerStatusDAO;

    @Test
    public void testGetLoggedIn() throws Exception {

        List<AllPlayerStatus> allPlayerStatusList = playerStatusDAO.getLoggedIn();
        assertEquals(1, allPlayerStatusList.size());
        assertEquals("stealthg0d", allPlayerStatusList.get(0).getScreenName());

    }

    @Test
    public void testGetServerVistits() throws Exception {

        List<ServerVisit> serverVisitImpls = playerStatusDAO.getServerVistits();

        assertTrue(serverVisitImpls.size()>0);

        for(ServerVisit serverVisitImpl : serverVisitImpls) {
            assertNotNull(serverVisitImpl.getLogTime());
            assertNotNull(serverVisitImpl.getCount());
            LOGGER.debug(serverVisitImpl.getLogTime() + " : " + serverVisitImpl.getCount());
        }
//        fail("Not Yet Implemented");
    }
}
