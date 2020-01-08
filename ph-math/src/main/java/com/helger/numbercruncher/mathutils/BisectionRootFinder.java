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

import com.helger.commons.equals.EqualsHelper;

/**
 * The root finder class that implements the bisection algorithm.
 */
public class BisectionRootFinder extends AbstractRootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x-negative value */
  private float m_fXNeg;
  /** x-middle value */
  private float m_fXMid = Float.NaN;
  /** x-positive value */
  private float m_fXPos;
  /** previous x-middle value */
  private float m_fPrevXMid;
  /** f(xNeg) */
  private float m_fNeg;
  /** f(xMid) */
  private float m_fMid;
  /** f(xPos) */
  private float m_fPos;

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
   *         in checkInterval
   */
  public BisectionRootFinder (final AbstractFunction function,
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
   * Return the current value of x-middle.
   *
   * @return the value
   */
  public float getXMid ()
  {
    return m_fXMid;
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
   * Return the current value of f(x-middle).
   *
   * @return the value
   */
  public float getFMid ()
  {
    return m_fMid;
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
   * Do the bisection iteration procedure.
   *
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    if (n == 1)
      return; // already initialized

    if (m_fMid < 0)
    {
      m_fXNeg = m_fXMid; // the root is in the xPos half
      m_fNeg = m_fMid;
    }
    else
    {
      m_fXPos = m_fXMid; // the root is in the xNeg half
      m_fPos = m_fMid;
    }
  }

  /**
   * Compute the next position of xMid.
   */
  @Override
  protected void computeNextPosition ()
  {
    m_fPrevXMid = m_fXMid;
    m_fXMid = (m_fXNeg + m_fXPos) / 2;
    m_fMid = m_aFunction.at (m_fXMid);
  }

  /**
   * Check the position of xMid.
   *
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws AbstractRootFinder.PositionUnchangedException
  {
    if (EqualsHelper.equals (m_fXMid, m_fPrevXMid))
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
    return Math.abs (m_fMid) < TOLERANCE;
  }
}
