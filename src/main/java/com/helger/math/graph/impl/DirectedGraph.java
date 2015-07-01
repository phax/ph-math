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
package com.helger.math.graph.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.state.EChange;
import com.helger.commons.state.ETriState;
import com.helger.math.graph.IMutableDirectedGraph;
import com.helger.math.graph.IMutableDirectedGraphNode;
import com.helger.math.graph.IMutableDirectedGraphObjectFactory;
import com.helger.math.graph.IMutableDirectedGraphRelation;
import com.helger.math.graph.iterate.DirectedGraphIteratorForward;
import com.helger.math.matrix.Matrix;

/**
 * A simple graph object that bidirectionally links graph nodes.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class DirectedGraph extends AbstractBaseGraph <IMutableDirectedGraphNode, IMutableDirectedGraphRelation>implements IMutableDirectedGraph
{
  private final IMutableDirectedGraphObjectFactory m_aFactory;
  private ETriState m_eCacheHasCycles = ETriState.UNDEFINED;

  public DirectedGraph (@Nullable final String sID, @Nonnull final IMutableDirectedGraphObjectFactory aFactory)
  {
    super (sID);
    ValueEnforcer.notNull (aFactory, "Factory");
    m_aFactory = aFactory;
  }

  public final boolean isDirected ()
  {
    return true;
  }

  private void _invalidateCache ()
  {
    // Reset the "has cycles" cached value
    m_eCacheHasCycles = ETriState.UNDEFINED;
  }

  @Nonnull
  public IMutableDirectedGraphNode createNode ()
  {
    // Create node with new ID
    final IMutableDirectedGraphNode aNode = m_aFactory.createNode ();
    if (addNode (aNode).isUnchanged ())
      throw new IllegalStateException ("The ID factory created the ID '" + aNode.getID () + "' that is already in use");
    return aNode;
  }

  @Nullable
  public IMutableDirectedGraphNode createNode (@Nullable final String sID)
  {
    final IMutableDirectedGraphNode aNode = m_aFactory.createNode (sID);
    return addNode (aNode).isChanged () ? aNode : null;
  }

  @Nonnull
  public EChange addNode (@Nonnull final IMutableDirectedGraphNode aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasRelations ())
      throw new IllegalArgumentException ("The node to be added already has incoming and/or outgoing relations and this is not allowed!");

    final String sID = aNode.getID ();
    if (m_aNodes.containsKey (sID))
      return EChange.UNCHANGED;
    m_aNodes.put (sID, aNode);

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange removeNode (@Nonnull final IMutableDirectedGraphNode aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasRelations ())
      throw new IllegalArgumentException ("The node to be removed already has incoming and/or outgoing relations and this is not allowed!");

    if (m_aNodes.remove (aNode.getID ()) == null)
      return EChange.UNCHANGED;

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange removeNodeAndAllRelations (@Nonnull final IMutableDirectedGraphNode aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");

    if (!m_aNodes.containsKey (aNode.getID ()))
      return EChange.UNCHANGED;

    // Remove all affected relations from all nodes
    for (final IMutableDirectedGraphRelation aRelation : aNode.getAllOutgoingRelations ())
      aRelation.getTo ().removeIncomingRelation (aRelation);
    for (final IMutableDirectedGraphRelation aRelation : aNode.getAllIncomingRelations ())
      aRelation.getFrom ().removeOutgoingRelation (aRelation);

    aNode.removeAllRelations ();
    if (removeNode (aNode).isUnchanged ())
      throw new IllegalStateException ("Inconsistency removing node and all relations");
    return EChange.CHANGED;
  }

  @Nonnull
  private IMutableDirectedGraphRelation _connect (@Nonnull final IMutableDirectedGraphRelation aRelation)
  {
    aRelation.getFrom ().addOutgoingRelation (aRelation);
    aRelation.getTo ().addIncomingRelation (aRelation);
    _invalidateCache ();
    return aRelation;
  }

  @Nonnull
  public IMutableDirectedGraphRelation createRelation (@Nonnull final IMutableDirectedGraphNode aFrom,
                                                       @Nonnull final IMutableDirectedGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (aFrom, aTo));
  }

  @Nonnull
  public IMutableDirectedGraphRelation createRelation (@Nullable final String sID,
                                                       @Nonnull final IMutableDirectedGraphNode aFrom,
                                                       @Nonnull final IMutableDirectedGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (sID, aFrom, aTo));
  }

  @Nonnull
  public EChange removeRelation (@Nullable final IMutableDirectedGraphRelation aRelation)
  {
    EChange ret = EChange.UNCHANGED;
    if (aRelation != null)
    {
      ret = ret.or (aRelation.getFrom ().removeOutgoingRelation (aRelation));
      ret = ret.or (aRelation.getTo ().removeIncomingRelation (aRelation));
      if (ret.isChanged ())
        _invalidateCache ();
    }
    return ret;
  }

  @Nonnull
  public IMutableDirectedGraphNode getSingleStartNode () throws IllegalStateException
  {
    final Set <IMutableDirectedGraphNode> aStartNodes = getAllStartNodes ();
    if (aStartNodes.size () > 1)
      throw new IllegalStateException ("Graph has more than one starting node");
    if (aStartNodes.isEmpty ())
      throw new IllegalStateException ("Graph has no starting node");
    return CollectionHelper.getFirstElement (aStartNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IMutableDirectedGraphNode> getAllStartNodes ()
  {
    final Set <IMutableDirectedGraphNode> aResult = new HashSet <IMutableDirectedGraphNode> ();
    for (final IMutableDirectedGraphNode aNode : m_aNodes.values ())
      if (!aNode.hasIncomingRelations ())
        aResult.add (aNode);
    return aResult;
  }

  @Nonnull
  public IMutableDirectedGraphNode getSingleEndNode () throws IllegalStateException
  {
    final Set <IMutableDirectedGraphNode> aEndNodes = getAllEndNodes ();
    if (aEndNodes.size () > 1)
      throw new IllegalStateException ("Graph has more than one ending node");
    if (aEndNodes.isEmpty ())
      throw new IllegalStateException ("Graph has no ending node");
    return CollectionHelper.getFirstElement (aEndNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IMutableDirectedGraphNode> getAllEndNodes ()
  {
    final Set <IMutableDirectedGraphNode> aResult = new HashSet <IMutableDirectedGraphNode> ();
    for (final IMutableDirectedGraphNode aNode : m_aNodes.values ())
      if (!aNode.hasOutgoingRelations ())
        aResult.add (aNode);
    return aResult;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IMutableDirectedGraphRelation> getAllRelations ()
  {
    final Map <String, IMutableDirectedGraphRelation> ret = new LinkedHashMap <String, IMutableDirectedGraphRelation> ();
    for (final IMutableDirectedGraphNode aNode : m_aNodes.values ())
      for (final IMutableDirectedGraphRelation aRelation : aNode.getAllRelations ())
        ret.put (aRelation.getID (), aRelation);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllRelationIDs ()
  {
    final Set <String> ret = new LinkedHashSet <String> ();
    for (final IMutableDirectedGraphNode aNode : m_aNodes.values ())
      ret.addAll (aNode.getAllRelationIDs ());
    return ret;
  }

  @Override
  @Nonnull
  public EChange clear ()
  {
    if (m_aNodes.isEmpty ())
      return EChange.UNCHANGED;
    m_aNodes.clear ();

    _invalidateCache ();
    return EChange.CHANGED;
  }

  public boolean containsCycles ()
  {
    // Use cached result?
    if (m_eCacheHasCycles.isUndefined ())
    {
      m_eCacheHasCycles = ETriState.FALSE;
      // Check all nodes, in case we a small cycle and a set of other nodes (see
      // test case testCycles2)
      for (final IMutableDirectedGraphNode aCurNode : m_aNodes.values ())
      {
        final DirectedGraphIteratorForward it = new DirectedGraphIteratorForward (aCurNode);
        while (it.hasNext () && !it.hasCycles ())
          it.next ();
        if (it.hasCycles ())
        {
          m_eCacheHasCycles = ETriState.TRUE;
          break;
        }
      }
    }

    // cannot be undefined here
    return m_eCacheHasCycles.getAsBooleanValue (true);
  }

  @Override
  public boolean isSelfContained ()
  {
    for (final IMutableDirectedGraphNode aNode : m_aNodes.values ())
    {
      for (final IMutableDirectedGraphRelation aRelation : aNode.getAllIncomingRelations ())
        if (!m_aNodes.containsKey (aRelation.getFromID ()))
          return false;
      for (final IMutableDirectedGraphRelation aRelation : aNode.getAllOutgoingRelations ())
        if (!m_aNodes.containsKey (aRelation.getToID ()))
          return false;
    }
    return true;
  }

  @Nonnull
  public Matrix createIncidenceMatrix ()
  {
    final int nNodeCount = getNodeCount ();
    final Matrix ret = new Matrix (nNodeCount, nNodeCount, 0);
    final IMutableDirectedGraphNode [] aNodes = m_aNodes.values ().toArray (new IMutableDirectedGraphNode [nNodeCount]);
    for (int nRow = 0; nRow < nNodeCount; ++nRow)
    {
      final IMutableDirectedGraphNode aNodeRow = aNodes[nRow];
      for (int nCol = 0; nCol < nNodeCount; ++nCol)
        if (nRow != nCol)
          if (aNodeRow.isToNode (aNodes[nCol]))
          {
            ret.set (nRow, nCol, 1);
            ret.set (nCol, nRow, -1);
          }
    }
    return ret;
  }
}
