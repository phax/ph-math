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
package com.helger.numbercruncher.primeutils;

/**
 * The current status of the Miller-Rabin test.
 */
public class MillerRabinStatus
{
  // Status codes
  public static final int DONT_KNOW_YET = 0;
  public static final int DEFINITELY_COMPOSITE = 1;
  public static final int PROBABLY_PRIME = 2;

  /** random base */
  int m_nRandomBase;
  /** shifted p-1 */
  int m_nShiftedPMinus1;
  /** no. of right shifts */
  int m_nRightShifts;
  /** counter */
  int m_nCounter;
  /** modulo value */
  int m_nModulo;
  /** status code */
  int m_nStatusCode;

  public int getB ()
  {
    return m_nRandomBase;
  }

  public int getK ()
  {
    return m_nShiftedPMinus1;
  }

  public int getS ()
  {
    return m_nRightShifts;
  }

  public int getIndex ()
  {
    return m_nCounter;
  }

  public int getValue ()
  {
    return m_nModulo;
  }

  public int getCode ()
  {
    return m_nStatusCode;
  }
}
