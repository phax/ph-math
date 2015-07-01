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
package numbercruncher.mathutils;

import com.helger.commons.equals.EqualsHelper;

/**
 * The root finder class that implements the regula falsi algorithm.
 */
public class RegulaFalsiRootFinder extends AbstractRootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x-negative value */
  protected float m_fXNeg;
  /** x-false value */
  protected float m_fXFalse = Float.NaN;
  /** x-positive value */
  protected float m_fXPos;
  /** previous x-false value */
  protected float m_fPrevXFalse;
  /** f(xNeg) */
  protected float m_fNeg;
  /** f(xFalse) */
  protected float m_fFalse = Float.NaN;
  /** f(xPos) */
  protected float m_fPos;

  /**
   * Constructor.
   *
   * @param function
   *        the functions whose roots to find
   * @param xMin
   *        the initial x-value where the function is negative
   * @param xMax
   *        the initial x-value where the function is positive
   * @throws AbstractRootFinder.InvalidIntervalException
   */
  public RegulaFalsiRootFinder (final AbstractFunction function,
                                final float xMin,
                                final float xMax) throws AbstractRootFinder.InvalidIntervalException
  {
    super (function, MAX_ITERS);
    checkInterval (xMin, xMax);

    final float yMin = function.at (xMin);
    final float yMax = function.at (xMax);

    // Initialize xNeg, fNeg, xPos, and fPos.
    if (yMin < 0)
    {
      m_fXNeg = xMin;
      m_fXPos = xMax;
      m_fNeg = yMin;
      m_fPos = yMax;
    }
    else
    {
      m_fXNeg = xMax;
      m_fXPos = xMin;
      m_fNeg = yMax;
      m_fPos = yMin;
    }
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return the current value of x-negative.
   *
   * @return the value
   */
  public float getXNeg ()
  {
    return m_fXNeg;
  }

  /**
   * Return the current value of x-false.
   *
   * @return the value
   */
  public float getXFalse ()
  {
    return m_fXFalse;
  }

  /**
   * Return the current value of x-positive.
   *
   * @return the value
   */
  public float getXPos ()
  {
    return m_fXPos;
  }

  /**
   * Return the current value of f(x-negative).
   *
   * @return the value
   */
  public float getFNeg ()
  {
    return m_fNeg;
  }

  /**
   * Return the current value of f(x-false).
   *
   * @return the value
   */
  public float getFFalse ()
  {
    return m_fFalse;
  }

  /**
   * Return the current value of f(x-positive).
   *
   * @return the value
   */
  public float getFPos ()
  {
    return m_fPos;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do the regula falsi iteration procedure.
   *
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    if (n == 1)
      return; // already initialized

    if (m_fFalse < 0)
    {
      m_fXNeg = m_fXFalse; // the root is in the xPos side
      m_fNeg = m_fFalse;
    }
    else
    {
      m_fXPos = m_fXFalse; // the root is in the xNeg side
      m_fPos = m_fFalse;
    }
  }

  /**
   * Compute the next position of x-false.
   */
  @Override
  protected void computeNextPosition ()
  {
    m_fPrevXFalse = m_fXFalse;
    m_fXFalse = m_fXPos - m_fPos * (m_fXNeg - m_fXPos) / (m_fNeg - m_fPos);
    m_fFalse = m_aFunction.at (m_fXFalse);
  }

  /**
   * Check the position of x-false.
   *
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws AbstractRootFinder.PositionUnchangedException
  {
    if (EqualsHelper.equals (m_fXFalse, m_fPrevXFalse))
    {
      throw new AbstractRootFinder.PositionUnchangedException ();
    }
  }

  /**
   * Indicate whether or not the algorithm has converged.
   *
   * @return true if converged, else false
   */
  @Override
  protected boolean hasConverged ()
  {
    return Math.abs (m_fFalse) < TOLERANCE;
  }
}
