package parallel;

import java.awt.image.BufferedImage;

import scene.Scene;
import akka.actor.UntypedActor;

public class RenderActor extends UntypedActor {
	public RenderActor() {

	}

	int from;

	int to;

	Scene s;

	BufferedImage bi;

	void render() {

		for (int i = from; i < to; i++) {

			for (int j = 0; j < s.height; j++) {

				bi.setRGB(i - from, j, s.calculatePixelColor(i, j).getRGB());

			}
		}

	}

	public void onReceive(Object message) {
		if (message instanceof StartRenderMessage) {

			this.from = ((StartRenderMessage) message).from;
			this.to = ((StartRenderMessage) message).to;
			this.s = ((StartRenderMessage) message).s;

			bi = new BufferedImage(to - from, s.height, BufferedImage.TYPE_INT_RGB);

			render();

			getSender().tell(new ImDoneMessage(new SerializableBufferedImage(bi), from, to), getSelf());

		}

		else
			unhandled(message);
	}
}