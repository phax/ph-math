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
package com.helger.math.graph;

import java.util.Set;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.ReturnsMutableCopy;

/**
 * Interface for a directed graph.
 *
 * @author Philip Helger
 * @param <N>
 *        Directed node class
 * @param <R>
 *        Directed relation class
 */
public interface IDirectedGraph <N extends IDirectedGraphNode <N, R>, R extends IDirectedGraphRelation <N, R>>
                                extends IBaseGraph <N, R>
{
  /**
   * Try to retrieve the single start node of this graph. A start node is
   * identified by having no incoming relations.
   *
   * @return The single start node and never <code>null</code>.
   * @throws IllegalStateException
   *         In case the graph has no or more than one start node.
   */
  @Nonnull
  N getSingleStartNode () throws IllegalStateException;

  /**
   * Get all start nodes of this graph. Start nodes are identified by having no
   * incoming relations.
   *
   * @return A set with all start nodes. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <N> getAllStartNodes ();

  /**
   * Try to retrieve the single end node of this graph. An end node is
   * identified by having no outgoing relations.
   *
   * @return The single end node and never <code>null</code>.
   * @throws IllegalStateException
   *         In case the graph has no or more than one end node.
   */
  @Nonnull
  N getSingleEndNode () throws IllegalStateException;

  /**
   * Get all end nodes of this graph. End nodes are identified by having no
   * outgoing relations.
   *
   * @return A set with all end nodes. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <N> getAllEndNodes ();
}
