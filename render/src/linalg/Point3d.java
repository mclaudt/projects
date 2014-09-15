package linalg;


public class Point3d {

	public float x;
	public float y;
	public float z;

	public Point3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}


	public Point3d add(Point3d b) {

		return new Point3d(this.x + b.x, this.y + b.y, this.z + b.z);

	}


	public float dot(Point3d b) {

		return this.x * b.x + this.y * b.y + this.z * b.z;

	}


	public Point3d scale(float a) {

		return new Point3d(this.x * a, this.y * a, this.z * a);

	}


	public float mod() {

		return (float) Math.sqrt(this.dot(this));

	}

}
