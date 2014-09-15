package parallel;

import java.io.Serializable;

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