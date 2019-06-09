package baseline;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

import simulation.Simulator;

public class GUI {

	private JFrame _window;
	private JPanel _panel;
	private ArrayList<JLabel> _labelList;
	private Simulator _simulator;
	
	public GUI(Simulator simulator){
		_labelList = new ArrayList<JLabel>();
		_simulator=simulator;
	}
	
	public void run(){
		_window = new JFrame("Processor Sharing");
		_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_window.getContentPane().setLayout(new FlowLayout());
		_window.setFocusable(true);
		_window.setFocusTraversalKeysEnabled(false);
		
		_panel = new JPanel();
		_panel.setLayout(new GridLayout(10, 1));
		
		for(int i=0; i<20; i++){
			_labelList.add(new JLabel());
			_panel.add(_labelList.get(i));
		}
		setLabelText();
		
		_window.add(_panel);
		_window.pack();
		_window.setVisible(true);		
	}
	
	public void setLabelText(){
		_labelList.get(0).setText("Number of Arrivals"+"\t"+": "+Math.round(_simulator._numberInOutput));
		_labelList.get(1).setText("Number of Departures"+"\t"+":"+Math.round(_simulator._numberOutOutput));
		_labelList.get(2).setText("Number of Good Abandoned"+"\t"+":"+Math.round(_simulator._numberGoodAbandonedOutput));
		_labelList.get(3).setText("Good Abandonment Rate"+"\t"+":"+Math.round(_simulator._goodAbandonmentRateOutput*10000.0)/10000.0);
		_labelList.get(4).setText("Number of Bad Abandoned"+"\t"+":"+Math.round(_simulator._numberBadAbandonedOutput));
		_labelList.get(5).setText("Bad Abandonment Rate"+"\t"+":"+Math.round(_simulator._badAbandonmentRateOutput*10000.0)/10000.0);
		_labelList.get(6).setText("Number of Bulked"+"\t"+":"+Math.round(_simulator._numberBulkedOutput));
		_labelList.get(7).setText("Max Number In System"+"\t"+":"+_simulator._maxNumberInSystemOutput);
		_labelList.get(8).setText("Average Waiting Time"+"\t"+":"+Math.round(_simulator._averageWaitingOutput*10000.0)/10000.0);
		_labelList.get(9).setText("Standard Deviation"+"\t"+":"+Math.round(_simulator._standardDeviation*10000.0)/10000.0);
		_labelList.get(10).setText("Run Time"+"\t"+":"+_simulator._runTime);
	}

}
