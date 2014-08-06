package firebrick.core;

public class Vec2f {
  public final double x, y;

  public Vec2f(double x, double y)
  {
    this.x = x;
    this.y = y;
  }

  public static double dot(Vec2f v1, Vec2f v2)
  {
    return v1.x * v2.x + v1.y * v2.y;
  }

  public static double cross(Vec2f v1, Vec2f v2)
  {
    return v1.x * v2.y - v1.y * v2.x;
  }

  public static double length(Vec2f v)
  {
    return (double)Math.sqrt(dot(v, v));
  }

  public static double approxLength(Vec2f v)
  {
    double dx = Math.abs(v.x), dy = Math.abs(v.y);

    if(dy > dx)
      return 0.41f * dx + 0.941246f * dy;
    else
      return 0.41f * dy + 0.941246f * dx;
  }

  public static double squaredDist(Vec2f a, Vec2f b)
  {
    double dx = b.x - a.x;
    double dy = b.y - a.y;
    return dx * dx + dy * dy;
  }

  public static double dist(Vec2f a, Vec2f b)
  {
    return (double)Math.sqrt(squaredDist(a, b));
  }

  public static Vec2f lerp(Vec2f a, Vec2f b, double k)
  {
    return new Vec2f(a.x * k + b.x * (1 - k), a.y * k + b.y * (1 - k));
  }

  public static double ang(Vec2f a, Vec2f b, Vec2f c)
  {
    double v1x = a.x - b.x, v1y = a.y - b.y;
    double v2x = c.x - b.x, v2y = c.y - b.y;

    double crs = v1x * v2y - v1y * v2x;
    double dt = v1x * v2x + v1y * v2y;

    return (double)Math.atan2(crs, dt);
  }

  public static Vec2f unit(Vec2f v)
  {
    double invLen = 1 / length(v);

    return new Vec2f(v.x * invLen, v.y * invLen);
  }

  public static Vec2f normal(Vec2f v)
  {
    return new Vec2f(-v.y, v.x);
  }

  public static Vec2f normal(Vec2f src, Vec2f dest)
  {
    return new Vec2f(-(dest.y - src.y), dest.x - src.x);
  }

  public static Vec2f unitNormal(Vec2f v)
  {
    double invLen = 1 / length(v);

    return new Vec2f(-v.y * invLen, v.x * invLen);
  }

  public static Vec2f unitNormal(Vec2f src, Vec2f dest)
  {
    double invLen = 1 / dist(src, dest);

    return new Vec2f(-(dest.y - src.y) * invLen, (dest.x - src.x) * invLen);
  }

  public static Vec2f rotated(Vec2f v, double alpha)
  {
    double c = (double)Math.cos(alpha), s = (double)Math.sin(alpha);
    return new Vec2f(v.x * c - v.y * s,
                     v.x * s + v.y * c);
  }

  // Sum:

  public static Vec2f sum(Vec2f v1, Vec2f v2)
  {
    return new Vec2f(v1.x + v2.x,
                     v1.y + v2.y);
  }

  public static Vec2f sum(Vec2f v1, Vec2f v2, Vec2f v3)
  {
    return new Vec2f(v1.x + v2.x + v3.x,
                     v1.y + v2.y + v3.y);
  }

  public static Vec2f sum(Vec2f v1, Vec2f v2, Vec2f v3, Vec2f v4)
  {
    return new Vec2f(v1.x + v2.x + v3.x + v4.x,
                     v1.y + v2.y + v3.y + v4.y);
  }

  public static Vec2f sum(Vec2f v1, Vec2f v2, Vec2f v3, Vec2f v4, Vec2f v5)
  {
    return new Vec2f(v1.x + v2.x + v3.x + v4.x + v5.x,
                     v1.y + v2.y + v3.y + v4.y + v5.y);
  }

  public static Vec2f sum(Vec2f v1, Vec2f v2, Vec2f v3, Vec2f v4, Vec2f v5, Vec2f v6)
  {
    return new Vec2f(v1.x + v2.x + v3.x + v4.x + v5.x + v6.x,
                     v1.y + v2.y + v3.y + v4.y + v5.y + v6.y);
  }

  // Difference:

  public static Vec2f diff(Vec2f v1, Vec2f v2)
  {
    return new Vec2f(v1.x - v2.x,
                     v1.y - v2.y);
  }

  public static Vec2f diff(Vec2f v1, Vec2f v2, Vec2f v3)
  {
    return new Vec2f(v1.x - v2.x - v3.x,
                     v1.y - v2.y - v3.y);
  }

  public static Vec2f diff(Vec2f v1, Vec2f v2, Vec2f v3, Vec2f v4)
  {
    return new Vec2f(v1.x - v2.x - v3.x - v4.x,
                     v1.y - v2.y - v3.y - v4.y);
  }

  public static Vec2f diff(Vec2f v1, Vec2f v2, Vec2f v3, Vec2f v4, Vec2f v5)
  {
    return new Vec2f(v1.x - v2.x - v3.x - v4.x - v5.x,
                     v1.y - v2.y - v3.y - v4.y - v5.y);
  }

  public static Vec2f diff(Vec2f v1, Vec2f v2, Vec2f v3, Vec2f v4, Vec2f v5, Vec2f v6)
  {
    return new Vec2f(v1.x - v2.x - v3.x - v4.x - v5.x - v6.x,
                     v1.y - v2.y - v3.y - v4.y - v5.y - v6.y);
  }

  public String toString()
  {
    return "(x: " + x + ", y: " + y + ")";
  }
}
