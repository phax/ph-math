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
package com.helger.numbercruncher.program8_1;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import com.helger.numbercruncher.graphutils.AbstractGraphPanel;
import com.helger.numbercruncher.graphutils.FunctionFrame;
import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.AbstractDiffEqSolver;
import com.helger.numbercruncher.mathutils.AbstractDifferentialEquation;
import com.helger.numbercruncher.mathutils.DataPoint;
import com.helger.numbercruncher.mathutils.EulersDiffEqSolver;
import com.helger.numbercruncher.mathutils.PredictorCorrectorDiffEqSolver;
import com.helger.numbercruncher.mathutils.RungeKuttaDiffEqSolver;

/**
 * The demo panel for the Differential Equation Solver program and applet.
 */
public final class SolveDiffEqPanel extends AbstractGraphPanel
{
  private static final int MAX_STEPS = 10;
  private static final int MAX_LINES = 1 << (MAX_STEPS + 1);

  private static final int EULER = 0;
  private static final int PREDICTOR_CORRECTOR = 1;
  private static final int RUNGE_KUTTA = 2;

  private static final String EQUATION_IMAGE_FILE_NAME = "diff-eq.gif";
  private static final String FUNCTION_FRAME_TITLE = "Click to choose a differential equation y'";

  /**
   * control panel
   */
  private final Panel controlPanel = new Panel ();
  /**
   * iteration label
   */
  private final Label iterationLabel = new Label ("Iteration:");
  /**
   * iteration text
   */
  private final Label iterationText = new Label (" ");
  /**
   * intervals label
   */
  private final Label intervalsLabel = new Label ("Intervals:");
  /**
   * intervals text
   */
  private final Label intervalsText = new Label (" ");
  /** h label */
  private final Label hLabel = new Label ("h:");
  /** h text */
  private final Label hText = new Label (" ");
  /**
   * algorithm label
   */
  private final Label solverLabel = new Label ("Algorithm:");
  /**
   * algorithm choice
   */
  private final Choice solverChoice = new Choice ();
  /** run button */
  private final Button runButton = new Button ("Run");
  /** step button */
  private final Button stepButton = new Button ("Step");

  /** function image file name */
  private final String functionImageFileName;
  /** function frame */
  private FunctionFrame functionFrame;
  /** function frame title */
  private final String functionFrameTitle;

  /** iteration counter */
  private int iteration;
  /** interval width */
  private float m_fH;

  /** selected equation */
  private PlotDiffEq plotEquation;
  /** differential equation */
  private AbstractDifferentialEquation equation;
  /** initial condition */
  private DataPoint initialCondition;

  /** thread for automatic stepping */
  private Thread runThread = null;

  /** true if pause button was pressed */
  private boolean paused = false;
  /** true to stop run */
  private boolean stop = false;

  /** line count */
  private int k;
  /** minimum x value */
  private float xMin;
  /** maximum x value */
  private float xMax;
  /** maximum y value */
  private float yMax;
  /** x delta per pixel */
  private float xDelta;
  /** y delta per pixel */
  private float yDelta;

  /**
   * array of plot endpoint columns #1
   */
  private final int cs1[] = new int [MAX_LINES];
  /**
   * array of plot endpoint rows #1
   */
  private final int rs1[] = new int [MAX_LINES];
  /**
   * array of plot endpoint columns #2
   */
  private final int cs2[] = new int [MAX_LINES];
  /**
   * array of plot endpoint rows #2
   */
  private final int rs2[] = new int [MAX_LINES];

  /** Differential equation solver */
  protected AbstractDiffEqSolver solver;

  /** Differential equations to solve */
  private static PlotDiffEq EQUATIONS[] = { new PlotDiffEq ("2x", -5.25f, 5.25f, -10.5f, 20.25f),
                                            new PlotDiffEq ("3x^2 + 6x - 9", -6.25f, 4.25f, -20.5f, 20.25f),
                                            new PlotDiffEq ("6x^2 - 20x + 11", -0.5f, 5.25f, -10.5f, 25.25f),
                                            new PlotDiffEq ("2xe^2x + y", -1.25f, 1.5f, -1.5f, 20.5f),
                                            new PlotDiffEq ("8x - 2y + 8", -1.25f, 4.25f, -25f, 25f),
                                            new PlotDiffEq ("xe^-2x - 2y", -1.25f, 1.25f, -1.25f, 1.25f), };

