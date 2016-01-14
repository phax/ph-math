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
package numbercruncher.program13_3;

import java.awt.Frame;

import numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 13-3d: The Borwein Pi Algorithm (Interactive Standalone Demo) Compute
 * digits of pi by the Borwein algorithm.
 */
public final class PiBorweinDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Computing pi";

  /**
   * Constructor.
   */
  private PiBorweinDemo ()
  {
    super (TITLE, new PiBorweinPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new PiBorweinDemo ();
    frame.setVisible (true);
  }
}
