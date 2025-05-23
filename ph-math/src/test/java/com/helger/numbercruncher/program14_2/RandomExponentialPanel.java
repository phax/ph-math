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
package com.helger.numbercruncher.program14_2;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import com.helger.numbercruncher.graphutils.AbstractGraphPanel;
import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.RandomExponential;
import com.helger.numbercruncher.randomutils.Buckets;

/**
 * The demo panel for the Random Exponential program and applet.
 */
public final class RandomExponentialPanel extends AbstractGraphPanel
{
  private static final String RUN = "Run";
  private static final String PAUSE = "Pause";
  private static final String RESET = "Reset";

  private static final String HEADER_LABEL = "Exponentially-Distributed Random Numbers";

  private static final String ALGORITHM_LABELS[] = { "Log Algorithm", "von Neumann Algorithm", };

  private static final PlotProperties LOG_PLOT_PROPS = new PlotProperties (-0.25f, 2.5f, -0.25f, 2.5f);
  private static final PlotProperties VON_NEUMANN_PLOT_PROPS = new PlotProperties (-0.5f, 12.5f, -0.25f, 1.75f);

  private static final float INIT_LOG_MEAN = 0.5f;
  private static final float VON_NEUMANN_MEAN = (float) (1 - 1 / Math.E);

  private static final int LOG_BUCKET_COUNT = 100;
  private static final int VON_NEUMANN_BUCKET_COUNT = 12;
  private static final int GROUP_SIZE = 100;
  private static final int LOG_BAR_FACTOR = 20;
  private static final int VON_NEUMANN_BAR_FACTOR = 1000;
  private static final int LOG_PLOT_FACTOR = 1;
  private static final int VON_NEUMANN_PLOT_FACTOR = 4;

  private static final int LOG = 0;
  private static final int VON_NEUMANN = 1;

  /**
   * control panel
   */
  private final Panel controlPanel = new Panel ();
  /** mean label */
  private final Label meanLabel = new Label ("Mean:");
  /** mean text */
  private final TextField meanText = new TextField (Float.toString (INIT_LOG_MEAN));
  /** values label */
  private final Label valuesLabel = new Label ("# Values:");
  /** values text */
  private final Label valuesText = new Label ("0");
  /**
   * algorithm label
   */
  private final Label algorithmLabel = new Label ("Algorithm:");
  /**
   * algorithm choice
   */
  private final Choice algorithmChoice = new Choice ();
  /** run button */
  private final Button runButton = new Button (RUN);
  /** reset button */
  private final Button resetButton = new Button (RESET);

  /** run thread */
  private RunThread runThread;
  /** true if pause button pressed */
  private boolean paused = false;
  /** true if done */
  private boolean done = false;

  /** plot properties */
  private PlotProperties plotProps;
  /** plot width */
  private int w;
  /** bar width */
  private int bWidth;
  /** x-axis row */
  private int xAxisRow;
  /** min plot x */
  private float xMin;
  /** max plot x */
  private float xMax;
  /** max plot y */
  private float yMax;
  /** plot x delta */
  private float xDelta;
  /** plot y delta */
  private float yDelta;

  /** mean */
  private float mean = INIT_LOG_MEAN;

  /** number of values */
  private int n = 0;
  /** number of buckets */
  private int bucketCount = LOG_BUCKET_COUNT;
  /** bar factor */
  private int barFactor = LOG_BAR_FACTOR;
  /** plot factor */
  private int plotFactor = LOG_PLOT_FACTOR;
  /** algorithm index */
  private int xAlgorithm;
  /** value buckets */
  private Buckets buckets;

  /** exponential random numbers */
  private final RandomExponential exponential = new RandomExponential ();

