/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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
package numbercruncher.randomutils;

import numbercruncher.mathutils.SystemOutAlignRight;

/**
 * Counters of random values that fall within each interval.
 */
public class Buckets
{
  private static final int MAX_BAR_SIZE = 50;

  private final SystemOutAlignRight m_aAlignRight = new SystemOutAlignRight ();

  /** number of intervals */
  private final int m_n;
  /** counters per interval */
  private final int m_aCounters[];

  /** minimum random value */
  private float m_fMin;
  /** maximum random value */
  private float m_fMax;
  /** from min to max */
  private float m_fWidth;

  /**
   * Constructor.
   *
   * @param n
   *        the number of intervals
   */
  public Buckets (final int n)
  {
    m_n = n;
    m_aCounters = new int [n];
    clear ();
  }

  /**
   * Return the counter value for interval i.
   *
   * @param i
   *        the value of i
   * @return the counter value
   */
  public int get (final int i)
  {
    return m_aCounters[i];
  }

  /**
   * Set the minimum and maximum random values.
   *
   * @param rMin
   *        the minimum value
   * @param rMax
   *        the maximum value
   */
  public void setLimits (final float rMin, final float rMax)
  {
    this.m_fMin = rMin;
    this.m_fMax = rMax;
    this.m_fWidth = (rMax - rMin) / m_n;
  }

  /**
   * Determine a random value's interval and count it.
   *
   * @param r
   *        the random value
   */
  public void put (final float r)
  {
    // Ignore the value if it's out of range.
    if ((r < m_fMin) || (r > m_fMax))
      return;

    // Determine its interval and count it.
    final int i = (int) ((r - m_fMin) / m_fWidth);
    ++m_aCounters[i];
  }

  /**
   * Clear all the interval counters.
   */
  public void clear ()
  {
    for (int i = 0; i < m_aCounters.length; ++i)
      m_aCounters[i] = 0;
  }

  /**
   * Print the counter values as a horizontal bar chart. Scale the chart so that
   * the longest bar is MAX_BAR_SIZE.
   */
  public void print ()
  {
    // Get the longest bar's length.
    int maxCount = 0;
    for (int i = 0; i < m_n; ++i)
    {
      maxCount = Math.max (maxCount, m_aCounters[i]);
    }

    // Compute the scaling factor.
    final float factor = ((float) MAX_BAR_SIZE) / maxCount;

    // Loop to print each bar.
    for (int i = 0; i < m_n; ++i)
    {
      final int b = m_aCounters[i];

      // Interval number.
      m_aAlignRight.print (i, 2);
      m_aAlignRight.print (b, 7);
      System.out.print (": ");

      // Bar.
      final int length = Math.round (factor * b);
      for (int j = 0; j < length; ++j)
        System.out.print ("*");
      System.out.println ();
    }
  }
}
