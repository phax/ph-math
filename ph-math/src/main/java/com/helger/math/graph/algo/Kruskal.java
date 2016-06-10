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
package com.helger.math.graph.algo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsTreeSet;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.string.StringHelper;
import com.helger.math.graph.IMutableGraphNode;
import com.helger.math.graph.IMutableGraphRelation;
import com.helger.math.graph.simple.ISimpleGraph;
import com.helger.math.graph.simple.SimpleGraph;
import com.helger.math.graph.simple.SimpleGraphObjectFastFactory;

/**
 * Find the minimum spanning tree of a graph, using Kruskal's algorithm.
 *
 * @author Philip Helger
 */
public final class Kruskal
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (Kruskal.class);

  private Kruskal ()
  {}

  public static final class Result
  {
    private final SimpleGraph m_aGraph;
    private final int m_nTotalWeight;

    public Result (@Nonnull final SimpleGraph aGraph, final int nTotalWeight)
    {
      if (aGraph == null)
        throw new NullPointerException ("graph");
      m_aGraph = aGraph;
      m_nTotalWeight = nTotalWeight;
    }

    @Nonnull
    public SimpleGraph getGraph ()
    {
      return m_aGraph;
    }

    public int getTotalWeight ()
    {
      return m_nTotalWeight;
    }

    @Nonnull
    @Nonempty
    public String getAsString ()
    {
      final StringBuilder aSB = new StringBuilder ();
      aSB.append ("Total weight ").append (m_nTotalWeight).append (" for nodes {");
      int nIndex = 0;
      for (final IMutableGraphNode aNode : m_aGraph.getAllNodes ().values ())
      {
        if (nIndex++ > 0)
          aSB.append (',');
        aSB.append ('\'').append (aNode.getID ()).append ('\'');
      }
      return aSB.append ('}').toString ();
    }
  }

  private static String _getWeightInfo (@Nonnull final IMutableGraphRelation aRel,
                                        @Nonnull @Nonempty final String sRelationCostAttr)
  {
    return "{" +
           StringHelper.getImploded (',', new CommonsTreeSet <> (aRel.getAllConnectedNodeIDs ())) +
           ":" +
           aRel.getAttributeAsInt (sRelationCostAttr) +
           "}";
  }

  @Nonnull
  public static Kruskal.Result applyKruskal (@Nonnull final ISimpleGraph aGraph,
                                             @Nonnull @Nonempty final String sRelationCostAttr)
  {
    final Collection <IMutableGraphRelation> aAllRelations = aGraph.getAllRelations ().values ();
    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Starting Kruskal on " + aAllRelations.size () + " relations");
    final List <IMutableGraphRelation> aSortedRelations = CollectionHelper.getSorted (aAllRelations,
                                                                                      Comparator.comparingInt (aObject -> aObject.getAttributeAsInt (sRelationCostAttr)));

    if (GlobalDebug.isDebugMode ())
    {
      s_aLogger.info ("Sorted relations: " +
                      StringHelper.getImploded (';',
                                                aSortedRelations,
                                                aRel -> _getWeightInfo (aRel, sRelationCostAttr)));
    }

    final SimpleGraph ret = new SimpleGraph (new SimpleGraphObjectFastFactory ());
    // Duplicate all nodes from source graph
    for (final IMutableGraphNode aNode : aGraph.getAllNodes ().values ())
    {
      final IMutableGraphNode aNewNode = ret.createNode (aNode.getID ());
      aNewNode.setAttributes (aNode.getAllAttributes ());
    }

    // Now start adding the relations (undirected!)
    int nRemainingRelations = aGraph.getNodeCount () - 1;
    int nTotalWeight = 0;
    for (final IMutableGraphRelation aRelation : aSortedRelations)
    {
      final int nWeight = aRelation.getAttributeAsInt (sRelationCostAttr);
      final IMutableGraphRelation aNewRelation = ret.createRelation (aRelation.getNode1ID (), aRelation.getNode2ID ());
      aNewRelation.setAttributes (aRelation.getAllAttributes ());
      if (ret.containsCycles ())
      {
        if (GlobalDebug.isDebugMode ())
          s_aLogger.info ("Ignoring " +
                          _getWeightInfo (aNewRelation, sRelationCostAttr) +
                          " because it introduces a cycle!");
        ret.removeRelation (aNewRelation);
      }
      else
      {
        if (GlobalDebug.isDebugMode ())
          s_aLogger.info ("Added " + _getWeightInfo (aNewRelation, sRelationCostAttr) + "!");
        nTotalWeight += nWeight;
        nRemainingRelations--;
        if (nRemainingRelations == 0)
          break;
      }
    }

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Having a total weight of " + nTotalWeight);

    return new Kruskal.Result (ret, nTotalWeight);
  }
}