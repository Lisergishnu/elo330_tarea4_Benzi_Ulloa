/**
 * Generates a one-way delay for UDP packets. Has a shared memory between threads for easier datagram managment.
 * @author mbenzit
 *
 */
import java.io.IOException;
import java.net.*;

public class Delayer {

	private int BUFFER_SIZE = 50;
	private int BUFFER_LENGTH = 1024;
	
	private String local_host;
	private int local_port;
	private String remote_host;
	private int remote_port;
	private int delay_avg;
	private int delay_variation;
	private int loss_percent;
	protected byte[][] circularBuffer;
	protected int sIndex, rIndex;
	protected DatagramSocket socket;
	
	private String sync = new String();
	
	private Thread sender;
	private Thread reciever;

	public Delayer(String local_host, int local_port, String remote_host,
			int remote_port, int delay_avg, int delay_variation,
			int loss_percent) throws SocketException  {
				this.local_host = local_host;
				this.local_port = local_port;
				this.remote_host = remote_host;
				this.remote_port = remote_port;
				this.delay_avg = delay_avg;
				this.delay_variation = delay_variation;
				this.loss_percent = loss_percent;
				
			socket = new DatagramSocket(local_port);
			circularBuffer = new byte[BUFFER_SIZE][];
			SenderThread s = new SenderThread();
			RecieverThread r = new RecieverThread();
			sIndex = 0;
			rIndex = 0;
			sender = new Thread(s);
			sender.start();
			reciever = new Thread(r);
			reciever.start();

			System.out.println("Listening port: " + local_port + " Dest port: " + remote_port);
	}

	/**
	 * Wrapper around both threads join(). Should wait until both threads are over.
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException {
		sender.join();
		reciever.join();
	}
	

	/* Hilo encargado del envio de datagramas */
	class SenderThread implements Runnable {
		private DatagramPacket packet = null;		
		private boolean ready = false;
		
		@Override
		public void run() {
			while(true){
				sendPacket();
			}
		}
		private void sendPacket(){
	        try {
	        	/* Si el packete en cola es nulo, esperar la llegada de un paquete nuevo,
	        	 * el hilo receptor generara la notificacion */
	        	synchronized(sync){
	        		if(circularBuffer[sIndex] == null){
	        			sync.wait();
						System.out.println("Delaying packet: " + new String(circularBuffer[sIndex]));
	        		}
	        		if(circularBuffer[sIndex] != null)
	        			ready = true;
	        	}
	        	
	        	/* Agregar delay solo cuando haya un paquete disponible,
	        	 * y hacerlo fuera de SYNC */
	        	if(ready){
	        		// TODO: Random Sleep time con jitter y loss percent
        			Thread.sleep(1000);
		        	ready = false;
			        try {
			        	synchronized(sync){
			        		System.out.println("Sending packet: " + new String(circularBuffer[sIndex]));
							packet = new DatagramPacket(circularBuffer[sIndex], BUFFER_LENGTH, InetAddress.getByName(remote_host), remote_port);
							socket.send(packet);
							circularBuffer[sIndex] = null;
					        sIndex = (sIndex+1) % BUFFER_SIZE;
			        	}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* Hilo encargado de la recepcion de datagramas */
	class RecieverThread implements Runnable {
		private DatagramPacket packet = null;		
		
		@Override
		public void run() {
			while(true){
				recvPacket();
			}
		}
		private void recvPacket(){
	        try {
	        	byte[] tempBuffer = new byte[BUFFER_LENGTH];
				packet = new DatagramPacket(tempBuffer, BUFFER_LENGTH);
				
				/* Lectura en circular Buffer y sIndex SYNC */
	        	synchronized(sync){
	        		if(circularBuffer[rIndex] != null) {
		        		System.out.println("Buffer override");
		        		sIndex = (sIndex+1) % BUFFER_SIZE;
		        	}
	        	}
	        	
	        	/* socket.receive no debe ir dentro de SYNC, 
	        	 * ya que por si mismo genera un estado de espera */
		        socket.receive(packet);
		        
		        /* Cuando llega un paquete nuevo, notificar */
				synchronized(sync){
			        circularBuffer[rIndex] = tempBuffer;
    		        rIndex = (rIndex+1) % BUFFER_SIZE;
			        sync.notify();
				}
		             
		        System.out.println("Receiving packet: " + new String(tempBuffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}