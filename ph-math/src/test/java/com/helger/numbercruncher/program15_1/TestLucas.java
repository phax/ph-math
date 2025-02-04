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
package com.helger.numbercruncher.program15_1;

import com.helger.numbercruncher.mathutils.SystemOutAlignRight;
import com.helger.numbercruncher.primeutils.ILucasCaller;
import com.helger.numbercruncher.primeutils.LucasFuncTest;
import com.helger.numbercruncher.primeutils.LucasStatus;

/**
 * PROGRAM 15-1: Lucas Test for Primality Demonstrate the Lucas test for
 * primality.
 */
public final class TestLucas implements ILucasCaller
{
  private int prevA = 0;
  private final SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

  /**
   * Test an integer p for primality.
   *
   * @param p
   *        the value of p
   */
  void test (final int p)
  {
    System.out.println ("\nTESTING " + p + "\n");
    ar.print ("a", 5);
    ar.print ("q", 10);
    ar.print ("exponent", 12);
    ar.print ("mod value", 12);
    ar.print ("status", 10);
    ar.underline ();

    prevA = 0;

    final boolean result = (new LucasFuncTest (p, this)).test ();

    System.out.println ();
    System.out.println (p + " is " + (result ? "prime." : "composite."));
  }

  /**
   * Report on the test status.
   *
   * @param status
   *        the test status
   */
  public void reportStatus (final LucasStatus status)
  {
    // Skip a line for a new value of a.
    if ((prevA != 0) && (status.getA () != prevA))
    {
      System.out.println ();
    }
    prevA = status.getA ();

    ar.print (status.getA (), 5);
    ar.print (status.getQ (), 10);
    ar.print (status.getExponent (), 12);
    ar.print (status.getValue (), 12);
    ar.print ((status.didPass () ? "pass" : "fail"), 10);
    ar.println ();
  }

  /**
   * Main.
   *
   * @param args
   *        the commandline arguments (ignored)
   */
  public static void main (final String args[])
  {
    final TestLucas lucas = new TestLucas ();

    // Test various integers. All but 21 are prime.
    lucas.test (7);
    lucas.test (15787);
    lucas.test (149287);
    lucas.test (21);
  }
}
/*
 * Output: TESTING 7 a q exponent mod value status
 * ------------------------------------------------- 2 1 6 1 pass 2 2 3 1 fail 3
 * 1 6 1 pass 3 2 3 6 pass 3 3 2 2 pass 7 is prime. TESTING 15787 a q exponent
 * mod value status ------------------------------------------------- 2 1 15786
 * 1 pass 2 2 7893 15786 pass 2 3 5262 11258 pass 2 877 18 9552 pass 15787 is
 * prime. TESTING 149287 a q exponent mod value status
 * ------------------------------------------------- 2 1 149286 1 pass 2 2 74643
 * 1 fail 3 1 149286 1 pass 3 2 74643 149286 pass 3 3 49762 22426 pass 3 139
 * 1074 131616 pass 3 179 834 123639 pass 149287 is prime. TESTING 21 a q
 * exponent mod value status ------------------------------------------------- 2
 * 1 20 4 fail 3 1 20 9 fail 4 1 20 16 fail 5 1 20 4 fail 6 1 20 15 fail 7 1 20
 * 7 fail 8 1 20 1 pass 8 2 10 1 fail 9 1 20 18 fail 10 1 20 16 fail 11 1 20 16
 * fail 12 1 20 18 fail 13 1 20 1 pass 13 2 10 1 fail 14 1 20 7 fail 15 1 20 15
 * fail 16 1 20 4 fail 17 1 20 16 fail 18 1 20 9 fail 19 1 20 4 fail 20 1 20 1
 * pass 20 2 10 1 fail 21 1 20 0 fail 21 is composite.
 */
