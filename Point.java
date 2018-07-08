public class Point {

	int x;
	int y;

	Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	boolean isEmpty() {
		if (x == -1 && y == -1) {
			return true;
		} else {
			return false;
		}
	}
	

	
}