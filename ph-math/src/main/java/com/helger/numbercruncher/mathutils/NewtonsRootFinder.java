/**
 * Copyright (C) 2014-2018 Philip Helger (www.helger.com)
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
 * The root finder class that implements Newton's algorithm.
 */
public class NewtonsRootFinder extends AbstractRootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x[n] value */
  private float m_fXn;
  /** x[n+1] value */
  private float m_fXnp1;
  /** previous x[n+1] value */
  private float m_fPrevXnp1;
  /** f(x[n]) */
  private float m_fFn;
  /** f(x[n+1]) */
  private float m_fFnp1;
  /** f'(x[n]) */
  private float m_fFpn;

  /**
   * Constructor.
   *
   * @param function
   *        the functions whose roots to find
   */
  public NewtonsRootFinder (final AbstractFunction function)
  {
    super (function, MAX_ITERS);
  }

  /**
   * Reset.
   *
   * @param x0
   *        the initial x-value
   */
  public void reset (final float x0)
  {
    super.reset ();

    m_fXnp1 = x0;
    m_fFnp1 = m_aFunction.at (m_fXnp1);
  }

  // ---------//
  // Getters //
  // ---------//

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

  /**
   * Return the current value of f'(x[n]).
   *
   * @return the value
   */
  public float getFpn ()
  {
    return m_fFpn;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do Newton's iteration procedure.
   *
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    m_fXn = m_fXnp1;
  }

  /**
   * Compute the next position of x[n+1].
   */
  @Override
  protected void computeNextPosition ()
  {
    m_fFn = m_fFnp1;
    m_fFpn = m_aFunction.derivativeAt (m_fXn);

    // Compute the value of x[n+1].
    m_fPrevXnp1 = m_fXnp1;
    m_fXnp1 = m_fXn - m_fFn / m_fFpn;

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
