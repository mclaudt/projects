package scene;

import java.awt.Color;
import java.util.Random;

import linalg.Point3d;
import linalg.Ray;
import linalg.Utils;

/** Сцена, при создании экземпляра наполняет себя объектами на основе random seed.
 * @author mclaudt
 *
 */
public class Scene {

	Random rand;
	public Scene(int width, int height,long seed) {
		this.width = width;
		this.height = height;
		
		rand = new Random(seed);
		
		generateSpheres( bodies);
		
	}

	
	int bodycount = 12;

	
	Body[] bodies = new Body[bodycount];

	
	Point3d sundir = new Point3d(1, 1, 0.7f);

	

	float phi_sun = (float) Math.atan2(sundir.y, sundir.x);
	float theta_sun = (float) Math.atan2(Math.sqrt(sundir.x * sundir.x + sundir.y * sundir.y), sundir.z);

	
	float deltaPhi = 0.3f;
	
	float deltaTheta = 0.3f;

	
	int width;
	public int height;



	
	float foc = 600.0f;

	
	float sh = 400f;

	
	int slices = 20;

	
	int maxiter = 5;

	
	int sphereRadiusMin = 20;
	int sphereRadiusMax = 200;

	
	
	int sphereBoxLength = 300;

	void generateSpheres(Body[] bodies) {	

		int max = sphereBoxLength;
		int min = -sphereBoxLength;
		
		for (int i = 0; i < bodies.length; i++) {
			Point3d c = null;

			

			boolean goodCoord = false;

			while (goodCoord == false) {

				float x = rand.nextInt((max - min) + 1) + min;
				float y = rand.nextInt((max - min) + 1) + min;				
				float z = rand.nextInt((max - min) + 1) + min;

		
				c = new Point3d(x, y, z);
				

				

				if (i == 0) {
					goodCoord = true;

				} else {

					search: {
						
						for (int j = 0; j < i; j++) {

							if (((SphereBody) bodies[j]).c.add(c.scale(-1)).mod() < ((SphereBody) bodies[j]).rad) {
								

								break search;

							}
						}

						goodCoord = true;

					}

				}

			}

			float rad = rand.nextInt((sphereRadiusMax - sphereRadiusMin) + 1) + sphereRadiusMin;

			
			boolean good = false;
			while (good == false) {
				
				

				if (i == 0) {
					
					good = true;

				} else {
					

					search2: {
						
						for (int j = 0; j < i; j++) {

							if (((SphereBody) bodies[j]).c.add(c.scale(-1)).mod() < ((SphereBody) bodies[j]).rad + rad) {
								
								rad = 3 * rad / 4f;

								break search2;

							}
						}

						good = true;

					}

				}

			}

			

			bodies[i] = new SphereBody(rad, c);

		}


	}

	
	float lum(Ray r) {

		
		
		

		float f = (float) ((r.dir.dot(sundir)) / (r.dir.mod() * sundir.mod()));

		
		if (Utils.IsInSector(r, phi_sun, theta_sun, deltaPhi, deltaTheta)) {
			
			return 1f;

		} else {

			
			
			
			return (float) Math.sin((f + 1) / 2 * Math.PI / 4);
		}

	}

	Color calclum3(Ray r, int counter) {


		if (counter > maxiter) {
			

			return Color.getHSBColor(1, 0, lum(r));

		}

		
		
		
		
		
		
		
		

		int ii = -1;
		Ray rr = null;
		
		float l = 10000f;
		for (int i = 0; i < bodies.length; i++) {

			Ray resp = bodies[i].inter(r);
			if (resp != null) {

				float lcand = resp.start.add(r.start.scale(-1)).mod();

				if (lcand < l) {
					rr = resp;
					ii = i;

					l = lcand;

				}

			}

		}

		if (ii == -1) {
			
			
			
			
			
			return Color.getHSBColor(0, 0, lum(r));

		} else {
			
			if (counter < 1) {

				
				
				
				
				
				
				
				
				
				

				
				
				

				

				int ccc = 0;

				for (int mi = 0; mi < slices + 1; mi++) {
					for (int mj = 0; mj < slices + 1; mj++) {

						

						float current_theta = theta_sun - deltaTheta + 2f * deltaTheta / slices * mi;
						float current_phi = phi_sun - deltaPhi + 2f * deltaPhi / slices * mj;
						Point3d tmp = new Point3d(1, current_phi, current_theta);

						Point3d p = Utils.FromSphericalToCartesian(tmp);
						
						

						myfor: for (int k = 0; k < bodies.length; k++) {

							Ray resp = bodies[k].inter(new Ray(rr.start, p));
							
							if (resp != null) {

								ccc++;
								break myfor;


							}
						}

					}

				}
				
				int shadow = Math.round(100 * ccc / ((float) (slices + 1) * (slices + 1)));

				return mix(mix(calclum3(rr, counter + 1), bodies[ii].getColor(), 80), Color.BLACK, 100 - shadow);

			} else {
				
				
				return mix(calclum3(rr, counter + 1), bodies[ii].getColor(), 80);

			}

		}

	}

	static Color mix(Color c1, Color c2, int percent) {

		return new Color((c1.getRed() * percent + (100 - percent) * c2.getRed()) / 25500f, (c1.getGreen() * percent + (100 - percent) * c2.getGreen()) / 25500f,
				(c1.getBlue() * percent + (100 - percent) * c2.getBlue()) / 25500f);
	}

	public Color calculatePixelColor(int x, int y) {

		
		
		Ray r = new Ray(new Point3d(foc + sh, 0, 0), new Point3d(sh - (foc + sh), (-width / 2 + x) - 0, (height / 2 - y) - 0));

		return calclum3(r, 0);

	}
	
}