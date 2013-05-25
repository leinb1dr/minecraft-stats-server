package com.drleinbach.minecraftstats.controller;

import com.drleinbach.minecraftstats.beans.ServerVisit;
import com.drleinbach.minecraftstats.dao.AllPlayerStatusDAO;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created: 5/24/13
 *
 * @author Daniel
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerStatsControllerTest {

    private static final Logger LOGGER = Logger.getLogger(PlayerStatsControllerTest.class);

    @InjectMocks
    private PlayerStatsController controller = new PlayerStatsController();

    @Mock
    private AllPlayerStatusDAO statusDAO;

    @Before
    public void setUp(){
    }


    public void testGetLoggedIn() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    public void testGetFrequencyNormal() throws Exception {
        Mockito.when(statusDAO.getServerVistits()).thenReturn(new ArrayList<ServerVisit>());
        Object o = controller.getFrequency();
        assertTrue(o instanceof ArrayList);
    }

    @Test
    public void testGetFrequencyException() throws Exception {
        Mockito.when(statusDAO.getServerVistits()).thenThrow(new NullPointerException("ERROR"));
        Object o = controller.getFrequency();
        assertNull(o);
    }


    public void testGetActiveStatus() throws Exception {

    }
}
