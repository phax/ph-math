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
package com.helger.math.graph.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.state.EChange;
import com.helger.commons.state.ETriState;
import com.helger.math.graph.IMutableGraph;
import com.helger.math.graph.IMutableGraphNode;
import com.helger.math.graph.IMutableGraphObjectFactory;
import com.helger.math.graph.IMutableGraphRelation;
import com.helger.math.graph.iterate.GraphIterator;
import com.helger.math.matrix.Matrix;

/**
 * A simple graph object that bidirectionally links graph nodes.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class Graph extends AbstractBaseGraph <IMutableGraphNode, IMutableGraphRelation> implements IMutableGraph
{
  private final IMutableGraphObjectFactory m_aFactory;
  private ETriState m_eCacheHasCycles = ETriState.UNDEFINED;

  public Graph (@Nullable final String sID, @Nonnull final IMutableGraphObjectFactory aFactory)
  {
    super (sID);
    if (aFactory == null)
      throw new NullPointerException ("factory");
    m_aFactory = aFactory;
  }

  public final boolean isDirected ()
  {
    return false;
  }

  private void _invalidateCache ()
  {
    // Reset the "has cycles" cached value
    m_eCacheHasCycles = ETriState.UNDEFINED;
  }

  @Nonnull
  public IMutableGraphNode createNode ()
  {
    // Create node with new ID
    final IMutableGraphNode aNode = m_aFactory.createNode ();
    if (addNode (aNode).isUnchanged ())
      throw new IllegalStateException ("The ID factory created the ID '" + aNode.getID () + "' that is already in use");
    return aNode;
  }

  @Nullable
  public IMutableGraphNode createNode (@Nullable final String sID)
  {
    final IMutableGraphNode aNode = m_aFactory.createNode (sID);
    return addNode (aNode).isChanged () ? aNode : null;
  }

  @Nonnull
  public EChange addNode (@Nonnull final IMutableGraphNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

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
  public EChange removeNode (@Nonnull final IMutableGraphNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (!isChangingConnectedObjectsAllowed () && aNode.hasRelations ())
      throw new IllegalArgumentException ("The node to be removed already has incoming and/or outgoing relations and this is not allowed!");

    if (m_aNodes.remove (aNode.getID ()) == null)
      return EChange.UNCHANGED;

    _invalidateCache ();
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange removeNodeAndAllRelations (@Nonnull final IMutableGraphNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (!m_aNodes.containsKey (aNode.getID ()))
      return EChange.UNCHANGED;

    // Remove all affected relations from all nodes
    for (final IMutableGraphRelation aRelation : aNode.getAllRelations ())
      for (final IMutableGraphNode aNode2 : aRelation.getAllConnectedNodes ())
        aNode2.removeRelation (aRelation);

    // Remove the node itself
    if (removeNode (aNode).isUnchanged ())
      throw new IllegalStateException ("Inconsistency removing node and all relations");
    return EChange.CHANGED;
  }

  @Nonnull
  private IMutableGraphRelation _connect (@Nonnull final IMutableGraphRelation aRelation)
  {
    EChange eChange = EChange.UNCHANGED;
    for (final IMutableGraphNode aNode : aRelation.getAllConnectedNodes ())
      eChange = eChange.or (aNode.addRelation (aRelation));
    if (eChange.isChanged ())
      _invalidateCache ();
    return aRelation;
  }

  @Nonnull
  public IMutableGraphRelation createRelation (@Nonnull final IMutableGraphNode aFrom,
                                               @Nonnull final IMutableGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (aFrom, aTo));
  }

  @Nonnull
  public IMutableGraphRelation createRelation (@Nullable final String sID,
                                               @Nonnull final IMutableGraphNode aFrom,
                                               @Nonnull final IMutableGraphNode aTo)
  {
    return _connect (m_aFactory.createRelation (sID, aFrom, aTo));
  }

  @Nonnull
  public EChange removeRelation (@Nullable final IMutableGraphRelation aRelation)
  {
    EChange ret = EChange.UNCHANGED;
    if (aRelation != null)
    {
      for (final IMutableGraphNode aNode : aRelation.getAllConnectedNodes ())
        ret = ret.or (aNode.removeRelation (aRelation));
      if (ret.isChanged ())
        _invalidateCache ();
    }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IMutableGraphRelation> getAllRelations ()
  {
    final Map <String, IMutableGraphRelation> ret = new LinkedHashMap <String, IMutableGraphRelation> ();
    for (final IMutableGraphNode aNode : m_aNodes.values ())
      for (final IMutableGraphRelation aRelation : aNode.getAllRelations ())
        ret.put (aRelation.getID (), aRelation);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllRelationIDs ()
  {
    final Set <String> ret = new LinkedHashSet <String> ();
    for (final IMutableGraphNode aNode : m_aNodes.values ())
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
      final List <IMutableGraphNode> aAllNodes = CollectionHelper.newList (m_aNodes.values ());
      while (!aAllNodes.isEmpty ())
      {
        // Iterate from the first node
        final GraphIterator it = new GraphIterator (aAllNodes.remove (0));
        if (it.hasCycles ())
        {
          m_eCacheHasCycles = ETriState.TRUE;
          break;
        }
        while (it.hasNext ())
        {
          // Remove from remaining list, because node is reachable from some
          // other node
          aAllNodes.remove (it.next ());
        }
      }
    }

    // cannot be undefined here
    return m_eCacheHasCycles.getAsBooleanValue (true);
  }

  public boolean isSelfContained ()
  {
    for (final IMutableGraphNode aNode : m_aNodes.values ())
      for (final IMutableGraphRelation aRelation : aNode.getAllRelations ())
        for (final IMutableGraphNode aRelNode : aRelation.getAllConnectedNodes ())
          if (!m_aNodes.containsKey (aRelNode.getID ()))
            return false;
    return true;
  }

  @Nonnull
  public Matrix createIncidenceMatrix ()
  {
    final int nNodeCount = getNodeCount ();
    final Matrix ret = new Matrix (nNodeCount, nNodeCount, 0);
    final IMutableGraphNode [] aNodes = m_aNodes.values ().toArray (new IMutableGraphNode [nNodeCount]);
    for (int nRow = 0; nRow < nNodeCount; ++nRow)
    {
      final IMutableGraphNode aNodeRow = aNodes[nRow];
      for (int nCol = 0; nCol < nNodeCount; ++nCol)
        if (nRow != nCol)
          if (aNodeRow.isConnectedWith (aNodes[nCol]))
          {
            ret.set (nRow, nCol, 1);
            ret.set (nCol, nRow, 1);
          }
    }
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    return super.equals (o);
  }

  @Override
  public int hashCode ()
  {
    return super.hashCode ();
  }
}
