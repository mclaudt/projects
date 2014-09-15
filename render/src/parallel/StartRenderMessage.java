package parallel;

import java.io.Serializable;
import scene.Scene;

@SuppressWarnings("serial")
public class StartRenderMessage implements Serializable {
	public int from;
	public int to;
	public Scene s;

	public StartRenderMessage(int from, int to, Scene s) {
		this.from = from;
		this.to = to;
		this.s = s;
	}

}