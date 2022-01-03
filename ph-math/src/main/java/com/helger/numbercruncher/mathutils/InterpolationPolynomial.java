/*
 * Copyright (C) 2014-2022 Philip Helger (www.helger.com)
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A polynomial interpolation function.
 */
public class InterpolationPolynomial implements IEvaluatable
{
  /** number of data points */
  private int m_nDataPoints;
  /** array of data points */
  private final DataPoint [] m_aData;
  /** divided difference table */
  private final float [] [] m_aDivDiff;

  /**
   * Constructor.
   *
   * @param data
   *        the array of data points
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP2")
  public InterpolationPolynomial (final DataPoint [] data)
  {
    m_aData = data;
    m_aDivDiff = new float [data.length] [data.length];

    for (final DataPoint element : data)
    {
      addDataPoint (element);
    }
  }

  /**
   * Constructor.
   *
   * @param maxPoints
   *        the maximum number of data points
   */
  public InterpolationPolynomial (final int maxPoints)
  {
    this.m_aData = new DataPoint [maxPoints];
    this.m_aDivDiff = new float [m_aData.length] [m_aData.length];
  }

  /**
   * Return the data points.
   *
   * @return the array of data points
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public DataPoint [] getDataPoints ()
  {
    return m_aData;
  }

  /**
   * Return the divided difference table.
   *
   * @return the table
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public float [] [] getDividedDifferenceTable ()
  {
    return m_aDivDiff;
  }

  /**
   * Return the current number of data points.
   *
   * @return the count
   */
  public int getDataPointCount ()
  {
    return m_nDataPoints;
  }

  /**
   * Add new data point: Augment the divided difference table by appending a new
   * entry at the bottom of each column.
   *
   * @param dataPoint
   *        the new data point
   */
  public void addDataPoint (final DataPoint dataPoint)
  {
    if (m_nDataPoints >= m_aData.length)
      return;

    m_aData[m_nDataPoints] = dataPoint;
    m_aDivDiff[m_nDataPoints][0] = dataPoint.getY ();

    ++m_nDataPoints;

    for (int order = 1; order < m_nDataPoints; ++order)
    {
      final int bottom = m_nDataPoints - order - 1;
      final float numerator = m_aDivDiff[bottom + 1][order - 1] - m_aDivDiff[bottom][order - 1];
      final float denominator = m_aData[bottom + order].getX () - m_aData[bottom].getX ();

      m_aDivDiff[bottom][order] = numerator / denominator;
    }
  }

  /**
   * Return the value of the polynomial interpolation function at x.
   * (Implementation of Evaluatable.)
   *
   * @param x
   *        the value of x
   * @return the value of the function at x
   */
  public float at (final float x)
  {
    if (m_nDataPoints < 2)
      return Float.NaN;

    float y = m_aDivDiff[0][0];
    float xFactor = 1;

    // Compute the value of the function.
    for (int order = 1; order < m_nDataPoints; ++order)
    {
      xFactor = xFactor * (x - m_aData[order - 1].getX ());
      y = y + xFactor * m_aDivDiff[0][order];
    }

    return y;
  }

  /**
   * Reset.
   */
  public void reset ()
  {
    m_nDataPoints = 0;
  }
}
