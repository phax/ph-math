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
package com.helger.numbercruncher.mathutils;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;

/**
 * Compute the Sieve of Eratosthenes and prime factors.
 */
public class PrimeFactors
{
  /**
   * Compute the Sieve of Eratosthenes.
   *
   * @param n
   *        the size of the sieve
   * @return the sieve as a boolean array (each element is true if the
   *         corresponding number is prime, false if the number is composite)
   */
  public static boolean [] primeSieve (final int n)
  {
    final int halfN = (n + 1) >> 1;
    final boolean sieve[] = new boolean [n + 1];

    // Initialize every integer from 2 onwards to prime.
    for (int i = 2; i <= n; ++i)
      sieve[i] = true;

    int prime = 2; // first prime number

    // Loop to create the sieve.
    while (prime < halfN)
    {

      // Mark as composites multiples of the prime.
      for (int composite = prime << 1; composite <= n; composite += prime)
        sieve[composite] = false;

      // Skip over composites to the next prime.
      while ((++prime < halfN) && (!sieve[prime]))
      {}
    }

    return sieve;
  }

  /**
   * Compute the prime factors of an integer value.
   *
   * @param pn
   *        the value to factor
   * @return an array of distinct prime factors
   */
  public static int [] factorsOf (final int pn)
  {
    int n = pn;
    final boolean isPrime[] = primeSieve (n); // primes <= n
    final ICommonsList <Integer> v = new CommonsArrayList <> ();

    // Loop to try prime divisors.
    for (int factor = 2; n > 1; ++factor)
    {
      if (isPrime[factor] && (n % factor == 0))
      {

        // Prime divisor found.
        v.add (Integer.valueOf (factor));

        // Factor out multiples of the divisor.
        do
        {
          n /= factor;
        } while (n % factor == 0);
      }
    }

    // Create an array of the distinct prime factors.
    final int factors[] = new int [v.size ()];
    for (int i = 0; i < v.size (); ++i)
    {
      factors[i] = v.get (i).intValue ();
    }
    return factors;
  }
}
