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
package numbercruncher.program3_1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * PROGRAM 3-1d: IEEE 754 Standard (Interactive Standalone Demo) Interactive
 * decompose and recompose floating-point numbers according to the IEEE 754
 * standard.
 */
public final class FPFormatsDemo extends Frame
{
  public FPFormatsDemo ()
  {
    setSize (new Dimension (610, 480));
    setBackground (SystemColor.scrollbar);
    setTitle ("IEEE 754 Floating-Point Formats");

    // Add the demo panel.
    setLayout (new BorderLayout ());
    add (new FPFormatsPanel (), BorderLayout.CENTER);

    // Window event handlers.
    addWindowListener (new WindowAdapter ()
    {
      @Override
      public void windowOpened (final WindowEvent ev)
      {
        repaint ();
      }

      @Override
      public void windowClosing (final WindowEvent ev)
      {
        System.exit (0);
      }
    });

    // Resize event handler.
    addComponentListener (new ComponentAdapter ()
    {
      @Override
      public void componentResized (final ComponentEvent ev)
      {
        repaint ();
      }
    });
  }

  // Main method
  public static void main (final String [] args)
  {
    (new FPFormatsDemo ()).setVisible (true);
  }
}
