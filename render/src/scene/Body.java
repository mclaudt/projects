package scene;

import java.awt.Color;

import linalg.Ray;




/** Интерфейс для тел - компонентов сцены.
 * @author mclaudt
 *
 */
interface Body {
	

	Ray inter(Ray r);


	Color getColor();

}
