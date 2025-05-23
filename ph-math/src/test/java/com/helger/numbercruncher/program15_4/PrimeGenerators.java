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
package com.helger.numbercruncher.program15_4;

import com.helger.commons.collection.impl.CommonsHashSet;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.numbercruncher.mathutils.SystemOutAlignRight;
import com.helger.numbercruncher.primeutils.PrimalityFuncTest;

/**
 * PROGRAM 15-4: Prime Number Generators Demonstrate two quadratic polynomials
 * that generate prime numbers.
 */
public final class PrimeGenerators
{
  private static final String EULER = "n^2 - 79n + 1601";
  private static final String GREENWOOD = "41n^2 - 4641n + 88007";
  private static final int WIDTH = GREENWOOD.length () + 5;

  private static SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

  public static void main (final String args[])
  {
    ar.print ("n", 2);
    ar.print (EULER, WIDTH);
    ar.print (GREENWOOD, WIDTH);
    ar.underline ();

    int ePrimeCount = 0; // count of Euler primes
    int gPrimeCount = 0; // count of Greenwood primes

    // set of Euler primes
    final ICommonsSet <Integer> ePrimes = new CommonsHashSet <> ();
    // set of Greenwood primes
    final ICommonsSet <Integer> gPrimes = new CommonsHashSet <> ();

    String eMark, gMark; // " " if prime, "c" if composite

    // Loop to test generated numbers.
    for (int n = 0; n <= 99; ++n)
    {

      // Prime number generators.
      final int ep = n * n - 79 * n + 1601; // Euler
      final int gp = Math.abs (41 * n * n - 4641 * n + 88007); // Greenwood

      // Test for primality.
      final boolean epIsPrime = (new PrimalityFuncTest (ep, 5)).test ();
      final boolean gpIsPrime = (new PrimalityFuncTest (gp, 5)).test ();

      ar.print (n, 2);

      if (epIsPrime)
      {
        ++ePrimeCount;
        ePrimes.add (Integer.valueOf (ep));
        eMark = " ";
      }
      else
      {
        eMark = "c";
      }

      if (gpIsPrime)
      {
        ++gPrimeCount;
        gPrimes.add (Integer.valueOf (gp));
        gMark = " ";
      }
      else
      {
        gMark = "c";
      }

      ar.print (ep + eMark, WIDTH);
      ar.print (gp + gMark, WIDTH);
      ar.println ();
    }

    ar.println ();

    ar.print (" ", 2);
    ar.print (ePrimeCount + " primes", WIDTH);
    ar.print (gPrimeCount + " primes", WIDTH);
    ar.println ();

    ar.print (" ", 2);
    ar.print (ePrimes.size () + " distinct", WIDTH);
    ar.print (gPrimes.size () + " distinct", WIDTH);
    ar.println ();
  }
}
/*
 * Output: n n^2 - 79n + 1601 41n^2 - 4641n + 88007
 * ------------------------------------------------------ 0 1601 88007 1 1523
 * 83407 2 1447 78889 3 1373 74453 4 1301 70099 5 1231 65827 6 1163 61637 7 1097
 * 57529 8 1033 53503 9 971 49559 10 911 45697 11 853 41917c 12 797 38219 13 743
 * 34603 14 691 31069 15 641 27617 16 593 24247 17 547 20959 18 503 17753c 19
 * 461 14629 20 421 11587 21 383 8627 22 347 5749 23 313 2953 24 281 239 25 251
 * 2393 26 223 4943 27 197 7411 28 173 9797c 29 151 12101 30 131 14323 31 113
 * 16463c 32 97 18521 33 83 20497c 34 71 22391 35 61 24203 36 53 25933 37 47
 * 27581 38 43 29147 39 41 30631 40 41 32033c 41 43 33353 42 47 34591 43 53
 * 35747 44 61 36821 45 71 37813 46 83 38723 47 97 39551 48 113 40297c 49 131
 * 40961 50 151 41543 51 173 42043 52 197 42461 53 223 42797 54 251 43051 55 281
 * 43223 56 313 43313 57 347 43321 58 383 43247c 59 421 43091c 60 461 42853 61
 * 503 42533 62 547 42131 63 593 41647 64 641 41081 65 691 40433 66 743 39703 67
 * 797 38891 68 853 37997 69 911 37021 70 971 35963 71 1033 34823c 72 1097 33601
 * 73 1163 32297 74 1231 30911 75 1301 29443 76 1373 27893 77 1447 26261 78 1523
 * 24547 79 1601 22751 80 1681c 20873 81 1763c 18913 82 1847 16871 83 1933 14747
 * 84 2021c 12541 85 2111 10253 86 2203 7883 87 2297 5431 88 2393 2897 89 2491c
 * 281 90 2591 2417 91 2693 5197 92 2797 8059 93 2903 11003 94 3011 14029 95
 * 3121 17137 96 3233c 20327 97 3347 23599 98 3463 26953 99 3581 30389 95 primes
 * 90 primes 55 distinct 90 distinct
 */