  /**
   * Constructor.
   */
  public SolveDiffEqPanel ()
  {
    super (EQUATIONS, EQUATIONS[0].getPlotProperties (), true, false);

    plotEquation = EQUATIONS[0];
    equation = (AbstractDifferentialEquation) plotEquation.getFunction ();
    initialCondition = plotEquation.getInitialCondition ();

    functionImageFileName = EQUATION_IMAGE_FILE_NAME;
    functionFrameTitle = FUNCTION_FRAME_TITLE;

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Controls.
    iterationLabel.setFont (labelFont);
    iterationLabel.setAlignment (Label.RIGHT);
    iterationText.setFont (textFont);
    iterationText.setAlignment (Label.LEFT);
    intervalsLabel.setFont (labelFont);
    intervalsLabel.setAlignment (Label.RIGHT);
    intervalsText.setFont (textFont);
    intervalsText.setAlignment (Label.LEFT);
    hLabel.setFont (labelFont);
    hLabel.setAlignment (Label.RIGHT);
    hText.setFont (textFont);
    hText.setAlignment (Label.LEFT);
    solverLabel.setFont (labelFont);
    solverLabel.setAlignment (Label.RIGHT);

    solverChoice.add ("Euler");
    solverChoice.add ("Pred.corr.");
    solverChoice.add ("RungeKutta");

    // Control panel.
    controlPanel.setLayout (new GridLayout (0, 5, 5, 2));
    controlPanel.add (intervalsLabel);
    controlPanel.add (intervalsText);
    controlPanel.add (iterationLabel);
    controlPanel.add (iterationText);
    controlPanel.add (runButton);
    controlPanel.add (hLabel);
    controlPanel.add (hText);
    controlPanel.add (solverLabel);
    controlPanel.add (solverChoice);
    controlPanel.add (stepButton);
    addDemoControls (controlPanel);

    runButton.setEnabled (true);
    stepButton.setEnabled (true);

    // Algorithm choice handler.
    solverChoice.addItemListener (ev -> draw ());

    // Step button handler.
    stepButton.addActionListener (ev -> {
      if ((runThread != null) && (runThread.isAlive ()))
      {
        paused = true;
        runButton.setEnabled (true);
        stepButton.setLabel ("Step");
      }
      else
      {
        step ();
      }
    });

    // Run button handler.
    runButton.addActionListener (ev -> {
      runButton.setEnabled (false);
      stepButton.setLabel ("Pause");
      paused = false;

      runThread = new RunThread ();
      runThread.start ();
    });
  }

  /**
   * Reset for a new differential equation or for a new solver algorithm.
   */
  private void reset ()
  {
    // Reinitialize the run and step buttons.
    runButton.setEnabled (true);
    stepButton.setEnabled (true);
    stepButton.setLabel ("Step");

    k = 0;
    iteration = 0;

    iterationText.setText ("0");
    intervalsText.setText (" ");
    hText.setText (" ");

    // Plot properties
    final PlotProperties props = getPlotProperties ();
    xMin = props.getXMin ();
    xMax = props.getXMax ();
    yMax = props.getYMax ();
    xDelta = props.getXDelta ();
    yDelta = props.getYDelta ();

    // Plot the differential equation.
    // plotFunction(plotEquation, Color.green);

    // Plot the initial condition data point.
    final float x = initialCondition.getX ();
    final float y = initialCondition.getY ();
    final int c = Math.round ((x - xMin) / xDelta);
    final int r = Math.round ((yMax - y) / yDelta);
    plotDot (c, r, 8, Color.red);

    // Create the chosen differential equation solver.
    switch (solverChoice.getSelectedIndex ())
    {

      case EULER:
      {
        solver = new EulersDiffEqSolver (equation);
        break;
      }

      case PREDICTOR_CORRECTOR:
      {
        solver = new PredictorCorrectorDiffEqSolver (equation);
        break;
      }

      case RUNGE_KUTTA:
      {
        solver = new RungeKuttaDiffEqSolver (equation);
        break;
      }
    }

    // The initial interval width.
    m_fH = Math.max (Math.abs (x - xMin), Math.abs (x - xMax));
  }

  // ----------------//
  // Function frame //
  // ----------------//

  /**
   * Open the function frame.
   */
  private void openFunctionFrame ()
  {
    functionFrame = new FunctionFrame (EQUATIONS, functionImageFileName, functionFrameTitle, this);
    functionFrame.setVisible (true);

    setHeaderImage (functionFrame.getImage ());
    setFunction (EQUATIONS[0]);
  }

