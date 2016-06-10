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
 * Implement Kahan's Summation Algorithm for the float type.
 */
public class KahanSummation
{
  /** the current running sum */
  private float m_fSum;
  /** the current correction */
  private float m_fCorrection;
  /** the current corrected added */
  private float m_fCorrectedAddend;

  /**
   * Constructor.
   */
  public KahanSummation ()
  {}

  /**
   * Return the current corrected value of the running sum.
   *
   * @return the running sum's value
   */
  public float value ()
  {
    return m_fSum + m_fCorrection;
  }

  /**
   * Return the corrected value of the current addend.
   *
   * @return the corrected addend value
   */
  public float correctedAddend ()
  {
    return m_fCorrectedAddend;
  }

  /**
   * Add the value of an addend to the running sum.
   *
   * @param addend
   *        the value
   */
  public void add (final float addend)
  {
    // Correct the addend value and add it to the running sum.
    m_fCorrectedAddend = addend + m_fCorrection;
    final float tempSum = m_fSum + m_fCorrectedAddend;

    // Compute the next correction and set the running sum.
    // The parentheses are necessary to compute the high-order
    // bits of the addend.
    m_fCorrection = m_fCorrectedAddend - (tempSum - m_fSum);
    m_fSum = tempSum;
  }

  /**
   * Clear the running sum and the correction.
   */
  public void clear ()
  {
    m_fSum = 0;
    m_fCorrection = 0;
  }
}