  /**
   * Constructor.
   */
  public RandomExponentialPanel ()
  {
    super (HEADER_LABEL, LOG_PLOT_PROPS);
    updatePlotProperties ();

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Controls.
    meanLabel.setFont (labelFont);
    meanLabel.setAlignment (Label.RIGHT);
    meanText.setFont (textFont);
    valuesLabel.setFont (labelFont);
    valuesLabel.setAlignment (Label.RIGHT);
    valuesText.setFont (textFont);
    valuesText.setAlignment (Label.LEFT);
    algorithmLabel.setFont (labelFont);
    algorithmLabel.setAlignment (Label.RIGHT);

    algorithmChoice.add ("Log");
    algorithmChoice.add ("von Neumann");

    // Control panel.
    controlPanel.setLayout (new GridLayout (0, 4, 5, 2));
    controlPanel.add (meanLabel);
    controlPanel.add (meanText);
    controlPanel.add (algorithmLabel);
    controlPanel.add (runButton);
    controlPanel.add (valuesLabel);
    controlPanel.add (valuesText);
    controlPanel.add (algorithmChoice);
    controlPanel.add (resetButton);
    addDemoControls (controlPanel);

    runButton.setEnabled (true);
    resetButton.setEnabled (false);

    // Algorithm choice handler.
    algorithmChoice.addItemListener (ev -> {
      xAlgorithm = algorithmChoice.getSelectedIndex ();
      switch (xAlgorithm)
      {

        case LOG:
        {
          setPlotProperties (LOG_PLOT_PROPS);
          mean = INIT_LOG_MEAN;
          barFactor = LOG_BAR_FACTOR;
          plotFactor = LOG_PLOT_FACTOR;

          meanText.setEnabled (true);
          updatePlotProperties ();

          bucketCount = LOG_BUCKET_COUNT;
          buckets = new Buckets (bucketCount);
          buckets.setLimits (0, xMax);

          break;
        }

        case VON_NEUMANN:
        {
          setPlotProperties (VON_NEUMANN_PLOT_PROPS);
          mean = VON_NEUMANN_MEAN;
          barFactor = VON_NEUMANN_BAR_FACTOR;
          plotFactor = VON_NEUMANN_PLOT_FACTOR;

          meanText.setEnabled (false);
          updatePlotProperties ();

          bucketCount = VON_NEUMANN_BUCKET_COUNT;
          buckets = new Buckets (bucketCount);
          buckets.setLimits (0, VON_NEUMANN_BUCKET_COUNT - 1);

          break;
        }
      }

      meanText.setText (Float.toString (mean));
      draw ();
    });

    // Run button handler.
    runButton.addActionListener (ev -> {
      if (runButton.getLabel ().equals (RUN))
      {
        run ();
      }
      else
      {
        pause ();
      }
    });

    // Reset button handler.
    resetButton.addActionListener (ev -> {
      n = 0;
      valuesText.setText ("0");

      draw ();
    });

    // Start with the logarithm algorithm.
    xAlgorithm = 0;
    bucketCount = LOG_BUCKET_COUNT;
    buckets = new Buckets (bucketCount);
    buckets.setLimits (0, xMax);
    meanText.setEnabled (true);
  }

  private void run ()
  {
    String text = null;

    try
    {
      meanText.requestFocus ();
      text = meanText.getText ();
      mean = Float.parseFloat (text);
    }
    catch (final NumberFormatException ex)
    {
      processUserError ("Invalid value '" + text + "'");
      return;
    }
    catch (final Exception ex)
    {
      processUserError (ex.getMessage ());
      return;
    }

    exponential.setParameters (mean);

    setHeaderLabel (HEADER_LABEL + " by the " + ALGORITHM_LABELS[xAlgorithm]);

    if (n == 0)
      draw ();

    runButton.setLabel (PAUSE);
    resetButton.setEnabled (false);
    meanText.setEnabled (false);
    algorithmChoice.setEnabled (false);

    paused = false;
    done = false;

    runThread = new RunThread ();
    runThread.start ();
  }

