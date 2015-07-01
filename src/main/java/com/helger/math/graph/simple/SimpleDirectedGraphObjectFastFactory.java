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
package com.helger.math.graph.simple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.math.graph.IMutableDirectedGraphNode;
import com.helger.math.graph.IDirectedGraphObjectFactory;
import com.helger.math.graph.IMutableDirectedGraphRelation;
import com.helger.math.graph.impl.DirectedGraphNodeFast;
import com.helger.math.graph.impl.DirectedGraphRelationFast;

/**
 * Default implementation of the {@link IDirectedGraphObjectFactory} with
 * {@link DirectedGraphNodeFast} and {@link DirectedGraphRelationFast}.
 * 
 * @author Philip Helger
 */
public class SimpleDirectedGraphObjectFastFactory implements IDirectedGraphObjectFactory
{
  @Nonnull
  public IMutableDirectedGraphNode createNode ()
  {
    return new DirectedGraphNodeFast ();
  }

  @Nonnull
  public IMutableDirectedGraphNode createNode (@Nullable final String sID)
  {
    return new DirectedGraphNodeFast (sID);
  }

  @Nonnull
  public IMutableDirectedGraphRelation createRelation (@Nonnull final IMutableDirectedGraphNode aFrom,
                                                @Nonnull final IMutableDirectedGraphNode aTo)
  {
    return new DirectedGraphRelationFast (aFrom, aTo);
  }

  @Nonnull
  public IMutableDirectedGraphRelation createRelation (@Nullable final String sID,
                                                @Nonnull final IMutableDirectedGraphNode aFrom,
                                                @Nonnull final IMutableDirectedGraphNode aTo)
  {
    return new DirectedGraphRelationFast (sID, aFrom, aTo);
  }
}
