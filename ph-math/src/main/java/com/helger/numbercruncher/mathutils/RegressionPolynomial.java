/**
 * Copyright (C) 2014-2020 Philip Helger (www.helger.com)
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

import com.helger.numbercruncher.matrix.ColumnVector;
import com.helger.numbercruncher.matrix.LinearSystem;
import com.helger.numbercruncher.matrix.MatrixException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A least-squares regression polynomial function.
 */
public class RegressionPolynomial implements IEvaluatable
{
  /** number of data points */
  private int m_n;
  /** degree of the polynomial */
  private final int m_nDegree;
  /** maximum no. of data points */
  private final int m_nMaxPoints;
  /** true if coefficients valid */
  private boolean m_bCoefsValid;
  /** warning message */
  private String m_sWarningMsg;

  /** data points */
  private DataPoint [] m_aData;

  /** coefficient matrix A */
  private LinearSystem m_aA;
  /** regression coefficients vector a */
  private ColumnVector m_aVA;
  /** right-hand-side vector b */
  private ColumnVector m_aVB;

  /**
   * Constructor.
   *
   * @param degree
   *        the degree of the polynomial
   * @param maxPoints
   *        the maximum number of data points
   */
  public RegressionPolynomial (final int degree, final int maxPoints)
  {
    m_nDegree = degree;
    m_nMaxPoints = maxPoints;
    m_aData = new DataPoint [maxPoints];
  }

  /**
   * Constructor.
   *
   * @param degree
   *        the degree of the polynomial
   * @param data
   *        the array of data points
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP2")
  public RegressionPolynomial (final int degree, final DataPoint [] data)
  {
    m_nDegree = degree;
    m_nMaxPoints = data.length;
    m_aData = data;
    m_n = data.length;
  }

  /**
   * Return the degree of the polynomial.
   *
   * @return the count
   */
  public int getDegree ()
  {
    return m_nDegree;
  }

  /**
   * Return the current number of data points.
   *
   * @return the count
   */
  public int getDataPointCount ()
  {
    return m_n;
  }

  /**
   * Return the data points.
   *
   * @return the count
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public DataPoint [] getDataPoints ()
  {
    return m_aData;
  }

  /**
   * Return the coefficients matrix.
   *
   * @return the A matrix
   * @throws MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public LinearSystem getCoefficientsMatrix () throws Exception, MatrixException
  {
    _validateCoefficients ();
    return m_aA;
  }

  /**
   * Return the regression coefficients.
   *
   * @return the a vector
   * @throws MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public ColumnVector getRegressionCoefficients () throws Exception, MatrixException
  {
    _validateCoefficients ();
    return m_aVA;
  }

  /**
   * Return the right hand side.
   *
   * @return the b vector
   * @throws MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public ColumnVector getRHS () throws Exception, MatrixException
  {
    _validateCoefficients ();
    return m_aVB;
  }

  /**
   * Return the warning message (if any).
   *
   * @return the message or null
   */
  public String getWarningMessage ()
  {
    return m_sWarningMsg;
  }

  /**
   * Add a new data point: Update the sums.
   *
   * @param dataPoint
   *        the new data point
   */
  public void addDataPoint (final DataPoint dataPoint)
  {
    if (m_n == m_nMaxPoints)
      return;

    m_aData[m_n++] = dataPoint;
    m_bCoefsValid = false;
  }

  /**
   * Return the value of the regression polynomial function at x.
   * (Implementation of Evaluatable.)
   *
   * @param x
   *        the value of x
   * @return the value of the function at x
   */
  public float at (final float x)
  {
    if (m_n < m_nDegree + 1)
      return Float.NaN;

    try
    {
      _validateCoefficients ();

      float xPower = 1;
      float y = 0;

      // Compute y = a[0] + a[1]*x + a[2]*x^2 + ... + a[n]*x^n
      for (int i = 0; i <= m_nDegree; ++i)
      {
        y += m_aVA.at (i) * xPower;
        xPower *= x;
      }

      return y;
    }
    catch (final MatrixException ex)
    {
      return Float.NaN;
    }
    catch (final Exception ex)
    {
      return Float.NaN;
    }
  }

  /**
   * Reset.
   */
  public void reset ()
  {
    m_n = 0;
    m_aData = new DataPoint [m_nMaxPoints];
    m_bCoefsValid = false;
  }

  /**
   * Compute the coefficients.
   *
   * @throws MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public void computeCoefficients () throws Exception, MatrixException
  {
    _validateCoefficients ();
  }

  /**
   * Validate the coefficients.
   *
   * @throws MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  private void _validateCoefficients () throws Exception, MatrixException
  {
    if (m_bCoefsValid)
      return;

    m_aA = new LinearSystem (m_nDegree + 1);
    m_aVB = new ColumnVector (m_nDegree + 1);

    // Compute the multipliers of a[0] for each equation.
    for (int r = 0; r <= m_nDegree; ++r)
    {
      final float sum = _sumXPower (r);
      int j = 0;

      if (Float.isInfinite (sum))
      {
        throw new Exception ("Overflow occurred.");
      }

      // Set the multipliers along the diagonal.
      for (int i = r; i >= 0; --i)
        m_aA.set (i, j++, sum);

      // Set the right-hand-side value.
      m_aVB.set (r, _sumXPowerY (r));
    }

    // Compute the multipliers of a[c] for the last equation.
    for (int c = 1; c <= m_nDegree; ++c)
    {
      final float sum = _sumXPower (m_nDegree + c);
      int i = m_nDegree;

      if (Float.isInfinite (sum))
      {
        throw new Exception ("Overflow occurred.");
      }

      // Set the multipliers along the diagonal.
      for (int j = c; j <= m_nDegree; ++j)
        m_aA.set (i--, j, sum);
    }

    m_sWarningMsg = null;

    // First try solving with iterative improvement. If that
    // fails, then try solving without iterative improvement.
    try
    {
      m_aVA = m_aA.solve (m_aVB, true);
    }
    catch (final MatrixException ex)
    {
      m_sWarningMsg = ex.getMessage ();
      m_aVA = m_aA.solve (m_aVB, false);
    }

    m_bCoefsValid = true;
  }

  /**
   * Compute the sum of the x coordinates each raised to an integer power.
   *
   * @return the sum
   */
  private float _sumXPower (final int power)
  {
    float sum = 0;

    for (int i = 0; i < m_n; ++i)
    {
      sum += (float) IntPower.raise (m_aData[i].getX (), power);
    }

    return sum;
  }

  /**
   * Compute the sum of the x coordinates each raised to an integer power and
   * multiplied by the corresponding y coordinate.
   *
   * @return the sum
   */
  private float _sumXPowerY (final int power)
  {
    float sum = 0;

    for (int i = 0; i < m_n; ++i)
    {
      sum += m_aData[i].getY () * IntPower.raise (m_aData[i].getX (), power);
    }

    return sum;
  }
}
