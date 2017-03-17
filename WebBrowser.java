import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static javafx.concurrent.Worker.State.FAILED;
 //Let the fun begin
public class WebBrowser extends JFrame {
    //Check if anyone is near the window
	private final JFXPanel jfxPanel = new JFXPanel();
	private WebEngine engine;
	int index=0,valid,curr=0;
	private final JPanel panel = new JPanel(new BorderLayout());
	private final JLabel lblStatus = new JLabel();
	//You didn't check
	//No one listens to me:(
	static ArrayList<String> history=new ArrayList<String>();
	private final JButton btnGo = new JButton("Go");
	private final JButton btnForward = new JButton("Forward");
	private final JButton btnBack = new JButton("Back");
	private final JTextField txtURL = new JTextField();
	private final JProgressBar progressBar = new JProgressBar();
	String temp;
    
	public WebBrowser() {
    	super();
    	initComponents();
	}

	//Only me and god know what i'm trying to do
	private void initComponents() {
    	createScene();
 
    	ActionListener al = new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
       		 valid=1;
   			 btnBack.setEnabled(true);
   			 btnForward.setEnabled(false);
       		 history.add(txtURL.getText());
       		 index++;
       		 curr=index;
            	loadURL(txtURL.getText());
        	}
    	};
    	//Okay now only god knows
    	btnGo.addActionListener(al);
    	txtURL.addActionListener(al);
   	 
    	ActionListener back = new ActionListener(){
   		 @Override
   		 public void actionPerformed(ActionEvent e) {
   			 // TODO Auto-generated method stub
   			 valid=0;
   			 btnForward.setEnabled(true);
   			 if(index>0){
   			 --curr;
   			 if(curr==0)
   				 btnBack.setEnabled(false);
   			 loadURL(history.get(curr));
   			 }
   		 }
    	};
   	 
    	ActionListener forw = new ActionListener(){
   		 @Override
   		 public void actionPerformed(ActionEvent e) {
   			 // TODO Auto-generated method stub
   			 valid=0;
   			 btnBack.setEnabled(true);
   			 if(index>0){
   				 ++curr;
   				 if(curr==index)
   					 btnForward.setEnabled(false);
   				 loadURL(history.get(curr));
   			 }
   		 }
    	};
    	btnBack.setEnabled(false);
    	btnForward.setEnabled(false);
    	btnBack.addActionListener(back);
    	btnForward.addActionListener(forw);
    	progressBar.setPreferredSize(new Dimension(150, 18));
    	progressBar.setStringPainted(true);
   	 
    	//Modify the underneath lines to change the layout for the top pane Advita
   	JPanel topBar = new JPanel(new BorderLayout(5, 0));
    	topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    	topBar.add(txtURL, BorderLayout.CENTER);
    	//topBar.add(btnGo, BorderLayout.CENTER);
    	topBar.add(btnBack,BorderLayout.WEST);
    	topBar.add(btnForward,BorderLayout.EAST);
   	 
   	 
 	 
   	/* JPanel topBar = new JPanel(new FlowLayout(5));
    	//topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    	topBar.add(txtURL, JLabel.CENTER);
    	topBar.add(btnGo);
    	topBar.add(btnBack);
    	topBar.add(btnForward);*/
  			 
    	//Modify Bottom pane
    	JPanel statusBar = new JPanel(new BorderLayout(5, 0));
    	statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    	statusBar.add(lblStatus, BorderLayout.CENTER);
    	statusBar.add(progressBar, BorderLayout.EAST);
    	//It's like I  have to tell you everything
    	panel.add(topBar, BorderLayout.NORTH);
    	panel.add(jfxPanel, BorderLayout.CENTER);
    	panel.add(statusBar, BorderLayout.SOUTH);
   	 
    	getContentPane().add(panel);
   	 
    	setPreferredSize(new Dimension(1024, 600));
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	pack();

	}
	//Mess with the code below and you shall die a painful death
	private void createScene() {
 
    	Platform.runLater(new Runnable() {
        	@Override
        	public void run() {
 
            	WebView view = new WebView();
            	engine = view.getEngine();
 
            	engine.titleProperty().addListener(new ChangeListener<String>() {
                	@Override
                	public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                    	SwingUtilities.invokeLater(new Runnable() {
                        	@Override
                        	public void run() {
                            	WebBrowser.this.setTitle(newValue);
                        	}
                    	});
                	}
            	});
 
            	engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
                	@Override
                	public void handle(final WebEvent<String> event) {
                    	SwingUtilities.invokeLater(new Runnable() {
                        	@Override
                        	public void run() {
                            	lblStatus.setText(event.getData());
                        	}
                    	});
                	}
            	});
 
            	engine.locationProperty().addListener(new ChangeListener<String>() {
                	@Override
                	public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
                    	SwingUtilities.invokeLater(new Runnable() {
                        	@Override
                        	public void run() {
                            	txtURL.setText(newValue);
                        	}
                    	});
                	}
            	});
            	//No idea what i'm doing
            	engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                	@Override
                	public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                    	SwingUtilities.invokeLater(new Runnable() {
                        	@Override
                        	public void run() {
                            	progressBar.setValue(newValue.intValue());
                        	}
                    	});
                	}
            	});
            	//I'm so fancy
            	engine.getLoadWorker()
                    	.exceptionProperty()
                    	.addListener(new ChangeListener<Throwable>() {
 
                        	public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                            	if (engine.getLoadWorker().getState() == FAILED) {
                                	SwingUtilities.invokeLater(new Runnable() {
                                    	@Override public void run() {
                                        	JOptionPane.showMessageDialog(
                                                	panel,
                                                	(value != null) ?
                                                	engine.getLocation() + "\n" + value.getMessage() :
                                                	engine.getLocation() + "\nUnexpected error.",
                                                	"Loading error...",
                                                	JOptionPane.ERROR_MESSAGE);
                                       			 history.remove(index--);
                                    	}
                                	});
                            	}
                        	}
                    	});

            	jfxPanel.setScene(new Scene(view));
        	}
    	});
	}
 
	public void loadURL(final String url) {
    	Platform.runLater(new Runnable() {
        	@Override
        	public void run() {
            	String tmp = toURL(url);
 
            	if (tmp == null) {
                	tmp = toURL("http://" + url);
            	}
            	temp=tmp;
            	engine.load(tmp);
        	}
    	});
	}

	private static String toURL(String str) {
    	try {
        	return new URL(str).toExternalForm();
    	} catch (MalformedURLException exception) {
            	return null;
    	}
	}

   
	//Da main and Da end
	public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {

        	public void run() {
            	WebBrowser browser = new WebBrowser();
            	browser.setVisible(true);
            	history.add("http://google.com");
            	browser.loadURL("http://google.com");
       	}	 
   	});
	}
}
