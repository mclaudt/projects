package linalg;


public class Utils {
	
	/** Возвращает сферические координаты данной точки
	 *  r, phi, theta
	 * @param r
	 * @return
	 */
	public static Point3d FromCartesianToSpherical(Point3d r){
		return new Point3d(r.mod(),(float) Math.atan2(r.y, r.x),(float) Math.atan2(Math.sqrt(r.x * r.x + r.y * r.y), r.z)
				
				
				);
		
		
		
	}
	
	/** Возвращает декартовы координаты из сферических
	 * r, phi, theta
	 * @param r
	 * @return
	 */
	public static Point3d FromSphericalToCartesian(Point3d r){
		return new Point3d(
				r.x*(float)(Math.sin(r.z)*Math.cos(r.y)),
				r.x*(float)(Math.sin(r.z)*Math.sin(r.y)),
				r.x*(float) (Math.cos(r.z))
				);
		
		
		
	}
	
	
	/** Определяет, попадает ли данный вектор в заданное окно
	 * Нужно для аналитического вычисления карты окружения в виде светлого "прямоугольника"
	 * (На самом деле сферического сектора)

	 * @param r Луч, от него берем лишь направление, исходная точка не важна - небосвод все равно больше.
	 * @param phi_sun Направление на окно(солнце)
	 * @param theta_sun Направление на окно(солнце)
	 * @param deltaPhi угловая полуширина окна
	 * @param deltaTheta угловая полувысота окна
	 * @return
	 */
	public static boolean IsInSector(Ray r,float phi_sun,float theta_sun,float deltaPhi, float deltaTheta){
		
		Point3d sph = Utils.FromCartesianToSpherical(r.dir);
		float phi = sph.y;
		float theta = sph.z;
		
		return ((phi > phi_sun - deltaPhi) && (phi < phi_sun + deltaPhi) && (theta > theta_sun - deltaTheta) && (theta < theta_sun + deltaTheta));
	}

}
