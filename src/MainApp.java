import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Entry point to erp_udp application. Takes argument inputs and prepares threads for delay as expected.
 * @author mbenzit
 *
 */
public class MainApp {

	public static void main(String[] args) {
		int delay_avg, delay_variation, loss_percent, local_port, remote_port;
		String remote_host;
		if (args.length != 5 && args.length != 6) {
			System.out.println("Usage: erp_udp [delay_avg] [delay_variation] [loss_percent] "
					+ "[local_port] (remote_host) [remote_port]");
			System.exit(0);
		}
		delay_avg = Integer.parseInt(args[0]);
		delay_variation = Integer.parseInt(args[1]);
		loss_percent = Integer.parseInt(args[2]);
		local_port = Integer.parseInt(args[3]);
		if (args.length == 5) {
			remote_host = "127.0.0.1";
			remote_port = Integer.parseInt(args[4]);
		} else {
			remote_host = args[4];
			remote_port = Integer.parseInt(args[5]);
		}
		//Generate delayers in both ways (two instances of the same object)
		Delayer outward;
		Delayer inward;
		
		/* Try catch para socketException y el thread join*/
		try {
			outward = new Delayer("127.0.0.1",local_port,remote_host,remote_port,delay_avg,delay_variation,loss_percent);
			inward = new Delayer(remote_host,remote_port+1,"127.0.0.1",local_port+3,delay_avg,delay_variation,loss_percent);
			outward.join();
			inward.join();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