  private void pause ()
  {
    paused = true;

    runButton.setLabel (RUN);
    resetButton.setEnabled (true);
  }

  /**
   * Reset.
   */
  private void reset ()
  {
    runButton.setEnabled (true);
    resetButton.setEnabled (false);
    if (xAlgorithm != VON_NEUMANN)
      meanText.setEnabled (true);
    algorithmChoice.setEnabled (true);

    updatePlotProperties ();
    plotFunction ();

    buckets.clear ();
  }

  // ---------------------//
  // Algorithm animation //
  // ---------------------//

  /**
   * The thread that automatically steps once per half second.
   */
  private class RunThread extends Thread
  {
    /**
     * Run the thread.
     */
    @Override
    public void run ()
    {
      while ((!paused) && (!done))
      {
        ++n;

        float x = 0;
        switch (xAlgorithm)
        {
          case LOG:
            x = exponential.nextLog ();
            break;
          case VON_NEUMANN:
            x = exponential.nextVonNeumann ();
            break;
        }

        buckets.put (x);

        if (n % GROUP_SIZE == 0)
        {
          yield ();
          valuesText.setText (Integer.toString (n));
          drawBars ();
        }
      }

      if (!paused)
      {
        runButton.setLabel (RUN);
        runButton.setEnabled (false);
        resetButton.setEnabled (true);
      }
    }
  }

  private void drawBars ()
  {
    float x = 0;
    float d = 0;

    switch (xAlgorithm)
    {
      case LOG:
        d = xMax / (bucketCount);
        break;
      case VON_NEUMANN:
        d = 1;
        break;
    }

    final float left = 0;
    final float right = xMax;

    int over = 0;
    int under = 0;

    for (int i = 0; i < bucketCount; ++i)
    {
      final float y = buckets.get (i) / ((float) barFactor);
      final int c = Math.round ((x - xMin) / xDelta);
      final int r = Math.round ((yMax - y) / yDelta);
      final int h = xAxisRow - r;

      if (h > 0)
        plotRectangle (c, r, bWidth, h, Color.red);

      if ((x > left) && (x < right))
      {
        if (y <= valueAt (x))
          ++under;
        else
          ++over;
      }

      x += d;
    }

    plotFunction ();
    done = over > under; // done if half of bars above plot
  }

  private void updatePlotProperties ()
  {
    plotProps = getPlotProperties ();

    w = plotProps.getWidth ();
    xAxisRow = plotProps.getXAxisRow ();
    xMin = plotProps.getXMin ();
    xMax = plotProps.getXMax ();
    yMax = plotProps.getYMax ();
    xDelta = plotProps.getXDelta ();
    yDelta = plotProps.getYDelta ();

    switch (xAlgorithm)
    {
      case LOG:
        bWidth = w / bucketCount;
        break;
      case VON_NEUMANN:
        bWidth = Math.round (1 / xDelta);
        break;
    }
  }

  // -----------------------------//
  // GraphPanel method overrides //
  // -----------------------------//

  /**
   * Draw the contents of the Euler's demo panel.
   */
  @Override
  public void draw ()
  {
    // Stop the run thread.
    paused = true;

    super.draw ();
    reset ();
  }

  /**
   * Return the value of the selected equation at x.
   *
   * @param x
   *        the value of x
   * @return the value of the function
   */
  @Override
  public float valueAt (final float x)
  {
    return (float) (((1 / mean) * Math.exp (-x / (plotFactor * mean))));
  }

  /**
   * Notification that the plot bounds changed. Redraw the panel.
   */
  @Override
  public void plotBoundsChanged ()
  {
    draw ();
  }

  // --------------------------//
  // DemoPanel implementation //
  // --------------------------//

  /**
   * Initialize the demo (callback from applet).
   */
  @Override
  public void initializeDemo ()
  {}

  /**
   * Close the demo (callback from applet).
   */
  @Override
  public void closeDemo ()
  {}
}
