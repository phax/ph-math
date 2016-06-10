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

import com.helger.commons.collection.attr.IAttributeContainer;
import com.helger.commons.id.IHasID;

/**
 * Base interface for graph nodes and graph relations.
 *
 * @author Philip Helger
 */
public interface IBaseGraphObject extends IHasID <String>, IAttributeContainer <String, Object>
{
  /**
   * Check if the object is directed or undirected. Directed nodes must
   * implement {@link IMutableDirectedGraphNode} whereas undirected relations
   * must implement {@link IMutableGraphNode}. Directed relations must implement
   * {@link IMutableDirectedGraphRelation} whereas undirected relations must
   * implement {@link IMutableGraphRelation}.
   *
   * @return <code>true</code> if it is a directed object "from" and "to"),
   *         <code>false</code> if it is an undirected object.
   */
  boolean isDirected ();
}