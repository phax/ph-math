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

import java.util.Random;

/**
 * Utility class that generates normally-distributed random values using several
 * algorithms.
 */
public class RandomNormal
{
  /** mean */
  private float m_fMean;
  /** standard deviation */
  private float m_fStddev;

  /**
   * next random value from the polar algorithm
   */
  private float m_fNextPolar;
  /**
   * true if the next polar value is available
   */
  private boolean m_bHaveNextPolar = false;

  /** generator of uniformly-distributed random values */
  private static final Random GENERATOR = new Random ();

  /**
   * Set the mean and standard deviation.
   *
   * @param mean
   *        the mean
   * @param stddev
   *        the standard deviation
   */
  public void setParameters (final float mean, final float stddev)
  {
    m_fMean = mean;
    m_fStddev = stddev;
  }

  /**
   * Compute the next random value using the Central Limit Theorem, which states
   * that the averages of sets of uniformly-distributed random values are
   * normally distributed.
   *
   * @return next value
   */
  public float nextCentral ()
  {
    // Average 12 uniformly-distributed random values.
    float sum = 0.0f;
    for (int j = 0; j < 12; ++j)
      sum += GENERATOR.nextFloat ();

    // Subtract 6 to center about 0.
    return m_fStddev * (sum - 6) + m_fMean;
  }

  /**
   * Compute the next random value using the polar algorithm. Requires two
   * uniformly-distributed random values in [-1, +1). Actually computes two
   * random values and saves the second one for the next invocation.
   *
   * @return next value
   */
  public float nextPolar ()
  {
    // If there's a saved value, return it.
    if (m_bHaveNextPolar)
    {
      m_bHaveNextPolar = false;
      return m_fNextPolar;
    }

    // point coordinates and their radius
    float u1, u2, r;

    do
    {
      // u1 and u2 will be uniformly-distributed
      // random values in [-1, +1).
      u1 = 2 * GENERATOR.nextFloat () - 1;
      u2 = 2 * GENERATOR.nextFloat () - 1;

      // Want radius r inside the unit circle.
      r = u1 * u1 + u2 * u2;
    } while (r >= 1);

    // Factor incorporates the standard deviation.
    final float factor = (float) (m_fStddev * Math.sqrt (-2 * Math.log (r) / r));

    // v1 and v2 are normally-distributed random values.
    final float v1 = factor * u1 + m_fMean;
    final float v2 = factor * u2 + m_fMean;

    // Save v1 for next time.
    m_fNextPolar = v1;
    m_bHaveNextPolar = true;

    return v2;
  }

  // Constants for the ratio algorithm.
  private static final float C1 = (float) Math.sqrt (8 / Math.E);
  private static final float C2 = (float) (4 * Math.exp (0.25));
  private static final float C3 = (float) (4 * Math.exp (-1.35));

  /**
   * Compute the next random value using the ratio algorithm. Requires two
   * uniformly-distributed random values in [0, 1).
   *
   * @return next value
   */
  public float nextRatio ()
  {
    float u, v, x, xx;

    do
    {
      // u and v are two uniformly-distributed random values
      // in [0, 1), and u != 0.
      while ((u = GENERATOR.nextFloat ()) == 0)
      {
        // try again if 0
      }
      v = GENERATOR.nextFloat ();

      // y coord of point (u, y)
      final float y = C1 * (v - 0.5f);

      // ratio of point's coords
      x = y / u;

      xx = x * x;
    } while ((xx > 5f - C2 * u) && // quick acceptance
             ((xx >= C3 / u + 1.4f) || // quick rejection
              (xx > (float) (-4 * Math.log (u)))) // final test
    );

    return m_fStddev * x + m_fMean;
  }
}
