/*******************************************************************************
 * Copyright (c) 2017 INRIA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     INRIA - initial API and implementation
 *     I3S Laboratory - API update and bug fix
 *******************************************************************************/
package org.eclipse.gemoc.execution.concurrent.ccsljavaengine.eventscheduling.trace;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

class Merger extends AbstractMerger {

	/**
	 * We want to bypass the reference merger that is eventually called by the 
	 * abstract merger, so we create this merger with a very high ranking.
	 * 
	 * The goal of the bypass is th replace the operation createCopy, so that
	 * objects are not created multiple times.
	 * 
	 * @author ebousse
	 *
	 */
	public static class CustomReferenceMerger extends ReferenceChangeMerger {

		final EcoreUtil.Copier copier = new EMFCompareCopier();

		protected EObject createCopy(EObject referenceObject) {

			if (copier.containsKey(referenceObject))
				return copier.get(referenceObject);
			return copier.copy(referenceObject);
		}

		@Override
		public int getRanking() {
			return Integer.MAX_VALUE;
		}

	}

	public Merger() {
		setRegistry(EMFCompareRCPPlugin.getDefault().getMergerRegistry());
		getRegistry().add(new CustomReferenceMerger());
	}

	@Override
	public boolean isMergerFor(Diff target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		this.mergeDiff(target, true, monitor);
	}

	@Override
	public void copyLeftToRight(Diff target, Monitor monitor) {
		this.mergeDiff(target, false, monitor);
	}

}
