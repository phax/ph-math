package numbercruncher.mathutils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PrimeFactorsTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PrimeFactorsTest.class);

  @Test
  public void test ()
  {
    final SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

    // Test Sieve of Eratosthenes.
    s_aLogger.info ("The Sieve of Eratosthenes:\n");
    final boolean isPrime[] = PrimeFactors.primeSieve (100);
    for (int i = 1; i <= 100; ++i)
    {
      if (isPrime[i])
        ar.print (i, 4);
      else
        ar.print (".", 4);
      if (i % 10 == 0)
        ar.println ();
    }

    // Test prime factors.
    final int k[] = { 84, 1409, 3141135, };
    for (final int element : k)
    {
      final int factors[] = PrimeFactors.factorsOf (element);
      final StringBuilder aSB = new StringBuilder ();
      aSB.append ("The prime factors of " + element + " are");
      for (final int factor : factors)
      {
        aSB.append (" " + factor);
      }
      s_aLogger.info (aSB.toString ());
    }
  }
}
/*
 * Output: The Sieve of Eratosthenes: . 2 3 . 5 . 7 . . . 11 . 13 . . . 17 . 19
 * . . . 23 . . . . . 29 . 31 . . . . . 37 . . . 41 . 43 . . . 47 . . . . . 53 .
 * . . . . 59 . 61 . . . . . 67 . . . 71 . 73 . . . . . 79 . . . 83 . . . . . 89
 * . . . . . . . 97 . . . The prime factors of 84 are 2 3 7 The prime factors of
 * 1409 are 1409 The prime factors of 3141135 are 3 5 29 83
 */
