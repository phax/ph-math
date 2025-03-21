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
package com.helger.numbercruncher.program4_8;

import com.helger.commons.equals.EqualsHelper;
import com.helger.numbercruncher.mathutils.SystemOutAlignRight;

/**
 * Program 4-8: Sum Numbers with Mixed Signs, Positive and Negative Subtotals
 * Sum a sequence of double numbers with alternating signs. Compute separate
 * positive and negative subtotals. The final sum should be 0.
 */
public final class MainSumMixedSignsPosNeg
{
  public static void main (final String args[])
  {
    final SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

    int k = 0;
    int odd = 1; // odd number
    double kFactorial = 1; // k!
    double posSum = 0; // running subtotal of pos fractions
    double negSum = 0; // running subtotal of neg fractions
    double prevPosSum = -1; // previous value of positive sum
    double prevNegSum = 1; // previous value of negative sum

    ar.print ("k", 2);
    ar.print ("Fraction", 25);
    ar.print ("Positive subtotal", 20);
    ar.print ("Negative subtotal", 21);
    ar.underline ();

    // Loop until the positive and negative subtotals
    // no longer change.
    do
    {
      // Positive fraction and subtotal.
      double posNumerator = odd * odd;
      posNumerator += odd * posNumerator;
      final double posFraction = posNumerator / kFactorial;
      prevPosSum = posSum;
      posSum += posFraction;

      ar.print (k, 2);
      ar.print (posFraction, 25);
      ar.print (posSum, 20);
      ar.println ();

      ++k;
      kFactorial *= k;
      odd += 2;

      // Negative fraction and subtotal.
      double negNumerator = odd * odd;
      negNumerator += odd * negNumerator;
      final double negFraction = -negNumerator / kFactorial;
      prevNegSum = negSum;
      negSum += negFraction;

      ar.print (k, 2);
      ar.print (negFraction, 25);
      ar.print (" ", 20);
      ar.print (negSum, 21);
      ar.println ();

      ++k;
      kFactorial *= k;
      odd += 2;
    } while (!EqualsHelper.equals (posSum, prevPosSum) || !EqualsHelper.equals (negSum, prevNegSum));

    System.out.println ("\nFinal sum = " + (posSum + negSum));
  }
}
/*
 * Output: k Fraction Positive subtotal Negative subtotal
 * -------------------------------------------------------------------- 0 2.0
 * 2.0 1 -36.0 -36.0 2 75.0 77.0 3 -65.33333333333333 -101.33333333333333 4
 * 33.75 110.75 5 -12.1 -113.43333333333332 6 3.286111111111111
 * 114.03611111111111 7 -0.7142857142857143 -114.14761904761903 8
 * 0.12901785714285716 114.16512896825397 9 -0.019896384479717814
 * -114.16751543209875 10 0.002673611111111111 114.16780257936507 11
 * -3.1806156806156807E-4 -114.16783349366682 12 3.392473010528566E-5
 * 114.16783650409518 13 -3.277972027972028E-6 -114.16783677163885 14
 * 2.89406911430721E-7 114.16783679350209 15 -2.35165579080923E-8
 * -114.16783679515541 16 1.7696492770897533E-9 114.16783679527174 17
 * -1.2398526491663746E-10 -114.1678367952794 18 8.125423849197927E-12
 * 114.16783679527987 19 -5.001434484046243E-13 -114.1678367952799 20
 * 2.901966448410855E-14 114.1678367952799 21 -1.5923761931532594E-15
 * -114.1678367952799 22 8.287361182067708E-17 114.1678367952799 23
 * -4.101498195323127E-18 -114.1678367952799 Final sum = 0.0
 */
