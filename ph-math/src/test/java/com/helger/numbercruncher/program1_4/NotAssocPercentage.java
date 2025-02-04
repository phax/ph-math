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
package com.helger.numbercruncher.program1_4;

import java.util.Random;

import com.helger.commons.equals.EqualsHelper;

/**
 * PROGRAM 1-4: Not Associative Percentage Figure out what percentage of
 * floating-point additions and multiplications fail their associative laws.
 */
public final class NotAssocPercentage
{
  private static final int TRIALS = 1000000; // one million

  public static void main (final String args[])
  {
    final Random random = new Random ();

    int addCount = 0;
    int multCount = 0;

    // Loop to perform trials.
    for (int i = 0; i < TRIALS; ++i)
    {

      // Generate three random floating-point values.
      final float a = random.nextFloat ();
      final float b = random.nextFloat ();
      final float c = random.nextFloat ();

      // Add both ways.
      final float s1 = (a + b) + c;
      final float s2 = a + (b + c);

      // Multiply both ways.
      final float p1 = (a * b) * c;
      final float p2 = a * (b * c);

      // Compare sums and products and count the failures.
      if (!EqualsHelper.equals (s1, s2))
        ++addCount;
      if (!EqualsHelper.equals (p1, p2))
        ++multCount;
    }

    System.out.println ((100 * addCount) / TRIALS + "% failures of the " + "associative law of addition.");
    System.out.println ((100 * multCount) / TRIALS + "% failures of the " + "associative law of multiplication.");
  }
}
/*
 * Output: 17% failures of the associative law of addition. 34% failures of the
 * associative law of multiplication.
 */
