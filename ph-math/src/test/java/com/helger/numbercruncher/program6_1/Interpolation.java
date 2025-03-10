/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.program6_1;

import com.helger.numbercruncher.mathutils.DataPoint;
import com.helger.numbercruncher.mathutils.InterpolationPolynomial;
import com.helger.numbercruncher.mathutils.SystemOutAlignRight;

/**
 * PROGRAM 6-1: Polynomial Interpolation Demonstrate polynomial interpolation by
 * using a divided difference table to construct an interpolation function for a
 * set of data points. Use the function to estimate new values.
 */
public final class Interpolation
{
  private static final int MAX_POINTS = 10;

  private static SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

  /**
   * Main program.
   *
   * @param args
   *        the array of runtime arguments
   */
  public static void main (final String args[])
  {
    final InterpolationPolynomial p = new InterpolationPolynomial (MAX_POINTS);
    final float x = 1.4f;

    p.addDataPoint (new DataPoint (1.12f, (float) Math.exp (1.12f)));
    p.addDataPoint (new DataPoint (1.55f, (float) Math.exp (1.55f)));
    printEstimate (p, x);

    p.addDataPoint (new DataPoint (1.25f, (float) Math.exp (1.25f)));
    printEstimate (p, x);

    p.addDataPoint (new DataPoint (1.92f, (float) Math.exp (1.92f)));
    printEstimate (p, x);

    p.addDataPoint (new DataPoint (1.33f, (float) Math.exp (1.33f)));
    printEstimate (p, x);

    p.addDataPoint (new DataPoint (1.75f, (float) Math.exp (1.75f)));
    printEstimate (p, x);
  }

  /**
   * Print the value of p(x).
   *
   * @param p
   *        the polynomial interpolation function
   * @param x
   *        the value of x
   */
  private static void printEstimate (final InterpolationPolynomial p, final float x)
  {
    printTable (p);

    final float est = p.at (x);
    final float exp = (float) Math.exp (x);
    final float errorPct = Math.abs (100 * (est - exp) / exp);

    System.out.println ("\nEstimate e^" + x + "  = " + est);
    System.out.println ("  Math.exp(" + x + ") = " + exp);
    System.out.println ("        % error = " + errorPct + "\n");
  }

  /**
   * Print the divided difference table.
   */
  private static void printTable (final InterpolationPolynomial p)
  {
    final int n = p.getDataPointCount ();
    final DataPoint data[] = p.getDataPoints ();
    final float dd[][] = p.getDividedDifferenceTable ();

    ar.print ("i", 1);
    ar.print ("x", 5);
    ar.print ("f(x)", 10);
    if (n > 1)
      ar.print ("First", 10);
    if (n > 2)
      ar.print ("Second", 10);
    if (n > 3)
      ar.print ("Third", 12);
    if (n > 4)
      ar.print ("Fourth", 12);
    if (n > 5)
      ar.print ("Fifth", 12);
    ar.underline ();

    for (int i = 0; i < n; ++i)
    {
      ar.print (i, 1);
      ar.print (data[i].getX (), 5);
      ar.print (dd[i][0], 10);

      for (int order = 1; order < n - i; ++order)
      {
        ar.print (dd[i][order], (order < 3) ? 10 : 12);
      }

      ar.println ();
    }
  }
}
/*
 * Output: i x f(x) First -------------------------- 0 1.12 3.0648541 3.82934 1
 * 1.55 4.71147 Estimate e^1.4 = 4.137069 Math.exp(1.4) = 4.0552 % error =
 * 2.0188677 i x f(x) First Second ------------------------------------ 0 1.12
 * 3.0648541 3.82934 1.8545004 1 1.55 4.71147 4.070425 2 1.25 3.4903429 Estimate
 * e^1.4 = 4.0591803 Math.exp(1.4) = 4.0552 % error = 0.09814953 i x f(x) First
 * Second Third ------------------------------------------------ 0 1.12
 * 3.0648541 3.82934 1.8545004 0.724587 1 1.55 4.71147 4.070425 2.43417 2 1.25
 * 3.4903429 4.971068 3 1.92 6.820958 Estimate e^1.4 = 4.0546155 Math.exp(1.4) =
 * 4.0552 % error = 0.014416116 i x f(x) First Second Third Fourth
 * ------------------------------------------------------------ 0 1.12 3.0648541
 * 3.82934 1.8545004 0.724587 0.17594407 1 1.55 4.71147 4.070425 2.43417
 * 0.7615353 2 1.25 3.4903429 4.971068 2.2666323 3 1.92 6.820958 5.1523986 4
 * 1.33 3.7810435 Estimate e^1.4 = 4.055192 Math.exp(1.4) = 4.0552 % error =
 * 1.998972E-4 i x f(x) First Second Third Fourth Fifth
 * ------------------------------------------------------------------------ 0
 * 1.12 3.0648541 3.82934 1.8545004 0.724587 0.17594407 0.037198737 1 1.55
 * 4.71147 4.070425 2.43417 0.7615353 0.19937928 2 1.25 3.4903429 4.971068
 * 2.2666323 0.80141115 3 1.92 6.820958 5.1523986 2.667338 4 1.33 3.7810435
 * 4.6989512 5 1.75 5.754603 Estimate e^1.4 = 4.0552006 Math.exp(1.4) = 4.0552 %
 * error = 1.1758659E-5
 */
