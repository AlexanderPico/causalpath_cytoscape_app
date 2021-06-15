package org.cytoscape.sample.internal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.DialogTaskManager;
import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;

import javax.swing.*;


public class CreateLegendAction extends AbstractCyAction {

	private static final long serialVersionUID = 1L;
	private CySwingApplication desktopApp;
	private final CytoPanel cytoPanelWest;
	private LegendPanel myControlPanel;
	DialogTaskManager dialogTaskManager = new DialogTaskManager() {
		@Override
		public JDialog getConfiguration(TaskFactory factory, Object tunableContext) {
			return null;
		}

		@Override
		public void setExecutionContext(Window context) {

		}

		@Override
		public void execute(TaskIterator iterator) {

		}
	} ;
	LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
	ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory;
	CyApplicationManager cyApplicationManagerServiceRef;
	public CreateLegendAction(CySwingApplication desktopApp,
							  LegendPanel myCytoPanel, LoadNetworkFileTaskFactory loadNetworkFileTaskFactory, CyApplicationManager cyApplicationManagerServiceRef,
							  ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory){
		super("Control Panel");
		setPreferredMenu("Apps.Samples");

		this.desktopApp = desktopApp;
		this.cyApplicationManagerServiceRef = cyApplicationManagerServiceRef;
		this.applyVisualStyleTaskFactory = applyVisualStyleTaskFactory;
		this.loadNetworkFileTaskFactory=loadNetworkFileTaskFactory;
		//Note: myControlPanel is bean we defined and registered as a service
		this.cytoPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.WEST);
		this.myControlPanel = myCytoPanel;
	}
	public void actionPerformed(ActionEvent e) {
		// If the state of the cytoPanelWest is HIDE, show it
		if (cytoPanelWest.getState() == CytoPanelState.HIDE) {
			cytoPanelWest.setState(CytoPanelState.DOCK);
		}	

		// Select my panel
		int index = cytoPanelWest.indexOfComponent(myControlPanel);
		if (index == -1) {
			return;
		}
		cytoPanelWest.setSelectedIndex(index);



		// Define a task and set the progress in the run() method
		System.out.println("Upload function entered");
		ImportVisualStyleTaskFactory importVisualStyleTaskFactory = new ImportVisualStyleTaskFactory(loadNetworkFileTaskFactory,cyApplicationManagerServiceRef,
					applyVisualStyleTaskFactory);
		TaskIterator taskIterator = importVisualStyleTaskFactory.createTaskIterator();
		//ImportVisualStyleTask importVisualStyleTask = new ImportVisualStyleTask(loadNetworkFileTaskFactory, cyApplicationManagerServiceRef.getCurrentNetworkView(),
		//		applyVisualStyleTaskFactory);

		System.out.println("execute succesfully");

		myControlPanel.getNetworkbutton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Network Button Pressed");
				dialogTaskManager.execute(taskIterator);
			}
		});


	}
	public void upload(){

	}

}