  /**
   * Choose a differential equation. (Callback from the function frame.)
   */
  @Override
  public void chooseFunction (final int index)
  {
    plotEquation = EQUATIONS[index];
    equation = (AbstractDifferentialEquation) plotEquation.getFunction ();
    initialCondition = plotEquation.getInitialCondition ();

    setFunction (plotEquation);
    draw ();
  }

  // ---------------------//
  // Algorithm animation //
  // ---------------------//

  /**
   * Do one iteration step.
   */
  private void step ()
  {
    ++iteration;
    plotApproximation ();

    iterationText.setText (Integer.toString (iteration));
    intervalsText.setText (Integer.toString (k));
    hText.setText (Float.toString (m_fH));

    m_fH /= 2;

    // Too many iterations?
    if (iteration == MAX_STEPS)
    {
      stop = true;

      runButton.setEnabled (false);
      stepButton.setEnabled (false);
    }
  }

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
      // Loop until the iteration count stops.
      while ((!paused) && (!stop))
      {
        step ();

        try
        {
          sleep (500); // half second
        }
        catch (final Exception ex)
        {}
      }

      // Unless it's only paused, disable this function.
      if (!paused)
      {
        runButton.setEnabled (false);
        stepButton.setEnabled (false);
        stepButton.setLabel ("Step");
      }
    }
  }

  /**
   * Plot the current approximation to the solution function.
   */
  private void plotApproximation ()
  {
    // Erase previous plot.
    if (k > 0)
    {
      plotLines (cs1, rs1, cs2, rs2, k, Color.red);
    }

    k = 0;
    plotForward (m_fH);
    plotBackward (m_fH);

    // Display current plot.
    plotLines (cs1, rs1, cs2, rs2, k, Color.red);
  }

  /**
   * Plot the solution approximation forward from the initial condition data
   * point.
   */
  private void plotForward (final float h)
  {
    solver.reset ();

    float x1 = initialCondition.getX ();
    float y1 = initialCondition.getY ();

    while (x1 < xMax)
    {
      final DataPoint point = solver.nextPoint (h);
      final float x2 = point.getX ();
      final float y2 = point.getY ();

      appendLine (x1, y1, x2, y2);

      x1 = x2;
      y1 = y2;
    }
  }

  /**
   * Plot the solution approximation backward from the initial condition data
   * point.
   */
  private void plotBackward (final float h)
  {
    solver.reset ();

    float x1 = initialCondition.getX ();
    float y1 = initialCondition.getY ();

    while (x1 > xMin)
    {
      final DataPoint point = solver.nextPoint (-h);
      final float x2 = point.getX ();
      final float y2 = point.getY ();

      appendLine (x1, y1, x2, y2);

      x1 = x2;
      y1 = y2;
    }
  }

  /**
   * Append a line segment to the plot of the solution approximation.
   */
  private void appendLine (final float x1, final float y1, final float x2, final float y2)
  {
    cs1[k] = Math.round ((x1 - xMin) / xDelta);
    cs2[k] = Math.round ((x2 - xMin) / xDelta);
    rs1[k] = Math.round ((yMax - y1) / yDelta);
    rs2[k] = Math.round ((yMax - y2) / yDelta);

    ++k;
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
    stop = false;

    setHeaderDisplay (plotEquation);
    super.draw ();
    reset ();
  }

  /**
   * Create the function frame or bring it to the front (callback from header
   * panel).
   */
  @Override
  public void doHeaderAction ()
  {
    if (functionFrame != null)
    {
      functionFrame.toFront ();
    }
    else
    {
      openFunctionFrame ();
    }
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
    return plotEquation.solutionAt (x);
  }

  /**
   * Notification that the plot bounds changed. Redraw the panel.
   */
  @Override
  public void plotBoundsChanged ()
  {
    draw ();
  }

  /**
   * Notification that a user input error occurred. Disable the run and step
   * buttons.
   */
  @Override
  protected void userErrorOccurred ()
  {
    runButton.setEnabled (false);
    stepButton.setEnabled (false);
  }

  // --------------------------//
  // DemoPanel implementation //
  // --------------------------//

  /**
   * Initialize the demo (callback from applet).
   */
  @Override
  public void initializeDemo ()
  {
    openFunctionFrame ();
  }

  /**
   * Close the demo (callback from applet).
   */
  @Override
  public void closeDemo ()
  {
    if (functionFrame != null)
    {
      functionFrame.setVisible (false);
      functionFrame.dispose ();
      functionFrame = null;
    }
  }
}
