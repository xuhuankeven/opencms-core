/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
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

import org.opencms.ade.containerpage.client.CmsContainerpageController;
import org.opencms.ade.containerpage.client.ui.css.I_CmsLayoutBundle;
import org.opencms.ade.containerpage.shared.CmsContainerElement;
import org.opencms.gwt.client.dnd.CmsDNDHandler.Orientation;
import org.opencms.gwt.client.dnd.I_CmsDraggable;
import org.opencms.gwt.client.dnd.I_CmsDropTarget;
import org.opencms.gwt.client.ui.CmsHighlightingBorder;
import org.opencms.gwt.client.util.CmsDomUtil;
import org.opencms.gwt.client.util.CmsPositionBean;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Group-container element. To be used for content elements within a container-page.<p>
 * The group-container acts as a draggable element and if edited as a container.<p>
 * 
 * @since 8.0.0
 */
public class CmsGroupContainerElementPanel extends CmsContainerPageElementPanel implements I_CmsDropContainer {

    /** The container type. */
    private String m_containerId;

    /** The editing marker. Used to highlight the container background while editing. */
    private Element m_editingMarker;

    /** The editing placeholder. Used within group-container editing. */
    private Element m_editingPlaceholder;

    /** The placeholder element. */
    private Element m_placeholder;

    /** The index of the current placeholder position. */
    private int m_placeholderIndex = -1;

    /** The resource type name. */
    private String m_resourceType;

    /** The cached highlighting position. */
    private CmsPositionBean m_ownPosition;

