/**
 * Entry point to erp_udp application. Takes argument inputs and prepares threads for delay as expected.
 * @author mbenzit
 *
 */
public class MainApp {

	public static void main(String[] args) {
		int delay_avg, delay_variation, loss_percent, local_port, remote_port;
		String remote_host;
		if (args.length != 6 || args.length != 7) {
			System.out.println("Usage: erp_udp [delay_avg] [delay_variation] [loss_percent] "
					+ "[local_port] (remote_host) [remote_port]");
			System.exit(0);
		}
		delay_avg = Integer.parseInt(args[1]);
		delay_variation = Integer.parseInt(args[2]);
		loss_percent = Integer.parseInt(args[3]);
		local_port = Integer.parseInt(args[4]);
		if (args.length == 6) {
			remote_host = "127.0.0.1";
			remote_port = Integer.parseInt(args[5]);
		} else {
			remote_host = args[5];
			remote_port = Integer.parseInt(args[6]);
		}
		
		//Generate delayers in both ways (two instances of the same object)
		Delayer outward = new Delayer("127.0.0.1",local_port,remote_host,remote_port,delay_avg,delay_variation,loss_percent);
		Delayer inward = new Delayer(remote_host,remote_port,"127.0.0.1",local_port,delay_avg,delay_variation,loss_percent);
		
		try {
			outward.join();
			inward.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
