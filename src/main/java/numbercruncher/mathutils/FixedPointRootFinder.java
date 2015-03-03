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

/**
 * The root finder class that implements the fixed-point iteration algorithm.
 */
public class FixedPointRootFinder extends AbstractRootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x[n] value */
  private float m_fXn = Float.NaN;
  /** previous x[n] value */
  private float m_fPrevXn;
  /** g(x[n]) */
  private float m_fGn;

  /**
   * Constructor.
   * 
   * @param function
   *        the functions whose roots to find
   */
  public FixedPointRootFinder (final AbstractFunction function)
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
    m_fGn = x0;
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
   * Return the current value of g(x[n]).
   * 
   * @return the value
   */
  public float getGn ()
  {
    return m_fGn;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do the fixed point iteration procedure. (Nothing to do!)
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {}

  /**
   * Compute the next position of xn.
   */
  @Override
  protected void computeNextPosition ()
  {
    m_fPrevXn = m_fXn;
    m_fXn = m_fGn;
    m_fGn = m_aFunction.at (m_fXn);
  }

  /**
   * Check the position of xn.
   * 
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws AbstractRootFinder.PositionUnchangedException
  {
    if (m_fXn == m_fPrevXn)
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
    return Math.abs ((m_fGn - m_fXn) / m_fXn) < TOLERANCE;
  }
}
