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
 * Raise a double value to an integer power.
 */
public final class IntPower
{
  /**
   * Compute and return x^power.
   *
   * @param px
   *        the x value
   * @param pexponent
   *        the integer power
   * @return x^power
   */
  public static double raise (final double px, final int pexponent)
  {
    double x = px;
    int exponent = pexponent;
    if (exponent < 0)
      return 1 / raise (x, -exponent);

    double power = 1;

    // Loop to compute x^exponent.
    while (exponent > 0)
    {

      // Is the rightmost exponent bit a 1?
      if ((exponent & 1) == 1)
        power *= x;

      // Square x and shift the exponent 1 bit to the right.
      x *= x;
      exponent >>= 1;
    }

    return power;
  }
}
