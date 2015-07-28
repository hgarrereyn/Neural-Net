package green.mainwindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import green.neural.net.ga.*;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class MainWindow extends JPanel {
	
	private static final Item[] creatureChoices = new Item[]{
		new Item("Creature with two legs", Creature.class),
		new Item("Creature with a short leg and a long leg", UnevenCreature.class),
		new Item("Wormlike creature", WormCreature.class),
		new Item("Simple creature with one joint", JointCreature.class),
		new Item("Creature with four legs", FourLegCreature.class)
	};
	private static JFrame f;
	
	private static class CreatureList extends AbstractListModel{
		
		public Object getElementAt(int i) {
			return (i+1) + " : " + creatureChoices[i].getDesc();
		}

		public int getSize() {
			return creatureChoices.length;
		}
		
	}
	
	private static class Item <T extends AbstractCreature> {
		private String desc;
		private Class<T> type;
		public Item(String desc, Class<T> type){
			this.desc = desc;
			this.type = type;
		}
		public String getDesc() { return desc; }
		public Class<T> getType() { return type; }
	}
	
	/**
	 * Creates new main window and lets user select creature type
	 */
	public void launch(){
		CreatureList listModel = new CreatureList();
		final JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectedIndex(0);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(380,320));
		
		final Runnable launch = new Runnable() {
		    public void run() {
		    	launchTrainer(creatureChoices[list.getSelectedIndex()].getType());//Call your function
		    }
		};
		
		JButton launchButton = new JButton("Launch");
		launchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Event: " + list.getSelectedIndex());
				new Thread(launch).start();
			}
		});
		
		f = new JFrame("Select creature type");
		f.setSize(400, 400);
		f.setResizable(false);
		f.add(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(listScroller);
		this.add(launchButton);
		f.getRootPane().setDefaultButton(launchButton);

		f.pack();
		
		f.setVisible(true);
		
		//launchTrainer(creatureChoices[0].getType());
		//
	}
	
	public static <T extends AbstractCreature> void launchTrainer(Class<T> creature){
		//f.setVisible(false);
		try {
			GeneticTrainer<T> trainer = new GeneticTrainer<T>(creature);
			trainer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
