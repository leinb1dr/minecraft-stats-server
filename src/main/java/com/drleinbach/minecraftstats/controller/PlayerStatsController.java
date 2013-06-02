package com.drleinbach.minecraftstats.controller;

import com.drleinbach.minecraftstats.beans.FullStats;
import com.drleinbach.minecraftstats.beans.ServerVisit;
import com.drleinbach.minecraftstats.dao.AllPlayerStatusDAO;
import com.drleinbach.model.AllPlayerStatus;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Properties;

/**
 * This controller is used to handle requests for data
 * concerning users and their logged in status.
 * <p/>
 * Created: 5/3/13
 *
 * @author Daniel
 */
@Controller
@RequestMapping("/stats")
public class PlayerStatsController {

    private static final Logger LOGGER = Logger.getLogger(PlayerStatsController.class);
    private static final Properties PROPERTIES = new Properties();
    private static FullStats stats;
    private static String handshake;

    static {
        try {
            PROPERTIES.load(new ClassPathResource("minecraft.properties").getInputStream());
        } catch (Exception e) {

        }
    }


    /**
     * Data access object used to get users stats.
     * This object is autowired in by the spring framework.
     */
    @Autowired
    private AllPlayerStatusDAO statsDAO;

    @RequestMapping(value = "/logged-in")
    @ResponseBody
    public Object getLoggedIn() {
        LOGGER.info("Start getLoggedIn");

        List<AllPlayerStatus> players = statsDAO.getLoggedIn();
        LOGGER.info("Got Logged In Users");

        return players;
    }

    /**
     * Handles a POST request to the get the frequency of logged in users.
     *
     * @return A list containing datetime ranges and logged in users for that
     *         time range.
     */
    @RequestMapping(value = "/frequency", method = RequestMethod.POST)
    @ResponseBody
    public Object getFrequency() {
        try {
            List<ServerVisit> visits = statsDAO.getServerVistits();
            LOGGER.debug(visits.size());
            return visits;
        } catch (Exception e) {
            LOGGER.error("Exception has occurred", e);
        }
        return null;
    }

    @RequestMapping(value = "/sse", method = RequestMethod.GET)
    public void serverSentEvent(HttpServletResponse response) {
        try {
            boolean changed = false;
            LOGGER.debug("Start event loop");
            while (!changed) {
                Object tmp = getActiveStatus();
                LOGGER.debug("Active Status: " + tmp);
                if (tmp instanceof FullStats) {
                    LOGGER.debug("Status is FullStats");
                    FullStats newStats = (FullStats) tmp;

                    if (stats == null || !stats.equals(newStats)) {
                        LOGGER.debug("Stats have changed");
                        stats = newStats;

                        ObjectMapper om = new ObjectMapper();
                        PrintWriter writer = response.getWriter();


                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/event-stream");
                        response.setHeader("Cache-Control", "no-cache");

                        writer.write("event:'running'");
                        writer.write("data:" + om.writeValueAsString(stats) + "\n");
                        writer.write("event:'logged-in'");
                        writer.write("data:" + om.writeValueAsString(getLoggedIn()) + "\n");
                        writer.write("event:'frequency'");
                        writer.write("data:" + om.writeValueAsString(getFrequency()) + "\n\n");

                        writer.flush();
                        writer.close();

                        changed = true;
                    } else {
                        LOGGER.debug("Stats were not new");
                        Thread.sleep(10000);
                    }

                }
            }
        } catch (Exception e) {
            LOGGER.debug("Error occured", e);
        }
    }

    /**
     * Handles a POST request to the get the active status of the server.
     *
     * @return true if the server is up, false if the server is stopped.
     */
    @RequestMapping(value = "/running", method = RequestMethod.POST)
    @ResponseBody
    public Object getActiveStatus() {


        try {

            boolean changed = false;

            String message = getHandshake();
            LOGGER.debug("Handshake Dump: " + message);

            LOGGER.debug("Send handshake");
            final byte[] challengeResponse = getResponse(message);
            LOGGER.debug("Recieve challenge");
            final String challenge = Integer.toHexString(Integer.parseInt(new String(challengeResponse).trim()));

            message = getStatsQuery(challenge);
            final byte[] query = getResponse(message);
            FullStats tmpStats = FullStats.getFullStats(new String(query).trim().split("\0"));

            return tmpStats;

        } catch (SocketTimeoutException e) {
            LOGGER.error("Socket timed out");
        } catch (Exception e) {
            LOGGER.error("Exception has occurred", e);
        }

        return false;
    }

    private String getHandshake() {
        if (handshake == null) {
            handshake = PROPERTIES.getProperty("server.query.magic")
                    + PROPERTIES.getProperty("server.query.hand-shake")
                    + PROPERTIES.getProperty("server.query.session-id");
        }
        return handshake;
    }

    private String getStatsQuery(String challenge) {
        return PROPERTIES.getProperty("server.query.magic")
                + PROPERTIES.getProperty("server.query.request")
                + PROPERTIES.getProperty("server.query.session-id")
                + "00" + challenge + "00000000";
    }

    private byte[] getResponse(final String message) throws IOException {
        final Integer socket = (int) (Math.random() * 1000 + 9000);
        final DatagramSocket serverSocket = new DatagramSocket(socket);
        serverSocket.setSoTimeout(10);

        final HexBinaryAdapter adapter = new HexBinaryAdapter();

        final byte[] receiveData = new byte[512];
        final byte[] sendData = adapter.unmarshal(message);

        final InetAddress mcServer = InetAddress.getByName(PROPERTIES.getProperty("server.query.ip"));

        final DatagramPacket request = new DatagramPacket(sendData, sendData.length, mcServer, 25565);
        final DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);

        serverSocket.send(request);
        serverSocket.receive(response);

        serverSocket.close();

        return response.getData();

    }

}
