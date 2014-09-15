package parallel;

/** Класс для логирования сообщений из тредов
 * @author mclaudt
 *
 */
public class Watcher {

	int counter = 0;

	boolean free = true;
	
	int cores;

	long startTime;

	public void start(int cores) {

		this.cores = cores;
		counter = 0;
		startTime = System.currentTimeMillis();
		free = false;

	}

	public void imdone() {
		
		counter++;

		if (counter == this.cores) {

			free = true;

			System.out.println("Succesfully completed on "+this.cores + " treads in " + (System.currentTimeMillis() - startTime)/1000f+" seconds.");

		}

	}

}
