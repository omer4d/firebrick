package firebrick.core.java;

public class Foo {
  public float mass;
  public float forceX, forceY;
  public float velX, velY;
  public float posX, posY;

  public Foo(float mass)
  {
    this.mass = mass;
  }

  public void sim(float dt, float g)
  {
    forceY += mass * g;
    velX += forceX / mass * dt;
    velY += forceY / mass * dt;
    posX += velX * dt;
    posY += velY * dt;
    forceX = 0;
    forceY = 0;
  }
}
