package org.bulatnig.smpp.net.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.testutil.UniquePortGenerator;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * TcpConnection test.
 *
 * @author Bulat Nigmatullin
 */
public class TcpConnectionTest {

    @Test(expected = IOException.class)
    public void targetPortClosedTest() throws IOException {
        Connection conn = new TcpConnection(new InetSocketAddress("localhost", UniquePortGenerator.generate()));
        conn.open();
    }

    @Test(expected = IOException.class)
    public void unknownHostnameTest() throws IOException {
        Connection conn = new TcpConnection(new InetSocketAddress("noname.com", UniquePortGenerator.generate()));
        conn.open();
    }

    @Test(expected = IOException.class)
    public void targetIPUnreachableTest() throws IOException {
        Connection conn = new TcpConnection(new InetSocketAddress("1.0.0.0", UniquePortGenerator.generate()));
        conn.open();
    }

}