    /**
     * Constructor.<p>
     * 
     * @param element the DOM element
     * @param parent the drag parent
     * @param clientId the client id
     * @param sitePath the element site-path
     * @param resourceType the resource type name
     * @param noEditReason the no edit reason, if empty, editing is allowed
     * @param hasSettings should be true if the element has settings which can be edited 
     * @param hasViewPermission indicates if the current user has view permissions on the element resource
     * @param hasWritePermission indicates if the current user has write permissions on the element resource
     * @param releasedAndNotExpired <code>true</code> if the element resource is currently released and not expired
     */
    public CmsGroupContainerElementPanel(
        Element element,
        I_CmsDropContainer parent,
        String clientId,
        String sitePath,
        String resourceType,
        String noEditReason,
        boolean hasSettings,
        boolean hasViewPermission,
        boolean hasWritePermission,
        boolean releasedAndNotExpired) {

        super(
            element,
            parent,
            clientId,
            sitePath,
            noEditReason,
            hasSettings,
            hasViewPermission,
            hasWritePermission,
            releasedAndNotExpired,
            true);
        m_resourceType = resourceType;
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#addDndChild(org.opencms.gwt.client.dnd.I_CmsDropTarget)
     */
    public void addDndChild(I_CmsDropTarget child) {

        throw new UnsupportedOperationException("Element groups do not support nested containers");
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#adoptElement(org.opencms.ade.containerpage.client.ui.CmsContainerPageElementPanel)
     */
    public void adoptElement(CmsContainerPageElementPanel containerElement) {

        assert getElement().equals(containerElement.getElement().getParentElement());
        getChildren().add(containerElement);
        adopt(containerElement);
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#checkMaxElementsOnEnter()
     */
    public void checkMaxElementsOnEnter() {

        // TODO: implement

    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#checkMaxElementsOnLeave()
     */
    public void checkMaxElementsOnLeave() {

        // TODO: implement

    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsDropTarget#checkPosition(int, int, Orientation)
     */
    public boolean checkPosition(int x, int y, Orientation orientation) {

        // ignore orientation
        int scrollTop = getElement().getOwnerDocument().getScrollTop();
        // use cached position
        int relativeTop = (y + scrollTop) - m_ownPosition.getTop();
        if ((relativeTop > 0) && (m_ownPosition.getHeight() > relativeTop)) {
            // cursor is inside the height of the element, check horizontal position
            int scrollLeft = getElement().getOwnerDocument().getScrollLeft();
            int relativeLeft = (x + scrollLeft) - m_ownPosition.getLeft();
            return (relativeLeft > 0) && (m_ownPosition.getWidth() > relativeLeft);
        }
        return false;
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#clearDnDChildren()
     */
    public void clearDnDChildren() {

        // nothing todo
    }

    /**
     * Clears the editing placeholder reference.<p>
     */
    public void clearEditingPlaceholder() {

        m_editingPlaceholder = null;
        m_editingMarker = null;
    }

    /**
     * Returns the container id.<p>
     *
     * @return the container id
     */
    public String getContainerId() {

        return m_containerId;
    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsNestedDropTarget#getDnDChildren()
     */
    public List<I_CmsDropTarget> getDnDChildren() {

        return null;
    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsDropTarget#getPlaceholderIndex()
     */
    public int getPlaceholderIndex() {

        return m_placeholderIndex;
    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsNestedDropTarget#hasDnDChildren()
     */
    public boolean hasDnDChildren() {

        return false;
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.CmsContainerPageElementPanel#hideEditableListButtons()
     */
    @Override
    public void hideEditableListButtons() {

        Iterator<Widget> it = iterator();
        while (it.hasNext()) {
            Widget child = it.next();
            if (child instanceof CmsContainerPageElementPanel) {
                ((CmsContainerPageElementPanel)child).hideEditableListButtons();
            }
        }
    }

    /**
     * Puts a highlighting border around the container content.<p>
     */
    public void highlightContainer() {

        getElement().addClassName(I_CmsLayoutBundle.INSTANCE.dragdropCss().dragging());
        // remove any remaining highlighting
        if (m_highlighting != null) {
            m_highlighting.removeFromParent();
        }
        // adding the 'clearFix' style to all targets containing floated elements
        // in some layouts this may lead to inappropriate clearing after the target, 
        // but it is still necessary as it forces the target to enclose it's floated content 
        if ((getWidgetCount() > 0)
            && !CmsDomUtil.getCurrentStyle(getWidget(0).getElement(), CmsDomUtil.Style.floatCss).equals(
                CmsDomUtil.StyleValue.none.toString())) {
            getElement().addClassName(I_CmsLayoutBundle.INSTANCE.dragdropCss().clearFix());
        }
        // cache the position info, to be used during drag and drop
        m_ownPosition = CmsPositionBean.getInnerDimensions(getElement(), 3, false);
        if (m_editingPlaceholder != null) {
            m_editingPlaceholder.getStyle().setHeight(m_ownPosition.getHeight() + 10, Unit.PX);
        }
        if (m_editingMarker != null) {
            m_editingMarker.getStyle().setHeight(m_ownPosition.getHeight() + 4, Unit.PX);
            m_editingMarker.getStyle().setWidth(m_ownPosition.getWidth() + 4, Unit.PX);
        }
        m_highlighting = new CmsHighlightingBorder(m_ownPosition, CmsHighlightingBorder.BorderColor.red);
        RootPanel.get().add(m_highlighting);
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.CmsContainerPageElementPanel#initInlineEditor(org.opencms.ade.containerpage.client.CmsContainerpageController)
     */
    @Override
    public void initInlineEditor(CmsContainerpageController controller) {

        // don to anything
    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsDropTarget#insertPlaceholder(com.google.gwt.dom.client.Element, int, int, Orientation)
     */
    public void insertPlaceholder(Element placeholder, int x, int y, Orientation orientation) {

        m_placeholderIndex = -1;
        m_placeholder = placeholder;
        repositionPlaceholder(x, y, orientation);
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#isDetailOnly()
     */
    public boolean isDetailOnly() {

        return false;
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#isDetailView()
     */
    public boolean isDetailView() {

        return false;
    }

    /**
     * Returns if this element represents a group container.<p>
     * 
     * @return <code>true</code> if this element represents a group container
     */
    public boolean isGroupContainer() {

        return CmsContainerElement.GROUP_CONTAINER_TYPE_NAME.equals(m_resourceType);
    }

    /**
     * Returns if this element represents an inherit container.<p>
     * 
     * @return <code>true</code> if this element represents an inherit container
     */
    public boolean isInheritContainer() {

        return CmsContainerElement.INHERIT_CONTAINER_TYPE_NAME.equals(m_resourceType);
    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsDropTarget#onDrop(org.opencms.gwt.client.dnd.I_CmsDraggable)
     */
    public void onDrop(I_CmsDraggable draggable) {

        // nothing to do

    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.I_CmsDropContainer#refreshHighlighting()
     */
    public void refreshHighlighting() {

        m_ownPosition = CmsPositionBean.getInnerDimensions(getElement(), 3, false);
        if (m_editingPlaceholder != null) {
            m_editingPlaceholder.getStyle().setHeight(m_ownPosition.getHeight() + 10, Unit.PX);
        }
        if (m_editingMarker != null) {
            m_editingMarker.getStyle().setHeight(m_ownPosition.getHeight() + 4, Unit.PX);
            m_editingMarker.getStyle().setWidth(m_ownPosition.getWidth() + 4, Unit.PX);
        }
        if (m_highlighting != null) {
            m_highlighting.setPosition(m_ownPosition);
        }
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.CmsContainerPageElementPanel#removeHighlighting()
     */
    @Override
    public void removeHighlighting() {

        if (m_highlighting != null) {
            m_highlighting.removeFromParent();
            m_highlighting = null;
        }

        getElement().removeClassName(I_CmsLayoutBundle.INSTANCE.dragdropCss().dragging());
        getElement().removeClassName(I_CmsLayoutBundle.INSTANCE.dragdropCss().clearFix());

    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsDropTarget#removePlaceholder()
     */
    public void removePlaceholder() {

        if (m_placeholder != null) {
            m_placeholder.removeFromParent();
            m_placeholder = null;
        }
        m_placeholderIndex = -1;
    }

    /**
     * @see org.opencms.gwt.client.dnd.I_CmsDropTarget#repositionPlaceholder(int, int, Orientation)
     */
    public void repositionPlaceholder(int x, int y, Orientation orientation) {

        int oldIndex = m_placeholderIndex;
        switch (orientation) {
            case HORIZONTAL:
                m_placeholderIndex = CmsDomUtil.positionElementInside(
                    m_placeholder,
                    getElement(),
                    m_placeholderIndex,
                    x,
                    -1);
                break;
            case VERTICAL:
                m_placeholderIndex = CmsDomUtil.positionElementInside(
                    m_placeholder,
                    getElement(),
                    m_placeholderIndex,
                    -1,
                    y);
                break;
            case ALL:
            default:
                m_placeholderIndex = CmsDomUtil.positionElementInside(
                    m_placeholder,
                    getElement(),
                    m_placeholderIndex,
                    x,
                    y);
                break;
        }
        if (oldIndex != m_placeholderIndex) {
            m_ownPosition = CmsPositionBean.getInnerDimensions(getElement(), 3, false);
        }
    }

    /**
     * Sets the container id.<p>
     *
     * @param containerId the container id to set
     */
    public void setContainerId(String containerId) {

        m_containerId = containerId;
    }

    /**
     * Sets the editing marker. Used to highlight the container background while editing.<p>
     *  
     * @param editingMarker the editing marker element
     */
    public void setEditingMarker(Element editingMarker) {

        m_editingMarker = editingMarker;
    }

    /**
     * Sets the editing placeholder.<p>
     * 
     * @param editingPlaceholder the editing placeholder element
     */
    public void setEditingPlaceholder(Element editingPlaceholder) {

        m_editingPlaceholder = editingPlaceholder;
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.CmsContainerPageElementPanel#showEditableListButtons()
     */
    @Override
    public void showEditableListButtons() {

        Iterator<Widget> it = iterator();
        while (it.hasNext()) {
            Widget child = it.next();
            if (child instanceof CmsContainerPageElementPanel) {
                ((CmsContainerPageElementPanel)child).showEditableListButtons();
            }
        }
    }

    /**
     * @see org.opencms.ade.containerpage.client.ui.CmsContainerPageElementPanel#updateOptionBarPosition()
     */
    @Override
    public void updateOptionBarPosition() {

        for (Widget widget : this) {
            if (widget instanceof CmsContainerPageElementPanel) {
                ((CmsContainerPageElementPanel)widget).updateOptionBarPosition();
            }
        }
    }
}
