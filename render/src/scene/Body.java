package scene;

import java.awt.Color;

import linalg.Ray;




interface Body {
	

	Ray inter(Ray r);


	Color getColor();

}
