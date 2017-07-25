/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.numbercruncher.mathutils;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * PROGRAM 12-2: Test BigFunctions Test the {@link BigFunctions} by comparing
 * results with class java.lang.Math.
 */
public final class BigFunctionsTest
{
  private static final int SCALE = 40;

  /**
   * Run the test.
   */
  @Test
  public void testRun ()
  {
    System.out.println ("2^(-25) = " + Math.pow (2, -25));
    System.out.println ("        = " + BigFunctions.intPower (BigDecimal.valueOf (2), -25, SCALE));

    System.out.println ();
    System.out.println ("sqrt 2 = " + Math.sqrt (2));
    System.out.println ("       = " + BigFunctions.sqrt (BigDecimal.valueOf (2), SCALE));

    System.out.println ();
    System.out.println ("2^(1/3) = " + Math.exp (Math.log (2) / 3));
    System.out.println ("        = " + BigFunctions.intRoot (BigDecimal.valueOf (2), 3, SCALE));

    System.out.println ();
    System.out.println ("e^(-19.5) = " + Math.exp (-19.5));
    System.out.println ("          = " + BigFunctions.exp (new BigDecimal ("-19.5"), SCALE));

    System.out.println ();
    System.out.println ("ln 2 = " + Math.log (2));
    System.out.println ("     = " + BigFunctions.ln (BigDecimal.valueOf (2), SCALE));

    System.out.println ();
    System.out.println ("arctan sqrt(3)/3 = " + Math.PI / 6);
    System.out.println ("                 = " +
                        BigFunctions.arctan (BigFunctions.sqrt (BigDecimal.valueOf (3), SCALE)
                                                         .divide (BigDecimal.valueOf (3),
                                                                  SCALE,
                                                                  BigDecimal.ROUND_HALF_EVEN),
                                             SCALE));
  }
}
/**
 * Output:<br>
 * 2^(-25) = 2.9802322387695312E-8<br>
 * = 0.0000000298023223876953125000000000000000<br>
 * <br>
 * sqrt 2 = 1.4142135623730951<br>
 * = 1.4142135623730950488016887242096980785696<br>
 * <br>
 * 2^(1/3) = 1.2599210498948732<br>
 * = 1.25992104989487316476721060727822835057024 <br>
 * <br>
 * e^(-19.5) = 3.398267819495071E-9<br>
 * = 0.0000000033982678194950712251407378768109<br>
 * <br>
 * ln 2 = 0.6931471805599453<br>
 * = 0.6931471805599453094172321214581765680755<br>
 * <br>
 * arctan sqrt(3)/3 = 0.5235987755982988<br>
 * = 0.52359877559829887307710723054658381403285
 */
