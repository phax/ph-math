/*
 * Copyright (C) 2014-2024 Philip Helger (www.helger.com)
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
 * The root finder class that implements the secant algorithm.
 */
public class SecantRootFinder extends AbstractRootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x[n-1] value */
  private float m_fXnm1;
  /** x[n] value */
  private float m_fXn;
  /** x[n+1] value */
  private float m_fXnp1 = Float.NaN;
  /** previous value of x[n+1] */
  private float m_fPrevXnp1;
  /** f(x[n-1]) */
  private float m_fFnm1;
  /** f([n]) */
  private float m_fFn;
  /** f(x[n+1]) */
  private float m_fFnp1;

  /**
   * Constructor.
   *
   * @param function
   *        the functions whose roots to find
   * @param x0
   *        the first initial x-value
   * @param x1
   *        the second initial x-value
   */
  public SecantRootFinder (final AbstractFunction function, final float x0, final float x1)
  {
    super (function, MAX_ITERS);

    // Initialize x[n-1], x[n], f(x[n-1]), and f(x[n]).
    m_fXnm1 = x0;
    m_fFnm1 = function.at (m_fXnm1);
    m_fXn = x1;
    m_fFn = function.at (m_fXn);
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return the current value of x[n-1].
   *
   * @return the value
   */
  public float getXnm1 ()
  {
    return m_fXnm1;
  }

  /**
   * Return the current value of x[n].
   *
   * @return the value
   */
  public float getXn ()
  {
    return m_fXn;
  }

  /**
   * Return the current value of x[n+1].
   *
   * @return the value
   */
  public float getXnp1 ()
  {
    return m_fXnp1;
  }

  /**
   * Return the current value of f(x[n-1]).
   *
   * @return the value
   */
  public float getFnm1 ()
  {
    return m_fFnm1;
  }

  /**
   * Return the current value of f(x[n]).
   *
   * @return the value
   */
  public float getFn ()
  {
    return m_fFn;
  }

  /**
   * Return the current value of f(x[n+1]).
   *
   * @return the value
   */
  public float getFnp1 ()
  {
    return m_fFnp1;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do the secant iteration procedure.
   *
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    if (n == 1)
      return; // already initialized

    // Use the latest two points.
    m_fXnm1 = m_fXn; // x[n-1] = x[n]
    m_fXn = m_fXnp1; // x[n] = x[n+1]
    m_fFnm1 = m_fFn; // f(x[n-1]) = f(x[n])
    m_fFn = m_fFnp1; // f(x[n]) = f(x[n+1])
  }

  /**
   * Compute the next position of x[n+1].
   */
  @Override
  protected void computeNextPosition ()
  {
    m_fPrevXnp1 = m_fXnp1;
    m_fXnp1 = m_fXn - m_fFn * (m_fXnm1 - m_fXn) / (m_fFnm1 - m_fFn);
    m_fFnp1 = m_aFunction.at (m_fXnp1);
  }

  /**
   * Check the position of x[n+1].
   *
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws AbstractRootFinder.PositionUnchangedException
  {
    if (EqualsHelper.equals (m_fXnp1, m_fPrevXnp1))
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
    return Math.abs (m_fFnp1) < TOLERANCE;
  }
}
