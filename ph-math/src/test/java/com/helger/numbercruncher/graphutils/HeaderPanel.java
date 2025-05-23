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
package com.helger.numbercruncher.graphutils;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;

/**
 * The header panel that displays the current function or a text label.
 */
final class HeaderPanel extends Panel
{
  private static final String FUNCTION = "function";
  private static final String MESSAGE = "message";

  private static final Color BACKGROUND_COLOR = Color.lightGray;
  private static final Color MAROON = new Color (128, 0, 0);

  /**
   * function card
   */
  private final Panel functionCard = new Panel ();
  /**
   * message card
   */
  private final Panel messageCard = new Panel ();
  /**
   * card layout
   */
  private final CardLayout cardLayout = new CardLayout ();
  /**
   * button panel
   */
  private final Panel buttonPanel = new Panel ();
  /**
   * image panel
   */
  private final ImagePanel imagePanel = new ImagePanel ();
  /**
   * header button
   */
  private final Button headerButton = new Button ("Show functions");
  /**
   * header label
   */
  private final Label label = new Label ();

  /** function image */
  private Image m_aImage;
  /** the selected function */
  private IPlottable m_aFunction;
  /** function panel dimensions */
  private Dimension fpDimensions;
  /** parent graph panel */
  private AbstractGraphPanel m_aGraphPanel;

  /**
   * Constructor.
   *
   * @param functions
   *        the array of functions to plot
   * @param graphPanel
   *        the parent graph panel
   */
  HeaderPanel (final IPlottable functions[], final AbstractGraphPanel graphPanel)
  {
    this ();
    this.m_aGraphPanel = graphPanel;

    fpDimensions = maxFunctionDimensions (functions);

    buttonPanel.setLayout (new FlowLayout ());
    buttonPanel.add (headerButton);

    imagePanel.setSize (fpDimensions);

    functionCard.add (buttonPanel, BorderLayout.WEST);
    functionCard.add (imagePanel, BorderLayout.CENTER);

    cardLayout.show (this, FUNCTION);

    // Header button handler.
    headerButton.addActionListener (ev -> HeaderPanel.this.m_aGraphPanel.doHeaderAction ());
  }

  /**
   * Constructor.
   *
   * @param headerText
   *        the header label text
   */
  HeaderPanel (final String headerText)
  {
    this ();

    label.setText (headerText);
    cardLayout.show (this, MESSAGE);
  }

  /**
   * Constructor.
   */
  private HeaderPanel ()
  {
    setBackground (BACKGROUND_COLOR);
    setLayout (cardLayout);

    add (functionCard, FUNCTION);
    add (messageCard, MESSAGE);

    functionCard.setBackground (BACKGROUND_COLOR);
    functionCard.setLayout (new BorderLayout ());

    messageCard.setBackground (BACKGROUND_COLOR);
    messageCard.setLayout (new BorderLayout ());

    label.setFont (new Font ("Dialog", Font.BOLD, 12));
    label.setAlignment (Label.CENTER);
    label.setForeground (Color.black);

    messageCard.add (label, BorderLayout.CENTER);
  }

  /**
   * Set the function image.
   *
   * @param image
   *        the image
   */
  void setImage (final Image image)
  {
    this.m_aImage = image;
  }

  /**
   * Set the header label text in the default black color.
   *
   * @param text
   *        the text
   */
  void setLabel (final String text)
  {
    setLabel (text, Color.black);
  }

  /**
   * Set the header label text in color.
   *
   * @param text
   *        the text
   * @param color
   *        the color
   */
  void setLabel (final String text, final Color color)
  {
    cardLayout.show (this, MESSAGE);
    label.setForeground (color);
    label.setText (text);
  }

  /**
   * Set a function and repaint the function panel.
   *
   * @param function
   *        the selected function index
   */
  void setFunction (final IPlottable function)
  {
    this.m_aFunction = function;
    cardLayout.show (this, FUNCTION);
    imagePanel.repaint ();
  }

  /**
   * Display an error message.
   *
   * @param message
   *        the error message
   */
  void displayError (final String message)
  {
    cardLayout.show (this, MESSAGE);
    label.setForeground (MAROON);
    label.setText ("ERROR:  " + message);
  }

  /**
   * Return the minimum size of the header panel. The height is either the
   * height of the tallest function in the image, or the height of the header
   * label.
   *
   * @return the minimum size
   */
  @Override
  public Dimension getMinimumSize ()
  {
    final int height = (fpDimensions != null) ? fpDimensions.height : label.getMinimumSize ().height;

    return new Dimension (Short.MAX_VALUE, height);
  }

  /**
   * Return the preferred size of the header panel, which is its minimum size.
   *
   * @return the preferred size
   */
  @Override
  public Dimension getPreferredSize ()
  {
    return getMinimumSize ();
  }

  /**
   * Return the maximum display dimensions of the functions.
   *
   * @param functions
   *        the functions to find roots for
   * @return the dimensions
   */
  private Dimension maxFunctionDimensions (final IPlottable functions[])
  {
    int maxWidth = 0;
    int maxHeight = 0;

    // Loop over the functions array to find the maximum width and height.
    for (final IPlottable function2 : functions)
    {
      final Rectangle r = function2.getRectangle ();
      if (r.width > maxWidth)
      {
        maxWidth = r.width;
      }
      if (r.height > maxHeight)
      {
        maxHeight = r.height;
      }
    }

    return new Dimension (maxWidth, maxHeight);
  }

  /**
   * The function panel class.
   */
  private class ImagePanel extends Panel
  {
    /** image buffer */
    private Image buffer;
    /** buffer graphics context */
    private Graphics bg;

    /**
     * Display the current function.
     */
    @Override
    public void paint (final Graphics g)
    {
      if (m_aFunction == null)
        return;

      // Get the function's display region.
      final Rectangle r = m_aFunction.getRectangle ();
      if (r == null)
        return;

      final Dimension fp = this.getSize ();

      // Create the image buffer.
      if (buffer == null)
      {
        buffer = createImage (fp.width, fp.height);
        bg = buffer.getGraphics ();
        bg.setColor (BACKGROUND_COLOR);
      }

      final int sx1 = r.x;
      final int sy1 = r.y;
      final int sx2 = r.x + r.width - 2;
      final int sy2 = r.y + r.height - 1;

      final int sw = sx2 - sx1 + 1;
      final int sh = sy2 - sy1 + 1;

      final int dx1 = (fp.width - sw) / 2;
      final int dy1 = (fp.height - sh) / 2;
      final int dx2 = dx1 + sw - 1;
      final int dy2 = dy1 + sh - 1;

      // Copy the image of the selected function into the buffer.
      bg.setPaintMode ();
      bg.fillRect (0, 0, fp.width, fp.height);
      bg.drawImage (m_aImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this);

      // Display the buffer.
      g.drawImage (buffer, 0, 0, this);
    }
  }
}
