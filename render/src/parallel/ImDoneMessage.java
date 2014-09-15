package parallel;

import java.io.Serializable;

/** Сообщение от актора отрисовки к актору-менеджеру о завершении работы. В нагрузку идет растр результата.
 * @author mclaudt
 *
 */
@SuppressWarnings("serial")
class ImDoneMessage implements Serializable {
	public SerializableBufferedImage result;
	public int from;
	public int to;

	public ImDoneMessage(SerializableBufferedImage result, int from, int to) {
		this.result = result;
		this.from = from;
		this.to = to;
	}
}