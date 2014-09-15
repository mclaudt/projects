package parallel;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import akka.actor.UntypedActor;

public class BossActor extends UntypedActor {
	long starttime;
	BufferedImage bi;

	JPanel jp;

	int cores;

	int counter;

	@Override
	public void onReceive(Object message) {
		if (message instanceof ImDoneMessage) {

			int from = ((ImDoneMessage) message).from;
			int to = ((ImDoneMessage) message).to;

			for (int i = from; i < to; i++) {

				for (int j = 0; j < ((ImDoneMessage) message).result.getImage().getHeight(); j++) {

					bi.setRGB(i, j, ((ImDoneMessage) message).result.getImage().getRGB(i - from, j));

				}

			}

			jp.repaint();

			counter++;

			if (counter == this.cores) {

				System.out.println("Succesfully completed on " + this.cores + " actors in "
						+ (System.currentTimeMillis() - starttime) / 1000f + " seconds.");

			}

		} else

		if (message instanceof WriteHereMessage) {

			this.bi = ((WriteHereMessage) message).bi;
			this.jp = ((WriteHereMessage) message).jp;
			this.cores = ((WriteHereMessage) message).count;

			starttime = System.currentTimeMillis();

			counter = 0;

		} else
			unhandled(message);

	}
}