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
package com.helger.math.graph;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.state.EChange;

/**
 * Base interface for graph node implementations.
 *
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IMutableDirectedGraphNode extends
                                           IMutableBaseGraphNode <IMutableDirectedGraphNode, IMutableDirectedGraphRelation>,
                                           IDirectedGraphNode <IMutableDirectedGraphNode, IMutableDirectedGraphRelation>
{
  /**
   * Add a new incoming relation to this node
   *
   * @param aRelation
   *        The relation to be added. May not be <code>null</code>.
   */
  void addIncomingRelation (@Nonnull IMutableDirectedGraphRelation aRelation);

  /**
   * Remove the passed relation from the set of incoming relations.
   *
   * @param aRelation
   *        The relation to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the passed relation was successfully
   *         removed from the incoming relations.
   */
  @Nonnull
  EChange removeIncomingRelation (@Nonnull IMutableDirectedGraphRelation aRelation);

  /**
   * Remove all incoming relations.
   *
   * @return {@link EChange#CHANGED} if the at least one relation was
   *         successfully removed from the incoming relations.
   */
  @Nonnull
  EChange removeAllIncomingRelations ();

  // --- outgoing ---

  /**
   * Add a new outgoing relation from this node
   *
   * @param aRelation
   *        The relation to be added. May not be <code>null</code>.
   */
  void addOutgoingRelation (@Nonnull IMutableDirectedGraphRelation aRelation);

  /**
   * Remove the passed relation from the set of outgoing relations.
   *
   * @param aRelation
   *        The relation to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the passed relation was successfully
   *         removed from the outgoing relations.
   */
  @Nonnull
  EChange removeOutgoingRelation (@Nonnull IMutableDirectedGraphRelation aRelation);

  /**
   * Remove all outgoing relations.
   *
   * @return {@link EChange#CHANGED} if the at least one relation was
   *         successfully removed from the outgoing relations.
   */
  @Nonnull
  EChange removeAllOutgoingRelations ();

  // --- incoming and/or outgoing

}
