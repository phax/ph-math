/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.primeutils;

/**
 * Primality test that combines the Miller-Rabin and Lucas tests.
 */
public final class PrimalityFuncTest
{
  /** number to test for primality */
  private final int m_nP;
  /**
   * number of times to run the Miller-Rabin test
   */
  private final int m_nIterations;

  /**
   * Constructor.
   *
   * @param p
   *        the number to test for primality
   * @param iterations
   *        the number of times to run the Miller-Rabin test
   */
  public PrimalityFuncTest (final int p, final int iterations)
  {
    this.m_nP = p;
    this.m_nIterations = iterations;
  }

  /**
   * Perform the primality test.
   *
   * @return true if p is prime, false if p is composite
   */
  public boolean test ()
  {
    return (new MillerRabinFuncTest (m_nP, m_nIterations, null)).test () && (new LucasFuncTest (m_nP, null)).test ();
  }
}
