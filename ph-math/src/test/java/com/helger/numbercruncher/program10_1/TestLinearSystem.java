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
package com.helger.numbercruncher.program10_1;

import java.util.Random;

import com.helger.numbercruncher.matrix.ColumnVector;
import com.helger.numbercruncher.matrix.LinearSystem;
import com.helger.numbercruncher.matrix.MatrixException;

/**
 * PROGRAM 10-1: Test class LinearSystem by solving a "random" system of linear
 * equations.
 */
public final class TestLinearSystem
{
  /**
   * Run the test.
   *
   * @param correct
   *        the known correct solution vector
   * @throws MatrixException
   *         if an error occurred
   */
  private void run (final ColumnVector correct) throws MatrixException
  {
    final int n = correct.size (); // # of equations
    final Random random = new Random (0); // random # generator

    final LinearSystem A = new LinearSystem (n); // matrix A
    final ColumnVector b = new ColumnVector (n); // vector b

    // Randomly generate the values of matrix A
    // between -10 and 10.
    for (int r = 0; r < n; ++r)
    {
      for (int c = 0; c < n; ++c)
      {
        A.set (r, c, 20 * random.nextFloat () - 10);
      }
    }

    // Compute the values of vector b using the correct solution.
    for (int r = 0; r < n; ++r)
    {
      float dot = 0;
      for (int c = 0; c < n; ++c)
      {
        dot += A.at (r, c) * correct.at (c);
      }
      b.set (r, dot);
    }

    System.out.println ("Coefficient matrix A");
    A.print (13, System.out);

    System.out.print ("\nb =");
    b.print (System.out);

    // Solve the system with iterative improvement.
    final ColumnVector x = A.solve (b, true);

    System.out.println ("\nDecomposed matrix LU");
    A.printDecomposed (13, System.out);

    System.out.print ("\nx =");
    x.print (System.out);

    // Compute the error vector and print its norm.
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
    // The known correct solution.
    final ColumnVector correct = new ColumnVector (new float [] { 1, 2, 3, 4, 5 });

    final TestLinearSystem test = new TestLinearSystem ();

    try
    {
      test.run (correct);
    }
    catch (final MatrixException ex)
    {
      System.out.println ("*** ERROR: " + ex.getMessage ());
      ex.printStackTrace ();
    }
  }
}
/*
 * Output: Coefficient matrix A Row 1: 4.619355 6.6288204 -5.189272 2.1269035
 * 2.7483473 Row 2: -3.8189888 1.0087395 -7.6598682 1.9509048 5.6306925 Row 3:
 * -3.3356323 -4.944477 -2.2962165 2.2607145 9.696831 Row 4: 9.656389 7.5836506
 * -9.538363 8.824982 -6.480465 Row 5: -4.500921 -3.741281 -7.422057 -2.6404858
 * -7.0679674 b = 24.558529 11.175966 37.413776 -0.8937969 -80.15143 Decomposed
 * matrix LU Row 1: 9.656389 7.5836506 -9.538363 8.824982 -6.480465 Row 2:
 * -0.3954883 4.007984 -11.432179 5.441082 3.0677445 Row 3: -0.46610805
 * -0.051517297 -12.456921 1.7532192 -9.930522 Row 4: 0.47837293 0.7487573
 * -0.63687897 -5.0521903 -2.7731104 Row 5: -0.34543267 -0.5800513 0.9811678
 * -1.3350756 15.278912 x = 1.0000005 1.9999993 2.9999995 3.9999998 5.0 Error
 * vector norm = 1.0115243E-6
 */
