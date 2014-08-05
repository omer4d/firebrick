package firebrick.core;

public class Vec2f {
  public final float x, y;

  public Vec2f(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  public static float dot(Vec2f v1, Vec2f v2)
  {
    return v1.x * v2.x + v1.y * v2.y;
  }

  public static float cross(Vec2f v1, Vec2f v2)
  {
    return v1.x * v2.y - v1.y * v2.x;
  }

  public static float length(Vec2f v)
  {
    return (float)Math.sqrt(dot(v, v));
  }

  public static float approxLength(Vec2f v)
  {
    float dx = Math.abs(v.x), dy = Math.abs(v.y);

    if(dy > dx)
      return 0.41f * dx + 0.941246f * dy;
    else
      return 0.41f * dy + 0.941246f * dx;
  }

  public static float squaredDist(Vec2f a, Vec2f b)
  {
    float dx = b.x - a.x;
    float dy = b.y - a.y;
    return dx * dx + dy * dy;
  }

  public static float dist(Vec2f a, Vec2f b)
  {
    return (float)Math.sqrt(squaredDist(a, b));
  }

  public static Vec2f lerp(Vec2f a, Vec2f b, float k)
  {
    return new Vec2f(a.x * k + b.x * (1.f - k), a.y * k + b.y * (1.f - k));
  }

  public static float ang(Vec2f a, Vec2f b, Vec2f c)
  {
    float v1x = a.x - b.x, v1y = a.y - b.y;
    float v2x = c.x - b.x, v2y = c.y - b.y;

    float crs = v1x * v2y - v1y * v2x;
    float dt = v1x * v2x + v1y * v2y;

    return (float)Math.atan2(crs, dt);
  }

  public static Vec2f unit(Vec2f v)
  {
    float invLen = 1.f / length(v);

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
    float invLen = 1.f / length(v);

    return new Vec2f(-v.y * invLen, v.x * invLen);
  }

  public static Vec2f unitNormal(Vec2f src, Vec2f dest)
  {
    float invLen = 1.f / dist(src, dest);

    return new Vec2f(-(dest.y - src.y) * invLen, (dest.x - src.x) * invLen);
  }

  public static Vec2f rotated(Vec2f v, float alpha)
  {
    float c = (float)Math.cos(alpha), s = (float)Math.sin(alpha);
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
}
