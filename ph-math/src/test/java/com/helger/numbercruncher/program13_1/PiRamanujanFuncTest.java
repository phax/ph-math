/**
 * Copyright (C) 2014-2018 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.program13_1;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import com.helger.numbercruncher.mathutils.BigFunctions;

/**
 * PROGRAM 13-3: Ramanujan's Formulas for pi Compute estimates of pi using
 * Ramanujan's formulas.
 */
public final class PiRamanujanFuncTest
{
  @Test
  public void testRun ()
  {
    int digits; // number of digits
    int scale;

    BigDecimal term, a, b, c, d, e, lnArg, pi;
    BigDecimal sqrt2, sqrt5, sqrt6, sqrt10, sqrt13, sqrt29;
    BigDecimal sqrt130, sqrt142, sqrt190, sqrt310, sqrt522;

    // --- 15 digits ---

    digits = 15;
    scale = digits + 2;

    sqrt2 = BigFunctions.sqrt (BigDecimal.valueOf (2), scale);
    sqrt5 = BigFunctions.sqrt (BigDecimal.valueOf (5), scale);
    sqrt13 = BigFunctions.sqrt (BigDecimal.valueOf (13), scale);
    sqrt130 = BigFunctions.sqrt (BigDecimal.valueOf (130), scale);

    term = BigDecimal.valueOf (12).divide (sqrt130, scale, RoundingMode.HALF_EVEN);
    a = BigDecimal.valueOf (2).add (sqrt5);
    b = BigDecimal.valueOf (3).add (sqrt13);
    lnArg = a.multiply (b).divide (sqrt2, RoundingMode.HALF_EVEN).setScale (scale, RoundingMode.HALF_EVEN);
    pi = term.multiply (BigFunctions.ln (lnArg, scale)).setScale (digits, RoundingMode.HALF_EVEN);
    System.out.println (digits + " digits: " + pi);

    // --- 16 digits ---

    digits = 16;
    scale = digits + 2;

    sqrt2 = BigFunctions.sqrt (BigDecimal.valueOf (2), scale);
    sqrt142 = BigFunctions.sqrt (BigDecimal.valueOf (142), scale);

    term = BigDecimal.valueOf (24).divide (sqrt142, scale, RoundingMode.HALF_EVEN);
    a = BigDecimal.valueOf (10).add (BigDecimal.valueOf (11).multiply (sqrt2)).divide (BigDecimal.valueOf (4),
                                                                                       scale,
                                                                                       RoundingMode.HALF_EVEN);
    b = BigDecimal.valueOf (10).add (BigDecimal.valueOf (7).multiply (sqrt2)).divide (BigDecimal.valueOf (4),
                                                                                      scale,
                                                                                      RoundingMode.HALF_EVEN);
    lnArg = BigFunctions.sqrt (a, scale).add (BigFunctions.sqrt (b, scale));
    pi = term.multiply (BigFunctions.ln (lnArg, scale)).setScale (digits, RoundingMode.HALF_EVEN);
    System.out.println (digits + " digits: " + pi);

    // --- 18 digits ---

    digits = 18;
    scale = digits + 2;

    sqrt2 = BigFunctions.sqrt (BigDecimal.valueOf (2), scale);
    sqrt10 = BigFunctions.sqrt (BigDecimal.valueOf (10), scale);
    sqrt190 = BigFunctions.sqrt (BigDecimal.valueOf (190), scale);

    term = BigDecimal.valueOf (12).divide (sqrt190, scale, RoundingMode.HALF_EVEN);
    a = BigDecimal.valueOf (2).multiply (sqrt2).add (sqrt10);
    b = BigDecimal.valueOf (3).add (sqrt10);
    lnArg = a.multiply (b).setScale (scale, RoundingMode.HALF_EVEN);
    pi = term.multiply (BigFunctions.ln (lnArg, scale)).setScale (digits, RoundingMode.HALF_EVEN);
    System.out.println (digits + " digits: " + pi);

    // --- 22 digits ---

    digits = 22;
    scale = digits + 2;

    sqrt2 = BigFunctions.sqrt (BigDecimal.valueOf (2), scale);
    sqrt5 = BigFunctions.sqrt (BigDecimal.valueOf (5), scale);
    sqrt10 = BigFunctions.sqrt (BigDecimal.valueOf (10), scale);
    sqrt310 = BigFunctions.sqrt (BigDecimal.valueOf (310), scale);

    term = BigDecimal.valueOf (12).divide (sqrt310, scale, RoundingMode.HALF_EVEN);
    a = BigDecimal.valueOf (3).add (sqrt5);
    b = BigDecimal.valueOf (2).add (sqrt2);
    c = BigDecimal.valueOf (5).add (BigDecimal.valueOf (2).multiply (sqrt10));
    d = BigFunctions.sqrt (BigDecimal.valueOf (61).add (BigDecimal.valueOf (20).multiply (sqrt10)), scale);
    e = c.add (d).multiply (a).multiply (b).setScale (scale, RoundingMode.HALF_EVEN);
    lnArg = e.divide (BigDecimal.valueOf (4), scale, RoundingMode.HALF_EVEN);
    pi = term.multiply (BigFunctions.ln (lnArg, scale)).setScale (digits, RoundingMode.HALF_EVEN);
    System.out.println (digits + " digits: " + pi);

    // --- 31 digits ---

    digits = 31;
    scale = digits + 2;

    sqrt2 = BigFunctions.sqrt (BigDecimal.valueOf (2), scale);
    sqrt6 = BigFunctions.sqrt (BigDecimal.valueOf (6), scale);
    sqrt29 = BigFunctions.sqrt (BigDecimal.valueOf (29), scale);
    sqrt522 = BigFunctions.sqrt (BigDecimal.valueOf (522), scale);

    term = BigDecimal.valueOf (4).divide (sqrt522, scale, RoundingMode.HALF_EVEN);
    a = BigDecimal.valueOf (5).add (sqrt29).divide (sqrt2, RoundingMode.HALF_EVEN);
    b = BigDecimal.valueOf (5)
                  .multiply (sqrt29)
                  .add (BigDecimal.valueOf (11).multiply (sqrt6))
                  .setScale (scale, RoundingMode.HALF_EVEN);
    c = BigDecimal.valueOf (9).add (BigDecimal.valueOf (3).multiply (sqrt6)).divide (BigDecimal.valueOf (4),
                                                                                     scale,
                                                                                     RoundingMode.HALF_EVEN);
    d = BigDecimal.valueOf (5).add (BigDecimal.valueOf (3).multiply (sqrt6)).divide (BigDecimal.valueOf (4),
                                                                                     scale,
                                                                                     RoundingMode.HALF_EVEN);
    e = BigFunctions.sqrt (c, scale).add (BigFunctions.sqrt (d, scale));
    lnArg = BigFunctions.intPower (a, 3, scale)
                        .multiply (b)
                        .multiply (BigFunctions.intPower (e, 6, scale))
                        .setScale (scale, RoundingMode.HALF_EVEN);
    pi = term.multiply (BigFunctions.ln (lnArg, scale)).setScale (digits, RoundingMode.HALF_EVEN);
    System.out.println (digits + " digits: " + pi);
  }
}
/**
 * Output:<br>
 * 15 digits: 3.141592653589793<br>
 * 16 digits: 3.1415926535897931<br>
 * 18 digits: 3.141592653589793238<br>
 * 22 digits: 3.1415926535897932384626<br>
 * 31 digits: 3.1415926535897932384626433832794
 */
