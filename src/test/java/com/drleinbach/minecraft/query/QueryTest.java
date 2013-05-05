package com.drleinbach.minecraft.query;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created: 5/3/13
 *
 * @author Daniel
 */
public class QueryTest {

    private static final Logger LOGGER = Logger.getLogger(QueryTest.class);

    //    @Test
    public void testQuery() throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        HexBinaryAdapter adapter = new HexBinaryAdapter();


        String magic = "FEFD";
        String type_hs = "09";
        String type_none = "00";
        String session_id = "00000001";
        String challenge;

        String tmp = magic + type_hs + session_id;
        LOGGER.debug("Handshake Dump: " + tmp);
        byte[] sendData = adapter.unmarshal(tmp);

        InetAddress mcServer = InetAddress.getByName("192.168.1.101");

        LOGGER.debug("Send handshake");
        DatagramPacket handshake = new DatagramPacket(sendData, sendData.length, mcServer, 25565);
        LOGGER.debug("Recieve challenge");
        DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);

        serverSocket.send(handshake);
        Long time = System.currentTimeMillis();
        serverSocket.receive(response);
        LOGGER.debug("Recieve Duration: " + (System.currentTimeMillis() - time));


        tmp = adapter.marshal(response.getData());
        LOGGER.debug("Response Dump: " + tmp);
        challenge = Integer.toHexString(Integer.parseInt(new String(response.getData()).trim()));
        LOGGER.debug("Challenge Dump: " + challenge);

        tmp = magic + type_none + session_id + "00" + challenge + "00000000";
        sendData = adapter.unmarshal(tmp);
        LOGGER.debug("Query Dump: " + tmp);

        DatagramPacket query = new DatagramPacket(sendData, sendData.length, mcServer, 25565);


        receiveData = new byte[1024];
        response = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.send(query);
        serverSocket.receive(response);

        System.out.println(Arrays.toString(new String(response.getData()).trim().split("\0")));


//
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        serverSocket.receive(receivePacket);
//        String sentence = new String(receivePacket.getData());
//        System.out.println("RECEIVED: " + sentence);
//        InetAddress IPAddress = receivePacket.getAddress();
//        int port = receivePacket.getPort();
//        String capitalizedSentence = sentence.toUpperCase();
//        sendData = capitalizedSentence.getBytes();
//        DatagramPacket sendPacket =
//                new DatagramPacket(sendData, sendData.length, IPAddress, port);
//        serverSocket.send(sendPacket);


    }

}
