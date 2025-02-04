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
package com.helger.numbercruncher.program11_2;

import com.helger.numbercruncher.matrix.ColumnVector;
import com.helger.numbercruncher.matrix.InvertibleMatrix;
import com.helger.numbercruncher.matrix.MatrixException;

/**
 * PROGRAM 11-2: Compare Solution Algorithms Compare algorithms for solving a
 * system of linear equations.
 */
public final class MainCompareSolutions
{
  /**
   * Run the test.
   *
   * @param A
   *        the coefficient matrix
   * @param b
   *        the right-hand-side vector
   * @param correct
   *        the known correct solution vector
   * @throws MatrixException
   *         if an error occurred
   */
  private void run (final InvertibleMatrix A, final ColumnVector b, final ColumnVector correct) throws MatrixException
  {
    System.out.println ("Coefficient matrix A");
    A.print (14, System.out);

    System.out.print ("\nb =");
    b.print (System.out);

    // Solve the system using LU decomposition
    // with iterative improvement.
    ColumnVector x = A.solve (b, true);
    System.out.println ("\nLU decomposition:");
    System.out.print ("   x =");
    x.print (System.out);
    System.out.println ("Error vector norm = " + x.subtract (correct).norm ());

    final InvertibleMatrix Ainv = A.inverse ();

    System.out.println ("\nA inverse");
    Ainv.print (14, System.out);

    final float detA = A.determinant ();
    final float condA = A.norm () * Ainv.norm ();

    // Solve the system by multiplying A-inverse by b.
    x = Ainv.multiply (b);
    System.out.println ("\nMultiplication by inverse:");
    System.out.print ("   x =");
    x.print (System.out);
    System.out.println ("Error vector norm = " + x.subtract (correct).norm ());

    System.out.println ("\n     Determinant of A = " + detA);
    System.out.println ("Condition number of A = " + condA);

    final int nRows = A.rowCount ();
    final InvertibleMatrix As[] = new InvertibleMatrix [nRows];
    final float dets[] = new float [nRows];

    // Loop to create matrices A(i) for Cramer's rule.
    for (int i = 0; i < nRows; ++i)
    {
      As[i] = new InvertibleMatrix (A.copyValues2D ());
      As[i].setColumn (b, i);
      dets[i] = As[i].determinant ();

      System.out.println ("\nA[" + (i + 1) + "], determinant = " + dets[i]);
      As[i].print (14, System.out);
    }

    // Solve the system using Cramer's rule.
    x = new ColumnVector (nRows);
    for (int i = 0; i < nRows; ++i)
      x.set (i, dets[i] / detA);

    System.out.println ("\nCramer's rule:");
    System.out.print ("   x =");
    x.print (System.out);
    System.out.println ("Error vector norm = " + x.subtract (correct).norm ());
  }

  /**
   * Main.
   *
   * @param args
   *        the array of arguments
   */
  public static void main (final String args[])
  {
    // Matrix A.
    final InvertibleMatrix A = new InvertibleMatrix (new float [] [] { { 3, 1, -5, 4 },
                                                                       { 2, -3, 3, -2 },
                                                                       { 5, -3, 4, 1 },
                                                                       { -2, 4, -3, -5 }, });

    // Column vector b.
    final ColumnVector b = new ColumnVector (new float [] { -18, 19, 22, -14 });

    // The known correct solution.
    final ColumnVector correct = new ColumnVector (new float [] { 1, -2, 3, -1 });

    final MainCompareSolutions compare = new MainCompareSolutions ();

    try
    {
      compare.run (A, b, correct);
    }
    catch (final MatrixException ex)
    {
      System.out.println ("*** ERROR: " + ex.getMessage ());
      ex.printStackTrace ();
    }
  }
}
/*
 * Output: Coefficient matrix A Row 1: 3.0 1.0 -5.0 4.0 Row 2: 2.0 -3.0 3.0 -2.0
 * Row 3: 5.0 -3.0 4.0 1.0 Row 4: -2.0 4.0 -3.0 -5.0 b = -18.0 19.0 22.0 -14.0
 * LU decomposition: x = 1.0 -2.0 3.0 -1.0 Error vector norm = 0.0 A inverse Row
 * 1: 0.08121827 -0.02538071 0.20812182 0.11675127 Row 2: -0.12436548
 * -0.49238577 0.33756346 0.16497461 Row 3: -0.19035533 -0.28426397 0.23096447
 * 0.007614213 Row 4: -0.017766498 -0.21319798 0.04822335 -0.11928934
 * Multiplication by inverse: x = 0.9999999 -1.9999995 2.9999998 -1.0000004
 * Error vector norm = 6.529362E-7 Determinant of A = 394.00003 Condition number
 * of A = 11.255114 A[1], determinant = 393.99997 Row 1: -18.0 1.0 -5.0 4.0 Row
 * 2: 19.0 -3.0 3.0 -2.0 Row 3: 22.0 -3.0 4.0 1.0 Row 4: -14.0 4.0 -3.0 -5.0
 * A[2], determinant = -787.99994 Row 1: 3.0 -18.0 -5.0 4.0 Row 2: 2.0 19.0 3.0
 * -2.0 Row 3: 5.0 22.0 4.0 1.0 Row 4: -2.0 -14.0 -3.0 -5.0 A[3], determinant =
 * 1182.0 Row 1: 3.0 1.0 -18.0 4.0 Row 2: 2.0 -3.0 19.0 -2.0 Row 3: 5.0 -3.0
 * 22.0 1.0 Row 4: -2.0 4.0 -14.0 -5.0 A[4], determinant = -394.00003 Row 1: 3.0
 * 1.0 -5.0 -18.0 Row 2: 2.0 -3.0 3.0 19.0 Row 3: 5.0 -3.0 4.0 22.0 Row 4: -2.0
 * 4.0 -3.0 -14.0 Cramer's rule: x = 0.9999998 -1.9999996 2.9999998 -1.0 Error
 * vector norm = 4.6552717E-7
 */
