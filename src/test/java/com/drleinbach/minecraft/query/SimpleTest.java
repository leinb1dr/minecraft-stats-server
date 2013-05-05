package com.drleinbach.minecraft.query;

import org.apache.log4j.Logger;

/**
 * Created: 5/3/13
 *
 * @author Daniel
 */
public class SimpleTest {


    private static final Logger LOGGER = Logger.getLogger(SimpleTest.class);

//    @Test
    public void test(){
        System.out.println("2013-04-27 01:00:00".equals("2013-04-27 02:00:00"));
        System.out.println("2013-04-27 01:00:00".contentEquals("2013-04-27 02:00:00"));
    }

}
