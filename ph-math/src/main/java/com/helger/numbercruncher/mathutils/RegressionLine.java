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
package com.helger.numbercruncher.mathutils;

/**
 * A least-squares regression line function.
 */
public class RegressionLine implements IEvaluatable
{
  /** sum of x */
  private double m_dSumX;
  /** sum of y */
  private double m_dSumY;
  /** sum of x*x */
  private double m_dSumXX;
  /** sum of x*y */
  private double m_dSumXY;

  /** line coefficient a0 */
  private float m_fA0;
  /** line coefficient a1 */
  private float m_fA1;

  /** number of data points */
  private int m_nDataPoints;
  /** true if coefficients valid */
  private boolean m_bCoefsValid;

  /**
   * Constructor.
   */
  public RegressionLine ()
  {}

  /**
   * Constructor.
   *
   * @param data
   *        the array of data points
   */
  public RegressionLine (final DataPoint [] data)
  {
    for (final DataPoint element : data)
    {
      addDataPoint (element);
    }
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
   * Return the coefficient a0.
   *
   * @return the value of a0
   */
  public float getA0 ()
  {
    _validateCoefficients ();
    return m_fA0;
  }

  /**
   * Return the coefficient a1.
   *
   * @return the value of a1
   */
  public float getA1 ()
  {
    _validateCoefficients ();
    return m_fA1;
  }

  /**
   * Return the sum of the x values.
   *
   * @return the sum
   */
  public double getSumX ()
  {
    return m_dSumX;
  }

  /**
   * Return the sum of the y values.
   *
   * @return the sum
   */
  public double getSumY ()
  {
    return m_dSumY;
  }

  /**
   * Return the sum of the x*x values.
   *
   * @return the sum
   */
  public double getSumXX ()
  {
    return m_dSumXX;
  }

  /**
   * Return the sum of the x*y values.
   *
   * @return the sum
   */
  public double getSumXY ()
  {
    return m_dSumXY;
  }

  /**
   * Add a new data point: Update the sums.
   *
   * @param dataPoint
   *        the new data point
   */
  public void addDataPoint (final DataPoint dataPoint)
  {
    m_dSumX += dataPoint.getX ();
    m_dSumY += dataPoint.getY ();
    m_dSumXX += dataPoint.getX () * dataPoint.getX ();
    m_dSumXY += dataPoint.getX () * dataPoint.getY ();

    ++m_nDataPoints;
    m_bCoefsValid = false;
  }

  /**
   * Return the value of the regression line function at x.
   *
   * @param x
   *        the value of x
   * @return the value of the function at x
   */
  public float at (final float x)
  {
    if (m_nDataPoints < 2)
      return Float.NaN;

    _validateCoefficients ();
    return m_fA0 + m_fA1 * x;
  }

  /**
   * Reset.
   */
  public void reset ()
  {
    m_nDataPoints = 0;
    m_dSumX = m_dSumY = m_dSumXX = m_dSumXY = 0;
    m_bCoefsValid = false;
  }

  /**
   * Validate the coefficients.
   */
  private void _validateCoefficients ()
  {
    if (m_bCoefsValid)
      return;

    if (m_nDataPoints >= 2)
    {
      final float xBar = (float) m_dSumX / m_nDataPoints;
      final float yBar = (float) m_dSumY / m_nDataPoints;

      m_fA1 = (float) ((m_nDataPoints * m_dSumXY - m_dSumX * m_dSumY) / (m_nDataPoints * m_dSumXX - m_dSumX * m_dSumX));
      m_fA0 = yBar - m_fA1 * xBar;
    }
    else
    {
      m_fA0 = m_fA1 = Float.NaN;
    }

    m_bCoefsValid = true;
  }
}
