package ru.lets.funlagitog;

import com.ericsson.otp.erlang.OtpAuthException;
import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import com.ericsson.otp.erlang.OtpPeer;
import com.ericsson.otp.erlang.OtpSelf;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author Евгений
 */
public class ConnectionService {

    private static ConnectionService CONNECTION_SERVICE;

    public static ConnectionService getConnectionService() throws IOException {
        if (CONNECTION_SERVICE == null) {
            CONNECTION_SERVICE = new ConnectionService();
        }
        return CONNECTION_SERVICE;
    }

    public ConnectionService() throws IOException {
        OtpNode node = new OtpNode("client");
        node.setCookie(COOKIE);
        mailBox = node.createMbox();
        Thread thread = new Thread(() -> {
            try {
                //TODO
                while (true) {
                    OtpErlangObject message = mailBox.receive();
                    //if stop in tuple - call stop in call controller
                    //if balance in tuple - 
//                    receiveBalance = true;
//                    balanceResult = message; //или элемент тьюпла
                    Thread.sleep(100);
                }
            } catch (OtpErlangDecodeException | OtpErlangExit | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private final static String COOKIE = "cookie";
    private final OtpMbox mailBox;
    private final String nodeName = "nodename@...";
    private final String mailBoxName = "mailbox";
    private final String moduleName = "module";
    private final String functionName = "function";

    public void connect() throws IOException, OtpErlangExit, OtpErlangDecodeException, UnknownHostException, OtpAuthException {
        OtpSelf self = new OtpSelf("javaSelf", COOKIE);
        OtpPeer other = new OtpPeer(nodeName);
        OtpConnection connection = self.connect(other);
        System.out.println(connection.isConnected());
        connection.sendRPC(moduleName, functionName, new OtpErlangObject[]{mailBox.self()});
    }

    private boolean receiveBalance = false;
    private OtpErlangObject balanceResult;

    public int findBalance() throws IOException, OtpErlangExit, OtpErlangDecodeException {
        mailBox.send(mailBoxName, nodeName, new OtpErlangString("balance"));
        while (!receiveBalance);
        int balance = Integer.parseInt(balanceResult.toString());
        return balance;
    }

    public void startCall(String telephone) {
        mailBox.send(mailBoxName, nodeName, new OtpErlangTuple(new OtpErlangObject[]{
            new OtpErlangString("call"), new OtpErlangString(telephone)
        }));

    }

    public void stopCall(String telephone) {
        mailBox.send(mailBoxName, nodeName, new OtpErlangString("stop"));
    }
}
