package parallel;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JPanel;

/** Сообщение от системы к главному актору перед началом расчета
 * @author mclaudt
 *
 */
@SuppressWarnings("serial")
public class WriteHereMessage implements Serializable {
	BufferedImage bi;
	JPanel jp;
	int count;

	public WriteHereMessage(BufferedImage bi, JPanel jp, int count) {

		this.bi = bi;
		this.jp = jp;
		this.count = count;

	}

}