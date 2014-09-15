/**
 * 
 */
package scene;

import java.awt.Color;
import java.util.Random;

import linalg.*;





/** Компонент в виде сферы
 * @author mclaudt
 *
 */
public class SphereBody implements Body {

	public float rad = 10;
	
	Point3d c = new Point3d(0, 0, 0);

	Color color;

	@Override
	public Ray inter(Ray r) {

		
		
		
		float t = c.add(r.start.scale(-1f)).dot(r.dir) / r.dir.dot(r.dir);

		if (t <= 0) {
			return null;
		}

		
		float l = (r.start.add(r.dir.scale(t)).add(c.scale(-1f))).mod();

		if (l < rad) {
	
			
			

			float a = r.dir.dot(r.dir);

			float b = -2 * c.add(r.start.scale(-1f)).dot(r.dir);

			float cc = (float) (Math.pow(c.add(r.start.scale(-1f)).mod(), 2) - rad * rad);

			float t1 = (float) ((-1 * b - Math.sqrt(Math.pow(b, 2) - 4 * a * cc)) / (2 * a));

			

			Point3d coll = r.start.add(r.dir.scale(t1));

			

			Point3d norm = coll.add(c.scale(-1f)).scale(1 / coll.add(c.scale(-1f)).mod());

			

			Point3d refl = r.dir.add(norm.scale(-2 * r.dir.dot(norm)));

			Ray res = new Ray(coll, refl);

			return res;

		} else {
			return null;
		}

	}

	public SphereBody(float rad, Point3d c) {

		Random r = new Random();
		this.rad = rad;
		this.c = c;		
		this.color = new Color(Color.HSBtoRGB(r.nextInt(256)/255f,1f,1f));
	}

	@Override
	public Color getColor() {	
		return this.color;
	}

}
