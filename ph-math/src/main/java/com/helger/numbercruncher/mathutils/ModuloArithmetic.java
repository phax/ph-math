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

/**
 * Perform multiplication and exponentiation modulo arithmetic.
 */
public class ModuloArithmetic
{
  /**
   * Multiply two integer values a and b modulo m.
   *
   * @param pa
   *        the value of a
   * @param pb
   *        the value of b
   * @param m
   *        the modulus m
   * @return the value of ab (mod m)
   */
  public static int multiply (final int pa, final int pb, final int m)
  {
    int a = pa;
    int b = pb;
    int product = 0;

    // Loop to compute product = (a*b)%m.
    while (a > 0)
    {

      // Does the rightmost bit of a == 1?
      if ((a & 1) == 1)
      {
        product += b;
        product %= m;
      }

      // Double b modulo m, and
      // shift a 1 bit to the right.
      b <<= 1;
      b %= m;
      a >>= 1;
    }

    return product;
  }

  /**
   * Raise a to the b power modulo m.
   *
   * @param pbase
   *        the value of a
   * @param pexponent
   *        the value of b
   * @param m
   *        the modulus m
   * @return the value of a^b (mod m)
   */
  public static int raise (final int pbase, final int pexponent, final int m)
  {
    int base = pbase;
    int exponent = pexponent;
    int power = 1;

    // Loop to compute power = (base^exponent)%m.
    while (exponent > 0)
    {

      // Does the rightmost bit of the exponent == 1?
      if ((exponent & 1) == 1)
      {
        power = multiply (power, base, m);
      }

      // Square the base modulo m and
      // shift the exponent 1 bit to the right.
      base = multiply (base, base, m);
      exponent >>= 1;
    }

    return power;
  }
}
