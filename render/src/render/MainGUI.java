package render;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import scene.Scene;
import net.miginfocom.swing.MigLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import parallel.*;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;

/** Точка входа, с GUI.
 * @author mclaudt
 *
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	static final int WIDTH = 600;
	static final int HEIGHT = 400;

	Scene s;

	BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	Random random = new Random();

	JPanel panel;

	Watcher w = new Watcher();

	private JPanel contentPane;
	private JSpinner spinner;
	private JPanel panel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {

		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.newScene();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void fillWithRed() {
		int qr = 255;
		int qg = 0;
		int qb = 0;
		int qcol = (qr << 16) | (qg << 8) | qb;

		for (int i = 0; i < WIDTH; i++) {

			for (int j = 0; j < HEIGHT; j++) {

				img.setRGB(i, j, qcol);

			}

		}

	}

	void gogogo() {

		fillWithRed();
		panel.repaint();

		w.start(cores);

		int step = WIDTH / cores;

		final ActorSystem system = ActorSystem.create("myAkkaRender");

		ActorRef boss = system.actorOf(Props.create(BossActor.class));

		boss.tell(new WriteHereMessage(img, panel, cores), ActorRef.noSender());

		List<ActorRef> refs = new ArrayList<ActorRef>();

		for (int i = 0; i < cores - 1; i++) {

			ActorRef renderer = system.actorOf(Props.create(RenderActor.class));
			refs.add(renderer);
			renderer.tell(new StartRenderMessage(step * i, step * (i + 1), s), boss);

		}

		ActorRef renderer = system.actorOf(Props.create(RenderActor.class));
		refs.add(renderer);
		renderer.tell(new StartRenderMessage(step * (cores - 1), WIDTH, s), boss);

	}

	int cores = 6;
	private JButton btnRenderActor;
	private JLabel lblNewLabel;
	private JButton btnGenerateNewScene;

	/**
	 * Create the frame.
	 */

	void gogogoThreads() {

		fillWithRed();
		panel.repaint();

		w.start(cores);

		int step = WIDTH / cores;

		for (int i = 0; i < cores - 1; i++) {

			LocalThread task = new LocalThread(step * i, step * (i + 1), s, img, panel);

			task.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if ("state".equals(evt.getPropertyName())) {

						if (evt.getNewValue() == StateValue.DONE) {
							w.imdone();

						}

					}
				}
			});

			task.execute();
		}

		LocalThread task = new LocalThread(step * (cores - 1), WIDTH, s, img, panel);

		task.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("state".equals(evt.getPropertyName())) {

					if (evt.getNewValue() == StateValue.DONE) {
						w.imdone();

					}

				}
			}
		});

		task.execute();

	}

	public MainGUI() {
		setTitle("Akka Renderer version 0.1");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 627, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:600px:grow"), }, new RowSpec[] { FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("400px:grow"), FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(51dlu;default):grow"), }));

		fillWithRed();

		panel = new JPanel() {

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.drawImage(img, 0, 0, null);

			}

		};
		contentPane.add(panel, "2, 2, fill, fill");

		panel_1 = new JPanel();
		contentPane.add(panel_1, "2, 4, center, top");
		panel_1.setLayout(new MigLayout("", "[][180.00][-27.00][-25.00][fill]", "[22.00px:n,grow][][][][]"));

		lblNewLabel = new JLabel("Number of actors/threads");
		panel_1.add(lblNewLabel, "cell 0 0 2 1,growx");

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(6), new Integer(1), null, new Integer(1)));
		spinner.setPreferredSize(new Dimension(45, 28));
		spinner.setMinimumSize(new Dimension(35, 20));

		spinner.setValue(cores);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				cores = (Integer) spinner.getValue();
			}
		});
		panel_1.add(spinner, "cell 3 0 2 1,alignx center,growy");

		JButton btnGo = new JButton("Render using threads");
		panel_1.add(btnGo, "cell 1 1 4 1,growx");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				gogogoThreads();

			}
		});

		btnRenderActor = new JButton("Render using actors");
		btnRenderActor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				gogogo();

			}
		});
		panel_1.add(btnRenderActor, "cell 1 2 4 1,growx");

		btnGenerateNewScene = new JButton("Generate new scene");
		btnGenerateNewScene.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				newScene();
			}
		});
		btnGenerateNewScene.setIcon(null);
		panel_1.add(btnGenerateNewScene, "cell 1 3 4 1,growx");

	}

	protected void newScene() {

		s = new Scene(WIDTH, HEIGHT, random.nextInt());

	}

}
