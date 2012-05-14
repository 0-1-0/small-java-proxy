package util;

/**
 * Class that represents total value of client's upload & download in bytes
 * @author Николай
 */
public class Traffic{
	private long u;
	private long d;
	
	public Traffic(long uploaded, long downloaded){
		u = uploaded;
		d = downloaded;
	}
	
	public void add(long u, long d){
		this.u += u;
		this.d += d;
	}
	
	public void add(Traffic t){
		this.d += t.getD();
		this.u += t.getU();
	}
	
	public long getU(){
		return u;
	}
	
	public long getD(){
		return d;
	}
}