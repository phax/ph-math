package numbercruncher.mathutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModuloArithmeticTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ModuloArithmetic.class);

  public void test ()
  {
    final int a = 3;
    final int b = 13;
    final int m = 5;

    // Test modulo multiplication.
    final int modProduct = ModuloArithmetic.multiply (a, b, m);
    s_aLogger.info (a + "*" + b + " = " + a * b);
    s_aLogger.info (a + "*" + b + " = " + modProduct + " (mod " + m + ")");

    s_aLogger.info ("");

    // Test modulo exponentiation.
    final int modPower = ModuloArithmetic.raise (a, b, m);
    s_aLogger.info (a + "^" + b + " = " + IntPower.raise (a, b));
    s_aLogger.info (a + "^" + b + " = " + modPower + " (mod " + m + ")");
  }
  /*
   * Output: 3*13 = 39 3*13 = 4 (mod 5) 3^13 = 1594323.0 3^13 = 3 (mod 5)
   */

}
