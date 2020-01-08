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

/**
 * The root finder class that implements the improved regula falsi algorithm.
 */
public class ImprovedRegulaFalsiRootFinder extends RegulaFalsiRootFinder
{
  /** previous f(xFalse) value */
  private float m_fPrevFFalse;

  private boolean m_bDecreasePos = false;
  private boolean m_bDecreaseNeg = false;

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
  public ImprovedRegulaFalsiRootFinder (final AbstractFunction function,
                                        final float xMin,
                                        final float xMax) throws AbstractRootFinder.InvalidIntervalException
  {
    super (function, xMin, xMax);
  }

  // ----------------------------------------//
  // Override RegulaFalsiRootFinder methods //
  // ----------------------------------------//

  /**
   * Do the improved regula falsi iteration procedure.
   *
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    super.doIterationProcedure (n);

    // Decrease the slope of the secant?
    if (m_bDecreasePos)
      m_fPos /= 2;
    if (m_bDecreaseNeg)
      m_fNeg /= 2;
  }

  /**
   * Compute the next position of xFalse.
   */
  @Override
  protected void computeNextPosition ()
  {
    m_fPrevXFalse = m_fXFalse;
    m_fPrevFFalse = m_fFalse;
    m_fXFalse = m_fXPos - m_fPos * (m_fXNeg - m_fXPos) / (m_fNeg - m_fPos);
    m_fFalse = m_aFunction.at (m_fXFalse);

    m_bDecreasePos = m_bDecreaseNeg = false;

    // If there was no sign change in f(xFalse),
    // or if this is the first iteration step,
    // then decrease the slope of the secant.
    if (Float.isNaN (m_fPrevFFalse) || (m_fPrevFFalse * m_fFalse > 0))
    {
      if (m_fFalse < 0)
        m_bDecreasePos = true;
      else
        m_bDecreaseNeg = true;
    }
  }
}
