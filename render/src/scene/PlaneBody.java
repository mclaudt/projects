package scene;

import java.awt.Color;
import linalg.Point3d;
import linalg.Ray;


	
	

/** Компонент в виде бесконечной плоскости.
 * @author mclaudt
 *
 */
public class PlaneBody implements Body {
		
		

		private final Color color;
		
	
		private final Point3d c;
		Point3d norm;
		
		

		@Override
		public Ray inter(Ray r) {
	
			
			
			
			float t = c.add(r.start.scale(-1f)).dot(norm) / r.dir.dot(norm)-0.001f;
			
			
			if (t <= 0) {
				return null;
			}
			
			Point3d coll = r.start.add(r.dir.scale(t));
			
			
			Point3d refl = r.dir.add(norm.scale(-2 * r.dir.dot(norm)));
			
			
			Ray res = new Ray(coll, refl);

			return res;
			
			
		}

		@Override
		public Color getColor() {
			
			return color;
		}

		public PlaneBody(Point3d c, Point3d norm) {

			
			this.norm = norm.scale(1/norm.mod());
			this.c = c;
			
			
			
			this.color = new Color(Color.HSBtoRGB(0,0,1f));
			
		}
	
	}


