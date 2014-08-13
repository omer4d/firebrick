package firebrick.core;

import clojure.lang.ITransientMap;

public class Grid {
  private ITransientMap[][] content;
  private int cols;
  private int rows;

  public Grid(int cols, int rows)
  {
    this.content = new ITransientMap[cols][rows];
    this.cols = cols;
    this.rows = rows;
  }

  public boolean validIJ(int i, int j)
  {
    return i >= 0 && i < cols && j >= 0 && j < rows;
  }

  public ITransientMap at(int i, int j)
  {
    return validIJ(i, j) ? content[i][j] : null;
  }

  public void set(int i, int j, ITransientMap t)
  {
    content[i][j] = t;
  }

  public int cols()
  {
    return this.cols;
  }

  public int rows()
  {
    return this.rows;
  }
}
