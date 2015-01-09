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
package numbercruncher.program5_1;

import numbercruncher.graphutils.DemoApplet;

/**
 * PROGRAM 5-1a: Bisection Algorithm (Interactive Applet) Interactively
 * demonstrate the Bisection Algorithm on various functions. Either single-step
 * or let the program automatically step once each half second.
 */
public class BisectionApplet extends DemoApplet
{
  /**
   * Constructor.
   */
  public BisectionApplet ()
  {
    super (new BisectionPanel ());
  }
}
