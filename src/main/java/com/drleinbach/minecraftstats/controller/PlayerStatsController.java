package com.drleinbach.minecraftstats.controller;

import com.drleinbach.minecraftstats.beans.ServerVisit;
import com.drleinbach.minecraftstats.dao.AllPlayerStatusDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;

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

    /**
     * Data access object used to get users stats.
     * This object is autowired in by the spring framework.
     */
    @Autowired
    private AllPlayerStatusDAO statsDAO;

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
//        return null;
    }

    /**
     * Handles a POST request to the get the active status of the server.
     *
     * @return true if the server is up, false if the server is stopped.
     */
    @RequestMapping(value = "/running", method = RequestMethod.POST)
    @ResponseBody
    public Object getActiveStatus() {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(9876);
            serverSocket.setSoTimeout(10);
            byte[] receiveData = new byte[512];
            final HexBinaryAdapter adapter = new HexBinaryAdapter();


            final String magic = "FEFD";
            final String type_hs = "09";
            final String session_id = "00000001";

            final String tmp = magic + type_hs + session_id;
            LOGGER.debug("Handshake Dump: " + tmp);
            final byte[] sendData = adapter.unmarshal(tmp);

            final InetAddress mcServer = InetAddress.getByName("localhost");

            LOGGER.debug("Send handshake");
            final DatagramPacket handshake = new DatagramPacket(sendData, sendData.length, mcServer, 25565);
            LOGGER.debug("Recieve challenge");

            final DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);


            serverSocket.send(handshake);
            serverSocket.receive(response);

            return true;

        } catch (SocketTimeoutException e) {
            LOGGER.error("Socket timed out");
        } catch (Exception e) {
            LOGGER.error("Exception has occurred", e);
        } finally {
            if(serverSocket != null)
                serverSocket.close();
        }

        return false;
    }

}
