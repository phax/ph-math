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
package numbercruncher.piutils;

import java.time.Duration;
import java.time.LocalDateTime;

import com.helger.commons.datetime.PDTFactory;

/**
 * Utility class for programs that compute pi.
 */
public abstract class AbstractPiFormula
{
  /**
   * Print the string containing the digits of pi.
   *
   * @param piString
   *        the string containing the digits of pi
   */
  protected void printPi (final String piString)
  {
    System.out.print ("\npi = " + piString.substring (0, 2));

    int index = 2;
    int line = 0;
    int group = 0;
    final int length = piString.length ();

    // Loop for each group of 5 digits
    while (index + 5 < length)
    {
      System.out.print (piString.substring (index, index + 5) + " ");
      index += 5;

      // End of line after 10 groups.
      if (++group == 10)
      {
        System.out.println ();

        // Print a blank line after 10 lines.
        if (++line == 10)
        {
          System.out.println ();
          line = 0;
        }

        System.out.print ("       ");
        group = 0;
      }
    }

    // Print the last partial line.
    if (index < length)
    {
      System.out.println (piString.substring (index));
    }
  }

  /**
   * Return a timestamp string that contains the elapsed time period.
   *
   * @param time
   *        the starting time of the period
   * @return the timestamp string
   */
  protected String timestamp (final long time)
  {
    // Current time followed by elapsed time as (hh:mm:ss).
    final LocalDateTime aLDT = PDTFactory.getCurrentLocalDateTime ();
    final LocalDateTime aOld = PDTFactory.createLocalDateTime (time);

    return aLDT.toLocalTime ().toString () + " (" + Duration.between (aOld, aLDT).toString () + ")";
  }
}
