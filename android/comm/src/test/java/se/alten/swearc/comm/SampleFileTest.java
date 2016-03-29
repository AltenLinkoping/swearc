package se.alten.swearc.comm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SampleFileTest {

  @Test
  public void testAdd() {
    assertEquals(4, SampleFile.add(1, 3));
  }

}
