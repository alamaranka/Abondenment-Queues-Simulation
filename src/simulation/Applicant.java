package simulation;

import java.util.concurrent.atomic.AtomicLong;


public class Applicant{
	private String _type;
	private double _remaining;
	private double _patience;
	private double _arrivalTime;
	static final AtomicLong NEXT_ID = new AtomicLong(0);
    public final long _id = NEXT_ID.getAndIncrement();
	
	public Applicant(String type, double patience, double arrivalTime){
		_type=type;
		_patience=patience;
		_arrivalTime=arrivalTime;
	}
	
	public String getType(){
		return _type;
	}
	
	public double getRemaining(){
		return _remaining;
	}
	
	public void setRemaining(double remaining){
		_remaining = remaining;
	}
	
	public double getPatience(){
		return _patience;
	}
	
	public double getArrival(){
		return _arrivalTime;
	}
}
