package simulation;

@SuppressWarnings("rawtypes")
public class Event implements Comparable{
	public static int GOODARRIVAL = 1;
	public static int BADARRIVAL = 2;
	public static int GOODABANDON = 3;
	public static int BADABANDON = 4;
	private double _eventTime;
	private int _eventType;
	private long _appId;
	
	public Event(int eventType, double eventTime, long appId){
		_eventTime=eventTime;
		_eventType=eventType;
		_appId=appId;
	}
	
	public double getTime(){
		return _eventTime; 
	}
	
	public int getType(){
		return _eventType;
	}
	
	public long getAppId(){
		return _appId;
	}
	
	public int compareTo (Object obj){
        Event e = (Event) obj;
        if (_eventTime < e._eventTime) {
            return -1;
        }
        else if (_eventTime > e._eventTime) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public boolean equals (Object obj){
        return (compareTo(obj) == 0);
    }
}
