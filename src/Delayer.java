/**
 * Generates a one-way delay for UDP packets. Has a shared memory between threads for easier datagram managment.
 * @author mbenzit
 *
 */
public class Delayer {

	private String local_host;
	private int local_port;
	private String remote_host;
	private int remote_port;
	private int delay_avg;
	private int delay_variation;
	private int loss_percent;
	
	private Thread sender;
	private Thread reciever;

	public Delayer(String local_host, int local_port, String remote_host,
			int remote_port, int delay_avg, int delay_variation,
			int loss_percent) {
				this.local_host = local_host;
				this.local_port = local_port;
				this.remote_host = remote_host;
				this.remote_port = remote_port;
				this.delay_avg = delay_avg;
				this.delay_variation = delay_variation;
				this.loss_percent = loss_percent;
				
			SenderThread s = new SenderThread();
			RecieverThread r = new RecieverThread();
			sender = new Thread(s);
			sender.start();
			reciever = new Thread(r);
			reciever.start();
	}

	/**
	 * Wrapper around both threads join(). Should wait until both threads are over.
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException {
		sender.join();
		reciever.join();
	}
	
	class SenderThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	
	class RecieverThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
