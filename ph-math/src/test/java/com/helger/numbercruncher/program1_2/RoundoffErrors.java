/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.program1_2;

/**
 * PROGRAM 1-2: Roundoff Errors Demonstrate how a tiny roundoff error can
 * explode into a much larger one.
 */
public final class RoundoffErrors
{
  public static void main (final String args[])
  {
    final float denominator = 20000000;
    final float a = 10000001 / denominator;
    final float b = 1 / 2f;
    final float diff1 = Math.abs (a - b);
    final float pctError1 = 100 * diff1 / b;

    final float inverse = 1 / diff1;
    final float diff2 = Math.abs (inverse - denominator);
    final float pctError2 = 100 * diff2 / denominator;

    System.out.println ("        a = " + a);
    System.out.println ("        b = " + b);
    System.out.println ("    diff1 = " + diff1);
    System.out.println ("pctError1 = " + pctError1 + "%");

    System.out.println ();
    System.out.println ("  inverse = " + inverse);
    System.out.println ("    diff2 = " + diff2);
    System.out.println ("pctError2 = " + pctError2 + "%");

    System.out.println ();
    System.out.println ("   factor = " + pctError2 / pctError1);
  }
}
/*
 * Output: a = 0.50000006 b = 0.5 diff1 = 5.9604645E-8 pctError1 = 1.1920929E-5%
 * inverse = 1.6777216E7 diff2 = 3222784.0 pctError2 = 16.11392% factor =
 * 1351733.6
 */
