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
package numbercruncher.program9_1;

import java.awt.Frame;

import numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 9-1d: Graphic Transformations (Interactive Standalone Demo)
 * Interactively demonstrate the use of graphic transformation matrices.
 */
public final class TransformationDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Graphic Transformations";

  /**
   * Constructor.
   */
  private TransformationDemo ()
  {
    super (TITLE, new TransformationPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new TransformationDemo ();
    frame.setVisible (true);
  }
}