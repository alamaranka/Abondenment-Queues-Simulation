package simulation;

import java.util.*;

public class Simulator {
	double simulationPeriod;
	double arrivalGood;
	double abandonmentGood;
	double arrivalBad;
	double abandonmentBad;
	double service;
	int capacity;
	public double _standardDeviation;
	private double _totalWaitingTime;
	private double _numberGoodIn; 
	private double _numberBadIn; 
	private double _numberIn; public double _numberInOutput = 0;
	private double _numberOut; public double _numberOutOutput = 0;
	private double _numberBulked; public double _numberBulkedOutput = 0;
	private double _numberGoodAbandoned; public double _numberGoodAbandonedOutput = 0;
	private double _numberBadAbandoned; public double _numberBadAbandonedOutput = 0;
	private double _goodAbandonmentRate; public double _goodAbandonmentRateOutput = 0;
	private double _badAbandonmentRate; public double _badAbandonmentRateOutput = 0;
	private double _averageWaiting; public double _averageWaitingOutput = 0;
	private int _maxNumberInSystem; public int _maxNumberInSystemOutput = 0;
	private static ArrayList<Event> _eventList;
	private ArrayList<Applicant> _system;
	private double _clock;
	public double _runTime;
	private Random _rand = new Random();
	private ArrayList<Applicant> _scheduledArrivers;
	private boolean _clockUpdated;
	
	public Simulator(double simulaitonPeriod, int capacity, double arrivalGood, double abandonmentGood, double arrivalBad, double abandonmentBad, double service){
		this.simulationPeriod=simulaitonPeriod;
		this.capacity=capacity;
		this.arrivalGood=arrivalGood;		
		this.abandonmentGood=abandonmentGood;
		this.arrivalBad=arrivalBad;
		this.abandonmentBad=abandonmentBad;
		this.service=service;
		_runTime = 0;
	}
	
	public double expo(double gamma){
        return (1.0 / gamma) * (-Math.log(1.0 - _rand.nextDouble()));
    }
	
	public boolean isSystemEmpty(){
		if(_system.size()==0){return true;}
		return false;
	}
	
	public boolean isSystemFull(){
		if(_system.size()==capacity){return true;}
		return false;
	}
	
	public ArrayList<Double> getServiceRemaining(){
		ArrayList<Double> r = new ArrayList<Double>();
		for(int i=0; i<_system.size(); i++){
			r.add(_system.get(i).getRemaining());
		}	
		return r;
	}
	
	public ArrayList<Integer> getEventType(){
		ArrayList<Integer> r = new ArrayList<Integer>();
		for(int i=0; i<_eventList.size(); i++){
			r.add(_eventList.get(i).getType());
		}	
		return r;
	}
	
	public void updateRemaining(double time){
		for(int i=0; i<_system.size(); i++){
			double current = _system.get(i).getRemaining();
			_system.get(i).setRemaining(current-time);
		}
	}
	
	public int getIndexOfArriver(Long id){
		for(int i=0; i<_scheduledArrivers.size(); i++){
			if(id==_scheduledArrivers.get(i)._id){
				return i;
			}
		}
		return -1;
	}
	
	public int getIndexOfEvent(Long id){
		for(int i=0; i<_eventList.size(); i++){
			if(id==_eventList.get(i).getAppId()){
				return i;
			}
		}
		return -1;
	}
	
	public void printResults(){
		System.out.println("Number of Arrivals: " + _numberInOutput);
		System.out.println("Number of Departures: " + _numberOutOutput);
		System.out.println("Number of Good Abandoned: " + _numberGoodAbandonedOutput);
		System.out.println("Good Abandonment Rate: " + _goodAbandonmentRateOutput);
		System.out.println("Number of Bad Abandoned: " + _numberBadAbandonedOutput);
		System.out.println("Bad Abandonment Rate: " + _badAbandonmentRateOutput);
		System.out.println("Number of Bulked: " + _numberBulkedOutput);
		System.out.println("Max Number In System: " + _maxNumberInSystemOutput);
		System.out.println("Average Waiting Time: " + _averageWaitingOutput);
	}
	
	public void scheduleAgain(){
		if(!getEventType().contains(Event.GOODARRIVAL)){scheduleGood();}
		if(!getEventType().contains(Event.BADARRIVAL)){scheduleBad();}
	}
	
	public void maxNumberInSet(int number){
		if(number>_maxNumberInSystem){
			_maxNumberInSystem = number;
		}
	}
	
	public void setup(){
		_system = new ArrayList<Applicant>();
		_eventList = new ArrayList<Event>();
		_scheduledArrivers = new ArrayList<Applicant>();
		_clock = 0.0; _totalWaitingTime = 0.0; _averageWaiting = 0;
		_numberGoodIn = _numberBadIn = _numberOut = 0; _numberBulked = 0;
		_numberGoodAbandoned = 0; _numberBadAbandoned = 0; _maxNumberInSystem = 0;
		_goodAbandonmentRate = 0; _badAbandonmentRate = 0; _numberIn = 0;
		scheduleGood();
		scheduleBad();
	}
	
	@SuppressWarnings("unchecked")
	public void scheduleGood(){
		//System.out.println("I am scheduleGood");
		Applicant goodArriver; 
		double goodArrivalTime = _clock + expo(arrivalGood);
		double goodAbandonmentTime = goodArrivalTime + expo(abandonmentGood); 
		goodArriver = new Applicant("good", goodAbandonmentTime, goodArrivalTime);
		_scheduledArrivers.add(goodArriver);
		_eventList.add(new Event(Event.GOODARRIVAL, goodArrivalTime, goodArriver._id));
		_eventList.add(new Event(Event.GOODABANDON, goodAbandonmentTime, goodArriver._id));
		Collections.sort(_eventList);
	}
	
