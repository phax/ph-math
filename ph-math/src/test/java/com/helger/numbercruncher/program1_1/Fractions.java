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
package com.helger.numbercruncher.program1_1;

/**
 * PROGRAM 1-1: Fractions Print and sum the values of the fractions 1/2, 1/3,
 * 1/4, and 1/5 to look for any roundoff errors.
 */
public final class Fractions
{
  private static final float HALF = 1 / 2f;
  private static final float THIRD = 1 / 3f;
  private static final float QUARTER = 1 / 4f;
  private static final float FIFTH = 1 / 5f;
  private static final float SIXTH = 1 / 6f;
  private static final float SEVENTH = 1 / 7f;

  private static final int FACTOR = 840;

  public static void main (final String args[])
  {
    System.out.println ("1/2 = " + HALF);
    System.out.println ("1/3 = " + THIRD);
    System.out.println ("1/4 = " + QUARTER);
    System.out.println ("1/5 = " + FIFTH);
    System.out.println ("1/6 = " + SIXTH);
    System.out.println ("1/7 = " + SEVENTH);

    float sum = 0;
    System.out.println ();

    for (int i = 0; i < FACTOR; ++i)
      sum += HALF;
    System.out.println ("1/2 summed " + FACTOR + " times = " + sum + " (should be " + FACTOR / 2 + ")");

    sum = 0;
    for (int i = 0; i < FACTOR; ++i)
      sum += THIRD;
    System.out.println ("1/3 summed " + FACTOR + " times = " + sum + " (should be " + FACTOR / 3 + ")");

    sum = 0;
    for (int i = 0; i < FACTOR; ++i)
      sum += QUARTER;
    System.out.println ("1/4 summed " + FACTOR + " times = " + sum + " (should be " + FACTOR / 4 + ")");

    sum = 0;
    for (int i = 0; i < FACTOR; ++i)
      sum += FIFTH;
    System.out.println ("1/5 summed " + FACTOR + " times = " + sum + " (should be " + FACTOR / 5 + ")");

    sum = 0;
    for (int i = 0; i < FACTOR; ++i)
      sum += SIXTH;
    System.out.println ("1/6 summed " + FACTOR + " times = " + sum + " (should be " + FACTOR / 6 + ")");

    sum = 0;
    for (int i = 0; i < FACTOR; ++i)
      sum += SEVENTH;
    System.out.println ("1/7 summed " + FACTOR + " times = " + sum + " (should be " + FACTOR / 7 + ")");
  }
}
/*
 * Output: 1/2 = 0.5 1/3 = 0.33333334 1/4 = 0.25 1/5 = 0.2 1/6 = 0.16666667 1/7
 * = 0.14285715 1/2 summed 840 times = 420.0 (should be 420) 1/3 summed 840
 * times = 279.99915 (should be 280) 1/4 summed 840 times = 210.0 (should be
 * 210) 1/5 summed 840 times = 167.99858 (should be 168) 1/6 summed 840 times =
 * 139.99957 (should be 140) 1/7 summed 840 times = 120.001114 (should be 120)
 */
