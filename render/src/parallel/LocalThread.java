package parallel;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import scene.Scene;

/** Тред для многопоточного расчета вне парадигмы akka.
 * @author mclaudt
 *
 */
public class LocalThread extends SwingWorker<String, Object> {

	int from;
	int to;

	BufferedImage img;

	Scene s;

	JPanel panel;

	public LocalThread(int from, int to, Scene s, BufferedImage img, JPanel panel) {
		this.from = from;
		this.to = to;
		this.s = s;
		this.img = img;
		this.panel = panel;

	}

	@Override
	public String doInBackground() {

		for (int i = from; i < to; i++) {

			for (int j = 0; j < s.height; j++) {

				img.setRGB(i, j, s.calculatePixelColor(i, j).getRGB());

			}

			panel.repaint();

		}

		return null;
	}

}
