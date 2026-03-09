package com.intern.hub.ticket.app;

import com.intern.hub.library.common.utils.Snowflake;

public class TestSnowflake {
    public static void main(String[] args) {
        try {
            Snowflake snowflake = new Snowflake(1, 1);
            System.out.println("Snowflake success: " + snowflake.next());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
