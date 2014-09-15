package parallel;

import java.io.Serializable;
import scene.Scene;

/** Сообщение от системы к акторам отрисовки со сценой и требуемым квадратом. Сигнал начала расчета.
 * @author mclaudt
 *
 */
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