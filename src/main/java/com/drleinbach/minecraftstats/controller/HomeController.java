package com.drleinbach.minecraftstats.controller;

import com.drleinbach.minecraftstats.dao.AllPlayerStatusDAO;
import com.drleinbach.model.AllPlayerStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Primary controller for the website. Dispatches the
 * home page to the user visiting the website.
 * <p/>
 * Created: 4/20/13
 *
 * @author Daniel
 */
@Controller
@RequestMapping("/")
public class HomeController {

    /**
     * Logger, used to record diagnostic information
     */
    private static final Logger LOGGER = Logger.getLogger(HomeController.class);

    /**
     * Data access object used to get users stats.
     * This object is autowired in by the spring framework.
     */
    @Autowired
    private AllPlayerStatusDAO statsDAO;

    /**
     * Handles a standard GET request to the root of the website,
     * returning the index and the list of logged in users.
     *
     * @return - Model and view with the list of logged in users and
     *         the location of the index.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHome() {
        List<AllPlayerStatus> players = statsDAO.getLoggedIn();
        System.out.println("Result of Query: " + players.size());
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i));
        }
        return new ModelAndView("index", "list", players);
    }
}
