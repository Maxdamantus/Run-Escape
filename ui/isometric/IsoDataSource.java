package ui.isometric;


public interface IsoDataSource {
	public IsoSquare squareAt(int x, int y);
	public void setViewableRect(int x, int y, int w, int h);
	public void update();
}