	@SuppressWarnings("unchecked")
	public void scheduleBad(){
		//System.out.println("I am scheduleBad");
		Applicant badArriver;
		double badArrivalTime = _clock + expo(arrivalBad);
		double badAbandonmentTime = badArrivalTime + expo(abandonmentBad);
		badArriver = new Applicant("bad", badAbandonmentTime, badArrivalTime);
		_scheduledArrivers.add(badArriver);
		_eventList.add(new Event(Event.BADARRIVAL, badArrivalTime, badArriver._id));
		_eventList.add(new Event(Event.BADABANDON, badAbandonmentTime, badArriver._id));
		Collections.sort(_eventList);
	}
	
	public void handleArrival(Event e){
		//System.out.println("I am handleArrival");
		if(e.getType()==Event.GOODARRIVAL){_numberGoodIn++;}
		else{_numberBadIn++;}
		Long id = e.getAppId();
		_scheduledArrivers.get(getIndexOfArriver(id)).setRemaining(expo(service));
		_system.add(_scheduledArrivers.get(getIndexOfArriver(id))); 
		maxNumberInSet(_system.size());
		_eventList.remove(e);
		scheduleAgain();
	}
		
	public void handleAbandonment(Event e){
		//System.out.println("I am handleAbandonment");
		_numberOut++;
		if(e.getType()==Event.GOODABANDON){
			_numberGoodAbandoned++;
		}
		else{
			_numberBadAbandoned++;
		}
		long id = e.getAppId(); int index = 0;
		for(int i=0; i<_system.size(); i++){
			if(_system.get(i)._id==id){index=i; break;}
		}
		_totalWaitingTime += _clock - _system.get(index).getArrival();
		_system.remove(index); 
		_eventList.remove(e);
		scheduleAgain();
	}
	
	public void handleServiceCompletion(Event e){
		//System.out.println("I am service");
		double min; double minService; int indexService; Long id; int indexEvent;
		min = Collections.min(getServiceRemaining()); 
		minService = (min * _system.size()) + _clock;
		indexService = getServiceRemaining().indexOf(min);
		if(minService < e.getTime()){
			_clock = minService; 
			_numberOut++;
			_clockUpdated = true;
			_totalWaitingTime += _clock - _system.get(indexService).getArrival();
			id = _system.get(indexService)._id;
			indexEvent = getIndexOfEvent(id);
			if(indexEvent!=-1){
				_eventList.remove(indexEvent);
			}
			_system.remove(indexService);
			if(!isSystemEmpty()){updateRemaining(min);}
			scheduleAgain();
		}else{
			if(!isSystemEmpty()){updateRemaining((e.getTime()-_clock)/_system.size());}
		}
	}
	
	public void simulate(){
		setup(); 
		while(_clock <= simulationPeriod){
			System.out.println(_clock+"\t"+_numberGoodAbandoned/(_numberGoodIn+1)+"\t"+_numberBadAbandoned/(_numberBadIn+1));
			_clockUpdated = false;
			Event e = _eventList.get(0); 
			if(!isSystemEmpty()){
				handleServiceCompletion(e);
			}
			if(!_clockUpdated){
				_clock = e.getTime();
				if (e.getType() == Event.GOODARRIVAL || e.getType() == Event.BADARRIVAL){
					if(!isSystemFull()){
						handleArrival(e);
					}else{
						_numberBulked++; 
						_eventList.remove(e);
						Long id = e.getAppId();
						int indexEvent = getIndexOfEvent(id);
						_eventList.remove(indexEvent);
						scheduleAgain();
					}
				}
				else{
					if(!isSystemEmpty()){handleAbandonment(e);}
				}	
			}
		}
		_averageWaiting = _totalWaitingTime / _numberOut;
		_numberIn = _numberGoodIn + _numberBadIn;
		_goodAbandonmentRate = _numberGoodAbandoned / _numberGoodIn;
		_badAbandonmentRate = _numberBadAbandoned / _numberBadIn;
	}
	
	public void run(int n){
		Long begins = System.currentTimeMillis();
		ArrayList<Double> avgs = new ArrayList<Double>();
		for(int i=0; i<n; i++){
			simulate();	
			_numberInOutput+=_numberIn;
			_numberOutOutput+=_numberOut;
			_numberGoodAbandonedOutput+=_numberGoodAbandoned;
			_goodAbandonmentRateOutput+=_goodAbandonmentRate;
			_numberBadAbandonedOutput+=_numberBadAbandoned;
			_badAbandonmentRateOutput+=_badAbandonmentRate;
			_numberBulkedOutput+=_numberBulked;
			_maxNumberInSystemOutput+=_maxNumberInSystem;
			_averageWaitingOutput+=_averageWaiting;
			avgs.add(_averageWaiting);
		}
		_numberInOutput/=n; _numberOutOutput/=n; _numberGoodAbandonedOutput/=n; _goodAbandonmentRateOutput/=n;
		_numberBadAbandonedOutput/=n; _badAbandonmentRateOutput/=n; _numberBulkedOutput/=n;
		_averageWaitingOutput/=n; _maxNumberInSystemOutput/=n;
		_standardDeviation=0;
		for(int i=0; i<avgs.size(); i++){
			_standardDeviation+=Math.pow(_averageWaitingOutput-avgs.get(i),2);
		}
		_standardDeviation = Math.sqrt(_standardDeviation);
		_standardDeviation/=n; _standardDeviation = 1.96 * _standardDeviation / Math.sqrt(n);
		Long ends = System.currentTimeMillis();
		_runTime = (ends-begins)/1000.0;
	}
}
