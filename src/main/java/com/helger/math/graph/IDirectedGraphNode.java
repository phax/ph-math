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

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.ReturnsMutableCopy;

/**
 * Base interface for graph node implementations.
 *
 * @author Philip Helger
 * @param <N>
 *        Directed node class
 * @param <R>
 *        Directed relation class
 */
@MustImplementEqualsAndHashcode
public interface IDirectedGraphNode <N extends IDirectedGraphNode <N, R>, R extends IDirectedGraphRelation <N, R>>
                                    extends IBaseGraphNode <N, R>
{
  /**
   * @return <code>true</code> if this node has at least one incoming relation.
   */
  boolean hasIncomingRelations ();

  /**
   * @return The number of incoming relations. Always &ge; 0.
   */
  @Nonnegative
  int getIncomingRelationCount ();

  /**
   * Check if this node has the passed relation as an incoming relations.
   *
   * @param aRelation
   *        The relation to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed relation is an incoming relation,
   *         <code>false</code> if not
   */
  boolean isIncomingRelation (@Nullable R aRelation);

  /**
   * @return All incoming relations and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <R> getAllIncomingRelations ();

  /**
   * Check if this graph node is directly connected to the passed node via an
   * incoming relation.
   *
   * @param aNode
   *        The node to be checked. May be <code>null</code>.
   * @return <code>true</code> if is connected, <code>false</code> if not
   */
  boolean isFromNode (@Nullable N aNode);

  /**
   * @return All nodes that are connected via incoming relations.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <N> getAllFromNodes ();

  /**
   * Find the incoming relation from the passed node to this node.
   *
   * @param aFromNode
   *        The from node to use. May be <code>null</code>.
   * @return <code>null</code> if there exists no incoming relation from the
   *         passed node to this node.
   */
  @Nullable
  R getIncomingRelationFrom (@Nullable N aFromNode);

  // --- outgoing ---

  /**
   * @return <code>true</code> if this node has at least one outgoing relation.
   */
  boolean hasOutgoingRelations ();

  /**
   * @return The number of outgoing relations. Always &ge; 0.
   */
  @Nonnegative
  int getOutgoingRelationCount ();

  /**
   * Check if this node has the passed relation as an outgoing relations.
   *
   * @param aRelation
   *        The relation to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed relation is an outgoing relation,
   *         <code>false</code> if not
   */
  boolean isOutgoingRelation (@Nullable R aRelation);

  /**
   * @return All outgoing relations and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <R> getAllOutgoingRelations ();

  /**
   * Check if this graph node is directly connected to the passed node via an
   * outgoing relation.
   *
   * @param aNode
   *        The node to be checked. May be <code>null</code>.
   * @return <code>true</code> if is connected, <code>false</code> if not
   */
  boolean isToNode (@Nullable N aNode);

  /**
   * @return All nodes that are connected via outgoing relations.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <N> getAllToNodes ();

  /**
   * Find the incoming relation from this node to the passed node.
   *
   * @param aToNode
   *        The to node to use. May be <code>null</code>.
   * @return <code>null</code> if there exists no incoming relation from this
   *         node to the passed node.
   */
  @Nullable
  R getOutgoingRelationTo (@Nullable N aToNode);

  // --- incoming and/or outgoing

  /**
   * Check if this node has incoming <b>or</b> outgoing relations. This is equal
   * to calling <code>hasIncomingRelations() || hasOutgoingRelations()</code>
   *
   * @return <code>true</code> if this node has at least one incoming or
   *         outgoing relation.
   */
  boolean hasIncomingOrOutgoingRelations ();

  /**
   * Check if this node has incoming <b>and</b> outgoing relations. This is
   * equal to calling
   * <code>hasIncomingRelations() &amp;&amp; hasOutgoingRelations()</code>
   *
   * @return <code>true</code> if this node has at least one incoming and at
   *         least one outgoing relation.
   */
  boolean hasIncomingAndOutgoingRelations ();
}
