/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/containerpage/client/ui/Attic/CmsToolbarGalleryMenu.java,v $
 * Date   : $Date: 2010/05/03 07:53:47 $
 * Version: $Revision: 1.5 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ade.containerpage.client.ui;

import org.opencms.ade.containerpage.client.CmsContainerpageHandler;
import org.opencms.ade.containerpage.client.draganddrop.CmsContainerDragHandler;
import org.opencms.ade.containerpage.client.ui.css.I_CmsLayoutBundle;
import org.opencms.ade.galleries.client.CmsGalleryFactory;
import org.opencms.ade.galleries.client.ui.CmsGalleryDialog;
import org.opencms.gwt.client.ui.I_CmsButton;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The gallery tool-bar menu.<p>
 * 
 * @author Tobias Herrmann
 * 
 * @version $Revision: 1.5 $
 * 
 * @since 8.0.0
 */
public class CmsToolbarGalleryMenu extends A_CmsToolbarMenu {

    /** The main content widget. */
    private FlowPanel m_content;

    private CmsContainerDragHandler m_dragHandler;

    private CmsGalleryDialog m_gallery;

    private boolean m_initialized;

    /**
     * Constructor.<p>
     * 
     * @param handler the container-page handler
     * @param dragHandler the container-page drag handler
     */
    public CmsToolbarGalleryMenu(CmsContainerpageHandler handler, CmsContainerDragHandler dragHandler) {

        super(I_CmsButton.ButtonData.ADD, handler);
        m_dragHandler = dragHandler;
        m_content = new FlowPanel();
        m_content.setStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().menuContent());

        setMenuWidget(m_content);
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsToolbarButton#onToolbarActivate()
     */
    public void onToolbarActivate() {

        if (!m_initialized) {
            m_gallery = CmsGalleryFactory.createDialog(m_dragHandler);
            SimplePanel tabsContainer = new SimplePanel();
            tabsContainer.addStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().menuTabContainer());
            tabsContainer.add(m_gallery);
            m_content.add(tabsContainer);
            m_initialized = true;
        }
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsToolbarButton#onToolbarDeactivate()
     */
    public void onToolbarDeactivate() {

        // nothing to do
    }

}